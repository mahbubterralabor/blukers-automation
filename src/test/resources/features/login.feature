Feature: Login
  @login @happy
    Scenario: Successful login with valid credentials
    Given I am on the login screen
    When I enter login email "mmh1bd+112@gmail.com"
    And I enter password "BlukerTesting@2026"
    And I tap on login button
    Then I should be navigated to job search page