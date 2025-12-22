package com.blukers.automation.driver;

import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.config.app.AppConfig;
import com.blukers.automation.config.appium.AppiumConfig;
import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.URL;

public final class DriverFactory {

    private DriverFactory() {
        // utility class
    }

    public static AppiumDriver createDriver(FrameworkConfig config) {

        try {
            return switch (config.getPlatform()) {
                case ANDROID -> createAndroidDriver(
                        config.getApp(),
                        config.getAppium(),
                        config.getAndroid()
                );
                case IOS -> createIOSDriver(
                        config.getApp(),
                        config.getAppium(),
                        config.getIos()
                );
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Appium driver", e);
        }
    }

    private static AppiumDriver createAndroidDriver(
            AppConfig app,
            AppiumConfig appium,
            AndroidConfig android
    ) throws Exception {

        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");

        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }

        options.setNoReset(app.isNoReset());
        options.setAppPackage(android.getAppPackage());
        options.setAppActivity(android.getAppActivity());
        if (android.getDeviceUdid() != null && !android.getDeviceUdid().isBlank()) {
            options.setUdid(android.getDeviceUdid());
        }


        return new AndroidDriver(
                new URL(appium.getServerUrl()),
                options
        );
    }

    private static AppiumDriver createIOSDriver(
            AppConfig app,
            AppiumConfig appium,
            IOSConfig ios
    ) throws Exception {

        XCUITestOptions options = new XCUITestOptions();

        options.setPlatformName("iOS");
        options.setAutomationName("XCUITest");

        if (app.getAppPath() != null && !app.getAppPath().isBlank()) {
            options.setApp(app.getAppPath());
        }

        options.setNoReset(app.isNoReset());
        options.setBundleId(ios.getBundleId());

        return new IOSDriver(
                new URL(appium.getServerUrl()),
                options
        );
    }
}
