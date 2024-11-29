package support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.Date;

public class Hooks {
    @Before
    public void before() {
        TestCase.initializeAllFields();
    }

    @After
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            Browser.takeScreenshot();
        }

        TestCase.finishedAt = new Date();
        if (TestCase.isBlockedByHighFee) {
            Browser.saveResults("blocked");
        } else {
            Browser.saveResults(scenario.isFailed() ? "fail" : "pass");
        }
        Browser.quit();
    }
}
