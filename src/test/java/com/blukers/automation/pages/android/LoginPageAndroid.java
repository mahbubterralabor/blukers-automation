package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;
import com.blukers.automation.steps.LoginSteps;


public class LoginPageAndroid extends BasePage{

    public LoginPageAndroid(){
        super("Login",Platform.ANDROID);
    }

    public boolean isLoginScreenVisible(){
        return isDisplayed("emailInput");
    }

}
