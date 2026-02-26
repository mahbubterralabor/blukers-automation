Feature: Login

  As a registered user
  I want to login to Blukers app using valid credentials
  So that I can redirect to Job Search Landing page
  For the invalid credentials I won't be able to login

  Background:
    Given I am on the home screen for login

  @login @login_valid @happy @regression @data_login_valid
  Scenario: Successful login with valid credentials
    When I tap on homepage login button
    And I enter login credentials
    And I tap on login button
    Then I should be navigated to job search page

  @login @login_invalid @negative @regression @data_login_invalid_password
  Scenario: Login try with incorrect password
    When I tap on homepage login button
    And I enter login credentials
    And I tap on login button
    Then I should remain on the login page