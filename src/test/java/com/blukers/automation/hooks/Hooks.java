package com.blukers.automation.hooks;

import com.blukers.automation.config.ConfigLoader;
import com.blukers.automation.config.ConfigValidator;
import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.driver.DriverService;
import com.blukers.automation.util.Log;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.slf4j.Logger;

/**
 * Cucumber Hooks:
 * - Loads + validates framework configuration
 * - Starts/stops driver per scenario
 *
 * This ensures DriverService stays focused on driver lifecycle only.
 */
public class Hooks {

    private static final Logger log = Log.get(Hooks.class);

    private FrameworkConfig config;

    @Before
    public void beforeScenario() {
        log.info("===== Scenario setup started =====");

        // Load configuration once per scenario
        config = ConfigLoader.load();

        // Validate configuration (throws if invalid)
        ConfigValidator.validate(config);

        // Start driver using already-loaded config
        DriverService.start(config);

        log.info("===== Scenario setup completed =====");
    }

    @After
    public void afterScenario() {
        log.info("===== Scenario teardown started =====");

        // Stop driver and clean up ThreadLocal
        DriverService.stop();

        log.info("===== Scenario teardown completed =====");
    }
}