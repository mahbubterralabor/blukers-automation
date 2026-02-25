package com.blukers.automation.hooks;

import com.blukers.automation.app.AppStateManager;
import com.blukers.automation.config.ConfigLoader;
import com.blukers.automation.config.ConfigValidator;
import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.driver.DriverService;
import com.blukers.automation.testdata.TestDataContext;
import com.blukers.automation.testdata.TestDataTagResolver;
import com.blukers.automation.util.Log;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;

public class Hooks {

    private static final Logger log = Log.get(Hooks.class);

    private FrameworkConfig config;

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("===== Scenario setup started =====");

        config = ConfigLoader.load();
        ConfigValidator.validate(config);
        DriverService.start(config);
        AppStateManager.relaunch(config);

        // Resolve and store @data_* key for this scenario
        TestDataTagResolver.resolveDataKey(scenario).ifPresent(key -> {
            TestDataContext.setDataKey(key);
            log.info("TestData key selected from tag: {}", key);
        });



        log.info("===== Scenario setup completed =====");
    }

    @After
    public void afterScenario() {
        log.info("===== Scenario teardown started =====");

        DriverService.stop();

        // Clear ThreadLocal testdata key
        TestDataContext.clear();

        log.info("===== Scenario teardown completed =====");
    }
}