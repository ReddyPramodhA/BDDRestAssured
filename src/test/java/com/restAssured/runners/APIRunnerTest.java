package com.restAssured.runners;

import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
        ,features = {"src/test/resources"}
        ,glue = {"com.restAssured.stepdef"}
        ,dryRun=false
        ,monochrome=true
        ,tags = "@test"
        )
public class APIRunnerTest extends RunnerBase {
}