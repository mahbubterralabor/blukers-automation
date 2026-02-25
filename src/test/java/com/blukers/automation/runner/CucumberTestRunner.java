package com.blukers.automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.blukers.automation.steps",
                "com.blukers.automation.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber-report.json"
        },
        tags = "@login_invalid"
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}