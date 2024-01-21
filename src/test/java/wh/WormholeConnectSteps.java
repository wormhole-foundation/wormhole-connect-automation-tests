package wh;

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

    String fromWallet = "";
    String toWallet = "";

    @Given("I open WH main page and enter password")
    public void iOpenWHMainPageAndEnterPassword() throws InterruptedException {
        Browser.driver.get("https://wormhole-connect.netlify.app/");

        Browser.driver.findElement(By.cssSelector("form [type='password']")).sendKeys(System.getenv("WH_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("form button.button")).click();
    }

    @And("I prepare to send {string} {string} from {string} using {string} to {string} using {string}")
    public void iPrepareToSendFromTo(String amount, String asset, String fromNetwork, String fromWallet, String toNetwork, String toWallet) throws InterruptedException {
        WebElement element = Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']"));
        element.click();

        Browser.driver.findElement(By.xpath("//*[text()='" + fromWallet + "']")).click();

        if (fromWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();
            Browser.switchToMetamaskWindow();

        Browser.driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys(System.getenv("METAMASK_PASSWORD"));
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
            Browser.switchToMainWindow();
        }

        Thread.sleep(1000);

        Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + toWallet + "']")).click();

        if (!fromWallet.equals("MetaMask") && toWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();
            Browser.switchToMetamaskWindow();

            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys("automation123");
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
            Browser.switchToMainWindow();
        }

        Browser.driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + fromNetwork + "']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='Select']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + asset + "']")).findElement(By.xpath("../../..")).click();
        Browser.driver.findElement(By.tagName("input")).sendKeys(amount);

        Thread.sleep(1000);
        try {
            // close popup
            Browser.driver.findElement(By.cssSelector("[data-testid='CloseIcon']")).click();
        } catch (Exception ignore) {
        }

        Browser.driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + toNetwork + "']")).click();

        Thread.sleep(3000); // wait UI to settle

        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
    }

    @When("I submit transfer")
    public void iSubmitTransfer() throws InterruptedException {
        WebElement approveButton = Browser.driver.findElement(By.xpath("//*[text()='Approve and proceed with transaction']"));
        Browser.scrollToElement(approveButton);

        approveButton.click();

        System.out.println("Waiting for MetaMask window to appear...");
        Browser.waitForMetamaskWindowToAppear();
        Browser.switchToMetamaskWindow();

        if (this.fromWallet.equals("MetaMask")) {
            System.out.println("Going to Approve adding new network (if MetaMask requires it)...");
            try {
                Browser.driver.findElement(By.xpath("//*[text()='Approve']")).click();
                Thread.sleep(5000);
            } catch (NoSuchElementException ignore) {
            }
            System.out.println("Going to Switch network (if MetaMask requires it)...");
            try {
                Browser.driver.findElement(By.xpath("//*[text()='Switch network']")).click();
                Thread.sleep(5000);
            } catch (NoSuchElementException ignore) {
            }

            System.out.println("Waiting for MetaMask window to appear...");
            Browser.waitForMetamaskWindowToAppear();
            Browser.switchToMetamaskWindow();

            WebElement metamaskFooterButton = Browser.driver.findElement(By.cssSelector("[data-testid='page-container-footer-next']"));
            Browser.scrollToElement(metamaskFooterButton);

        String buttonText = metamaskFooterButton.getText();
        int tries = 60;
        do {
            System.out.println(formatter.format(new Date()) + " MetaMask button text: " + buttonText);

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
        } else if (this.fromWallet.equals("Phantom")) {
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-form-password-input']")).sendKeys("automation123");
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-form-submit-button']")).click();

            Browser.driver.findElement(By.cssSelector("[data-testid='primary-button']")).click(); // Confirm

            Browser.waitForMetamaskWindowToDisappear();
        }

        Browser.switchToMainWindow();
    }

    @Then("I should see send from {string} link and send to {string} link")
    public void iShouldSeeFtmScanLink(String scanFrom, String scanTo) {
        Browser.implicitlyWait(60 * 30);

        WebElement sendFromLink = Browser.driver.findElement(By.xpath("//*[text()= '" + scanFrom + "' ]"));
        WebElement sendToLink = Browser.driver.findElement(By.xpath("//*[text()= '" + scanTo + "' ]"));

        assertTrue(sendToLink.isDisplayed());

        String fromTx = sendFromLink.findElement(By.xpath("..")).getAttribute("href");
        String toTx = sendToLink.findElement(By.xpath("..")).getAttribute("href");
        Browser.saveResults(fromTx, toTx);

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
