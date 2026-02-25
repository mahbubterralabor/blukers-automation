package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage_Backup;

/**
 * Android page for Signup Options screen
 * (Email / Google / Apple selection).
 */
public class SignUpOptionsPageAndroid extends BasePage_Backup {

    public SignUpOptionsPageAndroid() {
        super("SignUpOptionsPage", Platform.ANDROID);
    }

    public void selectEmailOption() {
        click("email_option");
    }

    public void selectGoogleOption() {
        click("google_option");
    }

    public void selectAppleOption() {
        click("apple_option");
    }
}