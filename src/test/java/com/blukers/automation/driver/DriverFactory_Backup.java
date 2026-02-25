package com.blukers.automation.driver;

import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.config.Platform;
import com.blukers.automation.config.app.AppConfig;
import com.blukers.automation.config.appium.AppiumConfig;
import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.URI;

/**
 * Creates AppiumDriver instances based on platform.
 * Used Fluent Interface (Method Chaining) for cleaner configuration.
 */
public final class DriverFactory_Backup {

    private DriverFactory_Backup() {}

    public static AppiumDriver createDriver(FrameworkConfig config) {
        Platform platform = config.getPlatform();
        AppConfig app = config.getApp();
        AppiumConfig appium = config.getAppium();

        try {
            return switch (platform) {
                case ANDROID -> createAndroidDriver(app, appium, config.getAndroid());
                case IOS -> createIOSDriver(app, appium, config.getIos());
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Appium driver for platform: " + platform, e);
        }
    }

    private static AppiumDriver createAndroidDriver(
            AppConfig app,
            AppiumConfig appium,
            AndroidConfig android

    ) throws Exception {

        // Method Chaining / Fluent Interface implementation
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setNoReset(app.isNoReset())
                .setAppPackage(android.getAppPackage())
                .setAppActivity(android.getAppActivity());


        // Conditional chaining for optional parameters
        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }

        if (android.getDeviceUdid() != null && !android.getDeviceUdid().isBlank()) {
            options.setUdid(android.getDeviceUdid());
        }

        return new AndroidDriver(URI.create(appium.getServerUrl()).toURL(), options);
    }

    private static AppiumDriver createIOSDriver(
            AppConfig app,
            AppiumConfig appium,
            IOSConfig ios
    ) throws Exception {

        // Method Chaining / Fluent Interface implementation
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setNoReset(app.isNoReset())
                .setBundleId(ios.getBundleId());

        // Conditional chaining for optional parameters
        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }

        return new IOSDriver(URI.create(appium.getServerUrl()).toURL(), options);
    }
}