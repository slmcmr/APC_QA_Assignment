package com.apc.step_definitions;

import com.apc.pojo_classes.booking_request_body.BookingRequestBody;
import com.apc.pojo_classes.booking_request_body.Bookingdates;
import com.apc.pojo_classes.booking_post_response.BookingResponse;
import com.apc.utilities.ConfigurationReader;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BookerCrudOpStepDefs {

    /**
     * This task could easily be accomplished using only Map data structure.
     * Pojo classes are used only for the sake of utilizing alternatives.
     **/

    Response response;
    Response authResponse;
    Response putResponse;
    Map<String,Object> putBody;

    // Initializing pojo objects
    Bookingdates dates = new Bookingdates();
    BookingRequestBody requestBody = new BookingRequestBody();
    BookingResponse responseBody = new BookingResponse();

    @When("user posts a valid data to create new booking")
     public void user_posts_a_valid_data_to_create_new_booking(Map<String,String> postData) {
        // Initializing pojo objects using data from feature file
        requestBody.setFirstname(postData.get("firstname"));
        requestBody.setLastname(postData.get("lastname"));
        requestBody.setTotalprice(Integer.parseInt(postData.get("totalprice")));
        requestBody.setDepositpaid(Boolean.parseBoolean(postData.get("depositpaid")));
        dates.setCheckin(postData.get("checkin"));
        dates.setCheckout(postData.get("checkout"));
        requestBody.setBookingdates(dates);
        requestBody.setAdditionalneeds(postData.get("additionalneeds"));

        // This is where the execution takes place
        response = RestAssured.given().contentType(ContentType.JSON).accept("application/json")
                .and().body(requestBody)
                .when().post("/booking");

        // responseBody pojo can be used as an alternative to RestAssured Response class
        responseBody = response.as(BookingResponse.class);

        // An arbitrary assertion just to verify we got a response
        response.then().assertThat().header("Date", notNullValue());
    }

    @Then("response status code is {int}")
    public void response_status_code_is(int statusCode) {
        assertEquals("Verify status code equals to the given parameter", statusCode, response.statusCode());
    }

    @Then("response data equals to data in the request body")
    public void response_data_equals_to_data_in_the_request_body() {
        assertEquals(requestBody.getFirstname(),response.path("booking.firstname"));
        assertEquals(requestBody.getLastname(),response.path("booking.lastname"));
        assertEquals(requestBody.getDepositpaid(),response.path("booking.depositpaid"));
        assertEquals(requestBody.getTotalprice(),response.path("booking.totalprice"));
        assertEquals(requestBody.getAdditionalneeds(),response.path("booking.additionalneeds"));
        assertEquals(requestBody.getBookingdates().getCheckin(),response.path("booking.bookingdates.checkin"));
        assertEquals(requestBody.getBookingdates().getCheckout(),response.path("booking.bookingdates.checkout"));
         }

    @Then("response body {string} field is not null")
    public void response_body_field_is_not_null(String responseField) {
        assertNotNull("verify responseField is not null", response.path(responseField));
    }

    @When("user updates the data above and makes a put request")
    public void user_updates_the_data_above_and_makes_a_put_request(Map<String,String> updatedData){
        // An auth token is required
        user_makes_a_post_call_with_a_valid_credential();
        user_should_get_an_auth_token_in_the_body();

        // Put request needs a body
        putBody = new HashMap<>();
        Map<String,String> bookingDates = new HashMap<>();
        putBody.put("firstname",updatedData.get("firstname"));
        putBody.put("lastname",updatedData.get("lastname"));
        putBody.put("totalprice",Integer.parseInt(updatedData.get("totalprice")));
        putBody.put("depositpaid",Boolean.parseBoolean(updatedData.get("depositpaid")));
        bookingDates.put("checkin",updatedData.get("checkin"));
        bookingDates.put("checkout",updatedData.get("checkout"));
        putBody.put("bookingdates",bookingDates);
        putBody.put("additionalneeds",updatedData.get("additionalneeds"));

        putResponse=RestAssured.given().contentType(ContentType.JSON).accept("application/json")
                .header("Cookie", "token="+authResponse.path("token"))
                .pathParam("id",response.path("bookingid"))
                .and().body(putBody).and()
                .when().put("booking/{id}}");

        assertEquals("verify the status code",200, putResponse.statusCode());
    }

    @Then("request and response bodies should be equal")
    public void request_and_response_bodies_should_be_equal() {
        assertEquals("",putBody.get("firstname"), putResponse.path("firstname"));
        assertEquals("",putBody.get("lastname"), putResponse.path("lastname"));
        assertEquals("",putBody.get("totalprice"), putResponse.path("totalprice"));
        assertEquals("",putBody.get("depositpaid"), putResponse.path("depositpaid"));
        assertEquals("",putBody.get("bookingdates"), putResponse.path("bookingdates"));
        assertEquals("",putBody.get("additionalneeds"), putResponse.path("additionalneeds"));
    }

    @When("user makes a post call with a valid credential")
    public void user_makes_a_post_call_with_a_valid_credential() {

        // A post call with credentials is needed to get an auth
        Map<String,String> bodyForAuth=new HashMap<>();
        bodyForAuth.put("username", ConfigurationReader.get("username"));
        bodyForAuth.put("password",ConfigurationReader.get("password"));

        authResponse=RestAssured.given().contentType(ContentType.JSON).accept(ContentType.ANY)
                .and().body(bodyForAuth)
                .when().post("/auth");

        assertEquals("verify status code is 200",200,authResponse.statusCode());
    }

    @Then("user should get an auth token in the body")
    public void user_should_get_an_auth_token_in_the_body() {
        assertNotNull("verify the token in the body",authResponse.path("token"));
    }

    @When("user calls the booking using id")
    public void user_calls_the_booking_using_id() {

        response=RestAssured.given().contentType(ContentType.JSON)
                .pathParam("id",responseBody.getBookingid())
                .and().when().get("booking/{id}");
    }

    @Then("request and response bodies should be equal for {string} call")
    public void request_and_response_bodies_should_be_equal_for_call(String callType) throws Exception {

        switch (callType){
            case "GET":
                assertEquals(requestBody.getFirstname(),response.path("firstname"));
                assertEquals(requestBody.getLastname(),response.path("lastname"));
                assertEquals(requestBody.getDepositpaid(),response.path("depositpaid"));
                assertEquals(requestBody.getTotalprice(),response.path("totalprice"));
                assertEquals(requestBody.getAdditionalneeds(),response.path("additionalneeds"));
                assertEquals(requestBody.getBookingdates().getCheckin(),response.path("bookingdates.checkin"));
                assertEquals(requestBody.getBookingdates().getCheckout(),response.path("bookingdates.checkout"));
                break;

            case "POST":
                assertEquals(requestBody.getFirstname(),response.path("booking.firstname"));
                assertEquals(requestBody.getLastname(),response.path("booking.lastname"));
                assertEquals(requestBody.getDepositpaid(),response.path("booking.depositpaid"));
                assertEquals(requestBody.getTotalprice(),response.path("booking.totalprice"));
                assertEquals(requestBody.getAdditionalneeds(),response.path("booking.additionalneeds"));
                assertEquals(requestBody.getBookingdates().getCheckin(),response.path("booking.bookingdates.checkin"));
                assertEquals(requestBody.getBookingdates().getCheckout(),response.path("booking.bookingdates.checkout"));
                break;

            case "PUT":
                assertEquals("",putBody.get("firstname"), putResponse.path("firstname"));
                assertEquals("",putBody.get("lastname"), putResponse.path("lastname"));
                assertEquals("",putBody.get("totalprice"), putResponse.path("totalprice"));
                assertEquals("",putBody.get("depositpaid"), putResponse.path("depositpaid"));
                assertEquals("",putBody.get("bookingdates"), putResponse.path("bookingdates"));
                assertEquals("",putBody.get("additionalneeds"), putResponse.path("additionalneeds"));
                break;
            default:
                throw new Exception("Only \"GET\", \"POST\" and \"PUT\" is allowed, and yes, CASE SENSITIVE!");
        }
    }

    @When("user makes delete call")
    public void user_makes_delete_call() {
        user_makes_a_post_call_with_a_valid_credential();
        user_should_get_an_auth_token_in_the_body();

        response=RestAssured.given().contentType(ContentType.JSON).accept("application/json")
                .header("Cookie", "token="+authResponse.path("token"))
                .pathParam("id",response.path("bookingid"))
                .and().when().delete("booking/{id}}");


    }
}
