package com.blukers.automation.config;

import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;

public final class ConfigValidator {

    private ConfigValidator() {
        // utility class
    }

    public static void validate(FrameworkConfig config) {

        if (config == null) {
            throw new IllegalStateException("FrameworkConfig must not be null");
        }

        if (config.getPlatform() == null) {
            throw new IllegalStateException("Platform must be specified");
        }

        if (config.getApp() == null) {
            throw new IllegalStateException("AppConfig must be specified");
        }

        if (config.getAppium() == null) {
            throw new IllegalStateException("AppiumConfig must be specified");
        }

        switch (config.getPlatform()) {
            case ANDROID -> validateAndroid(config.getAndroid());
            case IOS -> validateIOS(config.getIos());
            default -> throw new IllegalStateException(
                    "Unsupported platform: " + config.getPlatform()
            );
        }
    }

    private static void validateAndroid(AndroidConfig android) {
        if (android == null) {
            throw new IllegalStateException("AndroidConfig must be provided for ANDROID platform");
        }

        if (isBlank(android.getAppPackage())) {
            throw new IllegalStateException("android.app.package must be set");
        }

        if (isBlank(android.getAppActivity())) {
            throw new IllegalStateException("android.app.activity must be set");
        }
    }

    private static void validateIOS(IOSConfig ios) {
        if (ios == null) {
            throw new IllegalStateException("IOSConfig must be provided for IOS platform");
        }

        if (isBlank(ios.getBundleId())) {
            throw new IllegalStateException("ios.bundle.id must be set");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
