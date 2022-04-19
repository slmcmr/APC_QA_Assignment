@API_Smoke_Test
Feature: Create, Update and Delete a booking with API

  @BK-001
  Scenario: User can create a new booking
    When user posts a valid data to create new booking
      | firstname       | John         |
      | lastname        | Doe          |
      | totalprice      | 120          |
      | depositpaid     | true         |
      | checkin         | 2022-04-19   |
      | checkout        | 2022-05-03   |
      | additionalneeds | extra pillow |
    Then response status code is 200
    And request and response bodies should be equal for "POST" call
    And  response body "bookingid" field is not null

  @BK-002
  Scenario: User can update an existing booking
    Given user posts a valid data to create new booking
      | firstname       | John         |
      | lastname        | Doe          |
      | totalprice      | 120          |
      | depositpaid     | true         |
      | checkin         | 2022-04-19   |
      | checkout        | 2022-05-03   |
      | additionalneeds | extra pillow |
    When user updates the data above and makes a put request
      | firstname       | John          |
      | lastname        | AChangeIsMade |
      | totalprice      | 100           |
      | depositpaid     | true          |
      | checkin         | 2022-04-19    |
      | checkout        | 2022-05-04    |
      | additionalneeds | AChangeIsMade |
    Then request and response bodies should be equal for "PUT" call

  @BK-003
  Scenario: DELETE a booking by id
    Given user posts a valid data to create new booking
      | firstname       | John         |
      | lastname        | Doe          |
      | totalprice      | 120          |
      | depositpaid     | true         |
      | checkin         | 2022-04-19   |
      | checkout        | 2022-05-04   |
      | additionalneeds | extra pillow |
    And response status code is 200
    When user makes delete call
    Then response status code is 201
    When user calls the booking using id
    Then response status code is 404
