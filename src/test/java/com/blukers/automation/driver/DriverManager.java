package com.blukers.automation.driver;

import io.appium.java_client.AppiumDriver;

/**
 * Thread-safe driver storage using ThreadLocal.
 * Supports future parallel execution.
 */
public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        // utility class
    }

    public static void setDriver(AppiumDriver driver) {
        DRIVER.set(driver);
    }

    public static AppiumDriver getDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "Driver is not initialized. Did you forget to start it in Hooks?"
            );
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    /**
     * Quits the driver session (if present) and clears ThreadLocal to prevent leaks.
     */
    public static void quitDriver() {
        AppiumDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            DRIVER.remove();
        }
    }
}