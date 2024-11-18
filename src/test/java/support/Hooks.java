package support;

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
        Browser.sourceWallet = "";
        Browser.toWallet = "";
        Browser.sourceChain = "";
        Browser.destinationChain = "";
        Browser.fromAmount = "";
        Browser.sendingAmount = "";
        Browser.sourceToken = "";
        Browser.destinationToken = "";
        Browser.route = "";
        Browser.txFrom = "";
        Browser.txTo = "";
        Browser.fromBalance = "";
        Browser.toBalance = "";
        Browser.toFinalBalance = "";
        Browser.toNativeBalance = "";
        Browser.toFinalNativeBalance = "";
        Browser.convertingNativeBalance = false;
        Browser.sourceGasFeeUsd = "";
        Browser.destinationGasFeeUsd = "";
        Browser.screenshotUrl = "";
        Browser.isBlocked = false;

        Browser.metaMaskWasUnlocked = false;
        Browser.phantomWasUnlocked = false;
        Browser.leapWasUnlocked = false;
        Browser.spikaWasUnlocked = false;
    }

    @After
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            Browser.takeScreenshot();
        }

        Browser.finishedAt = new Date();
        if (Browser.isBlocked) {
            Browser.saveResults("blocked");
        } else {
            Browser.saveResults(scenario.isFailed() ? "fail" : "pass");
        }
        Browser.quit();
    }
}
