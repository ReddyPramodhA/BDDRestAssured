package com.restAssured.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;

public class MakePostStepDefinition {
	TestData td = new TestData();
	String url = td.getTestData("Post", "BaseUrl");
	String name = td.getTestData("Post", "username");
	String pass = td.getTestData("Post", "password");
	private RequestSpecification request;
    private Response response;
	
	@Given("^the user is authenticated$")
	public void theUserIsAuthenticated() {
		request = given().auth().preemptive().basic(name, pass).baseUri(url);
	}
	
	@When("^the user makes a POST request to create a new post with valid data$")
	public void theUserMakesAPostRequestToCreateANewPostWithValidData() {
		String title = td.getTestData("Post", "Title");
		String body = td.getTestData("Post", "PostData");
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
	@Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }
	
	@And("^the post ID should be a positive integer$")
    public void verifyPostIdIsPositiveInteger() {
		String data = response.getBody().toString();
		String regex = "PostID: (\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);        
        String id = matcher.group(1);
        int i = Integer.parseInt(id);
        Assert.assertTrue(i>0, "Post request didn't run properly");
    }
	
	@When("^the user makes a POST request to create a new post without a title$")
	public void theUserMakesAPostRequestToCreateANewPostWithoutATitle() {
		String title = "";
		String body = td.getTestData("Post", "PostData");
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
	@And("^the response should contain an error message indicating the missing title field$")
	public void theRepsonseShouldContainAnErrorMessageIndicatingTheMissingTitleField() {
		String errorMessage = td.getTestData("Post", "TitleMissingErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
	}
	
	@When("^the user makes a POST request to create a new post without a body$")
	public void theUserMakesAPostRequestToCreateANewPostWithoutABody() {
		String title = td.getTestData("Post", "Title");
		String body = "";
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
	@And("^the response should contain an error message indicating the missing body field$")
	public void theRepsonseShouldContainAnErrorMessageIndicatingTheMissingBodyField() {
		String errorMessage = td.getTestData("Post", "BodyMissingErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
	}
	
	@When("^the user makes a POST request to create a new post with an empty title and body$")
	public void theUserMakesAPostRequestToCreateANewPostWithAnEmptyTitleAndBody() {
		String title = "";
		String body = "";
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
	@And("^the response should contain error messages indicating the missing title and body fields$")
	public void theRepsonseShouldContainAnErrorMessageIndicatingTheMissingTitleAndBodyFields() {
		String errorMessage = td.getTestData("Post", "BothMissingErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
	}
	
	@When("^the user makes a POST request to create a new post with very long title and body$")
	public void theUserMakesAPostRequestToCreateANewPostWithVerLognTitleAndBody() {
		String title = td.getTestData("Post", "LongTitle");
		String body = td.getTestData("Post", "LongPostData");
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
	@And("^the response should contain error messages indicating the title and body length constraints$")
	public void theRepsonseShouldContainAnErrorMessageIndicatingTheTitleAndBodyLengthConstraints() {
		String errorMessage = td.getTestData("Post", "LengthErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
	}
	
	@Given("^the user is not authenticated$")
	public void theUserIsNotAuthenticated() {
		request = given().baseUri(url);
	}
	
	@When("^an unauthorized user makes a POST request to create a new post$")
	public void anUnauthorizedUserMakesAPostRequestToCreateNewPost() {
		String title = td.getTestData("Post", "Title");
		String body = td.getTestData("Post", "PostData");
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
	@Then("^the response should contain an error message indicating the user needs to authenticate$")
	public void theResponseShouldContainAnErrorMessageIndicatingTheUserNeedsToAuthenticate() {
		String errorMessage = td.getTestData("Post", "AuthenticateErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
	}
	
	@When("^the user makes a POST request to create a new post with additional unauthorized data$")
	public void theUserMakesAPostRequestToCreateNewPostWithAdditionalUnauthorizedData() {
		String title = td.getTestData("Post", "Title");
		String body = td.getTestData("Post", "PostData");
		String error = td.getTestData("Post", "AuthenticateErrorMessage");
		String test = td.getTestData("Post", "AuthenticateErrorMessage");
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\",\"error\":\""+error+"\",\"test\":\""+test+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
	@When("^the user makes a POST request to create a new post with special characters in the title and body$")
	public void creatingPostWithSpecialCharactersInTitleAndBody() {
		String title = td.getTestData("Post", "TitleWithSpecialCharacters");
		String body = td.getTestData("Post", "DataWithSpecialCharacters");
		String requestBody = "{\"title\":\""+title+"\",\"body\":\""+body+"\"}";
	    response = request.body(requestBody).post("/posts");
	}
	
}
