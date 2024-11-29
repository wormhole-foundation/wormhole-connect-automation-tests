package support;

import java.util.Date;

public class TestCase {
    public static boolean isMainnet = false;
    public static String url = "";
    public static Date startedAt;
    public static Date finishedAt;

    public static String sourceWallet = "";
    public static String sourceToken = "";
    public static String sourceChain = "";

    public static String destinationWallet = "";
    public static String destinationToken = "";
    public static String destinationChain = "";

    public static String inputAmount = "";

    public static String route = "";

    // Transaction Screen
    public static String wormholescanLink = "";

    public static String destinationAmount = "";
    public static String destinationStartingBalance = "";
    public static String destinationFinalBalance = "";

    public static String transactionGasFeeUsd = "";
    public static String claimGasFeeUsd = "";
    public static String destinationNativeBalance = "";
    public static String destinationFinalNativeBalance = "";

    public static boolean destinationNetworkNativeBalanceIsKnown = false;
    public static boolean requiresClaim = false;

    public static boolean isBlockedByHighFee = false;
    public static String screenshotUrl = "";
    public static boolean metaMaskWasUnlocked = false;
    public static boolean phantomWasUnlocked = false;
    public static boolean leapWasUnlocked = false;
    public static boolean spikaWasUnlocked = false;

    public static void initializeAllFields() {
        TestCase.isMainnet = false;
        TestCase.url = "";
        TestCase.startedAt = new Date();
        TestCase.finishedAt = null;
        TestCase.sourceWallet = "";
        TestCase.destinationWallet = "";
        TestCase.sourceChain = "";
        TestCase.destinationChain = "";
        TestCase.inputAmount = "";
        TestCase.destinationAmount = "";
        TestCase.sourceToken = "";
        TestCase.destinationToken = "";
        TestCase.route = "";
        TestCase.wormholescanLink = "";
        TestCase.destinationStartingBalance = "";
        TestCase.destinationFinalBalance = "";
        TestCase.destinationNativeBalance = "";
        TestCase.destinationFinalNativeBalance = "";
        TestCase.destinationNetworkNativeBalanceIsKnown = false;
        TestCase.transactionGasFeeUsd = "";
        TestCase.claimGasFeeUsd = "";
        TestCase.screenshotUrl = "";
        TestCase.isBlockedByHighFee = false;
        TestCase.requiresClaim = false;

        TestCase.metaMaskWasUnlocked = false;
        TestCase.phantomWasUnlocked = false;
        TestCase.leapWasUnlocked = false;
        TestCase.spikaWasUnlocked = false;
    }
}
