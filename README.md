AMSTERDAM PLATFORM CRERATION TASK
----------
######  ``Selim Camur``   ``April 19,2022``
<br/>



##### _TASK : 3 Test Cases_

The task includes 4 different API calls(CRUD). Normally it could be achieved<br/>
with the minimum line of code. However it is designed to be dynamic using the<br/> 
perks of the ``Java``, ``RestAssured``, ``Cucumber``, ``JUnit`` and ``Gherkin``. In terms of data<br/> 
structures, ``POJO`` classes are used along with ``Java Collections`` just to make use of<br/>
alternatives.<br/><br/> 
So, just by mixing in ``parameterized methods`` between each other and creating<br/> 
 a new scenario without a single line of code is possible. Tags are also added.<br/> 
 They can be used to run specific test scenarios. Assertions are mostly used<br/>
from the JUnit library. ``HamcrestMatchers`` library is
also used.<br/><br/>
Credentials and baseURI data are in a properties file and reached by a custom<br/>
``ConfigurationReader`` class.<br/><br/>
For reporting, ``cucumber-reports`` is used but built-in ``Cucumber Reports``<br/>
service is also available.<br/><br/>


##### _RUNNING THE TASK_
* Clone the repository
* Build it using ``maven verify``
* _html test report_ is available only through `mvn verify` command.<br/>
* Report is under ``/target/cucumber-html-reports``<br/><br/>

##### _Feature File Preview_
```gherkin
 # method for comparing body and payload takes parameters: GET,PUT and POST.
 # verifying status code is dynamic. it allows any number as parameter.
 # request and update bodies can be edited.
 
    @active
    Feature: Booker Crud Operations
    
      @post
      Scenario: Create and verify a new booking(POST call)
        When user posts a valid data to create new booking
          | firstname       | John         |
          | lastname        | Doe          |
          | totalprice      | 120          |
          | depositpaid     | true         |
          | checkin         | 2021-04-04   |
          | checkout        | 2021-05-04   |
          | additionalneeds | extra pillow |
        Then response status code is 200
        And request and response bodies should be equal for "POST" call
        And  response body "bookingid" field is not null
    
    
      @put
      Scenario: Update and verify a new booking(PUT call)
        Given user posts a valid data to create new booking
          | firstname       | John         |
          | lastname        | Doe          |
          | totalprice      | 120          |
          | depositpaid     | true         |
          | checkin         | 2021-04-04   |
          | checkout        | 2021-05-04   |
          | additionalneeds | extra pillow |
        When user updates the data above and makes a put request
          | firstname       | John          |
          | lastname        | AChangeIsMade |
          | totalprice      | 120           |
          | depositpaid     | true          |
          | checkin         | 2021-04-04    |
          | checkout        | 2021-05-04    |
          | additionalneeds | AChangeIsMade |
        Then request and response bodies should be equal for "PUT" call
    
    
      @get
      Scenario: GET a booking by id
        Given user posts a valid data to create new booking
          | firstname       | John         |
          | lastname        | Doe          |
          | totalprice      | 120          |
          | depositpaid     | true         |
          | checkin         | 2021-04-04   |
          | checkout        | 2021-05-04   |
          | additionalneeds | extra pillow |
        And response status code is 200
        When user calls the booking using id
        Then response status code is 200
        And request and response bodies should be equal for "GET" call
    
    
      @delete
      Scenario: DELETE a booking
        Given user posts a valid data to create new booking
          | firstname       | John         |
          | lastname        | Doe          |
          | totalprice      | 120          |
          | depositpaid     | true         |
          | checkin         | 2021-04-04   |
          | checkout        | 2021-05-04   |
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

```          

