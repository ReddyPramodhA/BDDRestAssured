package com.restAssured.stepdef;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.testng.Assert;

public class CommentsStepDefinitions {

    TestData td = new TestData();
	private RequestSpecification request;
    private Response response;
    private int postId;
    private int commentId;

    @Given("^there is an existing post with ID {int}$")
    public void existingPostWithId(int postId) {
        this.postId = postId;
    }

    @When("^the user comments on the post$")
    public void userCommentsOnPost() {
    	String commentMessage = td.getTestData("Comment", "CommentData");
        String requestBody = String.format("{\"postId\": %d, \"body\": \"%s\"}", postId, commentMessage);
        response = request.body(requestBody).post("/comments");
    }

    @Then("^the comment should be successfully added with a unique ID$")
    public void verifyCommentIsAddedWithUniqueId() {
        response.then().statusCode(201);
        commentId = response.jsonPath().getInt("id");
        Assert.assertNotNull(commentId);
    }
    
    @When("^the user comments on the post with an empty message$")
    public void theUserCommentsOnThePostWithAnEmptyMessage() {
    	String commentMessage = "";
        String requestBody = String.format("{\"postId\": %d, \"body\": \"%s\"}", postId, commentMessage);
        response = request.body(requestBody).post("/comments");
    }
    
    @And("^the response should contain an error message indicating the missing comment message field$")
    public void theResponseShouldContainAnErrorMessageIndicatingTheMissingCommentMessageField() {
    	String errorMessage = td.getTestData("Comment", "CommentMissingErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
    }
    
    @When("^the user comments on the non-existing post$")
    public void theUserCommentsOnTheNonExistingPost() {
    	String commentMessage = td.getTestData("Comment", "CommentData");
        String requestBody = String.format("{\"postId\": %d, \"body\": \"%s\"}", postId, commentMessage);
        response = request.body(requestBody).post("/comments");
    }
    
    @And("^the response should contain an error message indicating the post was not found$")
    public void theResponseShouldContainAnErrorMessageIndicatingThePostWasNotFound() {
    	String errorMessage = td.getTestData("Comment", "PostNotFoundErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
    }
    
    @When("^the user comments on the post with a very long message$")
    public void theUserCommentsOnThePostWithAVeryLongMessage() {
    	String commentMessage = td.getTestData("Comment", "LongComment");
        String requestBody = String.format("{\"postId\": %d, \"body\": \"%s\"}", postId, commentMessage);
        response = request.body(requestBody).post("/comments");
    }
    
    @And("^the response should contain an error message indicating the message length constraint$")
    public void theResponseShouldContainAnErrorMessageIndicatingTheMessageLenghtConstraint() {
    	String errorMessage = td.getTestData("Comment", "CommentLengthErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
    }
    
    @When("^an unauthorized user tries to comment on the post$")
    public void anUnauthorizedUserTriesToCommentOnThePost() {
    	String commentMessage = td.getTestData("Comment", "CommentData");
        String requestBody = String.format("{\"postId\": %d, \"body\": \"%s\"}", postId, commentMessage);
        response = request.body(requestBody).post("/comments");
    }
    
    @And("^the response should contain an error message indicating the user needs to authenticate$")
    public void theResponseShouldContainAnErrorMessageIndicatingTheUserNeedsToAuthenticate() {
    	String errorMessage = td.getTestData("Post", "AuthenticateErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
    }
    
    @When("^the user comments on the post with a message containing special characters$")
    public void theUserCommentsOnThePostWithMessageContainingSpecialCharacters() {
    	String commentMessage = td.getTestData("Comment", "CommentDataWithSpecialCharacters");
        String requestBody = String.format("{\"postId\": %d, \"body\": \"%s\"}", postId, commentMessage);
        response = request.body(requestBody).post("/comments");
    }
    
    @When("^the user comments on the post with the following message containing HTML tags:$")
    public void userCommentsOnPostWithHtmlTags(String commentMessage) {
        String requestBody = String.format("{\"postId\": %d, \"body\": \"%s\"}", postId, commentMessage);
        response = request.body(requestBody).post("/comments");
    }
    
    @And("^the comment should be successfully added with the HTML tags preserved and not interpreted as markup$")
    public void verifyCommentWithHtmlTagsPreserved() {
        response.then().statusCode(201);
        String commentBody = response.jsonPath().getString("body");
        assertNotNull(commentBody);
        // Assuming that the commentBody is stored as a string representation of JSON
        // If the comment is returned as a JSON object, you may need to modify this check accordingly
        Assert.assertTrue(commentBody.contains("<strong>This is a <em>test</em> comment.</strong>"));
    }
}