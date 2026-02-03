Feature: Login
  @login @happy
    Scenario: Successful login with valid credentials
    Given I am on the home screen for login
    When I tap on homepage login button
    And I enter login email "mmh1bd+112@gmail.com"
    And I enter password "BlukerTesting@2026"
    And I tap on login button
    Then I should be navigated to job search page