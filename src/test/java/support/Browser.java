package support;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Browser {
    public static ChromeDriver driver;

    public static void main(String[] args) {
        launch();
    }

    public static void launch() {
        System.out.println("Browser.launch");

        ChromeOptions opt = new ChromeOptions();
        if (System.getProperty("os.name").startsWith("Windows")) {
            opt.setBinary("C:/Program Files/chrome-win64-114.0.5735.90/chrome.exe");
        } else {
            opt.setBinary("/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing");
        }
        opt.addArguments("user-data-dir=" + Paths.get("chrome_profile").toAbsolutePath());
        opt.addArguments("profile-directory=Default");
        driver = new ChromeDriver(opt);
        implicitlyWait();
    }

    public static void quit() {
        driver.quit();
    }

    public static void noImplicitWait() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    public static void implicitlyWait() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public static void implicitlyWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public static boolean metamaskWindowIsOpened() {
        return driver.getWindowHandles().toArray().length > 1;
    }

    public static void waitForMetamaskWindowToAppear() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(d -> metamaskWindowIsOpened());
    }

    public static void waitForMetamaskWindowToDisappear() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> !metamaskWindowIsOpened());
    }

    public static void switchToMetamaskWindow() {
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
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File destination = new File("screenshots/" + date + "_test_failed.png");

        try {
            FileUtils.copyFile(screenshotFile, destination);

            System.out.println("Saved screenshot to: " + destination.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
