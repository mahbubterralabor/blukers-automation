package com.blukers.automation.driver;

import com.blukers.automation.config.ConfigLoader;
import com.blukers.automation.config.ConfigValidator;
import com.blukers.automation.config.FrameworkConfig;

import io.appium.java_client.AppiumDriver;

public final class DriverService {

    private DriverService() {
        // utility class
    }

    public static void start() {

        FrameworkConfig config = ConfigLoader.load();
        ConfigValidator.validate(config);

        AppiumDriver driver = DriverFactory.createDriver(config);
        DriverManager.setDriver(driver);
    }

    public static void stop() {
        DriverManager.unload();
    }
}
