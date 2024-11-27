package steps;

import pages.WormholePage;
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
import support.Browser;
import support.BrowserMainnet;
import support.TestCase;

import java.time.Duration;

import static junit.framework.TestCase.assertTrue;

/**
 * @deprecated
 */
public class WormholeConnectSteps {

    @Given("I launch testnet browser")
    public void iLaunchBrowser() {
        Browser.launch();
    }

    @Given("I launch mainnet browser")
    public void iLaunchMainnetBrowser() {
        TestCase.isMainnet = true;
        BrowserMainnet.launch();
    }

    @Given("I open wormhole-connect testnet")
    public void iOpenWormholeConnectTestnetPage() {
        TestCase.url = Browser.env.get("URL_WORMHOLE_CONNECT_TESTNET");
        Browser.driver.get(TestCase.url);
    }

    @Given("I open wormhole-connect mainnet")
    public void iOpenWormholeConnectMainnetPageAndEnterPassword() {
        TestCase.url = Browser.env.get("URL_WORMHOLE_CONNECT_MAINNET");
        Browser.driver.get(TestCase.url);
    }

    @Given("I open {string} URL")
    public void iOpenURL(String url) {
        TestCase.url = url;
        Browser.driver.get(TestCase.url);
    }

    @Given("I enter page password")
    public void iEnterPassword() {
        if (TestCase.url.contains("netlify.app")) {
            Browser.findElement(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
            Browser.findElement(PasswordPage.button).click();
        }
    }

    @Given("I open portal bridge mainnet")
    public void iOpenPortalBridgeMainnet() {
        TestCase.url = Browser.env.get("URL_PORTAL_BRIDGE_MAINNET");
        Browser.driver.get(TestCase.url);
    }

    @And("I prepare to send {string} {string} from {string}\\({string}) to {string}\\({string}) with {string} route")
    public void iFillInTransactionDetails(String amount, String asset, String fromNetwork, String fromWallet, String toNetwork, String toWallet, String route) throws InterruptedException {
        TestCase.sourceWallet = fromWallet;
        TestCase.destinationWallet = toWallet;
        TestCase.sourceChain = fromNetwork;
        TestCase.destinationChain = toNetwork;
        TestCase.sourceAmount = amount;
        TestCase.sourceToken = asset;
        TestCase.route = route;
        TestCase.wormholescanLink = "";
        TestCase.txTo = "";

        System.out.println("I prepare to send " + TestCase.sourceAmount + " " + TestCase.sourceToken + " from " + TestCase.sourceChain + " to " + TestCase.destinationWallet);

        if (TestCase.convertingNativeBalance) {
            Assert.assertNotEquals("Starting native balance was not checked", "", TestCase.toNativeBalance);
        }

        Browser.selectAssetInFromSection(TestCase.sourceWallet, TestCase.sourceChain, TestCase.sourceToken);

        Browser.findElement(WormholePage.DESTINATION_SELECT_NETWORK_BUTTON).click();
        Thread.sleep(1000);
        Browser.findElement(WormholePage.CHOOSE_NETWORK(TestCase.destinationChain)).click();
        Thread.sleep(3000); // wait for wallet auto-connect

        if (Browser.elementAppears(1, WormholePage.DESTINATION_CONNECT_WALLET_BUTTON)) {
            Browser.findElement(WormholePage.DESTINATION_CONNECT_WALLET_BUTTON).click();
            Browser.findElement(WormholePage.CHOOSE_WALLET(TestCase.destinationWallet)).click();

            if (TestCase.destinationWallet.equals("MetaMask") && !TestCase.metaMaskWasUnlocked) {
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.METAMASK_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_METAMASK"));
                Browser.findElement(ExtensionPage.METAMASK_UNLOCK_BUTTON).click();

                Browser.waitForExtensionWindowToDisappear();
                TestCase.metaMaskWasUnlocked = true;
            }

            if (TestCase.destinationWallet.equals("Leap") && !TestCase.leapWasUnlocked) {
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.LEAP_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_LEAP"));
                Browser.findElement(ExtensionPage.LEAP_UNLOCK_BUTTON).click();

                Browser.waitForExtensionWindowToDisappear();
                Thread.sleep(1000);

                TestCase.leapWasUnlocked = true;
            }
        }

        Browser.findElement(WormholePage.SOURCE_AMOUNT_INPUT).sendKeys(amount);
        Thread.sleep(1000);

        TestCase.fromBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);

        try {
            Browser.findElement(WormholePage.POPUP_CLOSE_BUTTON).click();
        } catch (Exception ignore) {
        }

        TestCase.destinationToken = Browser.findElement(WormholePage.DESTINATION_ASSET_BUTTON).getText();
        TestCase.destinationToken = TestCase.destinationToken.split("\n")[0]; // "CELO\n(Alfajores)" -> "CELO"
        if (TestCase.route.equals("eth-bridge-automatic") || TestCase.route.equals("wst-eth-bridge-automatic")) {
            TestCase.destinationAmount = Browser.findElement(WormholePage.DESTINATION_AMOUNT_INPUT_ETH_BRIDGE).getAttribute("value");
        } else {
            TestCase.destinationAmount = Browser.findElement(WormholePage.DESTINATION_AMOUNT_INPUT).getAttribute("value");
        }

        // work around to show balance in To section
        Browser.findElement(WormholePage.DESTINATION_ASSET_BUTTON).click();
        Browser.findElement(WormholePage.POPUP_CLOSE_BUTTON).click();
        // end

        TestCase.destinationBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.DESTINATION_BALANCE_TEXT);

