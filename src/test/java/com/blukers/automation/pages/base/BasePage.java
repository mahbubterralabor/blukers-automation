package com.blukers.automation.pages.base;

import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import com.blukers.automation.locators.LocatorResolver;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.Map;

public abstract class BasePage {

    protected final AppiumDriver driver;
    private final Map<String, LocatorModel> locators;

    protected BasePage(String pageName, String platform) {
        this.driver = DriverManager.getDriver();

        if (this.driver == null) {
            throw new WebDriverException(
                    "AppiumDriver is null. Ensure driver is started before using page objects."
            );
        }

        this.locators = LocatorLoader.load(platform, pageName);
    }

    protected WebElement find(String locatorKey) {
        LocatorModel model = getLocator(locatorKey);
        By by = LocatorResolver.resolve(model);
        return driver.findElement(by);
    }

    protected void click(String locatorKey) {
        find(locatorKey).click();
    }

    protected void type(String locatorKey, String text) {
        WebElement element = find(locatorKey);
        element.clear();
        element.sendKeys(text);
    }

    private LocatorModel getLocator(String locatorKey) {
        LocatorModel model = locators.get(locatorKey);

        if (model == null) {
            throw new IllegalArgumentException(
                    "Locator not found for key: " + locatorKey
            );
        }

        return model;
    }
}
