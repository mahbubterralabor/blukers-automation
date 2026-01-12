package com.blukers.automation.config;

import com.blukers.automation.config.app.AppConfig;
import com.blukers.automation.config.appium.AppiumConfig;
import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;
import com.blukers.automation.util.Log;

import org.slf4j.Logger;

/**
 * Validates framework configuration.
 * Fails fast if required values are missing or inconsistent.
 */
public final class ConfigValidator {

    private static final Logger log = Log.get(ConfigValidator.class);

    private ConfigValidator() {
        // utility class
    }

    /**
     * Validates the given FrameworkConfig.
     * Throws IllegalStateException if configuration is invalid.
     */
    public static void validate(FrameworkConfig config) {

        log.info("Validating framework configuration");

        if (config == null) {
            throw new IllegalStateException("FrameworkConfig is null");
        }

        validateApp(config.getApp());
        validateAppium(config.getAppium());

        switch (config.getPlatform()) {
            case ANDROID -> validateAndroid(config.getAndroid());
            case IOS -> validateIOS(config.getIos());
            default -> throw new IllegalStateException(
                    "Unsupported platform: " + config.getPlatform()
            );
        }

        log.info("Framework configuration validation successful");
    }

    // ----------------- COMMON VALIDATIONS -----------------

    private static void validateApp(AppConfig app) {

        if (app == null) {
            throw new IllegalStateException("AppConfig is missing");
        }

        // app.path can be empty if app already installed
        // noReset is always valid (boolean)
    }

    private static void validateAppium(AppiumConfig appium) {

        if (appium == null) {
            throw new IllegalStateException("AppiumConfig is missing");
        }

        require(appium.getServerUrl(), "appium.server.url");

        if (appium.getCommandTimeoutSeconds() <= 0) {
            throw new IllegalStateException(
                    "appium.command.timeout.seconds must be > 0"
            );
        }
    }

    // ----------------- PLATFORM VALIDATIONS -----------------

    private static void validateAndroid(AndroidConfig android) {

        if (android == null) {
            throw new IllegalStateException("AndroidConfig is missing");
        }

        require(android.getAppPackage(), "android.app.package");
        require(android.getAppActivity(), "android.app.activity");

        // android.device.udid is optional (local vs cloud)
    }

    private static void validateIOS(IOSConfig ios) {

        if (ios == null) {
            throw new IllegalStateException("IOSConfig is missing");
        }

        require(ios.getBundleId(), "ios.bundle.id");
    }

    // ----------------- HELPER -----------------

    private static void require(String value, String key) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required configuration missing or empty: " + key
            );
        }
    }
}