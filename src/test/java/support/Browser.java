package support;

import pages.ExtensionPage;
import pages.PasswordPage;
import pages.WormholePage;
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

    public static int waitSeconds = 0;
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
            System.err.println("Could not start Chrome. Please make sure all test browser windows are closed.");
            System.exit(1);
        }
        implicitlyWait();
    }

    public static void quit() {
        driver.quit();
    }

    public static void noImplicitWait() {
        implicitlyWait(0);
    }

    public static void implicitlyWait() {
        implicitlyWait(10);
    }

    public static void implicitlyWait(int seconds) {
        System.out.println("Implicit wait set to " + seconds + "s");
        Browser.waitSeconds = seconds;
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
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
            File destination = new File("screenshots/" + date + "_test_failed.png");

            FileUtils.copyFile(screenshotFile, destination);

            System.out.println("Saved screenshot to: " + destination.getAbsolutePath());
        } catch (WebDriverException e) {
            System.err.println("Could not save screenshot");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveResults(String status) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s = status +
                ";" + Browser.fromNetwork +
                ";" + Browser.toNetwork +
                ";" + Browser.route +
                ";" + Browser.url +
                ";" + Browser.fromAmount +
                ";" + Browser.fromAsset +
                ";" + Browser.fromBalance +
                ";" + Browser.toAmount +
                ";" + Browser.toAsset +
                ";" + Browser.toBalance +
                ";" + Browser.txFrom +
                ";" + Browser.txTo +
                ";" + Browser.toFinalBalance +
                ";" + Browser.toNativeBalance +
                ";" + Browser.toFinalNativeBalance +
                ";" + Browser.fromWallet +
                ";" + Browser.toWallet +
                ";" + dt.format(Browser.startedAt) +
                ";" + dt.format(Browser.finishedAt) +
                ";" + Browser.sourceGasFeeUsd +
                ";" + Browser.destinationGasFeeUsd +
                "\n";
        try {
            File f = new File("results/results.csv");
            f.getParentFile().mkdirs();
            f.createNewFile();
            Files.write(f.toPath(), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static WebElement findElementAndWait(By locator) throws NoSuchElementException {
        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(Browser.waitSeconds));
        try {
            WebElement el = webDriverWait
                    .until((webDriver) -> {
                        return Browser.driver.findElement(locator);
                    });
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignore) {
            }
            return el;
        } catch (TimeoutException ex) {
            throw new NoSuchElementException("Element was not found.", ex);
        }
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

        Browser.implicitlyWait(2);
        System.out.println("Going to Approve adding new network (if MetaMask requires it)...");
        try {
            Browser.findElementAndWait(ExtensionPage.METAMASK_APPROVE_BUTTON).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to confirm warning on Moonbase network (if MetaMask requires it)...");
        try {
            Browser.findElementAndWait(ExtensionPage.METAMASK_GOT_IT_BUTTON).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to Switch network (if MetaMask requires it)...");
        try {
            Browser.findElementAndWait(ExtensionPage.METAMASK_SWITCH_NETWORK_BUTTON).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        Browser.implicitlyWait();

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

                        WebElement metamaskFooterButton = Browser.findElementAndWait(ExtensionPage.METAMASK_FOOTER_NEXT_BUTTON);
                        String buttonText = metamaskFooterButton.getText();
                        System.out.println("MetaMask button text: " + buttonText);
                        if (buttonText.equals("Next")) {
                            metamaskFooterButton.click();
                        } else if (buttonText.equals("Approve")) {
                            metamaskFooterButton.click();
                            Browser.waitForExtensionWindowToDisappear();
                        } else if (buttonText.equals("Confirm")) {
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
        }
        throw new RuntimeException("Unsupported network: " + network);
    }

    public static void selectAssetInFromSection(String wallet, String network, String asset) throws InterruptedException {
        Browser.findElementAndWait(WormholePage.SOURCE_SELECT_NETWORK_BUTTON).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(WormholePage.CHOOSE_NETWORK(network)).click();
        Thread.sleep(1000);

        Browser.findElementAndWait(WormholePage.SOURCE_CONNECT_WALLET_BUTTON).click();
        Browser.findElementAndWait(WormholePage.CHOOSE_WALLET(wallet)).click();

        if (wallet.equals("MetaMask") && !Browser.metaMaskWasUnlocked) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElementAndWait(ExtensionPage.METAMASK_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.findElementAndWait(ExtensionPage.METAMASK_UNLOCK_BUTTON).click();

            try {
                System.out.println("Going to Reject a pending transaction (if it exists)...");
                Browser.implicitlyWait(3);
                Browser.findElementAndWait(ExtensionPage.METAMASK_CANCEL_BUTTON).click();
                Browser.implicitlyWait();
            } catch (NoSuchElementException ignore) {
            }

            Browser.waitForExtensionWindowToDisappear();
            Thread.sleep(1000);

            Browser.metaMaskWasUnlocked = true;
        }

        if (wallet.equals("Leap") && !Browser.leapWasUnlocked) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElementAndWait(ExtensionPage.LEAP_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_LEAP"));
            Browser.findElementAndWait(ExtensionPage.LEAP_UNLOCK_BUTTON).click();

            Browser.waitForExtensionWindowToDisappear();
            Thread.sleep(1000);

            Browser.leapWasUnlocked = true;
        }

        Browser.findElementAndWait(WormholePage.SOURCE_SELECT_ASSET_BUTTON).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(WormholePage.CHOOSE_ASSET(asset)).click();
        Thread.sleep(1000);
    }

    public static void moveSliderByOffset(int xOffset) throws InterruptedException {
        WebElement slider = Browser.findElementAndWait(WormholePage.SLIDER_THUMB);
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
