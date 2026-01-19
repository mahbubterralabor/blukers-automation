package com.blukers.automation.locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class LocatorModel {

    private String type;
    private String value;

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    /**
     * Converts locator model into Selenium/Appium By object.
     */
    public By toBy() {
        switch (type.toLowerCase()) {

            case "id":
                return By.id(value);

            case "xpath":
                return By.xpath(value);

            case "accessibility id":
            case "accessibilityid":
                return AppiumBy.accessibilityId(value);

            case "android uiautomator":
            case "uiautomator":
                return AppiumBy.androidUIAutomator(value);

            default:
                throw new IllegalArgumentException(
                        "Unsupported locator type: " + type
                );
        }
    }
}