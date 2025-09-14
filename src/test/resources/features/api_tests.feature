Feature: Back-End Automation Tests

  Scenario: Validate API Schema
    Given I send a GET request to "https://restcountries.com/v3.1/all?fields=name"
    Then the response should match the expected schema

  Scenario: Confirm Number of Countries
    Given I send a GET request to "https://restcountries.com/v3.1/all?fields=name"
    Then the total number of countries should be 195

  Scenario: Validate South African Sign Language
    Given I send a GET request to "https://restcountries.com/v3.1/all?fields=name"
    Then South Africa should include "South African Sign Language" in its official languages