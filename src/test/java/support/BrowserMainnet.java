package support;

import org.openqa.selenium.By;

public class BrowserMainnet extends Browser {
    public static void main(String[] args) {
        launch();

        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_MAINNET"));
        Browser.driver.findElement(By.cssSelector("form [type='password']")).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(By.cssSelector("form button.button")).click();
    }

    public static void launch() {
        launch("chrome_profile_mainnet");
    }
}
