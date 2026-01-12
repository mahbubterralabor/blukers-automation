package com.blukers.automation.config;

import com.blukers.automation.config.app.AppConfig;
import com.blukers.automation.config.appium.AppiumConfig;
import com.blukers.automation.config.platform.android.AndroidConfig;
import com.blukers.automation.config.platform.ios.IOSConfig;
import com.blukers.automation.util.Log;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework configuration from framework.properties
 * and builds a typed FrameworkConfig object.
 */
public final class ConfigLoader {

    private static final Logger log = Log.get(ConfigLoader.class);

    private static final String CONFIG_FILE = "framework.properties";

    private ConfigLoader() {
        // utility class
    }

    /**
     * Loads framework configuration.
     *
     * @return fully populated FrameworkConfig
     */
    public static FrameworkConfig load() {

        log.info("Loading framework configuration from {}", CONFIG_FILE);

        Properties props = new Properties();

        try (InputStream is =
                     ConfigLoader.class
                             .getClassLoader()
                             .getResourceAsStream(CONFIG_FILE)) {

            if (is == null) {
                throw new IllegalStateException(
                        "Configuration file not found: " + CONFIG_FILE
                );
            }

            props.load(is);

        } catch (Exception e) {
            log.error("Failed to load configuration file", e);
            throw new RuntimeException("Failed to load framework configuration", e);
        }

        FrameworkConfig config = buildConfig(props);

        log.info("Framework configuration loaded successfully");

        return config;
    }

    // ----------------- INTERNAL BUILDERS -----------------

    private static FrameworkConfig buildConfig(Properties props) {

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

        AndroidConfig androidConfig = new AndroidConfig(
                get(props, "android.app.package"),
                get(props, "android.app.activity"),
                get(props, "android.device.udid")
        );

        IOSConfig iosConfig = new IOSConfig(
                get(props, "ios.bundle.id")
        );

        return new FrameworkConfig(
                platform,
                appConfig,
                appiumConfig,
                androidConfig,
                iosConfig
        );
    }

    /**
     * Reads property with System override support.
     * System property (-Dkey=value) takes precedence.
     */
    private static String get(Properties props, String key) {
        return System.getProperty(key, props.getProperty(key));
    }
}