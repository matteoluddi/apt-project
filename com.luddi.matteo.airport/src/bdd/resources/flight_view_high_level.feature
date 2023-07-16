Feature: Flight View High Level
  Specifications of the behavior of the Flight View

  Background: 
    Given The database contains a few flights
    And The Flight View is shown

  Scenario: Add a new flight
    Given The user provides flight data in the text fields
    When The user clicks the "Add" button
    Then The list contains the new flight

  Scenario: Add a new flight with an existing id
    Given The user provides flight data in the text fields, specifying an existing id
    When The user clicks the "Add" button
    Then An error is shown containing the id of the existing flight

  Scenario: Delete a flight
    Given The user selects a flight from the list
    When The user clicks the "Delete Selected" button
    Then The flight is removed from the list

  Scenario: Delete a not existing flight
    Given The user selects a flight from the list
    But The flight is in the meantime removed from the database
    When The user clicks the "Delete Selected" button
    Then An error is shown containing the id of the selected flight
    And The flight is removed from the list

  Scenario: Change the passengers number 
    Given The user provides a passengers number in the passengers number field
    And The user selects a flight from the list
    When The user clicks the "Change" button
    Then The list contains the flight with the new passengers number value

  Scenario: Change the passengers number on a not existing flight
    Given The user provides a passengers number in the passengers number field
    And The user selects a flight from the list
    But The flight is in the meantime removed from the database
    When The user clicks the "Change" button
    Then An error is shown containing the id of the selected flight
    And The flight is removed from the list
