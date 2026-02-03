package com.blukers.automation.steps;

import com.blukers.automation.pages.android.HomePageAndroid;
import com.blukers.automation.pages.android.LoginPageAndroid;
import com.blukers.automation.pages.android.JobSearchLandingPageAndroid;
//import com.blukers.automation.steps.CommonSteps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;


public class LoginSteps {
    private final LoginPageAndroid loginPage;
    private final HomePageAndroid homePageAndroid;
    private final JobSearchLandingPageAndroid exploreJobs;
//    private final CommonSteps commonSteps;

    public LoginSteps(){
        this.loginPage= new LoginPageAndroid();
        this.homePageAndroid=new HomePageAndroid();
        this.exploreJobs = new JobSearchLandingPageAndroid();
//        this.commonSteps= new CommonSteps();
    }

    @Given("I am on the home screen for login")
    public void iAmOnTheHomeScreenForLogin(){
        Assert.assertTrue(
                homePageAndroid.isLoginButtonDisplayed(),
                "Login Option is not available"
        );
    }

    @When("I tap on homepage login button")
    public void iTapOnHomePageLoginButton(){
        homePageAndroid.tapLogin();
        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "It is not redirected to login screen"
        );
    }

    @When("I enter login email {string}")
    public void iEnterLoginEmail(String email) throws InterruptedException {
        loginPage.enterEmail(email);
        Thread.sleep(10000);
    }

    @When("I enter password {string}")
    public void iEnterLoginPassword(String password) throws InterruptedException {
        loginPage.adbType("passwordInput", password);
        //loginPage.enterPassword(password);
        Thread.sleep(10000);
        Assert.assertTrue(
                loginPage.isLoginButtonEnabled(),
                "Login Button is not enabled. Please check your input - email and password"
        );
    }

    @When("I tap on login button")
    public void iTapOnLoginButton(){
        loginPage.tapOnLogInButton();
    }

    @Then ("I should be navigated to job search page")
    public void iShouldBeNavigatedToJobSearchPage(){
        System.out.println("Checking for Job Search Page visibility...");
        Assert.assertTrue(
                exploreJobs.isJobSearchPageVisibile(),
                "Timed out waiting for Job Search page. Current screen might still be loading."
        );
    }
}
