package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ExtensionPage;
import pages.PasswordPage;
import pages.WormholePage;
import support.Browser;
import support.BrowserMainnet;
import support.TestCase;

import java.time.Duration;

public class WormholeConnectV2Steps {
    @Given("Test data: {string} {string} {string} {string} {string} {string} {string} {string} {string}")
    public void testData(String url, String route, String amount, String sourceToken, String sourceChain, String sourceWallet, String destinationToken, String destinationChain, String destinationWallet) {
        TestCase.url = url;
        TestCase.route = route;
        TestCase.inputAmount = amount;
        TestCase.sourceToken = sourceToken;
        TestCase.sourceChain = sourceChain;
        TestCase.sourceWallet = sourceWallet;
        TestCase.destinationToken = destinationToken;
        TestCase.destinationChain = destinationChain;
        TestCase.destinationWallet = destinationWallet;

        System.out.println("url: " + TestCase.url);
        System.out.println("route: " + TestCase.route);
        System.out.println("amount: " + TestCase.inputAmount);
        System.out.println("source token: " + TestCase.sourceToken);
        System.out.println("source chain: " + TestCase.sourceChain);
        System.out.println("source wallet: " + TestCase.sourceWallet);
        System.out.println("destination token: " + TestCase.destinationToken);
        System.out.println("destination chain: " + TestCase.destinationChain);
        System.out.println("destination wallet: " + TestCase.destinationWallet);
    }

    @Given("I open the page")
    public void opens() {
        Browser.determineEnvironment();
        Browser.launchBrowser();
        Browser.navigateToUrl();
        if (Browser.isNetlifyPage()) {
            Browser.enterPassword();
        }

    }

    @And("Transaction details entered")
    public void transactionDetailsEnteredToRoute() {
        Browser.validateRouteName();
        Browser.selectSourceChain();

        Browser.unlockMetaMaskIfNeeded(TestCase.metaMaskWasUnlocked);
        Browser.clickElement(WormholePage.FIND_TOKEN(TestCase.sourceToken));

        Browser.selectDestinationChain();
        TestCase.destinationStartingBalance = Browser.getDestinationTokenBalance(TestCase.destinationToken);

        Browser.pressEscape();
        Browser.sleep(1000);

        Browser.findElement(WormholePage.AMOUNT_INPUT).sendKeys(TestCase.inputAmount);
        Browser.sleep(3000);
        Browser.clickElement(WormholePage.REVIEW_TRANSACTION_BUTTON);
        Browser.clickElement(WormholePage.CONFIRM_TRANSACTION_BUTTON);
    }

    @And("Transaction approved in the wallet")
    public void transactionApprovedInTheWallet(){
        System.out.println("Confirming transaction in the wallet");

        switch (TestCase.sourceWallet) {
            case "MetaMask":
                Browser.confirmTransactionInMetaMask(false);
                break;
            case "Phantom":
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.PHANTOM_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_PHANTOM"));
                Browser.findElement(ExtensionPage.PHANTOM_SUBMIT_BUTTON).click();
                Browser.sleep(1000);

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
                Browser.sleep(1000);

                Browser.findElement(ExtensionPage.SUI_APPROVE_BUTTON).click();
                Browser.sleep(1000);

                try {
                    Browser.findElement(ExtensionPage.SUI_DIALOG_APPROVE_BUTTON).click();
                } catch (NoSuchElementException ignore) {
                }
                Browser.waitForExtensionWindowToDisappear();
                break;
            case "Leap":
                Browser.waitForExtensionWindowToAppear();
                Browser.findElement(ExtensionPage.LEAP_APPROVE_BUTTON).click();
                Browser.sleep(1000);

                Browser.waitForExtensionWindowToDisappear();
                break;
            case "Spika":
                Browser.waitForExtensionWindowToAppear();

                Browser.findElement(ExtensionPage.SPIKA_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SPIKA"));
                Browser.findElement(ExtensionPage.SPIKA_LOGIN_BUTTON).click();
                Browser.sleep(2000);

                Browser.findElement(ExtensionPage.SPIKA_APPROVE_BUTTON).click();
                Browser.sleep(1000);

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
        TestCase.wormholescanLink = Browser.findElement(WormholePage.VIEW_ON_WORMHOLESCAN_LINK).getAttribute("href");
        Assert.assertTrue(TestCase.wormholescanLink.startsWith("https://wormholescan.io/#/tx/"));
    }

    @And("Claim assets on destination network if needed")
    public void claimAssetsOnDestinationNetwork() throws InterruptedException {
        if (!TestCase.requiresClaim) {
            return;
        }

        System.out.println("Waiting for the Claim button...");
        Browser.findElement(3600, WormholePage.CLAIM_BUTTON_V2);
        Browser.sleep(5000);
        Browser.findElement(WormholePage.CLAIM_BUTTON_V2).click();
        Browser.sleep(2000);

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
                            Browser.sleep(2000);
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
            Browser.sleep(1000);

            Browser.findElement(ExtensionPage.SUI_APPROVE_BUTTON).click();
            Browser.sleep(1000);

            try {
                Browser.findElement(ExtensionPage.SUI_DIALOG_APPROVE_BUTTON).click();
            } catch (NoSuchElementException ignore) {
            }
            Browser.waitForExtensionWindowToDisappear();
        } else if (TestCase.destinationWallet.equals("Spika")) {
            Browser.waitForExtensionWindowToAppear();

            Browser.findElement(ExtensionPage.SPIKA_PASSWORD_INPUT).sendKeys(Browser.env.get("WALLET_PASSWORD_SPIKA"));
            Browser.findElement(ExtensionPage.SPIKA_LOGIN_BUTTON).click();
            Browser.sleep(2000);

            Browser.findElement(ExtensionPage.SPIKA_APPROVE_BUTTON).click();
            Browser.sleep(1000);

            Browser.waitForExtensionWindowToDisappear();
        } else if (TestCase.destinationWallet.equals("Leap")) {
            Browser.waitForExtensionWindowToAppear();
            Browser.findElement(ExtensionPage.LEAP_APPROVE_BUTTON).click();
            Browser.sleep(1000);

            Browser.waitForExtensionWindowToDisappear();
        } else {
            Browser.confirmTransactionInMetaMask(true);
        }
    }

    @Then("I check that amount is received")
    public void iCheckThatAmountIsReceived() {
        Browser.driver.get(TestCase.url);

        Browser.clickElement(WormholePage.SELECT_SOURCE_CHAIN);
        Browser.clickElement(WormholePage.SELECT_OTHER_SOURCE_CHAIN);
        Browser.clickElement(WormholePage.FIND_NETWORK(TestCase.destinationChain));
        Browser.clickElement(WormholePage.FIND_TOKEN(TestCase.destinationToken));


        TestCase.destinationFinalBalance = Browser.findElementAndWaitToHaveNumber(WormholePage.TOKEN_BALANCE_IN_TOKEN_LIST(TestCase.destinationToken));
        System.out.println("Destination chain final balance: " + TestCase.destinationFinalBalance + " " + TestCase.destinationToken);
        Assert.assertTrue("Balance should have increased", Double.parseDouble(TestCase.destinationFinalBalance) > Double.parseDouble(TestCase.destinationStartingBalance));
    }
}
