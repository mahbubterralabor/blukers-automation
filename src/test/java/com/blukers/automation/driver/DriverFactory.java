package com.blukers.automation.driver;

import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.config.Platform;
import com.blukers.automation.config.app.AppConfig;
import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;
import com.blukers.automation.util.Log;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;

import java.net.URI;
import java.time.Duration;

public final class DriverFactory {

    private static final Logger log = Log.get(DriverFactory.class);

    private DriverFactory() {}

    public static AppiumDriver createDriver(FrameworkConfig config) {
        Platform platform = config.getPlatform();
        log.info("Creating driver for platform={}", platform);

        try {
            AppiumDriver driver = switch (platform) {
                case ANDROID -> createAndroidDriver(config);
                case IOS -> createIOSDriver(config);
            };

            log.info("Driver created. sessionId={}, platformName={}, automationName={}",
                    driver.getSessionId(),
                    driver.getCapabilities().getCapability("platformName"),
                    driver.getCapabilities().getCapability("appium:automationName"));

            return driver;
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

        options.setCapability("appium:waitForIdleTimeout", 0);
        options.setCapability("appium:settings[ignoreUnimportantViews]", true);
        options.setCapability("appium:unicodeKeyboard", true);
        options.setCapability("appium:resetKeyboard", true);
        options.setCapability("appium:disableWindowAnimation", true);
        options.setCapability("appium:eventTimings", true);

        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }
        if (android.getDeviceUdid() != null && !android.getDeviceUdid().isBlank()) {
            options.setUdid(android.getDeviceUdid());
        }

        log.info("Android caps: appPackage={}, appActivity={}, udid={}, noReset={}",
                android.getAppPackage(), android.getAppActivity(), android.getDeviceUdid(), app.isNoReset());

        AndroidDriver driver = new AndroidDriver(URI.create(config.getAppium().getServerUrl()).toURL(), options);
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

        options.setCapability("appium:waitForQuiescence", false);

        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }

        log.info("iOS caps: bundleId={}, noReset={}, appPathPresent={}",
                ios.getBundleId(), app.isNoReset(),
                app.getAppPath() != null && !app.getAppPath().isBlank());

        IOSDriver driver = new IOSDriver(URI.create(config.getAppium().getServerUrl()).toURL(), options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        return driver;
    }
}