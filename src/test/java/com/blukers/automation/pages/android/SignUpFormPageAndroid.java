package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

public class SignUpFormPageAndroid extends BasePage {

    public SignUpFormPageAndroid() {
        super("SignUpForm", Platform.ANDROID);
    }

    public void waitForFormVisible() {
        waitForVisible("emailInput");
        waitForVisible("passwordInput");
        waitForVisible("signUpButton");
    }

    public void enterEmail(String email) {
        waitForVisible("emailInput");
        type("emailInput", email);
    }

    public void enterPassword(String password) {
        waitForVisible("passwordInput");
        type("passwordInput", password);
    }

    public void submitSignup() {
        click("signUpButton");
    }
}