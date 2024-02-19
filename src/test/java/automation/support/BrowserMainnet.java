package automation.support;

import automation.pages.PasswordPage;

public class BrowserMainnet extends Browser {
    public static void main(String[] args) {
        launch();

        Browser.driver.get(Browser.env.get("URL_WORMHOLE_CONNECT_MAINNET"));
        Browser.driver.findElement(PasswordPage.passwordInput).sendKeys(Browser.env.get("WORMHOLE_PAGE_PASSWORD"));
        Browser.driver.findElement(PasswordPage.button).click();
    }

    public static void launch() {
        launch("chrome_profile_mainnet");
    }
}
