package com.blukers.automation.pages.base;

import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public abstract class BasePage_Backup {

    protected final AppiumDriver driver;
    protected final Map<String, LocatorModel> locators;

    protected BasePage_Backup(String pageName, Platform platform) {
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
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator.toBy()));
    }

    /* ------------------ Action helpers ------------------ */

    protected void click(String key) {
        waitForVisible(key).click();
    }

    /**
     * Standard typing (sendKeys). Works for many native screens, can be flaky for Flutter.
     */
    protected void type(String key, String text) {
        WebElement element = waitForVisible(key);
        element.click(); // focus

        try { Thread.sleep(300); } catch (InterruptedException ignored) {}

        element.clear();
        element.sendKeys(text);
        hideKeyboard();
    }

    /**
     * ADB shell typing. Often more reliable for Flutter inputs.
     * Requires: automationName UiAutomator2 and device allows adb input.
     */
    protected void adbType(String key, String text) {
        WebElement element = waitForVisible(key);
        element.click();

        driver.executeScript("mobile: shell", Map.of(
                "command", "input",
                "args", List.of("text", text)
        ));

        hideKeyboard();
    }

    /**
     * Unified typing API for all pages:
     * Try sendKeys first, fallback to adbType if sendKeys fails / is flaky.
     */
    protected void enterText(String key, String text) {
        WebElement element = waitForVisible(key);
        element.click();
        //element.clear();
        try {
            element.clear();
        } catch (Exception ignored) {}

        // If Flutter ignores clear(), do an adb clear (Android only)
        try {
            driver.executeScript("mobile: shell", Map.of(
                    "command", "input",
                    "args", List.of("keyevent", "KEYCODE_MOVE_END")
            ));
            // delete a bunch of chars to be safe
            for (int i = 0; i < 50; i++) {
                driver.executeScript("mobile: shell", Map.of(
                        "command", "input",
                        "args", List.of("keyevent", "KEYCODE_DEL")
                ));
            }
        } catch (Exception ignored) {}

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        adbType(key, text);

        // IMPORTANT: Hide keyboard immediately so it doesn't cover the next field
        hideKeyboard();

        // Wait a moment for the UI to settle after keyboard hides
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

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