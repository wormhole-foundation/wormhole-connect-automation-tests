package support;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Google {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static NetHttpTransport HTTP_TRANSPORT;

    public static Credential getLoggedInUser() {
        String spreadsheetID = Browser.env.get("GOOGLE_SPREADSHEETS_DOCUMENT_ID");
        String folderId = Browser.env.get("GOOGLE_DRIVE_FOLDER_ID");
        if ((spreadsheetID == null || spreadsheetID.isEmpty()) && (folderId == null || folderId.isEmpty())) {
            System.out.println("Please set GOOGLE_SPREADSHEETS_DOCUMENT_ID in .env file to save results to Google Spreadsheets.");
            return null;
        }

        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            InputStream in = Google.class.getResourceAsStream("/credentials.json");
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Arrays.asList(SheetsScopes.DRIVE_FILE, SheetsScopes.SPREADSHEETS))
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("target")))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            Credential user = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            return user;
        } catch (Exception exception) {
            System.out.println("ERROR: " + exception.getMessage());
        }
        return null;
    }

    public static Sheets getSheetsService() {
        Credential user = getLoggedInUser();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, user)
                .setApplicationName("Wormhole Connect Automation")
                .build();
    }

    public static Drive getDriveService() {
        Credential user = getLoggedInUser();
        Drive.Builder builder = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, user);
        return builder
                .setApplicationName("Wormhole Connect Automation")
                .build();
    }

    public static boolean writeResultsToGoogleSpreadsheet(String[] values) {
        String spreadsheetID = Browser.env.get("GOOGLE_SPREADSHEETS_DOCUMENT_ID");
        String sheetName = Browser.env.get("GOOGLE_SPREADSHEETS_SHEET_NAME");

        if (spreadsheetID == null || spreadsheetID.isEmpty()) {
            return false;
        }

        List<List<Object>> data = new ArrayList<>();
        for (String value : values) {
            data.add(Arrays.asList(value));
        }

        try {
            ValueRange requestBody = new ValueRange()
                    .setValues(data)
                    .setMajorDimension("COLUMNS")
                    .setRange(sheetName);

            Sheets.Spreadsheets.Values.Append request =
                    getSheetsService().spreadsheets().values().append(spreadsheetID, sheetName, requestBody);
            request.setValueInputOption("USER_ENTERED");
            request.execute();

            return true;
        } catch (Exception exception) {
            System.out.println("Could not save results to Google Spreadsheets: " + exception.getMessage());
        }
        return false;
    }

    public static boolean uploadScreenshot(java.io.File file, String fileName) {
        String folderId = Browser.env.get("GOOGLE_DRIVE_FOLDER_ID");

        if (folderId == null || folderId.isEmpty()) {
            return false;
        }

        try {
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/png", file);

            // https://developers.google.com/drive/api/guides/folder#insert_a_file_in_a_folder
            File result = getDriveService().files().create(fileMetadata, mediaContent)
                    .setFields("id, parents, webViewLink")
                    .execute();
            Browser.screenshotUrl = result.getWebViewLink();

            return true;
        } catch (Exception exception) {
            System.out.println("Could not save file to Google Drive: " + exception.getMessage());
        }
        return false;
    }
}
