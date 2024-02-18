package wh;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ExtensionPage;
import pages.PasswordPage;
import pages.WormholePage;
import support.Browser;
import support.BrowserMainnet;

import java.time.Duration;

import static junit.framework.TestCase.assertTrue;

public class WormholeConnectSteps {

    @Given("I launch testnet browser")
    public void iLaunchBrowser() {
        Browser.launch();
    }

    @Given("I launch mainnet browser")
    public void iLaunchMainnetBrowser() {
        Browser.isMainnet = true;
        BrowserMainnet.launch();
    }

    @Given("I open wormhole-connect testnet")
    public void iOpenWormholeConnectTestnetPage() {
        Browser.url = Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET");
        Browser.driver.get(Browser.url);
    }

    @Given("I enter page password")
    public void iEnterPassword() {
        Browser.findElementAndWait(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.findElementAndWait(PasswordPage.button).click();
    }

    @Given("I open wormhole-connect mainnet")
    public void iOpenWormholeConnectMainnetPageAndEnterPassword() {
        Browser.url = Browser.env.get("URL_WORMHOLE_CONNECT_MAINNET");
        Browser.driver.get(Browser.url);
    }

    @Given("I open portal bridge mainnet")
    public void iOpenPortalBridgeMainnet() {
        Browser.url = Browser.env.get("URL_PORTAL_BRIDGE_MAINNET");
        Browser.driver.get(Browser.url);
    }

    @And("I prepare to send {string} {string} from {string}\\({string}) to {string}\\({string}) with {string} route")
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

        if (Browser.route.equals("automatic")) {
            Assert.assertNotEquals("Native balance was not checked", "", Browser.toNativeBalance);
        }

        Browser.selectAssetInFromSection(Browser.fromWallet, Browser.fromNetwork, Browser.fromAsset);

        Browser.findElementAndWait(WormholePage.DESTINATION_SELECT_NETWORK_BUTTON).click();
        Thread.sleep(1000);
        Browser.findElementAndWait(WormholePage.CHOOSE_NETWORK(Browser.toNetwork)).click();
        Thread.sleep(1000);

        Browser.findElementAndWait(WormholePage.DESTINATION_CONNECT_WALLET_BUTTON).click();
        Browser.findElementAndWait(WormholePage.CHOOSE_WALLET(Browser.toWallet)).click();

        if (Browser.toWallet.equals("MetaMask") && !Browser.metaMaskWasUnlocked) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElementAndWait(ExtensionPage.METAMASK_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
            Browser.findElementAndWait(ExtensionPage.METAMASK_UNLOCK_BUTTON).click();

            Browser.waitForExtensionWindowToDisappear();
            Browser.metaMaskWasUnlocked = true;
        }

        Browser.findElementAndWait(WormholePage.SOURCE_AMOUNT_INPUT).sendKeys(amount);
        Thread.sleep(1000);

        Browser.fromBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);

        try {
            Browser.findElementAndWait(WormholePage.POPUP_CLOSE_BUTTON).click();
        } catch (Exception ignore) {
        }

