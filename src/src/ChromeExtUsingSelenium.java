package src;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeExtUsingSelenium {
    public static void main(String[] args) throws InterruptedException {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("user-data-dir=/Users/tatjana.sadovska/Library/Application Support/Google/Chrome");
        opt.addArguments("profile-directory=Default");
        ChromeDriver driver = new ChromeDriver(opt);

        driver.get("https://google.com/");
    }
}
