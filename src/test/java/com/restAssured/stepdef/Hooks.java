package com.restAssured.stepdef;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import com.rexel.utils.TestUtils;


import java.io.IOException;

public class Hooks {
	
	TestUtils utils = new TestUtils();

	@Before
	public void initialize(Scenario scenario) throws Exception {
		String testName = scenario.getName();
		utils.log().info(testName);
	}
}