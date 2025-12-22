package com.blukers.automation.config;

import com.blukers.automation.config.app.AppConfig;
import com.blukers.automation.config.appium.AppiumConfig;
import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {

    private static FrameworkConfig config;

    private ConfigLoader() {
        // prevent instantiation
    }

    public static FrameworkConfig load() {

        if (config != null) {
            return config;
        }

        Properties props = new Properties();

        try (InputStream is =
                     ConfigLoader.class
                             .getClassLoader()
                             .getResourceAsStream("framework.properties")) {

            if (is == null) {
                throw new IllegalStateException("framework.properties not found in classpath");
            }

            props.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load framework.properties", e);
        }

        Platform platform = Platform.valueOf(
                get(props, "platform").toUpperCase()
        );

        AppConfig appConfig = new AppConfig(
                get(props, "app.path"),
                Boolean.parseBoolean(get(props, "no.reset"))
        );

        AppiumConfig appiumConfig = new AppiumConfig(
                get(props, "appium.server.url"),
                Integer.parseInt(get(props, "appium.command.timeout.seconds"))
        );

        AndroidConfig androidConfig = platform == Platform.ANDROID
                ? new AndroidConfig(
                get(props, "android.app.package"),
                get(props, "android.app.activity"),
                get(props, "android.device.udid")
        )
                : null;

        IOSConfig iosConfig = platform == Platform.IOS
                ? new IOSConfig(
                get(props, "ios.bundle.id")
        )
                : null;

        config = new FrameworkConfig(
                platform,
                appConfig,
                appiumConfig,
                androidConfig,
                iosConfig
        );

        return config;
    }

    private static String get(Properties props, String key) {
        return System.getProperty(key, props.getProperty(key));
    }
}
