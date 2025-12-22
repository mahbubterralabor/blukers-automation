package com.blukers.automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "classpath:features",
        glue = {
                "com.blukers.automation.steps",
                "com.blukers.automation.hooks"
        },
        plugin = {
                "pretty"
        }
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
