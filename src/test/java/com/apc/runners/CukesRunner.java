package com.apc.runners;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(

        plugin ={"json:target/cucumber.json",
                "html:target/default-html-reports.html",
                "rerun:target/rerun.txt"},
        features = "src/test/resources",
        glue = "com/apc/step_definitions",
        dryRun = false,
        tags = "@API_Smoke_Test",
        publish = true

)

public class CukesRunner {
}
