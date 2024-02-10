package wh;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import support.Browser;

import java.util.Date;

@RunWith(Cucumber.class)
@CucumberOptions()
public class CucumberTests {
    @Before
    public void startBrowser() {
        Browser.launch();

        Browser.metaMaskWasUnlocked = false;
        Browser.startedAt = new Date();
    }

    @After
    public void quitBrowser(Scenario scenario) {
        if (scenario.isFailed()) {
            Browser.takeScreenshot();
        }

        Browser.finishedAt = new Date();
        Browser.saveResults(scenario.isFailed() ? "fail" : "pass");

        Browser.quit();
    }
}
