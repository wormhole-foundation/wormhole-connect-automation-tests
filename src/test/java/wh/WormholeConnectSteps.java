package wh;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import support.Browser;

import static junit.framework.TestCase.assertTrue;

public class WormholeConnectSteps {

    @Given("I open wormhole-connect TESTNET and enter password")
    public void iOpenWormholeConnectTestnetPageAndEnterPassword() throws InterruptedException {
        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET"));

        Browser.findElementAndWait(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.findElementAndWait(By.cssSelector("form button.button")).click();
    }

    @Given("I open wormhole-connect MAINNET and enter password")
    public void iOpenWormholeConnectMainnetPageAndEnterPassword() throws InterruptedException {
        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_MAINNET"));

        Browser.findElementAndWait(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.findElementAndWait(By.cssSelector("form button.button")).click();
    }

    @And("I prepare to send {string} {string} from {string} using {string} to {string} using {string} via {string}")
    public void iPrepareToSendFromTo(String amount, String asset, String fromNetwork, String fromWallet, String toNetwork, String toWallet, String route) throws InterruptedException {
        Browser.fromWallet = fromWallet;
        Browser.toWallet = toWallet;
        Browser.fromNetwork = fromNetwork;
        Browser.toNetwork = toNetwork;
        Browser.fromAmount = amount;
        Browser.fromAsset = asset;
        Browser.route = route;
        Browser.txFrom = "";
        Browser.txTo = "";

        Browser.findElementAndWait(By.xpath("//*[text()='Connect wallet']")).click();

        Browser.findElementAndWait(By.xpath("//*[text()='" + fromWallet + "']")).click();

        if (Browser.fromWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-submit']")).click();

            try {
                System.out.println("Going to Reject a pending transaction (if it exists)...");
                Browser.implicitlyWait(3);
                Browser.findElementAndWait(By.cssSelector("[data-testid='page-container-footer-cancel']")).click();
                Browser.implicitlyWait();
            } catch (NoSuchElementException ignore) {
            }

            Browser.waitForMetamaskWindowToDisappear();
        }

        Thread.sleep(1000);

        Browser.findElementAndWait(By.xpath("//*[text()='Connect wallet']")).click();
        Browser.findElementAndWait(By.xpath("//*[text()='" + toWallet + "']")).click();

        if (!Browser.fromWallet.equals("MetaMask") && Browser.toWallet.equals("MetaMask")) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
        }

        Browser.findElementAndWait(By.xpath("//*[text()='Select network']")).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(By.xpath("//*[text()='" + fromNetwork + "']")).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(By.xpath("//*[text()='Select']")).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(By.xpath("//*[text()='" + asset + "']")).findElement(By.xpath("../../..")).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(By.tagName("input")).sendKeys(amount);
        Thread.sleep(1000);

        Browser.fromBalance = Browser.findElementAndWait(By.xpath("(//*[text()='Balance']/following-sibling::*)[1]")).getText();

        try {
            // close popup
            Browser.findElementAndWait(By.cssSelector("[data-testid='CloseIcon']")).click();
        } catch (Exception ignore) {
        }

        Browser.findElementAndWait(By.xpath("//*[text()='Select network']")).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(By.xpath("//*[text()='" + toNetwork + "']")).click();
        Thread.sleep(1000);

        Browser.toAsset = Browser.findElementAndWait(By.xpath("(//*[text()='Asset']/following-sibling::*)[2]")).getText();
        Browser.toAsset = Browser.toAsset.split("\n")[0]; // "CELO\n(Alfajores)" -> "CELO"
        Browser.toAmount = Browser.findElementAndWait(By.xpath("(//*[text()='Amount']/following-sibling::*/input)[2]")).getAttribute("value");
        Browser.toBalance = Browser.findElementAndWait(By.xpath("(//*[text()='Balance']/following-sibling::*)[2]")).getText();

        if (route.equals("automatic")) {
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Automatic Bridge')]"));
            Thread.sleep(2000);
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Automatic Bridge')]")).click();
        } else if (route.equals("manual")) {
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Manual Bridge')]"));
            Thread.sleep(2000);
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Manual Bridge')]")).click();
        }

        Thread.sleep(7000); // wait UI to settle
    }

    @Then("I check balance on destination chain")
    public void iCheckFinalBalance() throws InterruptedException {
        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET"));

        Browser.findElementAndWait(By.xpath("//*[text()='Connect wallet']")).click();

        Browser.findElementAndWait(By.xpath("//*[text()='" + Browser.toWallet + "']")).click();

        Browser.findElementAndWait(By.xpath("//*[text()='Select network']")).click();
        Browser.findElementAndWait(By.xpath("//*[text()='" + Browser.toNetwork + "']")).click();
        Browser.findElementAndWait(By.xpath("//*[text()='Select']")).click();
        Browser.findElementAndWait(By.xpath("//*[text()='" + Browser.toAsset + "']")).findElement(By.xpath("../../..")).click();

        Browser.toFinalBalance = Browser.findElementAndWait(By.xpath("(//*[text()='Balance']/following-sibling::*)[1]")).getText();
    }

    @When("I click on Approve button")
    public void iApproveTransfer() throws InterruptedException {
        WebElement approveButton = Browser.findElementAndWait(By.xpath("//*[text()='Approve and proceed with transaction']"));
        Browser.scrollToElement(approveButton);
        Thread.sleep(5000);
        approveButton.click();
        Thread.sleep(2000);
    }

    @When("I approve wallet notifications")
    public void iApproveWalletNotification() throws InterruptedException {
        System.out.println("Waiting for MetaMask window to appear...");
        Browser.waitForMetamaskWindowToAppear();

        if (Browser.fromWallet.equals("MetaMask")) {
            Browser.confirmTransactionInMetaMask();

        } else if (Browser.fromWallet.equals("Phantom")) {
            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-form-password-input']")).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-form-submit-button']")).click();

            Browser.findElementAndWait(By.cssSelector("[data-testid='primary-button']")).click(); // Confirm

            Browser.waitForMetamaskWindowToDisappear();
        } else if (Browser.fromWallet.equals("Sui")) {
            Browser.findElementAndWait(By.xpath("//*[text()='Unlock to Approve']/..")).click();

            Browser.findElementAndWait(By.xpath("//*[@name='password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_SUI"));
            Browser.findElementAndWait(By.xpath("//*[@role='dialog']//*[text()='Unlock']/..")).click();
            Thread.sleep(1000);

            Browser.findElementAndWait(By.xpath("//*[text()='Approve']/..")).click();
            Thread.sleep(1000);

            try {
                Browser.findElementAndWait(By.xpath("//*[@role='dialog']//*[text()='Approve']/..")).click();
            } catch (NoSuchElementException ignore) {
            }
            Browser.waitForMetamaskWindowToDisappear();
        }
    }

