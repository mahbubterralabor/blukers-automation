package com.blukers.automation.pages.base;

import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public abstract class BasePage {

    protected final AppiumDriver driver;
    protected final Map<String, LocatorModel> locators;

    protected BasePage(String pageName, Platform platform) {
        this.driver = DriverManager.getDriver();
        this.locators = LocatorLoader.load(platform, pageName);
    }

    /* ------------------ Core locator resolver ------------------ */

    protected WebElement find(String key) {
        LocatorModel locator = locators.get(key);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }
        return driver.findElement(locator.toBy());
    }

    /* ------------------ Wait helpers ------------------ */

    protected WebElement waitForVisible(String key) {
        LocatorModel locator = locators.get(key);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator.toBy())
        );
    }

    /* ------------------ Action helpers ------------------ */

    protected void click(String key) {
        waitForVisible(key).click();
    }

    protected void type(String key, String value) {
        WebElement element = waitForVisible(key);
        element.clear();
        element.sendKeys(value);
    }

    /* ------------------ State helpers ------------------ */

    protected boolean isDisplayed(String key) {
        try {
            return waitForVisible(key).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getText(String key) {
        return waitForVisible(key).getText();
    }
}