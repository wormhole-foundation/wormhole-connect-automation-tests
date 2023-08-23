package src;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeExtUsingSelenium {
    public static void main(String[] args) throws InterruptedException {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("user-data-dir=/Users/tatjana.sadovska/Library/Application Support/Google/Chrome");
        opt.addArguments("profile-directory=Default");
        ChromeDriver driver = new ChromeDriver(opt);

        driver.get("https://wormhole-connect.netlify.app/");

        synchronized (driver) {
            driver.wait(2000);
        }

        driver.findElement(By.xpath("//*[text()='Connect wallet']")).click();

        synchronized (driver) {
            driver.wait(3000);
        }

        String windowMain = (String) driver.getWindowHandles().toArray()[0];

        driver.findElement(By.xpath("//*[text()='Metamask']")).click();

        synchronized (driver) {
            driver.wait(3000);
        }

        String windowMetamask = (String) driver.getWindowHandles().toArray()[1];

        driver.switchTo().window(windowMetamask);

        System.out.println("we are at:");
        System.out.println(driver.getCurrentUrl());
    }
}