    @Then("I should see send from {string} link")
    public void iShouldSeeSendFromLink(String scanFrom) throws InterruptedException {
        Browser.implicitlyWait(60 * 60);
        System.out.println("Waiting for the send from link...");
        WebElement sendFromLink = Browser.findElementAndWait(By.xpath("//*[text()= '" + scanFrom + "' ]"));
        Browser.implicitlyWait();

        Browser.txFrom = sendFromLink.findElement(By.xpath("..")).getAttribute("href");
    }

    @Then("I should claim assets")
    public void iShouldClaimAssets() throws InterruptedException {
        if (Browser.route.equals("manual")) {
            Browser.implicitlyWait(60 * 60);
            System.out.println("Waiting for the Claim button...");
            Browser.findElementAndWait(By.xpath("//*[text()='Claim']"));
            System.out.println("Waiting to click on Claim button...");
            Thread.sleep(5000);
            Browser.findElementAndWait(By.xpath("//*[text()='Claim']")).click();
            Thread.sleep(2000);
            Browser.implicitlyWait();

            Browser.confirmTransactionInMetaMask();
        }
    }

    @Then("I should see send to {string} link")
    public void iShouldSeeSendToLink(String scanTo) throws InterruptedException {
        if (Browser.route.equals("automatic")) {
            Browser.implicitlyWait(60 * 60);
        } else {
            Browser.implicitlyWait(60 * 30);
        }

        System.out.println("Waiting for the send to link...");
        WebElement sendToLink = Browser.findElementAndWait(By.xpath("//*[text()='" + scanTo + "']"));

        assertTrue(sendToLink.isDisplayed());

        Browser.txTo = sendToLink.findElement(By.xpath("..")).getAttribute("href");

        System.out.println("Finished");
    }
}
