Feature: Request Flights

  Scenario: Single result from two providers
    Given A user requests best flight prices
    When The user sends a correct request
    Then The system will return expected result