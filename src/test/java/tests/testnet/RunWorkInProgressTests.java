package tests.testnet;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/testnet/", tags = "@wip", glue = {"steps", "support"})
public class RunWorkInProgressTests {
}
