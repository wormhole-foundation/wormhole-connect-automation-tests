package src;

import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class TransferNativeTokenTest extends TestCase {

    private ChromeDriver driver;
    private boolean success;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        ChromeOptions opt = new ChromeOptions();
        opt.setBinary("/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing");
        opt.addArguments("user-data-dir=/Users/tatjana.sadovska/Library/Application Support/Chromium");
        opt.addArguments("profile-directory=Default");
        driver = new ChromeDriver(opt);

        driver.get("https://wormhole-connect.netlify.app/");

        Thread.sleep(1000);
        driver.findElement(By.cssSelector("form [type='password']")).sendKeys(System.getenv("WH_PASSWORD"));
        driver.findElement(By.cssSelector("form button.button")).click();

        success = false;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        if (success) {
            driver.quit();
        }
    }

    public void testAlfajoresToFantom() throws InterruptedException {
        transferFromTo("Alfajores", "Fantom", "CELO", "0.3");
    }

    public void testAlfajoresToMoonbase() throws InterruptedException {
        transferFromTo("Alfajores", "Moonbase", "CELO", "0.3");
    }

    private void transferFromTo(String fromNetwork, String toNetwork, String asset, String amount) throws InterruptedException {
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

        driver.findElement(By.xpath("//*[text()='"+fromNetwork+"']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='BSC']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//*[text()='Select']")).click();
        Thread.sleep(500);

        Thread.sleep(1000);

        driver.findElement(By.xpath("//*[text()='"+asset+"']")).findElement(By.xpath("../../..")).click();
        Thread.sleep(500);

        driver.findElement(By.tagName("input")).sendKeys(amount);

        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[text()='BSC']")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[text()='"+toNetwork+"']")).click();

        Thread.sleep(3000);

        WebElement approveButton = driver.findElement(By.xpath("//*[text()='Approve and proceed with transaction']"));

        Actions actions = new Actions(driver);
        actions.moveToElement(approveButton);
        actions.perform();

        approveButton.click();

        System.out.println("URL: " + driver.getCurrentUrl());

        Thread.sleep(5000);
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);

        Thread.sleep(1500);

        WebElement metamaskFooterButton = driver.findElement(By.cssSelector("[data-testid='page-container-footer-next']"));

        actions = new Actions(driver);
        actions.moveToElement(metamaskFooterButton);
        actions.perform();

        String buttonText = metamaskFooterButton.getText();
        int tries = 10;
        do {
            System.out.println("Metamask button text: " + buttonText);

            if (buttonText.equals("Next")) {
                tries = 10;
                metamaskFooterButton.click();
                Thread.sleep(5000);
            } else if (buttonText.equals("Approve")) {
                tries = 10;
                metamaskFooterButton.click();
                Thread.sleep(12000);
            } else if (buttonText.equals("Confirm")) {
                metamaskFooterButton.click();
                Thread.sleep(25000);
                break;
            }

            buttonText = "";
            Thread.sleep(1000);
            if (driver.getWindowHandles().toArray().length > 1) {
                driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);
                try {
                    metamaskFooterButton = driver.findElement(By.cssSelector("[data-testid='page-container-footer-next']"));
                    if (metamaskFooterButton.isDisplayed()) {
                        buttonText = metamaskFooterButton.getText();
                    }
                } catch (NoSuchElementException ex) {
                }
            }
            tries = tries - 1;
        } while (tries > 0);

        driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);

        if (fromNetwork.equals("Alfajores")) {
            WebElement sendFromLink = driver.findElement(By.xpath("//*[text()='Celo Explorer']"));
            assertTrue(sendFromLink.isDisplayed());
        }
        if (toNetwork.equals("Fantom")) {
            WebElement sendToLink = driver.findElement(By.xpath("//*[text()='FtmScan']"));
            assertTrue(sendToLink.isDisplayed());
        }
        if (toNetwork.equals("Moonbase")) {
            WebElement sendToLink = driver.findElement(By.xpath("//*[text()='Moonscan']"));
            assertTrue(sendToLink.isDisplayed());
        }

        System.out.println("Finished");

        success = true;
    }
}
