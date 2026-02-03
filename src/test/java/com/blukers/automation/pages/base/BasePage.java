package com.blukers.automation.pages.base;

import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.HidesKeyboard;

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

    public void type(String key, String text) {
        WebElement element = waitForVisible(key);
        element.click(); // Gain focus

        // Give the app a millisecond to process the focus change
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        element.clear();
        element.sendKeys(text);
        hideKeyboard();
    }

    public void adbType(String key, String text) {
        WebElement element = waitForVisible(key);
        element.click();

        // We wrap the text in quotes so characters like '@' don't break the shell command
        String command = String.format("input text '%s'", text);

        driver.executeScript("mobile: shell", Map.of(
                "command", "input",
                "args", List.of("text", text)
        ));

        hideKeyboard();
    }

    public void hideKeyboard() {
        try {
            if (driver instanceof HidesKeyboard) {
                ((HidesKeyboard) driver).hideKeyboard();
                System.out.println("DEBUG: Keyboard hidden successfully.");
            }
        } catch (Exception e) {
            // We catch the exception because Appium throws an error
            // if you try to hide a keyboard that isn't actually there.
            System.out.println("DEBUG: Keyboard was not present, skipping hide command.");
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