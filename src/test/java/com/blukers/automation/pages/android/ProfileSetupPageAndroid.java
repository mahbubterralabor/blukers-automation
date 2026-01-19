package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

public class ProfileSetupPageAndroid extends BasePage {

    public ProfileSetupPageAndroid() {
        super("ProfileSetup", Platform.ANDROID);
    }

    public boolean isGetStartedButtonVisible() {
        return isDisplayed("getStartedButton");
    }
}