package com.blukers.automation.steps;

import com.blukers.automation.pages.android.HomePageAndroid;
import com.blukers.automation.pages.android.SignUpFormPageAndroid;
import com.blukers.automation.pages.android.SignUpOptionsPageAndroid;
import com.blukers.automation.pages.android.ProfileSetupPageAndroid;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class SignupSteps {

    private final HomePageAndroid homePage = new HomePageAndroid();
    private final SignUpOptionsPageAndroid optionsPage = new SignUpOptionsPageAndroid();
    private final SignUpFormPageAndroid signupFormPage = new SignUpFormPageAndroid();
    private final ProfileSetupPageAndroid profileSetupPage = new ProfileSetupPageAndroid();

    @Given("I am on the home screen")
    public void iAmOnTheHomeScreen() {
        Assert.assertTrue(
                homePage.isSignupButtonVisible(),
                "Signup button should be visible on Home screen"
        );
    }

    @When("I choose to sign up with email")
    public void iChooseToSignUpWithEmail() {
        homePage.tapSignup();
        optionsPage.selectEmailOption();
    }

    @When("I enter email {string} and password {string}")
    public void iEnterEmailAndPassword(String email, String password) {
        signupFormPage.enterEmail(email);
        signupFormPage.enterPassword(password);
    }

    @When("I submit the signup form")
    public void iSubmitTheSignupForm() {
        signupFormPage.submitSignup();
    }

    @Then("I should be navigated to profile setup page")
    public void iShouldBeNavigatedToProfileSetupPage() {

        // REAL assertion – no temporary pass
        Assert.assertTrue(
                profileSetupPage.isGetStartedButtonVisible(),
                "Profile Setup page was not displayed after signup"
        );
    }
}