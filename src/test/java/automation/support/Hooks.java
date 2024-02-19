package automation.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.Date;

public class Hooks {
    @Before
    public void before() {
        Browser.isMainnet = false;
        Browser.url = "";
        Browser.startedAt = new Date();
        Browser.finishedAt = null;
        Browser.fromWallet = "";
        Browser.toWallet = "";
        Browser.fromNetwork = "";
        Browser.toNetwork = "";
        Browser.fromAmount = "";
        Browser.toAmount = "";
        Browser.fromAsset = "";
        Browser.toAsset = "";
        Browser.route = "";
        Browser.txFrom = "";
        Browser.txTo = "";
        Browser.fromBalance = "";
        Browser.toBalance = "";
        Browser.toFinalBalance = "";
        Browser.toNativeBalance = "";
        Browser.toFinalNativeBalance = "";
        Browser.sourceGasFeeUsd = "";
        Browser.destinationGasFeeUsd = "";
        Browser.metaMaskWasUnlocked = false;
        Browser.phantomWasUnlocked = false;
        Browser.leapWasUnlocked = false;
    }

    @After
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            Browser.takeScreenshot();
        }

        Browser.finishedAt = new Date();
        Browser.saveResults(scenario.isFailed() ? "fail" : "pass");

        Browser.quit();
    }
}
