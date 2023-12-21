package wh;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import support.Browser;

public class FaucetSteps {
    @Before
    public void startBrowser() {
        Browser.launch();
    }

    @After
    public void quitBrowser() {
        Browser.quit();
    }

    @Given("I request CELO tokens on faucet.celo.org")
    public void iRequestCELOTokensOnFaucetCeloOrg() throws InterruptedException {
        Browser.driver.get("https://faucet.celo.org/alfajores");
        Browser.driver.findElement(By.cssSelector("form input[type=text]")).clear();
        Browser.driver.findElement(By.cssSelector("form input[type=text]")).sendKeys("0x6FBd5A25aa9a2f40E3b013db720c7b46f22C6116");
        Browser.driver.findElement(By.cssSelector("form button[type=submit]")).click();

        Browser.implicitlyWait(60);
        Browser.driver.findElement(By.xpath("//*[text()='View on Celo Explorer']"));
        Thread.sleep(3000);
    }
}
