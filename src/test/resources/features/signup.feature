
@happypath @regression
Feature: New User Registration
  As a new user
  I want to sign up using email
  So that I can create my account and proceed to profile setup

  Background:
    Given I am on the home screen for signup

  @email @signup
  Scenario: Successful signup with email
    When I tap on sign up button
    And I tap on email option
    And I enter email "mmh1bd+130@gmail.com" and password "BlukerTesting@2026"
    And I submit the signup form
    Then I should be navigated to profile setup page


  @gmail
  Scenario: Successful signup with google
    When I choose to sign up with Google
    And I select existing Google account from the system picker
    Then I should be navigated to profile setup page
