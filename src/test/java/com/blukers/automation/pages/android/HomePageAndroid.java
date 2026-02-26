package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

public class HomePageAndroid extends BasePage {

    private static final String PAGE_NAME = "HomePage";

    public HomePageAndroid() {
        super(PAGE_NAME, Platform.ANDROID);
    }

    /* ---------------- Actions ---------------- */

    public void tapSignup() { click("signup_button");}

    public void tapLogin() {
        click("login_button");
    }

    /* ---------------- State checks ---------------- */

    public boolean isSignupButtonVisible() {
        try {
            return isDisplayed("signup_button");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginButtonDisplayed() {
        try {
            return isDisplayed("login_button");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginButtonNotVisible() {
        return !isLoginButtonDisplayed();
    }

    public String getErrorMessage() {
        try {
            return getText("error_message");
        } catch (Exception e) {
            return null;
        }
    }

}