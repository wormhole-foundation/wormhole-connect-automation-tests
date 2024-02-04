package wh;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import support.Browser;

public class FaucetSteps {
    @Given("I request CELO tokens on faucet.celo.org")
    public void iRequestCELOTokensOnFaucetCeloOrg() throws InterruptedException {
        Browser.driver.get("https://faucet.celo.org/alfajores");
        Browser.driver.findElement(By.cssSelector("form input[type=text]")).clear();
        Browser.driver.findElement(By.cssSelector("form input[type=text]")).sendKeys("0x0a370E7c5F7F944d451B48bc151a7BEC454bc185");
        Browser.driver.findElement(By.cssSelector("form button[type=submit]")).click();

        Browser.implicitlyWait(60);
        Browser.driver.findElement(By.xpath("//*[text()='View on Celo Explorer']"));
        Thread.sleep(3000);
    }
}
