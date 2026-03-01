package com.blukers.automation.hooks;

import com.blukers.automation.app.AppStateManager;
import com.blukers.automation.app.AuthState;
import com.blukers.automation.config.ConfigLoader;
import com.blukers.automation.config.ConfigValidator;
import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.driver.DriverService;
import com.blukers.automation.testdata.TestDataContext;
import com.blukers.automation.testdata.TestDataTagResolver;
import com.blukers.automation.util.Log;
import io.cucumber.java.*;
import org.slf4j.Logger;

public class Hooks {

    private static final Logger log = Log.get(Hooks.class);

    private static FrameworkConfig CONFIG;

    // Tag names (keep simple and explicit)
    private static final String TAG_RELAUNCH = "@relaunch";

    @BeforeAll
    public static void beforeAll() {
        log.info("===== Test run setup started =====");
        CONFIG = ConfigLoader.load();
        ConfigValidator.validate(CONFIG);
        DriverService.start(CONFIG);
        AppStateManager.ensureForeground(CONFIG);
        AuthState.ensureLogOut();


        log.info("===== Test run setup completed =====");
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("===== Scenario setup started =====");

        // 1) Test data tag resolve (thread local)
        TestDataTagResolver.resolveDataKey(scenario).ifPresent(key -> {
            TestDataContext.setDataKey(key);
            log.info("TestData key selected from tag: {}", key);
        });

        // 2) Decide how to prepare app state
        if (scenario.getSourceTagNames().contains(TAG_RELAUNCH)) {
            log.info("Scenario has {} tag. Performing relaunch.", TAG_RELAUNCH);
            AppStateManager.relaunch(CONFIG);
        } else {
            // health check -> if needs relaunch then relaunch, else ensureForeground
            AppStateManager.HealthStatus health = AppStateManager.checkHealth(CONFIG);

            switch (health) {
                case SESSION_INVALID -> {
                    // Relauch cannot fix a dead session; restart driver session
                    log.warn("Appium session invalid. Restarting driver session.");
                    DriverService.stop();
                    DriverService.start(CONFIG);
                    AppStateManager.ensureForeground(CONFIG); // after new session
                }
                case RELAUNCH_RECOMMENDED -> {
                    log.warn("Health check recommends relaunch. Performing relaunch.");
                    AppStateManager.relaunch(CONFIG);
                }
                case OK -> {
                    AppStateManager.ensureForeground(CONFIG);
                }
            }
        }

        // 3) Always guarantee a known auth state (logout requires app active)
        AuthState.ensureLogOut();

        log.info("===== Scenario setup completed =====");
    }

    @After
    public void afterScenario() {
        log.info("===== Scenario teardown started =====");

        // Clear ThreadLocal testdata key
        TestDataContext.clear();

        log.info("===== Scenario teardown completed =====");
    }

    @AfterAll
    public static void afterAll() {
        log.info("===== Test run teardown started =====");
        try {
            AuthState.ensureLogOut();
            DriverService.stop();
        } finally {
            CONFIG = null;

        }
        log.info("===== Test run teardown completed =====");
    }
}