        Browser.toAsset = Browser.findElementAndWait(WormholePage.DESTINATION_ASSET_BUTTON).getText();
        Browser.toAsset = Browser.toAsset.split("\n")[0]; // "CELO\n(Alfajores)" -> "CELO"
        Browser.toAmount = Browser.findElementAndWait(WormholePage.DESTINATION_AMOUNT_INPUT).getAttribute("value");
        Browser.toBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.DESTINATION_BALANCE_TEXT);

        if (route.equals("automatic")) {
            // choose Manual and then again Automatic to enable native gas section
            Browser.findElementAndWait(WormholePage.AUTOMATIC_BRIDGE_OPTION).click();
            Thread.sleep(1000);
            Browser.findElementAndWait(WormholePage.MANUAL_BRIDGE_OPTION).click();
            Thread.sleep(1000);
            Browser.findElementAndWait(WormholePage.AUTOMATIC_BRIDGE_OPTION).click();
            Thread.sleep(1000);
        } else if (route.equals("manual")) {
            Browser.findElementAndWait(WormholePage.MANUAL_BRIDGE_OPTION).click();
        } else if (route.equals("cosmos")) {
            Browser.findElementAndWait(WormholePage.COSMOS_GATEWAY_OPTION).click();
        } else if (route.equals("circle-manual")) {
            Browser.findElementAndWait(WormholePage.CIRCLE_MANUAL_OPTION).click();
        } else if (route.equals("circle-automatic")) {
            Browser.findElementAndWait(WormholePage.CIRCLE_AUTOMATIC_OPTION).click();
        }

        Thread.sleep(3000); // wait UI to settle
    }

    @Then("I check balance has increased on destination chain")
    public void iCheckFinalBalance() throws InterruptedException {
        Browser.driver.get(Browser.url);

        System.out.println("Checking " + Browser.toAsset + " balance on " + Browser.toNetwork + " (" + Browser.toWallet + ")");
        Browser.selectAssetInFromSection(Browser.toWallet, Browser.toNetwork, Browser.toAsset);

        Browser.toFinalBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);
        System.out.println(Browser.toFinalBalance + " " + Browser.toAsset);

        if (Browser.route.equals("automatic")) {
            String nativeAsset = Browser.getNativeAssetByNetworkName(Browser.toNetwork);

            System.out.println("Checking native asset (" + nativeAsset + ") balance on " + Browser.toNetwork + " (" + Browser.toWallet + ")");
            Browser.findElementAndWait(WormholePage.OPEN_ASSET_LIST()).click();
            Browser.findElementAndWait(WormholePage.CHOOSE_ASSET(nativeAsset)).click();

            Browser.toFinalNativeBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);
            System.out.println(Browser.toFinalNativeBalance + " " + nativeAsset);

            Assert.assertTrue("Native balance should have increased", Double.parseDouble(Browser.toFinalNativeBalance) > Double.parseDouble(Browser.toNativeBalance));
        }
        Assert.assertTrue("Balance should have increased", Double.parseDouble(Browser.toFinalBalance) > Double.parseDouble(Browser.toBalance));
    }

    @And("I check native balance on {string} using {string}")
    public void iCheckNativeBalanceOnUsing(String toNetwork, String toWallet) throws InterruptedException {
        String nativeAsset = Browser.getNativeAssetByNetworkName(toNetwork);

        System.out.println("Checking native asset (" + nativeAsset + ") balance on " + toNetwork + " (" + toWallet + ")");
        Browser.selectAssetInFromSection(toWallet, toNetwork, nativeAsset);

        Browser.toNativeBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);
        System.out.println(Browser.toNativeBalance + " " + nativeAsset);
    }

    @When("I click on Approve button")
    public void iApproveTransfer() throws InterruptedException {
        WebElement approveButton = Browser.findElementAndWait(WormholePage.APPROVE_BUTTON);
        Browser.scrollToElement(approveButton);
        Thread.sleep(5000);
        approveButton.click();
        Thread.sleep(2000);
    }

    @When("I approve wallet notifications")
    public void iApproveWalletNotification() throws InterruptedException {
        System.out.println("Going to confirm transaction...");

        if (Browser.fromWallet.equals("MetaMask")) {
            Browser.confirmTransactionInMetaMask(false);

        } else if (Browser.fromWallet.equals("Phantom")) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElementAndWait(ExtensionPage.PHANTOM_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
            Browser.findElementAndWait(ExtensionPage.PHANTOM_SUBMIT_BUTTON).click();

            Browser.findElementAndWait(ExtensionPage.PHANTOM_PRIMARY_BUTTON).click(); // Confirm

            Browser.waitForExtensionWindowToDisappear();
        } else if (Browser.fromWallet.equals("Sui")) {
            Browser.waitForExtensionWindowToAppear();
            Browser.findElementAndWait(ExtensionPage.SUI_UNLOCK_TO_APPROVE_BUTTON).click();

            Browser.findElementAndWait(ExtensionPage.SUI_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SUI"));
            Browser.findElementAndWait(ExtensionPage.SUI_UNLOCK_BUTTON).click();
            Thread.sleep(1000);

            Browser.findElementAndWait(ExtensionPage.SUI_APPROVE_BUTTON).click();
            Thread.sleep(1000);

            try {
                Browser.findElementAndWait(ExtensionPage.SUI_DIALOG_APPROVE_BUTTON).click();
            } catch (NoSuchElementException ignore) {
            }
            Browser.waitForExtensionWindowToDisappear();
        } else if (Browser.fromWallet.equals("Leap")) {
            Browser.waitForExtensionWindowToAppear();
            Browser.findElementAndWait(ExtensionPage.LEAP_APPROVE_BUTTON).click();
            Thread.sleep(1000);

            Browser.waitForExtensionWindowToDisappear();
        }
    }

    @Then("I should see Send From link")
    public void iShouldSeeSendFromLink() {
        Browser.implicitlyWait(60 * 60);
        System.out.println("Waiting for the send from link...");
        WebElement sendFromLink = Browser.findElementAndWait(WormholePage.SOURCE_SCAN_LINK());
        Browser.implicitlyWait();

        Browser.txFrom = sendFromLink.getAttribute("href");
    }

    @Then("I should claim assets")
    public void iShouldClaimAssets() throws InterruptedException {
        if (Browser.route.equals("manual") || Browser.route.equals("circle-manual")) {
            Browser.implicitlyWait(60 * 60);
            System.out.println("Waiting for the Claim button...");
            Browser.findElementAndWait(WormholePage.CLAIM_BUTTON);
            System.out.println("Waiting to click on Claim button...");
            Thread.sleep(5000);
            Browser.findElementAndWait(WormholePage.CLAIM_BUTTON).click();
            Thread.sleep(2000);
            Browser.implicitlyWait();

            if (Browser.toWallet.equals("Phantom")) {
                Browser.waitForExtensionWindowToAppear();

                Browser.findElementAndWait(ExtensionPage.PHANTOM_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
                Browser.findElementAndWait(ExtensionPage.PHANTOM_SUBMIT_BUTTON).click();

                WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(900));
                webDriverWait
                        .until(webDriver -> {
                            if (Browser.extensionWindowIsOpened()) {
                                Browser.switchToExtensionWindow();
                                Browser.findElementAndWait(ExtensionPage.PHANTOM_PRIMARY_BUTTON).click(); // Confirm
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ignore) {
                                }
                                Browser.switchToMainWindow();
                                return null;
                            }
                            return Browser.driver.findElement(WormholePage.TRANSACTION_COMPLETE_MESSAGE);
                        });

                Browser.waitForExtensionWindowToDisappear();
            } else {
                Browser.confirmTransactionInMetaMask(true);
            }
        }
    }

    @Then("I should see Send To link")
    public void iShouldSeeSendToLink() {
        if (Browser.route.equals("automatic") || Browser.route.equals("circle-automatic")) {
            Browser.implicitlyWait(60 * 60);
        } else {
            if (Browser.toWallet.equals("Phantom")) {
                Browser.implicitlyWait();
            } else {
                Browser.implicitlyWait(60 * 30);
            }
        }

        System.out.println("Waiting for the send to link...");
        WebElement sendToLink = Browser.findElementAndWait(WormholePage.DESTINATION_SCAN_LINK());

        assertTrue(sendToLink.isDisplayed());

        Browser.implicitlyWait();

        Browser.txTo = sendToLink.getAttribute("href");

        System.out.println("Finished");
    }

    @And("I move slider")
    public void iMoveSlider() throws InterruptedException {
        Browser.moveSliderByOffset(220);
    }
}
