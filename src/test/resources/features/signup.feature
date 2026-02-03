Feature: New User Registration

  As a new user
  I want to sign up using email
  So that I can create my account and proceed to profile setup

  @signup @happy
  Scenario: Successful signup with email
    Given I am on the home screen for signup
    When I choose to sign up with email
    And I enter email "mmh1bd+106@gmail.com" and password "BluTest@2026"
    And I submit the signup form
    Then I should be navigated to profile setup page