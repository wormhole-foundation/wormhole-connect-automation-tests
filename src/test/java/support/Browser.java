package support;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
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

    public static int waitSeconds = 10;

    public static void main(String[] args) {
        launch();

        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET"));
        Browser.driver.findElement(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(PasswordPage.button).click();
    }

    public static void launch() {
        launch("chrome_profile_testnet");
    }

    protected static void launch(String profile) {
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
        waitSeconds = 10;
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

    public static void pressEscape() {
        Actions actions = new Actions(Browser.driver);
        actions.sendKeys(Keys.ESCAPE);
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
            TestCase.screenshotUrl = "N/A";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveResults(String status) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        String[] fields = {
                TestCase.route,

                TestCase.sourceChain + "\n" + TestCase.destinationChain,

                TestCase.inputAmount + " " + TestCase.sourceToken + "\n" +
                        TestCase.destinationAmount + " " + TestCase.destinationToken,

                TestCase.sourceWallet + "\n" + TestCase.destinationWallet,

                TestCase.wormholescanLink,

                "-", // Tx is displayed in the In-progress widget
                (TestCase.requiresClaim ? "-" : "n/a"), // Tx can be resumed
                "-", // Tx is displayed in history
                status,

                TestCase.url,

                dt.format(TestCase.startedAt),
                dt.format(TestCase.finishedAt),
                TestCase.screenshotUrl,

                "Automation: " + Google.emailAddress,
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
        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(waitSeconds));
        try {
            webDriverWait.until((webDriver) -> {
                return Browser.driver.findElement(locator);
            });

            Browser.sleep(500); // UI settle
            return Browser.driver.findElement(locator); // find element again in case it moved
        } catch (TimeoutException ex) {
            throw new NoSuchElementException("Element was not found.", ex);
        }
    }

    public static boolean elementAppears(int seconds, By locator) {
        if (seconds > 10) {
            System.out.println("Checking if element (" + locator.toString() + ") appears in " + seconds + "s");
        }
        waitSeconds = seconds;
        try {
            Browser.findElement(locator);
            return true;
        } catch (NoSuchElementException ignore) {
        }
        waitSeconds = 10;
        return false;
    }

    public static WebElement findElement(int seconds, By locator) {
        if (seconds > 10) {
            System.out.println("Waiting for element (" + locator.toString() + ")  to appear in " + seconds + "s");
        }
        waitSeconds = seconds;
        WebElement element = Browser.findElement(locator);
        waitSeconds = 10;
        return element;
    }

    public static void waitToBeClickable(WebElement el) throws NoSuchElementException {
        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(waitSeconds));
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

    public static void confirmTransactionInMetaMask(boolean isClaimStep) {
        Browser.waitForExtensionWindowToAppear();

        System.out.println("Going to Approve adding new network (if MetaMask requires it)...");
        try {
            Browser.findElement(2, ExtensionPage.METAMASK_APPROVE_BUTTON).click();
            Browser.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to confirm warning (if MetaMask requires it)...");
        try {
            Browser.findElement(2, ExtensionPage.METAMASK_GOT_IT_BUTTON).click();
            Browser.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to Switch network (if MetaMask requires it)...");
        try {
            Browser.findElement(2, ExtensionPage.METAMASK_SWITCH_NETWORK_BUTTON).click();
            Browser.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }

        System.out.println("Confirming transaction in MetaMask...");

        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(900));
        webDriverWait
                .until(webDriver -> {
                    if (Browser.extensionWindowIsOpened()) {
                        Browser.switchToExtensionWindow();

                        if (TestCase.isMainnet) {
                            try {
                                WebElement gasAmount = Browser.driver.findElement(ExtensionPage.METAMASK_GAS_AMOUNT_TEXT);
                                String gasFeeText = gasAmount.getText().replace("$", "");
                                double gasFeeUsd = Double.parseDouble(gasFeeText);
                                if (isClaimStep) {
                                    TestCase.claimGasFeeUsd = gasFeeText;
                                } else {
                                    TestCase.transactionGasFeeUsd = gasFeeText;
                                }
                                if (gasFeeUsd >= 3.0) {
                                    TestCase.isBlockedByHighFee = true;
                                    Assert.fail("Fee exceeds 3$ in MetaMask");
                                }
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

                        Browser.sleep(2000);
                        return null;
                    }
                    if (isClaimStep) {
                        return Browser.driver.findElement(WormholePage.TRANSACTION_COMPLETE_MESSAGE_V2);
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
            case "Sepolia":
                return "Etherscan";
            case "Mumbai":
            case "Polygon":
                if (TestCase.isMainnet) {
                    return "PolygonScan";
                }
                return "Polygonscan";
            case "BSC":
                return "BscScan";
            case "Fuji":
            case "Avalanche":
                return "Avascan";
            case "Fantom":
                if (TestCase.isMainnet) {
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
                return "Arbitrum Goerli Explorer";
            case "Arbitrum":
                return "Arbitrum Explorer";
            case "Optimism Goerli":
                return "Optimistic Goerli";
            case "Optimism":
                return "Optimistic Etherscan";
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
            case "Aptos":
                return "Aptos Explorer";
            case "Injective":
                return "Injective Explorer";
        }
        throw new RuntimeException("Unsupported network: " + network);
    }


    public static String getNativeAssetByNetworkName(String network) {
        switch (network) {
            case "Ethereum":
                return "ETH";
            case "Polygon":
                return "POL";
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
            case "Base":
                return "ETH";
            case "Arbitrum":
                return "ETH";
            case "Optimism":
                return "ETH";
            case "Solana":
                return "SOL";
            case "Sui":
                return "SUI";
        }
        throw new RuntimeException("Unsupported network: " + network);
    }

    public static void selectAssetInFromSection(String wallet, String network, String asset) {
        Browser.findElement(WormholePage.SOURCE_SELECT_NETWORK_BUTTON).click();
        Browser.sleep(1000);
        Browser.findElement(WormholePage.CHOOSE_NETWORK(network)).click();
        Browser.sleep(3000); // wait for wallet auto-connect

        if (Browser.elementAppears(1, WormholePage.SOURCE_CONNECT_WALLET_BUTTON)) {
            Browser.findElement(WormholePage.SOURCE_CONNECT_WALLET_BUTTON).click();
            Browser.findElement(WormholePage.CHOOSE_WALLET(wallet)).click();

            if (wallet.equals("MetaMask") && !TestCase.metaMaskWasUnlocked) {
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.METAMASK_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
                Browser.findElement(ExtensionPage.METAMASK_UNLOCK_BUTTON).click();

                try {
                    System.out.println("Going to Reject a pending transaction (if it exists)...");
                    Browser.findElement(3, ExtensionPage.METAMASK_CANCEL_BUTTON).click();
                } catch (NoSuchElementException ignore) {
                }

                Browser.waitForExtensionWindowToDisappear();
                Browser.sleep(1000);

                TestCase.metaMaskWasUnlocked = true;
            }

            if (wallet.equals("Leap") && !TestCase.leapWasUnlocked) {
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.LEAP_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_LEAP"));
                Browser.findElement(ExtensionPage.LEAP_UNLOCK_BUTTON).click();

                Browser.waitForExtensionWindowToDisappear();
                Browser.sleep(1000);

                TestCase.leapWasUnlocked = true;
            }
        }

        Browser.findElement(WormholePage.SOURCE_SELECT_ASSET_BUTTON).click();
        Browser.sleep(1000);
        Browser.findElement(WormholePage.CHOOSE_ASSET(asset)).click();
        Browser.sleep(1000);
    }

    public static void moveSliderByOffset(int xOffset) {
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

    public static void clickElement(By locator) {
        WebElement element = findElement(locator);
        waitToBeClickable(element);
        element.click();
    }

    public static void unlockMetaMask() {
        waitForExtensionWindowToAppear();

        WebElement passwordInput = findElement(ExtensionPage.METAMASK_PASSWORD_INPUT);
        WebElement unlockButton = findElement(ExtensionPage.METAMASK_UNLOCK_BUTTON);

        passwordInput.sendKeys(env.get("WALLET_PASSWORD_METAMASK"));
        unlockButton.click();

        waitForExtensionWindowToDisappear();
        TestCase.metaMaskWasUnlocked = true;
    }

    public static void validateRouteName() {
        switch (TestCase.route) {
            case "Token Bridge Manual route":
            case "CCTP Manual route":
            case "NTT Manual route":
                TestCase.requiresClaim = true;
                return;

            case "Token Bridge Automatic route":
            case "CCTP Automatic route":
            case "Mayan Route":
            case "Mayan Swift route":
            case "Mayan MCTP route":
            case "NTT Automatic route":
            case "NTT + Axelar":
                TestCase.requiresClaim = false;
                return;
            default:
                throw new RuntimeException("Unsupported route: " + TestCase.route);
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }
}
