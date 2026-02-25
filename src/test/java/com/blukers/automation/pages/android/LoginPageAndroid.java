package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage_Backup;

/**
 * Android Page Object for Login screen.
 *
 * Expected locator keys in locators/android/LoginPage.json:
 * - emailInput
 * - passwordInput
 * - loginButton
 *
 * Optional (for invalid login assertions):
 * - loginErrorMessage   (or rename, but keep in sync with this class)
 */
public class LoginPageAndroid extends BasePage_Backup {

    private static final String PAGE_NAME = "LoginPage";

    public LoginPageAndroid() {
        super(PAGE_NAME, Platform.ANDROID);
    }

    /* ------------------ Screen state ------------------ */

    public boolean isLoginScreenVisible() {
        // Any unique element on Login screen is fine
        return isDisplayed("emailInput") || isDisplayed("passwordInput") || isDisplayed("loginButton");
    }

    public void waitForLoginScreen() {
        waitForVisible("emailInput");
        waitForVisible("passwordInput");
        waitForVisible("loginButton");
    }

    /* ------------------ Actions ------------------ */

    public void enterEmail(String email) {
        enterText("emailInput", email);
    }

    public void enterPassword(String password) {
        enterText("passwordInput", password);
    }

    public void enterCredentials(String email, String password) {
        enterEmail(email);
        enterPassword(password);
    }

    public void tapOnLogInButton() {
        click("loginButton");
    }

    /* ------------------ Button state ------------------ */

    public boolean isLoginButtonEnabled() {
        return isEnabled("loginButton");
    }

    /* ------------------ Error handling ------------------ */

    /**
     * For invalid login scenarios.
     * Add locator key "loginErrorMessage" in LoginPage.json.
     */
    public boolean isLoginErrorMessageVisible() {
        return isDisplayed("loginErrorMessage");
    }

    public String getLoginErrorMessageText() {
        return getText("loginErrorMessage");
    }
}