package tests.mainnet;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/mainnet/", tags = "@new", glue = {"steps", "support"})
public class RunNewTests {
}
