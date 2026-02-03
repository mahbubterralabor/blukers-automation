package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;
import com.blukers.automation.steps.LoginSteps;


public class LoginPageAndroid extends BasePage{

    public LoginPageAndroid(){
        super("LoginPage",Platform.ANDROID);
    }

    public boolean isLoginScreenVisible(){
        return isDisplayed("emailInput");
    }

    public void enterEmail(String email){
        type("emailInput",email);
    }

    public void enterPassword(String password){
        type("passwordInput",password);
    }

    public boolean isLoginButtonEnabled(){
        return isEnabled("loginButton");
    }

    public void tapOnLogInButton(){
        click("loginButton");
    }





}
