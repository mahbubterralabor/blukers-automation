package com.blukers.automation.steps;

import com.blukers.automation.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;

public class SanitySteps {

    @Given("the framework is initialized")
    public void the_framework_is_initialized() {
        System.out.println("Framework sanity check passed.");
    }

    @Given("the Appium driver is available")
    public void the_appium_driver_is_available() {
        AppiumDriver driver = DriverManager.getDriver();

        if (driver == null) {
            throw new IllegalStateException("Appium driver is NOT initialized");
        }

        System.out.println("Appium driver is available: " + driver.getSessionId());
    }
}
