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

    @Given("I am on the home screen for signup")
    public void iAmOnTheHomeScreeForSignUpn() {
        Assert.assertTrue(
                homePage.isSignupButtonVisible(),
                "Signup button isn't visibile"
        );
    }

    @When("I tap on sign up button")
    public void iTapOnSignUpButton() {
        homePage.tapSignup();
    }

    @When("I tap on email option")
    public void iTapOnEmailOption(){
        optionsPage.selectEmailOption();
        signupFormPage.waitForFormVisible();
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
        Assert.assertTrue(
                profileSetupPage.isGetStartedButtonVisible(),
                "Profile Setup page was not displayed after signup"
        );
    }

    /*
    When I choose to sign up with Google
    And I select existing Google account from the system picker
    Then I should be navigated to profile setup page
     */

    @When("When I choose to sign up with Google")
    public void whenIChooseToSignUpWithGoogle(){
        //homePage.tapSignUpGoogle();
    }

}