package com.blukers.automation.steps;

import com.blukers.automation.pages.android.HomePageAndroid;
import com.blukers.automation.pages.android.JobSearchLandingPageAndroid;
import com.blukers.automation.pages.android.LoginPageAndroid;
import com.blukers.automation.testdata.TestDataContext;
import com.blukers.automation.testdata.TestDataLoader;
import com.blukers.automation.testdata.model.LoginData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final HomePageAndroid homePage = new HomePageAndroid();
    private final LoginPageAndroid loginPage = new LoginPageAndroid();
    private final JobSearchLandingPageAndroid jobSearchPage = new JobSearchLandingPageAndroid();

    private LoginData loginData;

    @Given("I am on the home screen for login")
    public void iAmOnTheHomeScreenForLogin() {

        Assert.assertTrue(
                homePage.isLoginButtonDisplayed(),
                "Login option is not available on Home screen."
        );
    }

    @When("I tap on homepage login button")
    public void iTapOnHomepageLoginButton() {
        homePage.tapLogin();

        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "Home screen did not redirect to Login screen."
        );
    }

    @When("I enter login credentials")
    public void iEnterLoginCredentials() {
        String dataKey = TestDataContext.getDataKey();
        Assert.assertNotNull(dataKey,
                "Missing @data_* tag on Scenario (Hooks could not resolve a dataKey). Example: @data_login_valid"
        );

        // For login feature, file name is "login" => testdata/login.json
        this.loginData = TestDataLoader.load("login", dataKey, LoginData.class);

        Assert.assertNotNull(
                loginData,
                "LoginData was not loaded. Check testdata/login.json and tag key: " + dataKey
        );

        loginPage.enterEmail(loginData.getEmail());
        loginPage.enterPassword(loginData.getPassword());
    }

    @When("I tap on login button")
    public void iTapOnLoginButton() {
        loginPage.tapOnLogInButton();
    }

    @Then("I should be navigated to job search page")
    public void iShouldBeNavigatedToJobSearchPage() {
        Assert.assertTrue(
                jobSearchPage.isJobSearchPageVisibile(),
                "Timed out waiting for Job Search page after login."
        );
    }

    @Then ("I should remain on the login page")
    public void iShouldRemainOnTheLoginPage(){
        Assert.assertTrue(
                loginPage.isLoginButtonEnabled(),
                "App is login with incorrect credentials"
        );
    }
}