package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

public class HomePageAndroid extends BasePage {

    private static final String PAGE_NAME = "HomePage";

    public HomePageAndroid() {
        super(PAGE_NAME, Platform.ANDROID);
    }

    // -------- Actions --------

    public void tapLogin() {
        click("loginButton");
    }

    // -------- State --------

    public boolean isLoginButtonDisplayed() {
        return isDisplayed("loginButton");
    }

    public boolean isLoginButtonNotVisible() {
        return isNotDisplayed("loginButton");
    }

    public String getErrorMessage() {
        return getText("errorMessage");
    }
}