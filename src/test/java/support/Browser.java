package support;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.http.ClientConfig;
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

    public static String fromWallet = "";
    public static String toWallet = "";
    public static String fromNetwork = "";
    public static String toNetwork = "";
    public static String amount = "";
    public static String asset = "";
    public static String route = "";
    public static String txFrom = "";
    public static String txTo = "";

    public static void main(String[] args) {
        launch();

        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET"));
        Browser.driver.findElement(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("form button.button")).click();
    }

    public static void launch() {
        System.out.println("Browser.launch");

        env = Dotenv.load();

        ChromeOptions opt = new ChromeOptions();
        if (System.getProperty("os.name").startsWith("Windows")) {
            opt.setBinary("C:/Program Files/chrome-win64-114.0.5735.90/chrome.exe");
        } else {
            opt.setBinary("/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing");
        }
        opt.addArguments("user-data-dir=" + Paths.get("chrome_profile").toAbsolutePath());
        opt.addArguments("profile-directory=Default");
        ClientConfig config = ClientConfig.defaultConfig().readTimeout(Duration.ofHours(2));
        driver = new ChromeDriver(ChromeDriverService.createDefaultService(), opt, config);
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public static boolean metamaskWindowIsOpened() {
        return driver.getWindowHandles().toArray().length > 1;
    }

    public static void waitForMetamaskWindowToAppear() {
        waitForMetamaskWindowToAppear(60);
    }

    public static void waitForMetamaskWindowToAppear(int seconds) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        wait.until(d -> metamaskWindowIsOpened());
        switchToMetamaskWindow();
    }

    public static void waitForMetamaskWindowToDisappear() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> !metamaskWindowIsOpened());
        switchToMainWindow();
    }

    public static void switchToMetamaskWindow() {
        System.out.println("Switching to MetaMask window...");
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
        String date = (new Date()).toString();
        String s = date + ";" +
                Browser.route + ";" +
                Browser.amount + ";" + Browser.asset + ";" +
                Browser.fromNetwork + ";" + Browser.fromWallet + ";" +
                Browser.toNetwork + ";" + Browser.toWallet + ";" +
                status + "\n";
        try {
            File f = new File("results/results.csv");
            f.getParentFile().mkdirs();
            f.createNewFile();
            Files.write(f.toPath(), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void confirmTransactionInMetaMask() throws InterruptedException {
        Browser.waitForMetamaskWindowToAppear();

        System.out.println("Going to Approve adding new network (if MetaMask requires it)...");
        try {
            Browser.driver.findElement(By.xpath("//*[text()='Approve']")).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to confirm warning on Moonbase network (if MetaMask requires it)...");
        try {
            Browser.driver.findElement(By.xpath("//*[text()='Got it']")).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Going to Switch network (if MetaMask requires it)...");
        try {
            Browser.driver.findElement(By.xpath("//*[text()='Switch network']")).click();
            Thread.sleep(2000);
        } catch (NoSuchElementException ignore) {
        }
        System.out.println("Waiting for MetaMask window to appear...");
        Browser.waitForMetamaskWindowToAppear(600);

        WebElement metamaskFooterButton = Browser.driver.findElement(By.cssSelector("[data-testid='page-container-footer-next']"));
        Browser.scrollToElement(metamaskFooterButton);

        System.out.println("Confirming transaction in MetaMask...");
        String buttonText = metamaskFooterButton.getText();
        int tries = 60;
        do {
            System.out.println(formatter.format(new Date()) + " MetaMask button text: " + buttonText);

            if (buttonText.equals("Next")) {
                metamaskFooterButton.click();
            } else if (buttonText.equals("Approve")) {
                metamaskFooterButton.click();
                Browser.waitForMetamaskWindowToDisappear();
            } else if (buttonText.equals("Confirm")) {
                metamaskFooterButton.click();
                Browser.waitForMetamaskWindowToDisappear();
                break;
            }

            buttonText = "<no button>";
            Thread.sleep(1000);
            if (Browser.metamaskWindowIsOpened()) {
                Browser.switchToMetamaskWindow();
                try {
                    Browser.noImplicitWait();
                    metamaskFooterButton = Browser.driver.findElement(By.cssSelector("[data-testid='page-container-footer-next']"));
                    if (metamaskFooterButton.isDisplayed()) {
                        buttonText = metamaskFooterButton.getText();
                    }
                } catch (NoSuchElementException ignored) {
                }
                Browser.implicitlyWait();
            }
            tries = tries - 1;
        } while (tries > 0);

        System.out.println("Transaction was confirmed in MetaMask");
        Browser.switchToMainWindow();
    }
}
