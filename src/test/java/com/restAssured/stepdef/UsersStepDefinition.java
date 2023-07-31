package com.restAssured.stepdef;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

public class UsersStepDefinition {

    private RequestSpecification request;
    private Response response;
    private int pageSize;
    private int totalUsers;
    TestData td = new TestData();


    @When("^the user makes a GET request to fetch the list of users$")
    public void userFetchesListOfUsers() {
        response = request.get("/users");
    }

    @Then("^the response status code should be {int}$")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("^the response should contain a non-empty list of users$")
    public void verifyResponseContainsNonEmptyListOfUsers() {
        List<Map<String, Object>> userList = response.jsonPath().getList(".");
        Assert.assertNotNull(userList);
        Assert.assertFalse(userList.isEmpty());
    }

    @Then("^each user in the list should have the following attributes:$")
    public void verifyUserAttributes(List<Map<String, String>> userAttributes) {
        List<Map<String, Object>> userList = response.jsonPath().getList(".");
        Assert.assertNotNull(userList);

        for (Map<String, Object> user : userList) {
            for (Map.Entry<String, String> entry : userAttributes.get(0).entrySet()) {
                String attributeName = entry.getKey();
                String attributeType = entry.getValue();

                Assert.assertTrue(user.containsKey(attributeName));

                switch (attributeType) {
                    case "int":
                    	Assert.assertTrue(user.get(attributeName) instanceof Integer);
                        break;
                    case "string":
                    	Assert.assertTrue(user.get(attributeName) instanceof String);
                        break;
                    case "string (valid email)":
                    	Assert.assertTrue(user.get(attributeName) instanceof String);
                        String email = (String) user.get(attributeName);
                        Assert.assertTrue(isValidEmail(email));
                        break;
                    default:
                        System.out.println("Didn't find any case to run");
                        break;
                }
            }
        }
    }

    
    private boolean isValidEmail(String email) {
        return email.contains("@");
    }
    
    @When("^an unauthorized user makes a GET request to fetch the list of users$")
    public void anUnauthorizedUserMakesGetRequestToFetchTheListOfUsers() {
    	response = request.get("/users");
    }
    
    @When("^the user makes a GET request to fetch the list of users with page number {int} and page size {int}$")
    public void userFetchesListOfUsersWithPagination(int pageNumber, int pageSize) {
        this.pageSize = pageSize;
        response = request.queryParam("_page", pageNumber)
                .queryParam("_limit", pageSize)
                .get("/users");
        totalUsers = Integer.parseInt(response.header("X-Total-Count"));
    }
    
    @Then("^the number of users in the response should be equal to the specified page size$")
    public void verifyNumberOfUsersInResponse() {
        List<Map<String, Object>> userList = response.jsonPath().getList(".");
        Assert.assertNotNull(userList);
        Assert.assertEquals(pageSize, userList.size());
    }
    
    @Then("^the response headers should contain the total count of users$")
    public void verifyResponseHeadersContainTotalCountOfUsers() {
    	Assert.assertNotNull(totalUsers);
    }
    
    @When("^the user makes a GET request to fetch the list of users with invalid page number {int}$")
    public void userFetchesWithInvalidPageNumber(int pageNumber) {
    	response = request.queryParam("_page", pageNumber).queryParam("_limit", pageSize).get("/users");
    }
    
    @And("^the response should contain an error message indicating that the page is not found$")
    public void verifyInvalidPageNumberErrorMessage() {
    	String errorMessage = td.getTestData("Users", "PageNumberErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
    }
    
    @When("^the user makes a GET request to fetch the list of users with page size {int}$")
    public void userFetchesWithInvalidPageSize(int pageSize) {
    	response = request.queryParam("_limit", pageSize).get("/users");
    }
    
    @And("^the response should contain an error message indicating that the page size is not valid$")
    public void verifyErrorMessageIndicationInvalidPageSize() {
    	String errorMessage = td.getTestData("Users", "PageNumberErrorMessage");
		Assert.assertEquals(errorMessage, response.jsonPath().get("message"));
    }
}

