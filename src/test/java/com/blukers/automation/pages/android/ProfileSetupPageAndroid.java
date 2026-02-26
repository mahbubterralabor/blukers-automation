package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

public class ProfileSetupPageAndroid extends BasePage {

    public ProfileSetupPageAndroid() {
        super("ProfileSetupPage", Platform.ANDROID);
    }

    public boolean isGetStartedButtonVisible() {
        return isDisplayed("getStartedButton");
    }

    public void tapGetStartedButton(){
        click("getStartedButton");
    }
}