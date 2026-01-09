package com.blukers.automation.pages.base;

import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import com.blukers.automation.locators.LocatorResolver;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.util.Map;

public abstract class BasePage {

    protected final AppiumDriver driver;
    private final Map<String, LocatorModel> locators;

    protected BasePage(String pageName, Platform platform) {
        this.driver = DriverManager.getDriver();
        this.locators = LocatorLoader.load(platform, pageName);
    }

    protected By getBy(String key) {
        LocatorModel model = locators.get(key);
        if (model == null) {
            throw new IllegalArgumentException("Locator not found. key=" + key);
        }
        return LocatorResolver.resolve(model);
    }

    // --------------------
    // Actions
    // --------------------

    protected void click(String key) {
        driver.findElement(getBy(key)).click();
    }

    // --------------------
    // State helpers
    // --------------------

    protected boolean isDisplayed(String key) {
        try {
            return driver.findElement(getBy(key)).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Negative check:
     * - If element is missing => not displayed (true)
     * - If element exists but hidden => true
     */
    protected boolean isNotDisplayed(String key) {
        try {
            return !driver.findElement(getBy(key)).isDisplayed();
        } catch (NoSuchElementException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Safe text getter:
     * - returns null if element missing or not readable
     */
    protected String getText(String key) {
        try {
            return driver.findElement(getBy(key)).getText();
        } catch (NoSuchElementException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}