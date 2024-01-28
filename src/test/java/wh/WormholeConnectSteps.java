package wh;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import support.Browser;

import static junit.framework.TestCase.assertTrue;

public class WormholeConnectSteps {

    String fromWallet = "";
    String toWallet = "";
    String fromNetwork = "";
    String toNetwork = "";
    String amount = "";
    String asset = "";
    String route = "";

    @Given("I open WH main page and enter password")
    public void iOpenWHMainPageAndEnterPassword() throws InterruptedException {
        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET"));

        Browser.driver.findElement(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("form button.button")).click();
    }

    @And("I prepare to send {string} {string} from {string} using {string} to {string} using {string} via {string}")
    public void iPrepareToSendFromTo(String amount, String asset, String fromNetwork, String fromWallet, String toNetwork, String toWallet, String route) throws InterruptedException {
        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
        this.fromNetwork = fromNetwork;
        this.toNetwork = toNetwork;
        this.amount = amount;
        this.asset = asset;
        this.route = route;

        WebElement element = Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']"));
        element.click();

        Browser.driver.findElement(By.xpath("//*[text()='" + fromWallet + "']")).click();

        if (this.fromWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
        }

        Thread.sleep(1000);

        Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + toWallet + "']")).click();

        if (!this.fromWallet.equals("MetaMask") && this.toWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
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

        if (route.equals("automatic")) {
            Browser.driver.findElement(By.xpath("//*[contains(text(),'Automatic Bridge')]")).click();
        } else if (route.equals("manual")) {
            Browser.driver.findElement(By.xpath("//*[contains(text(),'Manual Bridge')]")).click();
        }

        Thread.sleep(3000); // wait UI to settle
    }

    @When("I submit transfer")
    public void iSubmitTransfer() throws InterruptedException {
        WebElement approveButton = Browser.driver.findElement(By.xpath("//*[text()='Approve and proceed with transaction']"));
        Browser.scrollToElement(approveButton);

        approveButton.click();

        System.out.println("Waiting for MetaMask window to appear...");
        Browser.waitForMetamaskWindowToAppear();

        if (this.fromWallet.equals("MetaMask")) {
            Browser.confirmTransactionInMetaMask();

        } else if (this.fromWallet.equals("Phantom")) {
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-form-password-input']")).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-form-submit-button']")).click();

            Browser.driver.findElement(By.cssSelector("[data-testid='primary-button']")).click(); // Confirm

            Browser.waitForMetamaskWindowToDisappear();
        }
    }

    @Then("I should see send from {string} link and send to {string} link")
    public void iShouldSeeFtmScanLink(String scanFrom, String scanTo) throws InterruptedException {
        if (this.route.equals("automatic")) {
            Browser.implicitlyWait(60 * 30);
        } else if (this.route.equals("manual")) {
            Browser.implicitlyWait(60 * 30);
            Browser.driver.findElement(By.xpath("//*[text()='Claim']")).click();
            Browser.implicitlyWait();

            Browser.confirmTransactionInMetaMask();

            Browser.implicitlyWait(60 * 3);
        }

        WebElement sendFromLink = Browser.driver.findElement(By.xpath("//*[text()= '" + scanFrom + "' ]"));
        WebElement sendToLink = Browser.driver.findElement(By.xpath("//*[text()= '" + scanTo + "' ]"));

        assertTrue(sendToLink.isDisplayed());

        String fromTx = sendFromLink.findElement(By.xpath("..")).getAttribute("href");
        String toTx = sendToLink.findElement(By.xpath("..")).getAttribute("href");

        String scenarioText = "Sent " + this.amount + " " + this.asset +
                " from " + this.fromNetwork + " (" + this.fromWallet + ")" +
                " to " + this.toNetwork + " (" + this.toWallet + "). " +
                "Route: " + this.route;
        Browser.saveResults(scenarioText, fromTx, toTx);

        System.out.println("Finished");
    }
}
