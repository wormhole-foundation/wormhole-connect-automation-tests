package wh;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import support.Browser;

import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;

public class WormholeConnectSteps {

    SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

    @Given("I open WH main page and enter password")
    public void iOpenWHMainPageAndEnterPassword() throws InterruptedException {
        Browser.driver.get("https://wormhole-connect.netlify.app/");

        Browser.driver.findElement(By.cssSelector("form [type='password']")).sendKeys(System.getenv("WH_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("form button.button")).click();
    }

    @And("I prepare to send {string} {string} from {string} to {string}")
    public void iPrepareToSendFromTo(String amount, String asset, String fromNetwork, String toNetwork) throws InterruptedException {
        WebElement element = Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']"));
        element.click();

        Browser.driver.findElement(By.xpath("//*[text()='Metamask']")).click();

        Browser.waitForMetamaskWindowToAppear();
        Browser.switchToMetamaskWindow();

        Browser.driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys(System.getenv("METAMASK_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

        Browser.waitForMetamaskWindowToDisappear();
        Browser.switchToMainWindow();

        Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='Metamask']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + fromNetwork + "']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='BSC']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='Select']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + asset + "']")).findElement(By.xpath("../../..")).click();
        Browser.driver.findElement(By.tagName("input")).sendKeys(amount);
        Browser.driver.findElement(By.xpath("//*[text()='BSC']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + toNetwork + "']")).click();

        Thread.sleep(3000); // wait UI to settle
    }

    @When("I submit transfer")
    public void iSubmitTransfer() throws InterruptedException {
        WebElement approveButton = Browser.driver.findElement(By.xpath("//*[text()='Approve and proceed with transaction']"));
        Browser.scrollToElement(approveButton);

        approveButton.click();

        Browser.waitForMetamaskWindowToAppear();
        Browser.switchToMetamaskWindow();

        try {
            Browser.driver.findElement(By.xpath("//*[text()='Switch network']")).click();
            Thread.sleep(5000);
        } catch (NoSuchElementException ignore) {
        }

        Browser.waitForMetamaskWindowToAppear();
        Browser.switchToMetamaskWindow();

        WebElement metamaskFooterButton = Browser.driver.findElement(By.cssSelector("[data-testid='page-container-footer-next']"));
        Browser.scrollToElement(metamaskFooterButton);

        String buttonText = metamaskFooterButton.getText();
        int tries = 60;
        do {
            System.out.println(formatter.format(new Date()) + " Metamask button text: " + buttonText);

            if (buttonText.equals("Next")) {
                metamaskFooterButton.click();
                // Browser.waitForMetamaskWindowToDisappear();
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

        Browser.switchToMainWindow();
    }

    @Then("I should see FtmScan link")
    public void iShouldSeeFtmScanLink() {
        Browser.implicitlyWait(120);
        WebElement sendToLink = Browser.driver.findElement(By.xpath("//*[text()='FtmScan']"));
        assertTrue(sendToLink.isDisplayed());

        System.out.println("Finished");
    }

    @Then("I claim token")
    public void iClaimToken() throws InterruptedException {
        Browser.implicitlyWait(180);
        Browser.driver.findElement(By.xpath("//*[text()='Claim']")).click();
        Browser.implicitlyWait();

        Browser.waitForMetamaskWindowToAppear();
        Browser.switchToMetamaskWindow();

        Browser.driver.findElement(By.xpath("//*[text()='Switch network']")).click();

        Thread.sleep(5000);

        Browser.waitForMetamaskWindowToAppear();
        Browser.switchToMetamaskWindow();

        WebElement metamaskFooterButton = Browser.driver.findElement(By.cssSelector("[data-testid='page-container-footer-next']"));
        Browser.scrollToElement(metamaskFooterButton);

        String buttonText = metamaskFooterButton.getText();
        int tries = 60;
        do {
            System.out.println(formatter.format(new Date()) + " Metamask button text: " + buttonText);

            if (buttonText.equals("Next")) {
                metamaskFooterButton.click();
                // Browser.waitForMetamaskWindowToDisappear();
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

        Browser.switchToMainWindow();
    }
}
