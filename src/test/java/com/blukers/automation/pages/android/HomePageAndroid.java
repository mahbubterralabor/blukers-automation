package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

public class HomePageAndroid extends BasePage {

    private static final String PAGE_NAME = "HomePage";

    public HomePageAndroid() {
        super(PAGE_NAME, Platform.ANDROID);
    }

    public void tapLogin() {
        click("loginButton");
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed("loginButton");
    }
}
