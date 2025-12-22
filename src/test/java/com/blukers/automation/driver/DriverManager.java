package com.blukers.automation.driver;

import io.appium.java_client.AppiumDriver;

public final class DriverManager {

    private static AppiumDriver driver;

    private DriverManager() {
        // prevent instantiation
    }

    public static AppiumDriver getDriver() {
        return driver;
    }

    static void setDriver(AppiumDriver driverInstance) {
        driver = driverInstance;
    }

    static void unload() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
