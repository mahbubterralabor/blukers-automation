package com.blukers.automation.steps;

import com.blukers.automation.pages.android.LoginPageAndroid;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class LoginSteps {
    private final LoginPageAndroid loginPage;

    public LoginSteps(){
        this.loginPage= new LoginPageAndroid();
    }

    @Given("I am on the login screen")
    public void iAmOnTheLoginScreen(){
        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "Login Screen is not visible"
        );
    }

    @When("I enter login email {string}")
    public void iEnterLoginEmail(String email){
        loginPage.enterEmail(email);
    }

    @When("I enter login password {string}")
    public void iEnterLoginPasswod(String password){
        loginPage.enterPassword(password);
    }

    @Then("the login button should be enabled")
    public void theLoginButtonShouldBeEnabled(){
        Assert.assertTrue(
                loginPage.isLoginButtonEnabled(),
                "Login button is not enabled after entering "
        );
    }
}
