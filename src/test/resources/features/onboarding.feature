@happypath @regression
  Feature: New User Registration and Onboarding
    As a new user
    I want to signup using email and complete my onboarding
    So that I can search jobs

    Background:
      Given I am on the home screen for signup

    @email @onboarding
    Scenario: New User Registration and Onboarding with email
      Given I have completed the signup with email "mmh1bd+141@gmail.com" and password "BlukerTesting@2026"

      When I start onboarding from profile setup
      And I complete personal details selecting gender "Male", Date of Birth "2000-Februay-21", Enter Phonenumber "641-819-1344", First "Luthar", LastName "Kind"
      And I complete city details
      And I complete language selection
      And I complete job roles selection
      And I complete shift availability selection
      And I complete personal note
      And I accept terms and finish onboarding
      Then I should be navigated to job search page
