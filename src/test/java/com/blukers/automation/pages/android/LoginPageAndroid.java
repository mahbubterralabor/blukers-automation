package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

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
public class LoginPageAndroid extends BasePage {

    private static final String PAGE_NAME = "LoginPage";

    public LoginPageAndroid() {
        super(PAGE_NAME, Platform.ANDROID);
    }

    /* ------------------ Screen state ------------------ */

    public boolean isLoginScreenVisible() {
        return isDisplayed("emailInput");
    }

    /* ------------------ Actions ------------------ */

    public void enterEmail(String email) {
        adbType("emailInput", email);
    }

    public void enterPassword(String password) {
        adbType("passwordInput", password);
    }


    public void tapOnLogInButton() {
        click("loginButton");
    }

    /* ------------------ Button state ------------------ */

    public boolean isLoginButtonEnabled() {
        return isEnabled("loginButton");
    }

    /* ------------------ Error handling ------------------ */

    public boolean isLoginErrorMessageVisible() {
        return isDisplayed("loginErrorMessage");
    }

    public String getLoginErrorMessageText() {
        return getText("loginErrorMessage");
    }
}