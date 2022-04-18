@active
Feature: Booker Crud Operations

  @post
  Scenario: Create and verify a new booking(POST call)
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


  @put
  Scenario: Update and verify an existing booking(PUT call)
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


  @get
  Scenario: GET a booking by id
    Given user posts a valid data to create new booking
      | firstname       | John         |
      | lastname        | Doe          |
      | totalprice      | 120          |
      | depositpaid     | true         |
      | checkin         | 2022-04-19   |
      | checkout        | 2022-05-04   |
      | additionalneeds | extra pillow |
    And response status code is 200
    When user calls the booking using id
    Then response status code is 200
    And request and response bodies should be equal for "GET" call


  @delete
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


  @auth
  Scenario: Make a POST call with valid credentials to get a AUTH TOKEN
    When user makes a post call with a valid credential
    Then user should get an auth token in the body

