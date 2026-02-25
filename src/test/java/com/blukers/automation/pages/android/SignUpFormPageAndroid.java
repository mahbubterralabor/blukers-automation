package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage_Backup;

public class SignUpFormPageAndroid extends BasePage_Backup {

    public SignUpFormPageAndroid() {
        super("SignUpFormPage", Platform.ANDROID);
    }

    public void waitForFormVisible() {
        waitForVisible("emailInput");
        waitForVisible("passwordInput");
        waitForVisible("signUpButton");
    }

    public void enterEmail(String email) {
        waitForVisible("emailInput");
        adbType("emailInput", email);
    }

    public void enterPassword(String password) {
        waitForVisible("passwordInput");
        adbType("passwordInput", password);
    }

    public void submitSignup() {
        click("signUpButton");
    }
}