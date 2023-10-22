package src;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class ChromeExtUsingSelenium {
    public static void main(String[] args) throws InterruptedException {
        ChromeOptions opt = new ChromeOptions();
        opt.setBinary("/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing");
        opt.addArguments("user-data-dir=/Users/tatjana.sadovska/Library/Application Support/Chromium");
        opt.addArguments("profile-directory=Default");
        opt.addExtensions (new File("metamask-chrome-10.34.5.crx"));
        ChromeDriver driver = new ChromeDriver(opt);

        driver.get("https://wormhole-connect.netlify.app/");

        Thread.sleep(1000);
        driver.findElement(By.cssSelector("form [type='password']")).sendKeys(System.getenv("WH_PASSWORD"));
        driver.findElement(By.cssSelector("form button.button")).click();

        WebElement element = driver.findElement(By.xpath("//*[text()='Connect wallet']"));
        element.click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("//*[text()='Metamask']")).click();

        Thread.sleep(3000);

        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);

        System.out.println("URL: " + driver.getCurrentUrl());

        driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys(System.getenv("METAMASK_PASSWORD"));
        driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

        Thread.sleep(1000);

        driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);

        driver.findElement(By.xpath("//*[text()='Connect wallet']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[text()='Metamask']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='Alfajores']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='Fantom']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='Select']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='CELO']")).findElement(By.xpath("../../..")).click();
        Thread.sleep(500);

        driver.findElement(By.id("sendAmt")).sendKeys("0.1");
        Thread.sleep(2000);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 350)", "");
    }
}
