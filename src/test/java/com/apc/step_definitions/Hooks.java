package com.apc.step_definitions;

import com.apc.utilities.ConfigurationReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;


public class Hooks {
    

    @Before
    public void setup(){
        RestAssured.baseURI= ConfigurationReader.get("baseURL");

    }

    @After
    public void tearDown(){
        // Possible DB connections will be terminated in tearDown method
    }
}
