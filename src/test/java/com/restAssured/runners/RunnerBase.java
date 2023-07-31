package com.restAssured.runners;

import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.*;


public class RunnerBase {
	private static final ThreadLocal<TestNGCucumberRunner> testNGCucumberRunner = new ThreadLocal<>();
	public static TestNGCucumberRunner getRunner() {
		return testNGCucumberRunner.get();
	}

	private static void setRunner(TestNGCucumberRunner testNGCucumberRunner1) {
		testNGCucumberRunner.set(testNGCucumberRunner1);
	}
	@BeforeClass(alwaysRun = true)
	public void setUpClass() throws Exception {

		setRunner(new TestNGCucumberRunner(this.getClass()));
	}

	@Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
	public void scenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) throws Throwable {
		getRunner().runScenario(pickle.getPickle());
	}

	@DataProvider
	public static Object[][] scenarios() {
		return getRunner().provideScenarios();
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() {
		if (testNGCucumberRunner != null) {
			getRunner().finish();
		}
	}
}
