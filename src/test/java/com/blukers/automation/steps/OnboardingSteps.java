package com.blukers.automation.steps;

import com.blukers.automation.pages.android.*;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class OnboardingSteps {
    //existing signup pages to be reused for Signup
    private final HomePageAndroid homePageAndroid = new HomePageAndroid();
    private final SignUpOptionsPageAndroid signUpOptionsPageAndroid = new SignUpOptionsPageAndroid();
    private final SignUpFormPageAndroid signUpFormPageAndroid = new SignUpFormPageAndroid();
    private final JobSearchLandingPageAndroid jobSearchLandingPageAndroid = new JobSearchLandingPageAndroid();
    private final ProfileSetupPageAndroid profileSetupPageAndroid = new ProfileSetupPageAndroid();



    // Onboarding Page CLasses
    private final PersonalDetailsAndroid personalDetails = new PersonalDetailsAndroid();
    private final CityDetailsAndroid cityDetails = new CityDetailsAndroid();
    private final LanguageSelectionAndroid languageSelection = new LanguageSelectionAndroid();
    private final ShiftAvailabilityAndroid shiftAvailability = new ShiftAvailabilityAndroid();




    // Signup a new user - used already created page methods
    @Given ("I have completed the signup with email {string} and password {string}")
    public void iHaveCompletedTheSignupWithEmailAndPassword(String email, String password){
        // check Landing Page is shown or not
        Assert.assertTrue(homePageAndroid.isSignupButtonVisible(),"Landing page is not loaded - Sign up button not available");

        // click on Signup Button
        homePageAndroid.tapSignup();
        // click on Email Option for signup
        signUpOptionsPageAndroid.selectEmailOption();

        //make sure signup form page is ready
        signUpFormPageAndroid.waitForFormVisible();

        //enter email and password
        signUpFormPageAndroid.enterEmail(email);
        //enter password
        signUpFormPageAndroid.enterPassword(password);
        // submit
        signUpFormPageAndroid.submitSignup();
        //check it is redirected to SignUpFormPage or not
        Assert.assertTrue(profileSetupPageAndroid.isGetStartedButtonVisible(),"It is not redirected to Profie setup page");
    }

    @When("I start onboarding from profile setup")
    public void iStartOnboardingFromProfileSetup(){
        //profileSetupPageAndroid.isGetStartedButtonVisible();
        profileSetupPageAndroid.tapGetStartedButton();
        Assert.assertTrue(
                personalDetails.isPersonalDetailsDisplayed(),
                "It is not redirected to personal details screen"
        );
    }

    @When("I complete personal details")
    public void iCompletePersonalDetails(){
        //personalDetails.completePersonalDetailsAndContinue();

    }
}
