package com.blukers.automation.locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public final class LocatorResolver {

    private LocatorResolver() {
        // utility class
    }

    public static By resolve(LocatorModel model) {

        String type = model.getType();
        String value = model.getValue();

        if (type == null || value == null) {
            throw new IllegalArgumentException(
                    "Locator type or value is null: " + model
            );
        }

        return switch (type.toLowerCase()) {

            case "id" ->
                    By.id(value);

            case "accessibilityid", "accessibility_id" ->
                    AppiumBy.accessibilityId(value);

            case "xpath" ->
                    By.xpath(value);

            case "classname", "class_name" ->
                    By.className(value);

            case "androiduiautomator", "android_uiautomator" ->
                    AppiumBy.androidUIAutomator(value);

            case "iosclasschain", "ios_class_chain" ->
                    AppiumBy.iOSClassChain(value);

            case "iospredicate", "ios_predicate" ->
                    AppiumBy.iOSNsPredicateString(value);

            default ->
                    throw new UnsupportedOperationException(
                            "Unsupported locator type: " + type
                    );
        };
    }
}