        switch (route) {
            case "xlabs-bridge-automatic":
            case "circle-automatic":
                // choose Manual and then again Automatic to enable native gas section
                Browser.findElement(WormholePage.AUTOMATIC_BRIDGE_OPTION).click();
                Thread.sleep(1000);
                Browser.findElement(WormholePage.MANUAL_BRIDGE_OPTION).click();
                Thread.sleep(1000);
                Browser.findElement(WormholePage.AUTOMATIC_BRIDGE_OPTION).click();
                Thread.sleep(1000);
                break;
            case "circle-cctp-automatic":
                // choose Manual and then again Automatic to enable native gas section
                Browser.findElement(WormholePage.CCTP_AUTOMATIC_BRIDGE_OPTION).click();
                Thread.sleep(1000);
                Browser.findElement(WormholePage.CCTP_MANUAL_BRIDGE_OPTION).click();
                Thread.sleep(1000);
                Browser.findElement(WormholePage.CCTP_AUTOMATIC_BRIDGE_OPTION).click();
                Thread.sleep(1000);
                break;
            case "wormhole-bridge-manual":
            case "circle-manual":
                Browser.findElement(WormholePage.MANUAL_BRIDGE_OPTION).click();
                break;
            case "circle-cctp-manual":
                Browser.findElement(WormholePage.CCTP_MANUAL_BRIDGE_OPTION).click();
                break;
            case "cosmos-manual":
                Browser.findElement(WormholePage.COSMOS_MANUAL_GATEWAY_OPTION).click();
                break;
            case "cosmos-automatic":
                Browser.findElement(WormholePage.COSMOS_AUTOMATIC_GATEWAY_OPTION).click();
                break;
            case "eth-bridge-automatic":
                Browser.findElement(WormholePage.ETH_BRIDGE_AUTOMATIC_OPTION).click();
                break;
            case "wst-eth-bridge-automatic":
                Browser.findElement(WormholePage.ETH_BRIDGE_AUTOMATIC_OPTION).click();
                break;
            case "route-option-nttRelay":
                Browser.findElement(WormholePage.NTT_AUTOMATIC_OPTION).click();
                break;
            case "route-option-nttManual":
                Browser.findElement(WormholePage.NTT_MANUAL_OPTION).click();
                break;
        }

