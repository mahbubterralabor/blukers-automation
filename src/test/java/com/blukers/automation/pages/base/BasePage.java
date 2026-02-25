package com.blukers.automation.pages.base;

import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public abstract class BasePage {
    protected final AppiumDriver driver;
    protected final Map<String, LocatorModel> locators;
    private final WebDriverWait fastWait;

    protected BasePage(String pageName, Platform platform) {
        this.driver = DriverManager.getDriver();
        this.locators = LocatorLoader.load(platform, pageName);
        // Polling every 100ms so Appium "wakes up" faster when an element is ready
        this.fastWait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofMillis(100));
    }

    protected WebElement waitForPresence(String key) {
        return fastWait.until(ExpectedConditions.presenceOfElementLocated(locators.get(key).toBy()));
    }

    public void enterText(String key, String text) {
        WebElement element = waitForPresence(key);
        element.click();

        // OPTIMIZATION: Do NOT call clear() or hideKeyboard() here.
        // Use a single shell command to clear the existing text if necessary.
        driver.executeScript("mobile: shell", Map.of(
                "command", "input",
                "args", List.of("keyevent", "KEYCODE_MOVE_END", "--longpress", "67")
        ));

        // Direct input: This is the fastest way to send text to a focused field
        String escapedText = text.replace(" ", "%s");
        driver.executeScript("mobile: shell", Map.of(
                "command", "input",
                "args", List.of("text", escapedText)
        ));

        // IMPORTANT: Only hide keyboard after the PASSWORD, never between email and password.
        if (key.toLowerCase().contains("password")) {
            // Use a Back keyevent instead of hideKeyboard() - it's much faster
            driver.executeScript("mobile: shell", Map.of(
                    "command", "input",
                    "args", List.of("keyevent", "4") // 4 is the Back button
            ));
        }
    }

    public void click(String key) {
        // Taps immediately once found in the XML tree
        waitForPresence(key).click();
    }
}