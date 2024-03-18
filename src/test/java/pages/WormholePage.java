package pages;

import org.openqa.selenium.By;
import support.Browser;

public class WormholePage {
    // Setup Screen
    public static final By SOURCE_SELECT_NETWORK_BUTTON = By.xpath("//*[text()='Select network']"); // data-testid="source-section-select-network-button"
    public static final By SOURCE_CONNECT_WALLET_BUTTON = By.xpath("//*[text()='Connect wallet']"); // data-testid="source-section-connect-wallet-button"
    public static final By SOURCE_SELECT_ASSET_BUTTON = By.xpath("//*[text()='Select']"); // data-testid="source-section-select-asset-button"
    public static final By SOURCE_AMOUNT_INPUT = By.tagName("input"); // data-testid="source-section-amount-input"
    public static final By SOURCE_BALANCE_TEXT = By.xpath("(//*[text()='Balance']/following-sibling::*)[1]"); // data-testid="source-section-balance-text"

    public static final By DESTINATION_SELECT_NETWORK_BUTTON = By.xpath("//*[text()='Select network']"); // data-testid="destination-section-select-network-button"
    public static final By DESTINATION_CONNECT_WALLET_BUTTON = By.xpath("//*[text()='Connect wallet']"); // data-testid="destination-section-connect-wallet-button"
    public static final By DESTINATION_ASSET_BUTTON = By.xpath("(//*[text()='Asset']/following-sibling::*)[2]"); // see DESTINATION_ASSET_TEXT
    public static final By DESTINATION_ASSET_TEXT = By.xpath("(//*[text()='Asset']/following-sibling::*)[2]"); // data-testid="destination-section-asset-text"
    public static final By DESTINATION_AMOUNT_INPUT = By.xpath("(//*[text()='Amount']/following-sibling::*/input)[2]"); // data-testid="destination-section-amount-input"
    public static final By DESTINATION_BALANCE_TEXT = By.xpath("(//*[text()='Balance']/following-sibling::*)[2]"); // data-testid="destination-section-balance-text"

    public static final By AUTOMATIC_BRIDGE_OPTION = By.xpath("//*[contains(text(),'Automatic Bridge')]"); // data-testid="automatic-route-option"
    public static final By MANUAL_BRIDGE_OPTION = By.xpath("//*[contains(text(),'Manual Bridge')]"); // data-testid="manual-route-option"
    public static final By COSMOS_AUTOMATIC_GATEWAY_OPTION = By.xpath("//*[contains(text(),'Cosmos Gateway')][2]");
    public static final By COSMOS_MANUAL_GATEWAY_OPTION = By.xpath("//*[contains(text(),'Cosmos Gateway')]"); // data-testid="cosmos-gateway-route-option"
    public static final By CIRCLE_AUTOMATIC_OPTION = By.xpath("//*[contains(text(),'Receive tokens automatically')]//ancestor::*[contains(@class, 'MuiCollapse-root') and contains(@class, '-option')]"); // data-testid="circle-automatic-route-option"
    public static final By CIRCLE_MANUAL_OPTION = By.xpath("//*[contains(text(),'Approve transfer with destination wallet')]//ancestor::*[contains(@class, 'MuiCollapse-root') and contains(@class, '-option')]"); // data-testid="circle-manual-route-option"
    public static final By SLIDER_THUMB = By.cssSelector(".MuiSlider-thumb");

    public static final By POPUP_CLOSE_BUTTON = By.cssSelector("[data-testid='CloseIcon']");
    public static final By APPROVE_BUTTON = By.xpath("//*[text()='Approve and proceed with transaction']"); // data-testid="approve-button"

    public static final By APPROVE_ERROR_MESSAGE = By.xpath("//*[text()='Error with transfer, please try again']"); // data-testid="approve-error-message"

    // Redeem Screen
    public static final By REDEEM_SCREEN_HEADER = By.xpath("//*[text()='Bridge']"); // data-testid="redeem-screen-header"
    public static final By TRANSACTION_COMPLETE_MESSAGE = By.xpath("//*[text()='The bridge is now complete.']"); // data-testid="transaction-complete-message"
    public static final By CLAIM_BUTTON = By.xpath("//*[text()='Claim']"); // data-testid="claim-button"
    public static final By CLAIM_ERROR_MESSAGE = By.xpath("//*[contains(text()='Your claim has failed, please try again')]");

    public static By SOURCE_SCAN_LINK() {
        return By.xpath("//*[text()= '" + Browser.getScanLinkTextByNetworkName(Browser.fromNetwork) + "' ]/.."); // data-testid="source-section-scan-link"
    }

    public static By DESTINATION_SCAN_LINK() {
        return By.xpath("//*[text()='Send to']/../following-sibling::*//*[text()='" + Browser.getScanLinkTextByNetworkName(Browser.toNetwork) + "']/.."); // data-testid="destination-section-scan-link"
    }

    public static By OPEN_ASSET_LIST() {
        return By.xpath("//*[contains(text(), '" + Browser.toAsset + "')]");
    }

    public static By CHOOSE_WALLET(String wallet) {
        return By.xpath("//*[@role='dialog']//*[text()='" + wallet + "']");
    }

    public static By CHOOSE_NETWORK(String network) {
        return By.xpath("//*[text()='" + network + "']");
    }

    public static By CHOOSE_ASSET(String wallet) {
        return By.xpath("//*[contains(@class, 'MuiDialog-root')]//*[text()='" + wallet + "']/../../..");
    }
}
