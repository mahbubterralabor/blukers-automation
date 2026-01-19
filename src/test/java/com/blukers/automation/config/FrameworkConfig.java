package com.blukers.automation.config;

import com.blukers.automation.config.app.AppConfig;
import com.blukers.automation.config.appium.AppiumConfig;
import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;

public final class FrameworkConfig {

    private final Platform platform;
    private final AppConfig app;
    private final AppiumConfig appium;
    private final AndroidConfig android;
    private final IOSConfig ios;

    public FrameworkConfig(
            Platform platform,
            AppConfig app,
            AppiumConfig appium,
            AndroidConfig android,
            IOSConfig ios
    ) {
        this.platform = platform;
        this.app = app;
        this.appium = appium;
        this.android = android;
        this.ios = ios;
    }

    public Platform getPlatform() {
        return platform;
    }

    public AppConfig getApp() {
        return app;
    }

    public AppiumConfig getAppium() {
        return appium;
    }

    public AndroidConfig getAndroid() {
        return android;
    }

    public IOSConfig getIos() {
        return ios;
    }
}