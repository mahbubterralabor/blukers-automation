package com.blukers.automation.hooks;

import com.blukers.automation.driver.DriverService;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class DriverHooks {

    @Before
    public void beforeScenario() {
        DriverService.start();
    }

    @After
    public void afterScenario() {
        DriverService.stop();
    }
}
