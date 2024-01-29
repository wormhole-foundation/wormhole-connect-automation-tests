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

    @Given("I open wormhole-connect TESTNET and enter password")
    public void iOpenWormholeConnectTestnetPageAndEnterPassword() throws InterruptedException {
        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET"));

        Browser.driver.findElement(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("form button.button")).click();
    }
    @Given("I open wormhole-connect MAINNET and enter password")
    public void iOpenWormholeConnectMainnetPageAndEnterPassword() throws InterruptedException {
        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_MAINNET"));

        Browser.driver.findElement(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("form button.button")).click();
    }

    @And("I prepare to send {string} {string} from {string} using {string} to {string} using {string} via {string}")
    public void iPrepareToSendFromTo(String amount, String asset, String fromNetwork, String fromWallet, String toNetwork, String toWallet, String route) throws InterruptedException {
        Browser.fromWallet = fromWallet;
        Browser.toWallet = toWallet;
        Browser.fromNetwork = fromNetwork;
        Browser.toNetwork = toNetwork;
        Browser.amount = amount;
        Browser.asset = asset;
        Browser.route = route;

        WebElement element = Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']"));
        element.click();

        Browser.driver.findElement(By.xpath("//*[text()='" + fromWallet + "']")).click();

        if (Browser.fromWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
        }

        Thread.sleep(1000);

        Browser.driver.findElement(By.xpath("//*[text()='Connect wallet']")).click();
        Browser.driver.findElement(By.xpath("//*[text()='" + toWallet + "']")).click();

        if (!Browser.fromWallet.equals("MetaMask") && Browser.toWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
        }

        Browser.driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Thread.sleep(1000);
        Browser.driver.findElement(By.xpath("//*[text()='" + fromNetwork + "']")).click();
        Thread.sleep(1000);
        Browser.driver.findElement(By.xpath("//*[text()='Select']")).click();
        Thread.sleep(1000);
        Browser.driver.findElement(By.xpath("//*[text()='" + asset + "']")).findElement(By.xpath("../../..")).click();
        Thread.sleep(1000);
        Browser.driver.findElement(By.tagName("input")).sendKeys(amount);

        Thread.sleep(1000);
        try {
            // close popup
            Browser.driver.findElement(By.cssSelector("[data-testid='CloseIcon']")).click();
        } catch (Exception ignore) {
        }

        Browser.driver.findElement(By.xpath("//*[text()='Select network']")).click();
        Thread.sleep(1000);
        Browser.driver.findElement(By.xpath("//*[text()='" + toNetwork + "']")).click();
        Thread.sleep(1000);

        if (route.equals("automatic")) {
            Browser.driver.findElement(By.xpath("//*[contains(text(),'Automatic Bridge')]"));
            Thread.sleep(2000);
            Browser.driver.findElement(By.xpath("//*[contains(text(),'Automatic Bridge')]")).click();
        } else if (route.equals("manual")) {
            Browser.driver.findElement(By.xpath("//*[contains(text(),'Manual Bridge')]"));
            Thread.sleep(2000);
            Browser.driver.findElement(By.xpath("//*[contains(text(),'Manual Bridge')]")).click();
        }

        Thread.sleep(7000); // wait UI to settle
    }

    @When("I submit transfer")
    public void iSubmitTransfer() throws InterruptedException {
        WebElement approveButton = Browser.driver.findElement(By.xpath("//*[text()='Approve and proceed with transaction']"));
        Browser.scrollToElement(approveButton);
        Thread.sleep(5000);
        approveButton.click();
        Thread.sleep(2000);

        System.out.println("Waiting for MetaMask window to appear...");
        Browser.waitForMetamaskWindowToAppear();

        if (Browser.fromWallet.equals("MetaMask")) {
            Browser.confirmTransactionInMetaMask();

        } else if (Browser.fromWallet.equals("Phantom")) {
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-form-password-input']")).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
            Browser.driver.findElement(By.cssSelector("[data-testid='unlock-form-submit-button']")).click();

            Browser.driver.findElement(By.cssSelector("[data-testid='primary-button']")).click(); // Confirm

            Browser.waitForMetamaskWindowToDisappear();
        }
    }

    @Then("I should see send from {string} link")
    public void iShouldSeeSendFromLink(String scanFrom) {
        Browser.implicitlyWait(60 * 60);
        System.out.println("Waiting for the send from link...");
        WebElement sendFromLink = Browser.driver.findElement(By.xpath("//*[text()= '" + scanFrom + "' ]"));
        Browser.implicitlyWait();

        Browser.txFrom = sendFromLink.findElement(By.xpath("..")).getAttribute("href");
    }

    @Then("I should claim assets")
    public void iShouldClaimAssets() throws InterruptedException {
        if (Browser.route.equals("manual")) {
            Browser.implicitlyWait(60 * 60);
            System.out.println("Waiting for the Claim button...");
            Browser.driver.findElement(By.xpath("//*[text()='Claim']"));
            System.out.println("Waiting to click on Claim button...");
            Thread.sleep(5000);
            Browser.driver.findElement(By.xpath("//*[text()='Claim']")).click();
            Thread.sleep(2000);
            Browser.implicitlyWait();

            Browser.confirmTransactionInMetaMask();
        }
    }

    @Then("I should see send to {string} link")
    public void iShouldSeeSendToLink(String scanTo) {
        if (Browser.route.equals("automatic")) {
            Browser.implicitlyWait(60 * 60);
        } else {
            Browser.implicitlyWait(60 * 30);
        }

        System.out.println("Waiting for the send to link...");
        WebElement sendToLink = Browser.driver.findElement(By.xpath("//*[text()='" + scanTo + "']"));

        assertTrue(sendToLink.isDisplayed());

        Browser.txTo = sendToLink.findElement(By.xpath("..")).getAttribute("href");

        System.out.println("Finished");
    }
}