        Thread.sleep(3000); // wait UI to settle
    }

    @Then("I check balance has increased on destination chain")
    public void iCheckFinalBalance() throws InterruptedException {
        Browser.driver.get(TestCase.url);
        if (TestCase.destinationChain.equals("Solana")) {
            System.out.println("Waiting 20 seconds to receive asset on Solana");
            Thread.sleep(20000);
        }
        System.out.println("Checking " + TestCase.destinationToken + " balance on " + TestCase.destinationChain + " (" + TestCase.destinationWallet + ")");
        Browser.selectAssetInFromSection(TestCase.destinationWallet, TestCase.destinationChain, TestCase.destinationToken);

        TestCase.destinationFinalBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);
        System.out.println(TestCase.destinationFinalBalance + " " + TestCase.destinationToken);

        if (TestCase.convertingNativeBalance) {
            String nativeAsset = Browser.getNativeAssetByNetworkName(TestCase.destinationChain);

            System.out.println("Checking native asset (" + nativeAsset + ") balance on " + TestCase.destinationChain + " (" + TestCase.destinationWallet + ")");
            Browser.findElement(WormholePage.SOURCE_SELECT_ASSET_BUTTON).click();
            Browser.findElement(WormholePage.CHOOSE_ASSET(nativeAsset)).click();

            TestCase.toFinalNativeBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);
            System.out.println(TestCase.toFinalNativeBalance + " " + nativeAsset);

            Assert.assertTrue("Native balance should have increased", Double.parseDouble(TestCase.toFinalNativeBalance) > Double.parseDouble(TestCase.toNativeBalance));
        }
        Assert.assertTrue("Balance should have increased", Double.parseDouble(TestCase.destinationFinalBalance) > Double.parseDouble(TestCase.destinationBalance));
    }

    @And("I check native balance on {string} using {string}")
    public void iCheckNativeBalanceOnToNetworkUsingToWallet(String toNetwork, String toWallet) throws InterruptedException {
        TestCase.convertingNativeBalance = true;

        String nativeAsset = Browser.getNativeAssetByNetworkName(toNetwork);

        System.out.println("Checking native asset (" + nativeAsset + ") balance on " + toNetwork + " (" + toWallet + ")");
        Browser.selectAssetInFromSection(toWallet, toNetwork, nativeAsset);

        TestCase.toNativeBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.SOURCE_BALANCE_TEXT);
        System.out.println(TestCase.toNativeBalance + " " + nativeAsset);
    }

    @When("I click on Approve button")
    public void iApproveTransfer() throws InterruptedException {
        WebElement approveButton = Browser.findElement(WormholePage.APPROVE_BUTTON);
        Browser.scrollToElement(approveButton);
        Thread.sleep(5000);
        approveButton.click();
        Thread.sleep(2000);
    }

    @When("I approve wallet notifications")
    public void iApproveWalletNotification() throws InterruptedException {
        System.out.println("Going to confirm transaction...");

        switch (TestCase.sourceWallet) {
            case "MetaMask":
                Browser.confirmTransactionInMetaMask(false);

                break;
            case "Phantom":
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.PHANTOM_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
                Browser.findElement(ExtensionPage.PHANTOM_SUBMIT_BUTTON).click();
                Thread.sleep(1000);

                if (Browser.elementAppears(10, ExtensionPage.IGNORE_WARNING_PROCEED_ANYWAY_LINK)) {
                    Browser.findElement(ExtensionPage.IGNORE_WARNING_PROCEED_ANYWAY_LINK).click();
                }
                Browser.findElement(ExtensionPage.PHANTOM_PRIMARY_BUTTON).click(); // Confirm


                Browser.waitForExtensionWindowToDisappear();
                break;
            case "Sui":
                Browser.waitForExtensionWindowToAppear();
                Browser.findElement(ExtensionPage.SUI_UNLOCK_TO_APPROVE_BUTTON).click();

                Browser.findElement(ExtensionPage.SUI_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SUI"));
                Browser.findElement(ExtensionPage.SUI_UNLOCK_BUTTON).click();
                Thread.sleep(1000);

                Browser.findElement(ExtensionPage.SUI_APPROVE_BUTTON).click();
                Thread.sleep(1000);

                try {
                    Browser.findElement(ExtensionPage.SUI_DIALOG_APPROVE_BUTTON).click();
                } catch (NoSuchElementException ignore) {
                }
                Browser.waitForExtensionWindowToDisappear();
                break;
            case "Leap":
                Browser.waitForExtensionWindowToAppear();
                Browser.findElement(ExtensionPage.LEAP_APPROVE_BUTTON).click();
                Thread.sleep(1000);

                Browser.waitForExtensionWindowToDisappear();
                break;
            case "Spika":
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.SPIKA_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SPIKA"));
                Browser.findElement(ExtensionPage.SPIKA_LOGIN_BUTTON).click();
                Thread.sleep(2000);

                Browser.findElement(ExtensionPage.SPIKA_APPROVE_BUTTON).click();
                Thread.sleep(1000);

                Browser.waitForExtensionWindowToDisappear();
                break;
        }

        WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(120));
        webDriverWait.until(webDriver -> {
            if (Browser.elementAppears(0, WormholePage.REDEEM_SCREEN_HEADER)) {
                return true;
            }
            if (Browser.elementAppears(0, WormholePage.APPROVE_ERROR_MESSAGE)) {
                Assert.fail("Transaction failed: " + Browser.findElement(WormholePage.APPROVE_ERROR_MESSAGE).getText());
            }
            return null;
        });
    }

    @Then("I should see Send From link")
    public void iShouldSeeSendFromLink() {
        Browser.findElement(120, WormholePage.REDEEM_SCREEN_HEADER);

        System.out.println("Waiting for the send from link...");
        WebElement sendFromLink = Browser.findElement(3600, WormholePage.SOURCE_SCAN_LINK());

        TestCase.wormholescanLink = sendFromLink.getAttribute("href");
    }

    @Then("I should claim assets")
    public void iShouldClaimAssets() throws InterruptedException {
        if (TestCase.route.equals("wormhole-bridge-manual") || TestCase.route.equals("circle-manual") || TestCase.route.equals("cosmos-manual") || TestCase.route.equals("route-option-nttManual")) {
            System.out.println("Waiting for the Claim button...");
            Browser.findElement(3600, WormholePage.CLAIM_BUTTON);
            Thread.sleep(5000);
            Browser.findElement(WormholePage.CLAIM_BUTTON).click();
            Thread.sleep(2000);

            if (TestCase.destinationWallet.equals("Phantom")) {
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.PHANTOM_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
                Browser.findElement(ExtensionPage.PHANTOM_SUBMIT_BUTTON).click();

                WebDriverWait webDriverWait = new WebDriverWait(Browser.driver, Duration.ofSeconds(900));
                webDriverWait
                        .until(webDriver -> {
                            if (Browser.extensionWindowIsOpened()) {
                                Browser.switchToExtensionWindow();
                                if (Browser.elementAppears(1, ExtensionPage.IGNORE_WARNING_PROCEED_ANYWAY_LINK)) {
                                    Browser.findElement(ExtensionPage.IGNORE_WARNING_PROCEED_ANYWAY_LINK).click();
                                    return null;
                                }
                                Browser.findElement(ExtensionPage.PHANTOM_PRIMARY_BUTTON).click(); // Confirm
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
            } else if (TestCase.destinationWallet.equals("Sui")) {
                Browser.waitForExtensionWindowToAppear();
                Browser.findElement(ExtensionPage.SUI_UNLOCK_TO_APPROVE_BUTTON).click();

                Browser.findElement(ExtensionPage.SUI_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SUI"));
                Browser.findElement(ExtensionPage.SUI_UNLOCK_BUTTON).click();
                Thread.sleep(1000);

                Browser.findElement(ExtensionPage.SUI_APPROVE_BUTTON).click();
                Thread.sleep(1000);

                try {
                    Browser.findElement(ExtensionPage.SUI_DIALOG_APPROVE_BUTTON).click();
                } catch (NoSuchElementException ignore) {
                }
                Browser.waitForExtensionWindowToDisappear();
            } else if (TestCase.destinationWallet.equals("Spika")) {
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.SPIKA_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SPIKA"));
                Browser.findElement(ExtensionPage.SPIKA_LOGIN_BUTTON).click();
                Thread.sleep(2000);

                Browser.findElement(ExtensionPage.SPIKA_APPROVE_BUTTON).click();
                Thread.sleep(1000);

                Browser.waitForExtensionWindowToDisappear();
            } else if (TestCase.destinationWallet.equals("Leap")) {
                Browser.waitForExtensionWindowToAppear();
                Browser.findElement(ExtensionPage.LEAP_APPROVE_BUTTON).click();
                Thread.sleep(1000);

                Browser.waitForExtensionWindowToDisappear();
            }
            else {
                Browser.confirmTransactionInMetaMask(true);
            }
        }
    }

    @Then("I should see Send To link")
    public void iShouldSeeSendToLink() {
        int waitSeconds;
        if (TestCase.route.equals("automatic") || TestCase.route.equals("circle-automatic")) {
            waitSeconds = 60 * 30;
        } else {
            if (TestCase.destinationWallet.equals("Phantom")) {
                waitSeconds = 60 * 10;
            } else {
                waitSeconds = 60 * 30;
            }
        }

        System.out.println("Waiting for the send to link...");
        WebElement sendToLink = Browser.findElement(waitSeconds, WormholePage.DESTINATION_SCAN_LINK());

        assertTrue(sendToLink.isDisplayed());

        TestCase.txTo = sendToLink.getAttribute("href");

        System.out.println("Finished");
    }

    @And("I move slider")
    public void iMoveSlider() throws InterruptedException {
        Browser.moveSliderByOffset(220);
    }
}
