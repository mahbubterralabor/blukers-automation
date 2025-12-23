package com.blukers.automation.config;

public enum Platform {
    // These constants are now "Self-Describing"
    ANDROID("Android", "UiAutomator2"),
    IOS("iOS", "XCUITest");

    private final String platformName;
    private final String automationName;

    Platform(String platformName, String automationName) {
        this.platformName = platformName;
        this.automationName = automationName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getAutomationName() {
        return automationName;
    }
}