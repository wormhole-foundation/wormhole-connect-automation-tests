package support;

import java.util.Date;

public class TestCase {
    public static boolean isMainnet = false;
    public static String url = "";
    public static Date startedAt;
    public static Date finishedAt;
    public static String sourceWallet = "";
    public static String destinationWallet = "";
    public static String sourceChain = "";
    public static String destinationChain = "";
    public static String sourceAmount = "";
    public static String destinationAmount = "";
    public static String sourceToken = "";
    public static String destinationToken = "";
    public static String route = "";
    public static String wormholescanLink = "";
    public static String txTo = "";
    public static String fromBalance = "";
    public static String destinationBalance = "";
    public static String destinationFinalBalance = "";
    public static String sourceGasFeeUsd = "";
    public static String destinationGasFeeUsd = "";
    public static String screenshotUrl = "";
    public static String toNativeBalance = "";
    public static String toFinalNativeBalance = "";

    public static boolean convertingNativeBalance = false;
    public static boolean isBlockedByHighFee = false;
    public static boolean requiresClaim = false;
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
        TestCase.sourceAmount = "";
        TestCase.destinationAmount = "";
        TestCase.sourceToken = "";
        TestCase.destinationToken = "";
        TestCase.route = "";
        TestCase.wormholescanLink = "";
        TestCase.txTo = "";
        TestCase.fromBalance = "";
        TestCase.destinationBalance = "";
        TestCase.destinationFinalBalance = "";
        TestCase.toNativeBalance = "";
        TestCase.toFinalNativeBalance = "";
        TestCase.convertingNativeBalance = false;
        TestCase.sourceGasFeeUsd = "";
        TestCase.destinationGasFeeUsd = "";
        TestCase.screenshotUrl = "";
        TestCase.isBlockedByHighFee = false;
        TestCase.requiresClaim = false;

        TestCase.metaMaskWasUnlocked = false;
        TestCase.phantomWasUnlocked = false;
        TestCase.leapWasUnlocked = false;
        TestCase.spikaWasUnlocked = false;
    }
}
