package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ExtensionPage;
import pages.PasswordPage;
import pages.WormholePage;
import support.Browser;
import support.BrowserMainnet;

import java.time.Duration;

import static junit.framework.TestCase.assertTrue;

public class WormholeConnectV2Steps {
    @Given("{string} opens")
    public void opens(String build) {
        if (build.contains("mainnet") || build.contains("portalbridge.com")) {
            Browser.isMainnet = true;
            BrowserMainnet.launch();
        } else {
            Browser.isMainnet = false;
            Browser.launch();
        }
        Browser.url = build;
        Browser.driver.get(Browser.url);
        if (build.contains("netlify.app")) {
            Browser.findElement(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
            Browser.findElement(PasswordPage.button).click();
        }
    }

    @And("Transaction details entered: {string} {string} {string} to {string} {string}, from {string} to {string} route {string}")
    public void transactionDetailsEnteredToRoute(String amount, String sourceToken, String sourceChain, String destinationToken, String destinationChain, String sourceWallet, String destinationWallet, String route) throws InterruptedException {
        Browser.sourceWallet = sourceWallet;
        Browser.destinationWallet = destinationWallet;
        Browser.sourceChain = sourceChain;
        Browser.sourceToken = sourceToken;
        Browser.destinationChain = destinationChain;
        Browser.destinationToken = destinationToken;
        Browser.sendingAmount = amount;

        Browser.validateRouteName();

        Browser.clickElement(WormholePage.EXPAND_SOURCE_MORE_ICON);
        Browser.clickElement(WormholePage.OTHER_SOURCE_CHAIN_ICON);
        Browser.clickElement(WormholePage.FIND_NETWORK(Browser.sourceChain));

        if (!Browser.metaMaskWasUnlocked) {
            Browser.unlockMetaMask();
        }

        Browser.clickElement(WormholePage.FIND_TOKEN(Browser.sourceToken));
        Browser.clickElement(WormholePage.EXPAND_DESTINATION_MORE_ICON);
        Browser.clickElement(WormholePage.OTHER_DESTINATION_CHAIN_ICON);
        Browser.clickElement(WormholePage.FIND_NETWORK(Browser.destinationChain));
        Browser.pressEscape();
        Thread.sleep(1000);
        Browser.findElement(WormholePage.AMOUNT_INPUT).sendKeys(amount);
        Thread.sleep(3000);
        Browser.clickElement(WormholePage.REVIEW_TRANSACTION_BUTTON);

        // Review page
        Browser.clickElement(WormholePage.CONFIRM_TRANSACTION_BUTTON);
    }

    @And("Transaction approved in the wallet")
    public void transactionApprovedInTheWallet() throws InterruptedException {
        System.out.println("Going to confirm transaction...");

        switch (Browser.sourceWallet) {
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
            if (Browser.elementAppears(0, WormholePage.VIEW_ON_WORMHOLESCAN_LINK)) {
                return true;
            }
            if (Browser.elementAppears(0, WormholePage.APPROVE_ERROR_MESSAGE)) {
                Assert.fail("Transaction failed: " + Browser.findElement(WormholePage.APPROVE_ERROR_MESSAGE).getText());
            }
            return null;
        });
    }

    @And("Link to Wormholescan is displayed")
    public void linkToWormholescanIsDisplayed() {
        Browser.wormholescanLink = Browser.findElement(WormholePage.VIEW_ON_WORMHOLESCAN_LINK).getAttribute("href");

        Assert.assertTrue(Browser.wormholescanLink.startsWith("https://wormholescan.io/#/tx/"));
    }

    @And("Claim assets on destination network if needed \\(requires claim: {string})")
    public void claimAssetsOnDestinationNetwork(String requiresClaim) throws InterruptedException {
        if (requiresClaim.equals("no")) {
            return;
        }

        System.out.println("Waiting for the Claim button...");
        Browser.findElement(3600, WormholePage.CLAIM_BUTTON_V2);
        Thread.sleep(5000);
        Browser.findElement(WormholePage.CLAIM_BUTTON_V2).click();
        Thread.sleep(2000);

        if (Browser.destinationWallet.equals("Phantom")) {
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
        } else if (Browser.destinationWallet.equals("Sui")) {
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
        } else if (Browser.destinationWallet.equals("Spika")) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElement(ExtensionPage.SPIKA_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SPIKA"));
            Browser.findElement(ExtensionPage.SPIKA_LOGIN_BUTTON).click();
            Thread.sleep(2000);

            Browser.findElement(ExtensionPage.SPIKA_APPROVE_BUTTON).click();
            Thread.sleep(1000);

            Browser.waitForExtensionWindowToDisappear();
        } else if (Browser.destinationWallet.equals("Leap")) {
            Browser.waitForExtensionWindowToAppear();
            Browser.findElement(ExtensionPage.LEAP_APPROVE_BUTTON).click();
            Thread.sleep(1000);

            Browser.waitForExtensionWindowToDisappear();
        } else {
            Browser.confirmTransactionInMetaMask(true);
        }
    }
}
