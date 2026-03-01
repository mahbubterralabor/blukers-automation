package com.blukers.automation.pages.base;

import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.locators.LocatorLoader;
import com.blukers.automation.locators.LocatorModel;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.AppiumBy;
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
    }

    protected void type(String key, String text) {
        adbType(key, text);
    }

    /**
     * Scrolls (Android) until element becomes visible, then clicks it.
     * Use this ONLY for elements that can be off-screen (e.g., Logout).
     *
     * Side effects:
     * - No changes to existing click/waits
     * - Only scrolls when you call this method explicitly
     */
    protected void scrollToAndClick(String key) {
        LocatorModel locator = locators.get(key);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }

        // Fast path: if already visible, do normal click
        if (isDisplayed(key)) {
            click(key);
            return;
        }

        // Android-only: use UiScrollable to bring element into view
        // Works best when locator is accessibilityId (content-desc) or resource-id.
        String platformName = String.valueOf(driver.getCapabilities().getCapability("platformName"));
        if (!"ANDROID".equalsIgnoreCase(platformName)) {
            // If you later run iOS and need scroll, implement separately; for now fail fast.
            throw new UnsupportedOperationException("scrollToAndClick is implemented for Android only.");
        }

        // If your LocatorModel is accessibilityId for Logout, this will work:
        // scrollIntoView(description("Logout"))
        // We do NOT assume key == value. We try to infer from locator.toBy().
        String uiScroll = buildUiScrollable(locator);

        driver.findElement(AppiumBy.androidUIAutomator(uiScroll));

        // Now element should be visible
        click(key);
    }

    private String buildUiScrollable(LocatorModel locator) {
        // We cannot reliably parse "By" into its raw value, so we support common cases by reading
        // LocatorModel fields if they exist. If your LocatorModel does not expose these getters,
        // replace these two lines accordingly.
        String type = locator.getType();   // <-- adjust ONLY if your LocatorModel uses different name
        String value = locator.getValue(); // <-- adjust ONLY if your LocatorModel uses different name

        if (type == null) type = "";
        if (value == null) value = "";

        String scrollable = "new UiScrollable(new UiSelector().scrollable(true)).setAsVerticalList()";

        return switch (type) {
            case "accessibilityId" ->
                    scrollable + ".scrollIntoView(new UiSelector().description(\"" + escape(value) + "\"))";
            case "id" ->
                    scrollable + ".scrollIntoView(new UiSelector().resourceId(\"" + escape(value) + "\"))";
            case "uiautomator" ->
                // If the locator itself is uiautomator, we cannot embed it safely in UiScrollable.
                // Best effort: just scroll forward once.
                    scrollable + ".scrollForward()";
            default ->
                // Fallback: scroll forward once
                    scrollable + ".scrollForward()";
        };
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
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
        if (value != null && !value.trim().isEmpty()) {
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