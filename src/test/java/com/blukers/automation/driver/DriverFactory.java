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
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {}

    public static AppiumDriver createDriver(FrameworkConfig config) {
        Platform platform = config.getPlatform();
        try {
            return switch (platform) {
                case ANDROID -> createAndroidDriver(config);
                case IOS -> createIOSDriver(config);
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Appium driver for: " + platform, e);
        }
    }

    private static AppiumDriver createAndroidDriver(FrameworkConfig config) throws Exception {
        AppConfig app = config.getApp();
        AndroidConfig android = config.getAndroid();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setNoReset(app.isNoReset())
                .setAppPackage(android.getAppPackage())
                .setAppActivity(android.getAppActivity());

        // --- SPEED & PERFORMANCE CAPABILITIES ---

        // 1. Disable Idle Wait: Forces immediate action despite Flutter animations
        options.setCapability("appium:waitForIdleTimeout", 0);

        // 2. Ignore Views: Simplifies the UI tree so Appium finds elements faster
        options.setCapability("appium:settings[ignoreUnimportantViews]", true);

        // 3. Fast Input: Uses Appium's internal IME for rapid text entry
        options.setCapability("appium:unicodeKeyboard", true);
        options.setCapability("appium:resetKeyboard", true);

        // 4. OS Optimization: Prevents Android window animations from delaying clicks
        options.setCapability("appium:disableWindowAnimation", true);

        // 5. Debugging: Adds timestamps to logs to identify bottlenecks
        options.setCapability("appium:eventTimings", true);

        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }
        if (android.getDeviceUdid() != null && !android.getDeviceUdid().isBlank()) {
            options.setUdid(android.getDeviceUdid());
        }

        AndroidDriver driver = new AndroidDriver(URI.create(config.getAppium().getServerUrl()).toURL(), options);

        // CRITICAL: Set Implicit Wait to ZERO. Only use WebDriverWait in BasePage.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        return driver;
    }

    private static AppiumDriver createIOSDriver(FrameworkConfig config) throws Exception {
        AppConfig app = config.getApp();
        IOSConfig ios = config.getIos();

        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setNoReset(app.isNoReset())
                .setBundleId(ios.getBundleId());

        // iOS Performance Optimization
        options.setCapability("appium:waitForQuiescence", false);

        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }

        IOSDriver driver = new IOSDriver(URI.create(config.getAppium().getServerUrl()).toURL(), options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        return driver;
    }
}