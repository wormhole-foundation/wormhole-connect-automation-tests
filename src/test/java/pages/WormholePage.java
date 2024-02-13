package pages;

import org.openqa.selenium.By;
import support.Browser;

public class WormholePage {
    public static final By THE_BRIDGE_IS_NOW_COMPLETE_TEXT = By.xpath("//*[text()='The bridge is now complete.']");
    public static final By CONNECT_SOURCE_WALLET = By.xpath("//*[text()='Connect wallet']");
    public static final By CONNECT_DESTINATION_WALLET = By.xpath("//*[text()='Connect wallet']");
    public static final By SELECT_SOURCE_ASSET = By.xpath("//*[text()='Select']");
    public static final By SELECT_NETWORK = By.xpath("//*[text()='Select network']");
    public static final By SLIDER_THUMB = By.cssSelector(".MuiSlider-thumb");
    public static final By DESTINATION_ASSET = By.xpath("(//*[text()='Asset']/following-sibling::*)[2]");
    public static final By DESTINATION_AMOUNT = By.xpath("(//*[text()='Amount']/following-sibling::*/input)[2]");
    public static final By DESTINATION_BALANCE = By.xpath("(//*[text()='Balance']/following-sibling::*)[2]");
    public static final By AUTOMATIC_BRIDGE = By.xpath("//*[contains(text(),'Automatic Bridge')]");
    public static final By MANUAL_BRIDGE = By.xpath("//*[contains(text(),'Manual Bridge')]");
    public static final By AMOUNT_INPUT = By.tagName("input");
    public static final By SOURCE_BALANCE = By.xpath("(//*[text()='Balance']/following-sibling::*)[1]");
    public static final By POPUP_CLOSE_ICON = By.cssSelector("[data-testid='CloseIcon']");
    public static final By APPROVE_AND_PROCEED_WITH_TRANSACTION_BUTTON = By.xpath("//*[text()='Approve and proceed with transaction']");
    public static final By CLAIM_BUTTON = By.xpath("//*[text()='Claim']");

    public static By CHOOSE_TO_WALLET() {
        return By.xpath("//*[text()='" + Browser.toWallet + "']");
    }

    public static By CHOOSE_TO_NETWORK() {
        return By.xpath("//*[text()='" + Browser.toNetwork + "']");
    }

    public static By SELECT_TO_NETWORK() {
        return By.xpath("//*[text()='" + Browser.getNativeAssetByNetworkName(Browser.toNetwork) + "']/../../..");
    }

    public static By SELECT_TO_ASSET() {
        return By.xpath("//*[contains(text(), '" + Browser.toAsset + "')]");
    }

    public static By SCAN_FROM_LINK() {
        return By.xpath("//*[text()= '" + Browser.getScanLinkTextByNetworkName(Browser.fromNetwork) + "' ]/.."); // should have href attribute
    }

    public static By SCAN_TO_LINK_IN_TO_SECTION() {
        return By.xpath("//*[text()='Send to']/../following-sibling::*//*[text()='" + Browser.getScanLinkTextByNetworkName(Browser.toNetwork) + "']/.."); // should have href attribute
    }

    public static By CHOOSE_WALLET(String wallet) {
        return By.xpath("//*[text()='" + wallet + "']");
    }

    public static By CHOOSE_NETWORK(String network) {
        return By.xpath("//*[text()='" + network + "']");
    }

    public static By CHOOSE_ASSET(String wallet) {
        return By.xpath("//*[text()='" + wallet + "']/../../..");
    }
}
