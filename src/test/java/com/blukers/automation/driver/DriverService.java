package com.blukers.automation.driver;

import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.util.Log;
import org.slf4j.Logger;

/**
 * Responsible ONLY for starting and stopping the Appium driver.
 * Does NOT load or validate configuration.
 */
public final class DriverService {

    private static final Logger log = Log.get(DriverService.class);

    private DriverService() {
        // utility class
    }

    /**
     * Starts Appium driver for the current scenario.
     */
    public static void start(FrameworkConfig config) {
        log.info("Starting Appium driver");

        if (DriverManager.hasDriver()) {
            log.warn("Driver already exists for this thread. Skipping start.");
            return;
        }

        try {
            DriverManager.setDriver(
                    DriverFactory_Backup.createDriver(config)
            );
            log.info("Appium driver started successfully");
        } catch (Exception e) {
            log.error("Failed to start Appium driver", e);
            throw e;
        }
    }

    /**
     * Stops Appium driver for the current scenario.
     */
    public static void stop() {
        log.info("Stopping Appium driver");

        try {
            DriverManager.quitDriver();
            log.info("Appium driver stopped successfully");
        } catch (Exception e) {
            log.error("Error while stopping Appium driver", e);
        }
    }
}