package com.blukers.automation.pages.base;

import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public abstract class BasePage {

    protected final AppiumDriver driver;
    protected final Map<String, LocatorModel> locators;

    protected BasePage(String pageName, Platform platform) {
        this.driver = DriverManager.getDriver();
        this.locators = LocatorLoader.load(platform, pageName);
    }

    /* ------------------ Wait helpers ------------------ */

    protected WebElement waitForVisible(String key) {
        LocatorModel locator = locators.get(key);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator.toBy()));
    }

    /* ------------------ Action helpers ------------------ */

    protected void click(String key) {
        waitForVisible(key).click();
        //driver.findElement(By.id(key)).click();
    }

    protected void type(String key, String text) {
        adbType(key,text);
    }

    /**
     * ADB shell typing. Often more reliable for Flutter inputs.
     * Requires: automationName UiAutomator2 and device allows adb input.
     */
    protected void adbType(String key, String text) {
        WebElement element = waitForVisible(key);
        element.click();
        element.clear();
        String value = element.getText();
        if (!value.trim().isEmpty()) {
            try {
                for (int i = 0; i < 50; i++) {
                    driver.executeScript("mobile: shell", Map.of(
                            "command", "input",
                            "args", List.of("keyevent", "KEYCODE_DEL")
                    ));
                }
            } catch (Exception ignored) {
            }
        }
        try {
            driver.executeScript("mobile: shell", Map.of(
                    "command", "input",
                    "args", List.of("text", text)
            ));
        } catch (Exception ignored) {
        }

        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        hideKeyboard();
    }

    /**
     * Unified typing API for all pages:
     * Try sendKeys first, fallback to adbType if sendKeys fails / is flaky.
     */
    protected void hideKeyboard() {
        try {
            if (driver instanceof HidesKeyboard) {
                ((HidesKeyboard) driver).hideKeyboard();
            }
        } catch (Exception ignored) {
            // Keyboard may not be present; do not fail tests for this.
        }
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

    protected boolean isEnabled(String key) {
        try {
            return waitForVisible(key).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}