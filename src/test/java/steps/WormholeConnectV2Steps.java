package steps;

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

public class WormholeConnectV2Steps {
    @Given("{string} opens")
    public void opens(String build) {
        Browser.isMainnet = true;
        BrowserMainnet.launch();
        Browser.url = build;
        Browser.driver.get(Browser.url);
        if (build.contains("https://wormhole-connect-mainnet.netlify.app/")) {
            Browser.findElement(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
            Browser.findElement(PasswordPage.button).click();
        }
    }

    @And("Transaction details entered: {string} {string} {string} to {string} {string}, route {string}")
    public void transactionDetailsEnteredToRoute(String amount, String source_asset, String source_chain, String destination_asset, String destination_chain, String route) {
        Browser.fromNetwork = source_chain;
        Browser.fromAsset = source_asset;

        Browser.clickElement(WormholePage.EXPAND_MORE_ICON);
        Browser.clickElement(WormholePage.ADD_ICON);
        Browser.clickElement(WormholePage.FIND_NETWORK(Browser.fromNetwork));

        if (!Browser.metaMaskWasUnlocked) {
            Browser.unlockMetaMask();
        }

        Browser.clickElement(WormholePage.FIND_NETWORK(Browser.fromAsset));
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
