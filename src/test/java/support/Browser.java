package support;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ExtensionPage;
import pages.PasswordPage;
import pages.WormholePage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Browser {
    public static ChromeDriver driver;
    public static Dotenv env;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

    public static boolean isMainnet = false;
    public static String url = "";
    public static Date startedAt;
    public static Date finishedAt;
    public static String fromWallet = "";
    public static String toWallet = "";
    public static String fromNetwork = "";
    public static String toNetwork = "";
    public static String fromAmount = "";
    public static String toAmount = "";
    public static String fromAsset = "";
    public static String toAsset = "";
    public static String route = "";
    public static String txFrom = "";
    public static String txTo = "";
    public static String fromBalance = "";
    public static String toBalance = "";
    public static String toFinalBalance = "";
    public static String sourceGasFeeUsd = "";
    public static String destinationGasFeeUsd = "";
    public static String screenshotUrl = "";

    public static int waitSeconds = 10;
    public static String toNativeBalance = "";
    public static String toFinalNativeBalance = "";
    public static boolean metaMaskWasUnlocked = false;
    public static boolean phantomWasUnlocked = false;
    public static boolean leapWasUnlocked = false;

    public static void main(String[] args) {
        launch();

        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET"));
        Browser.driver.findElement(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(PasswordPage.button).click();
    }

    public static void launch() {
        launch("chrome_profile_testnet");
    }

    public static void launch(String profile) {
        System.out.println("Browser.launch (" + profile + ")");

        env = Dotenv.load();
        Google.getLoggedInUser(); // login to Google Services

        ChromeOptions opt = new ChromeOptions();
        if (System.getProperty("os.name").startsWith("Windows")) {
            opt.setBinary("C:/Program Files/chrome-win64-114.0.5735.90/chrome.exe");
        } else {
            opt.setBinary("/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing");
        }
        opt.addArguments("user-data-dir=" + Paths.get(profile).toAbsolutePath());
        opt.addArguments("profile-directory=Default");
        ClientConfig config = ClientConfig.defaultConfig().readTimeout(Duration.ofHours(2));
        try {
            driver = new ChromeDriver(ChromeDriverService.createDefaultService(), opt, config);
        } catch (Exception ex) {
            System.err.println("Could not start Chrome. Please make sure all test browser windows are closed. ERROR: " + ex.getMessage());
            System.exit(1);
        }
        Browser.waitSeconds = 10;
    }

    public static void quit() {
        driver.quit();
    }

    public static boolean extensionWindowIsOpened() {
        return driver.getWindowHandles().toArray().length > 1;
    }

    public static void waitForExtensionWindowToAppear() {
        waitForExtensionWindowToAppear(60);
    }

    public static void waitForExtensionWindowToAppear(int seconds) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        wait.until(d -> extensionWindowIsOpened());
        switchToExtensionWindow();
    }

    public static void waitForExtensionWindowToDisappear() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(d -> !extensionWindowIsOpened());
        switchToMainWindow();
    }

    public static void switchToExtensionWindow() {
        System.out.println("Switching to extension window...");
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);
    }

    public static void switchToMainWindow() {
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);
    }

    public static void scrollToElement(WebElement element) {
        Actions actions = new Actions(Browser.driver);
        actions.moveToElement(element);
        actions.perform();
    }

    public static void takeScreenshot() {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String fileName = date + "_test_failed.png";
            boolean uploaded = Google.uploadScreenshot(screenshotFile, fileName);
            if (!uploaded) {
                File destinationFile = new File("screenshots/" + fileName);
                FileUtils.copyFile(screenshotFile, destinationFile);
            }
        } catch (WebDriverException e) {
            System.err.println("Could not take a screenshot");
            Browser.screenshotUrl = "N/A";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveResults(String status) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        String[] fields = {
                status,
                Browser.fromNetwork,
                Browser.toNetwork,
                Browser.route,
                Browser.url,
                Browser.fromAmount,
                Browser.fromAsset,
                Browser.fromBalance,
                Browser.toAmount,
                Browser.toAsset,
                Browser.toBalance,
                Browser.txFrom,
                Browser.txTo,
                Browser.toFinalBalance,
                Browser.toNativeBalance,
                Browser.toFinalNativeBalance,
                Browser.fromWallet,
                Browser.toWallet,
                dt.format(Browser.startedAt),
                dt.format(Browser.finishedAt),
                Browser.sourceGasFeeUsd,
                Browser.destinationGasFeeUsd,
                Browser.screenshotUrl,
                dateOnly.format(Browser.startedAt),
                String.valueOf(isEvmNetwork(Browser.fromNetwork) && isEvmNetwork(Browser.toNetwork)),
                String.valueOf(isSolanaNetwork(Browser.fromNetwork) || isSolanaNetwork(Browser.toNetwork)),
                String.valueOf(isCosmosNetwork(Browser.fromNetwork) || isCosmosNetwork(Browser.toNetwork)),
                String.valueOf(isSuiNetwork(Browser.fromNetwork) || isSuiNetwork(Browser.toNetwork)),
                String.valueOf(isAptosNetwork(Browser.fromNetwork) || isAptosNetwork(Browser.toNetwork)),
                String.valueOf(isCircleRoute(Browser.route)),
                "",
                urlToEnvironment(Browser.url)
        };

        boolean savedSuccessfully = Google.writeResultsToGoogleSpreadsheet(fields);
        if (!savedSuccessfully) {
            // save to results.csv if could not save to Google Sheet
            String csvRow = String.join(";", fields) + "\n";
            try {
                File csvFile = new File("results/results.csv");
                csvFile.getParentFile().mkdirs();
                csvFile.createNewFile();
                Files.write(csvFile.toPath(), csvRow.getBytes(), StandardOpenOption.APPEND);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static String urlToEnvironment(String url) {
        switch (url) {
            case "https://wormhole-connect.netlify.app/":
                return "wormhole-testnet";
            case "https://wormhole-connect-mainnet.netlify.app/":
                return "wormhole-mainnet";
            case "https://portalbridge.com/":
                return "portal-mainnet";
        }
        return "";
    }

    private static boolean isCircleRoute(String route) {
        switch (route) {
            case "circle-manual":
            case "circle-automatic":
                return true;
        }
        return false;
    }

    private static boolean isEvmNetwork(String network) {
        switch (network) {
            case "Goerli":
            case "Ethereum":
            case "Mumbai":
            case "Polygon":
            case "BSC":
            case "Fuji":
            case "Avalanche":
            case "Fantom":
            case "Alfajores":
            case "Celo":
            case "Moonbase":
            case "Moonbeam":
            case "Base Goerli":
            case "Base":
            case "Klaytn":
            case "Arbitrum Goerli":
            case "Arbitrum":
            case "Optimism Goerli":
            case "Optimism":
            case "Sepolia":
            case "Arbitrum Sepolia":
            case "Base Sepolia":
            case "Optimism Sepolia":
                return true;
        }
        return false;
    }

    private static boolean isSolanaNetwork(String network) {
        return network.equals("Solana");
    }

    private static boolean isCosmosNetwork(String network) {
        switch (network) {
            case "Kujira":
            case "Osmosis":
            case "Evmos":
            case "Cosmoshub":
                return true;
        }
        return false;
    }

    private static boolean isSuiNetwork(String network) {
        return network.equals("Sui");
    }

    private static boolean isAptosNetwork(String network) {
        return network.equals("Aptos");
    }

    public static WebElement findElement(By locator) throws NoSuchElementException {
        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(Browser.waitSeconds));
        try {
            webDriverWait.until((webDriver) -> {
                return Browser.driver.findElement(locator);
            });
            try {
                Thread.sleep(500); // UI settle
            } catch (InterruptedException ignore) {
            }
            return Browser.driver.findElement(locator); // find element again in case it moved
        } catch (TimeoutException ex) {
            throw new NoSuchElementException("Element was not found.", ex);
        }
    }

    public static WebElement findElementIgnoreIfMissing(int seconds, By locator) {
        if (seconds > 10) {
            System.out.println("Checking if element (" + locator.toString() + ") appears in " + seconds + "s");
        }
        Browser.waitSeconds = seconds;
        try {
            return Browser.findElement(locator);
        } catch (NoSuchElementException ignore) {
        }
        Browser.waitSeconds = 10;
        return null;
    }

    public static WebElement findElement(int seconds, By locator) {
        if (seconds > 10) {
            System.out.println("Waiting for element (" + locator.toString() + ")  to appear in " + seconds + "s");
        }
        Browser.waitSeconds = seconds;
        WebElement element = Browser.findElement(locator);
        Browser.waitSeconds = 10;
        return element;
    }

    public static void waitToBeClickable(WebElement el) throws NoSuchElementException {
        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(Browser.waitSeconds));
        try {
            webDriverWait
                    .until(ExpectedConditions.elementToBeClickable(el));
        } catch (TimeoutException ex) {
            throw new NoSuchElementException("Element cannot be clicked.", ex);
        }
    }

    public static String findElementAndWaitToHaveNumber(By locator) throws NoSuchElementException {
        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(60));
        try {
            return webDriverWait.
                    ignoring(NumberFormatException.class)
                    .until(webDriver -> {
                        WebElement found = webDriver.findElement(locator);
                        String text = found.getText().replaceAll("\n.*", "");
                        Double.parseDouble(text);
                        return text;
                    });
        } catch (TimeoutException ex) {
            throw new NoSuchElementException("Element does not contain a number.", ex);
        }
    }

    public static void confirmTransactionInMetaMask(boolean isClaimStep) throws InterruptedException {
        Browser.waitForExtensionWindowToAppear();

        System.out.println("Going to Approve adding new network (if MetaMask requires it)...");
        try {
            Browser.findElement(2, ExtensionPage.METAMASK_APPROVE_BUTTON).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to confirm warning on Moonbase network (if MetaMask requires it)...");
        try {
            Browser.findElement(2, ExtensionPage.METAMASK_GOT_IT_BUTTON).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to Switch network (if MetaMask requires it)...");
        try {
            Browser.findElement(2, ExtensionPage.METAMASK_SWITCH_NETWORK_BUTTON).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }

        System.out.println("Confirming transaction in MetaMask...");

        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(900));
        webDriverWait
                .until(webDriver -> {
                    if (Browser.extensionWindowIsOpened()) {
                        Browser.switchToExtensionWindow();

                        if (Browser.isMainnet) {
                            try {
                                WebElement gasAmount = Browser.driver.findElement(ExtensionPage.METAMASK_GAS_AMOUNT_TEXT);
                                String gasFeeText = gasAmount.getText().replace("$", "");
                                double gasFeeUsd = Double.parseDouble(gasFeeText);
                                if (isClaimStep) {
                                    Browser.destinationGasFeeUsd = gasFeeText;
                                } else {
                                    Browser.sourceGasFeeUsd = gasFeeText;
                                }
                                Assert.assertTrue(gasFeeUsd < 3.0);
                            } catch (NoSuchElementException | NumberFormatException ignore) {
                            }
                        }

                        WebElement metamaskFooterButton = Browser.findElement(ExtensionPage.METAMASK_FOOTER_NEXT_BUTTON);
                        String buttonText = metamaskFooterButton.getText();
                        System.out.println("MetaMask button text: " + buttonText);
                        switch (buttonText) {
                            case "Next":
                                metamaskFooterButton.click();
                                break;
                            case "Approve":
                                metamaskFooterButton.click();
                                Browser.waitForExtensionWindowToDisappear();
                                break;
                            case "Confirm":
                                Browser.waitToBeClickable(metamaskFooterButton);
                                metamaskFooterButton.click();
                                Browser.waitForExtensionWindowToDisappear();
                                if (isClaimStep) {
                                    return null; // do not stop, wait for "The bridge is now complete." message
                                }
                                return metamaskFooterButton;
                        }

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ignore) {
                        }
                        return null;
                    }
                    if (isClaimStep) {
                        return Browser.driver.findElement(WormholePage.TRANSACTION_COMPLETE_MESSAGE);
                    }
                    return null;
                });

        System.out.println("Transaction was confirmed in MetaMask");
        Browser.switchToMainWindow();
    }

    public static String getScanLinkTextByNetworkName(String network) {
        switch (network) {
            case "Goerli":
            case "Ethereum":
                return "Etherscan";
            case "Mumbai":
            case "Polygon":
                if (Browser.isMainnet) {
                    return "PolygonScan";
                }
                return "Polygonscan";
            case "BSC":
                return "BscScan";
            case "Fuji":
            case "Avalanche":
                return "Avascan";
            case "Fantom":
                if (Browser.isMainnet) {
                    return "FTMscan";
                }
                return "FtmScan";
            case "Alfajores":
            case "Celo":
                return "Celo Explorer";
            case "Moonbase":
            case "Moonbeam":
                return "Moonscan";
            case "Base Goerli":
            case "Base":
                return "BaseScan";
            case "Arbitrum Goerli":
            case "Arbitrum":
                return "Arbitrum Goerli Explorer";
            case "Optimism Goerli":
            case "Optimism":
                return "Optimism Goerli";
            case "Solana":
                return "Solana Explorer";
            case "Sui":
                return "Sui Explorer";
            case "Kujira":
                return "Kujira Finder";
            case "Evmos":
            case "Osmosis":
                return "MintScan";
            case "Klaytn":
                return "Klaytn Scope";
        }
        throw new RuntimeException("Unsupported network: " + network);
    }


    public static String getNativeAssetByNetworkName(String network) {
        switch (network) {
            case "Goerli":
            case "Ethereum":
                return "ETH";
            case "Mumbai":
            case "Polygon":
                return "MATIC";
            case "BSC":
                return "BNB";
            case "Fuji":
            case "Avalanche":
                return "AVAX";
            case "Fantom":
                return "FTM";
            case "Alfajores":
            case "Celo":
                return "CELO";
            case "Moonbase":
            case "Moonbeam":
                return "GLMR";
            case "Base Goerli":
            case "Base":
                return "ETH";
            case "Arbitrum Goerli":
            case "Arbitrum":
                return "ETH";
            case "Optimism Goerli":
            case "Optimism":
                return "ETH";
            case "Solana":
                return "SOL";
            case "Sui":
                return "SUI";
        }
        throw new RuntimeException("Unsupported network: " + network);
    }

    public static void selectAssetInFromSection(String wallet, String network, String asset) throws InterruptedException {
        Browser.findElement(WormholePage.SOURCE_SELECT_NETWORK_BUTTON).click();
        Thread.sleep(1000);
        Browser.findElement(WormholePage.CHOOSE_NETWORK(network)).click();
        Thread.sleep(1000);

        Browser.findElement(WormholePage.SOURCE_CONNECT_WALLET_BUTTON).click();
        Browser.findElement(WormholePage.CHOOSE_WALLET(wallet)).click();

        if (wallet.equals("MetaMask") && !Browser.metaMaskWasUnlocked) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElement(ExtensionPage.METAMASK_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.findElement(ExtensionPage.METAMASK_UNLOCK_BUTTON).click();

            try {
                System.out.println("Going to Reject a pending transaction (if it exists)...");
                Browser.findElement(3, ExtensionPage.METAMASK_CANCEL_BUTTON).click();
            } catch (NoSuchElementException ignore) {
            }

            Browser.waitForExtensionWindowToDisappear();
            Thread.sleep(1000);

            Browser.metaMaskWasUnlocked = true;
        }

        if (wallet.equals("Leap") && !Browser.leapWasUnlocked) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElement(ExtensionPage.LEAP_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_LEAP"));
            Browser.findElement(ExtensionPage.LEAP_UNLOCK_BUTTON).click();

            Browser.waitForExtensionWindowToDisappear();
            Thread.sleep(1000);

            Browser.leapWasUnlocked = true;
        }

        Browser.findElement(WormholePage.SOURCE_SELECT_ASSET_BUTTON).click();
        Thread.sleep(1000);
        Browser.findElement(WormholePage.CHOOSE_ASSET(asset)).click();
        Thread.sleep(1000);
    }

    public static void moveSliderByOffset(int xOffset) throws InterruptedException {
        WebElement slider = Browser.findElement(WormholePage.SLIDER_THUMB);
        Browser.scrollToElement(slider);

        int xPosition = slider.getLocation().x;

        (new Actions(Browser.driver))
                .clickAndHold(slider)
                .moveByOffset(xOffset, 0)
                .release()
                .build()
                .perform();

        int xPositionUpdated = Browser.driver.findElement(WormholePage.SLIDER_THUMB).getLocation().x;
        Assert.assertTrue("Slider should move", xPositionUpdated > xPosition);
    }
}
