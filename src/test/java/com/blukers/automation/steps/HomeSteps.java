package com.blukers.automation.steps;

import com.blukers.automation.pages.android.HomePageAndroid;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class HomeSteps {

    private final HomePageAndroid homePage = new HomePageAndroid();

    @When("I tap on login button")
    public void iTapOnLoginButton() {
        homePage.tapLogin();
    }

    @Then("login button should be visible")
    public void loginButtonShouldBeVisible() {
        Assert.assertTrue(
                homePage.isLoginButtonDisplayed(),
                "Login button is not visible on Home screen"
        );
    }
}
