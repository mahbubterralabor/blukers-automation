package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.locators.LocatorModel;
import com.blukers.automation.pages.base.BasePage;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

public class ProfilePageAndroid extends BasePage {

    private static final String PAGE_NAME = "ProfilePage";

    public ProfilePageAndroid() {
        super(PAGE_NAME, Platform.ANDROID);
    }

    // Your stable rule
    public boolean isOnProfilePage() {
        return isDisplayed("Account_Button");
    }

    /**
     * Open Profile from bottom nav (only when bottom nav is visible).
     * Uses your JSON key: Bottom_Profile_Icon
     */
    public void openFromBottomNav() {
        click("Bottom_Profile_Icon");

        // Confirm navigation by waiting for a stable element
        if (!isOnProfilePage()) {
            throw new IllegalStateException("Failed to open Profile page. Account_Button not detected.");
        }
    }

    /**
     * Logout is at the bottom of Profile page -> scroll until present then click.
     */
    public void logout() {
        if (!isOnProfilePage()) {
            throw new IllegalStateException("logout() called but not on Profile page. Account_Button not detected.");
        }

        scrollDownUntilLogoutVisible();
        click("Logout_Button");
    }

    /**
     * IMPORTANT:
     * - Do NOT use isDisplayed() inside the scroll loop (it waits up to 5s each time).
     * - Use a fast presence check, then swipe immediately.
     */
    private void scrollDownUntilLogoutVisible() {
        int maxSwipes = 8;

        for (int i = 0; i < maxSwipes; i++) {
            if (isPresentFast("Logout_Button")) {
                return;
            }

            swipeUpW3C();

            // tiny settle time so UI updates after swipe
            try { Thread.sleep(250); } catch (InterruptedException ignored) {}
        }

        // Final check
        if (!isPresentFast("Logout_Button")) {
            throw new IllegalStateException("Element not found after scroll: Logout_Button");
        }
    }

    /**
     * Fast presence check without WebDriverWait.
     * This avoids 5s waits inside scroll loops.
     */
    private boolean isPresentFast(String key) {
        LocatorModel locator = locators.get(key);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for key: " + key);
        }
        return !driver.findElements(locator.toBy()).isEmpty();
    }

    /**
     * Real swipe using W3C actions (reliable with java-client 10).
     */
    private void swipeUpW3C() {
        Dimension size = driver.manage().window().getSize();

        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.75);
        int endY   = (int) (size.getHeight() * 0.25);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(450), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }
}