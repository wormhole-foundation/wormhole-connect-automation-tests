package wh;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import support.Browser;

import java.time.Duration;

import static junit.framework.TestCase.assertTrue;

public class WormholeConnectSteps {

    @Given("I open wormhole-connect testnet")
    public void iOpenWormholeConnectTestnetPage() {
        Browser.url = Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET");
        Browser.driver.get(Browser.url);
    }

    @Given("I enter page password")
    public void iEnterPassword() {
        Browser.findElementAndWait(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.findElementAndWait(By.cssSelector("form button.button")).click();
    }

    @Given("I open wormhole-connect mainnet and enter password")
    public void iOpenWormholeConnectMainnetPageAndEnterPassword() {
        Browser.url = Browser.env.get("URL_WORMHOLE_CONNECT_MAINNET");
        Browser.driver.get(Browser.url);

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

        Browser.selectAssetInFromSection(Browser.fromWallet, Browser.fromNetwork, Browser.fromAsset);

        Browser.findElementAndWait(By.xpath("//*[text()='Connect wallet']")).click();
        Browser.findElementAndWait(By.xpath("//*[text()='" + toWallet + "']")).click();

        if (Browser.toWallet.equals("MetaMask") && !Browser.metaMaskWasUnlocked) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-password']")).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-submit']")).click();

            Browser.waitForMetamaskWindowToDisappear();
        }

        Browser.findElementAndWait(By.tagName("input")).sendKeys(amount);
        Thread.sleep(1000);

        Browser.fromBalance = Browser.findElementAndWaitToHaveNumber(By.xpath("(//*[text()='Balance']/following-sibling::*)[1]"));

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
        Browser.toBalance = Browser.findElementAndWaitToHaveNumber(By.xpath("(//*[text()='Balance']/following-sibling::*)[2]"));

        if (route.equals("automatic")) {
            // choose Manual and then again Automatic to enable native gas section
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Automatic Bridge')]")).click();
            Thread.sleep(1000);
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Manual Bridge')]")).click();
            Thread.sleep(1000);
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Automatic Bridge')]")).click();
            Thread.sleep(1000);
        } else if (route.equals("manual")) {
            Browser.findElementAndWait(By.xpath("//*[contains(text(),'Manual Bridge')]")).click();
        }

        Thread.sleep(3000); // wait UI to settle
    }

    @Then("I check balance has increased on destination chain")
    public void iCheckFinalBalance() throws InterruptedException {
        Browser.driver.get(Browser.url);

        Browser.selectAssetInFromSection(Browser.toWallet, Browser.toNetwork, Browser.toAsset);

        Browser.toFinalBalance = Browser.findElementAndWaitToHaveNumber(By.xpath("(//*[text()='Balance']/following-sibling::*)[1]"));

        if (Browser.route.equals("automatic")) {
            Browser.findElementAndWait(By.xpath("//*[contains(text(), '" + Browser.toAsset + "')]")).click();
            Browser.findElementAndWait(By.xpath("//*[text()='" + Browser.getNativeAssetByNetworkName(Browser.toNetwork) + "']")).findElement(By.xpath("../../..")).click();

            Browser.toFinalNativeBalance = Browser.findElementAndWaitToHaveNumber(By.xpath("(//*[text()='Balance']/following-sibling::*)[1]"));

            Assert.assertTrue("Balance should have increased", Double.parseDouble(Browser.toFinalBalance) > Double.parseDouble(Browser.toBalance));
            Assert.assertTrue("Native balance should have increased", Double.parseDouble(Browser.toFinalNativeBalance) > Double.parseDouble(Browser.toNativeBalance));
        }
    }

    @And("I check native balance on {string} using {string}")
    public void iCheckNativeBalanceOnUsing(String toNetwork, String toWallet) throws InterruptedException {
        Browser.selectAssetInFromSection(toWallet, toNetwork, Browser.getNativeAssetByNetworkName(toNetwork));

        Browser.toNativeBalance = Browser.findElementAndWaitToHaveNumber(By.xpath("(//*[text()='Balance']/following-sibling::*)[1]"));
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
        System.out.println("Going to confirm transaction...");

        if (Browser.fromWallet.equals("MetaMask")) {
            Browser.confirmTransactionInMetaMask();

        } else if (Browser.fromWallet.equals("Phantom")) {
            Browser.waitForMetamaskWindowToAppear();

            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-form-password-input']")).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
            Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-form-submit-button']")).click();

            Browser.findElementAndWait(By.cssSelector("[data-testid='primary-button']")).click(); // Confirm

            Browser.waitForMetamaskWindowToDisappear();
        } else if (Browser.fromWallet.equals("Sui")) {
            Browser.waitForMetamaskWindowToAppear();
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

    @Then("I should see Send From link")
    public void iShouldSeeSendFromLink() {
        String scanFrom = Browser.getScanLinkTextByNetworkName(Browser.fromNetwork);

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

            if (Browser.toWallet.equals("Phantom")) {
                Browser.waitForMetamaskWindowToAppear();

                Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-form-password-input']")).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
                Browser.findElementAndWait(By.cssSelector("[data-testid='unlock-form-submit-button']")).click();

                WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(900));
                webDriverWait
                        .until(webDriver -> {
                            if (Browser.metamaskWindowIsOpened()) {
                                Browser.switchToMetamaskWindow();
                                Browser.findElementAndWait(By.cssSelector("[data-testid='primary-button']")).click(); // Confirm
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ignore) {
                                }
                                return null;
                            }
                            Browser.switchToMainWindow();
                            return Browser.driver.findElement(By.xpath("//*[text()='The bridge is now complete.']"));
                        });

                Browser.waitForMetamaskWindowToDisappear();
            } else {
                Browser.confirmTransactionInMetaMask();
            }
        }
    }

    @Then("I should see Send To link")
    public void iShouldSeeSendToLink() {
        String scanTo = Browser.getScanLinkTextByNetworkName(Browser.toNetwork);

        if (Browser.route.equals("automatic")) {
            Browser.implicitlyWait(60 * 60);
        } else {
            Browser.implicitlyWait(60 * 30);
        }

        System.out.println("Waiting for the send to link...");
        WebElement sendToSection = Browser.findElementAndWait(By.xpath("//*[text()='Send to']/../following-sibling::*"));
        WebElement sendToLink = sendToSection.findElement(By.xpath("//*[text()='" + scanTo + "']"));

        assertTrue(sendToLink.isDisplayed());

        Browser.implicitlyWait();

        Browser.txTo = sendToLink.findElement(By.xpath("..")).getAttribute("href");

        System.out.println("Finished");
    }

    @And("I move slider")
    public void iMoveSlider() throws InterruptedException {
        Browser.moveSliderByOffset(220);
    }

}
