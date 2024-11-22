package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.PasswordPage;
import pages.WormholePage;
import support.Browser;
import support.BrowserMainnet;

import static junit.framework.TestCase.assertTrue;

public class WormholeConnectV2Steps {
    @Given("{string} opens")
    public void opens(String build) {
        Browser.isMainnet = true;
        BrowserMainnet.launch();
        Browser.url = build;
        Browser.driver.get(Browser.url);
        if (build.contains("netlify.app")) {
            Browser.findElement(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
            Browser.findElement(PasswordPage.button).click();
        }
    }

    @And("Transaction details entered: {string} {string} {string} to {string} {string}, route {string}")
    public void transactionDetailsEnteredToRoute(String amount, String sourceToken, String sourceChain, String destinationToken, String destinationChain, String route) throws InterruptedException {
        Browser.sourceChain = sourceChain;
        Browser.sourceToken = sourceToken;
        Browser.destinationChain = destinationChain;
        Browser.destinationToken = destinationToken;
        Browser.sendingAmount = amount;

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
        Browser.findElement(WormholePage.AMOUNT_INPUT).sendKeys(amount);
        Thread.sleep(3000);
        Browser.clickElement(WormholePage.REVIEW_TRANSACTION_BUTTON);
    }

    @And("Transaction approved in the wallet")
    public void transactionApprovedInTheWallet() {
    }

    @And("Link to Wormholescan is displayed")
    public void linkToWormholescanIsDisplayed() {
    }

    @Then("Balance has increased on destination chain")
    public void balanceHasIncreasedOnDestinationChain() {
    }
}
