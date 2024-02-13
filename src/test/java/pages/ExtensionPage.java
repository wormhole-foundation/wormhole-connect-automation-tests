package pages;

import org.openqa.selenium.By;

public class ExtensionPage {

    // MetaMask

    public static final By METAMASK_PASSWORD_INPUT = By.cssSelector("[data-testid='unlock-password']");
    public static final By METAMASK_UNLOCK_BUTTON = By.cssSelector("[data-testid='unlock-submit']");

    public static final By METAMASK_FOOTER_NEXT_BUTTON = By.cssSelector("[data-testid='page-container-footer-next']");
    public static final By METAMASK_CANCEL_BUTTON = By.cssSelector("[data-testid='page-container-footer-cancel']");
    public static final By METAMASK_APPROVE_BUTTON = By.xpath("//*[text()='Approve']");
    public static final By METAMASK_GOT_IT_BUTTON = By.xpath("//*[text()='Got it']");
    public static final By METAMASK_SWITCH_NETWORK_BUTTON = By.xpath("//*[text()='Switch network']");

    // Phantom

    public static final By PHANTOM_PASSWORD_INPUT = By.cssSelector("[data-testid='unlock-form-password-input']");
    public static final By PHANTOM_SUBMIT_BUTTON = By.cssSelector("[data-testid='unlock-form-submit-button']");
    public static final By PHANTOM_PRIMARY_BUTTON = By.cssSelector("[data-testid='primary-button']");

    // Sui

    public static final By SUI_UNLOCK_TO_APPROVE_BUTTON = By.xpath("//*[text()='Unlock to Approve']/..");
    public static final By SUI_PASSWORD_INPUT = By.xpath("//*[@name='password']");
    public static final By SUI_UNLOCK_BUTTON = By.xpath("//*[@role='dialog']//*[text()='Unlock']/..");
    public static final By SUI_APPROVE_BUTTON = By.xpath("//*[text()='Approve']/..");
    public static final By SUI_DIALOG_APPROVE_BUTTON = By.xpath("//*[@role='dialog']//*[text()='Approve']/..");
}
