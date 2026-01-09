package com.blukers.automation.steps;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.android.HomePageAndroid;
import com.blukers.automation.testdata.TestDataLoader;
import com.blukers.automation.testdata.model.LoginData;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class HomeSteps {

    private final HomePageAndroid homePage = new HomePageAndroid();

    // ---------------- Actions ----------------

    @When("I tap on login button")
    public void iTapOnLoginButton() {
        homePage.tapLogin();
    }

    @When("I login with {string} credentials")
    public void iLoginWithCredentials(String userType) {

        LoginData data =
                TestDataLoader.getTyped(
                        Platform.ANDROID,
                        "login",
                        userType,
                        LoginData.class
                );

        String username = data.getUsername();
        String password = data.getPassword();

        // UI interaction will be added later
        // homePage.login(username, password);
    }

    // ---------------- Assertions ----------------

    @Then("login button should be visible")
    public void loginButtonShouldBeVisible() {
        Assert.assertTrue(
                homePage.isLoginButtonDisplayed(),
                "Login button is not visible on Home screen"
        );
    }

    @Then("login button should not be visible")
    public void loginButtonShouldNotBeVisible() {
        Assert.assertTrue(
                homePage.isLoginButtonNotVisible(),
                "Login button is visible but should not be"
        );
    }

    @Then("error message {string} should be shown")
    public void errorMessageShouldBeShown(String expectedMessage) {
        Assert.assertEquals(
                homePage.getErrorMessage(),
                expectedMessage,
                "Error message does not match"
        );
    }
}