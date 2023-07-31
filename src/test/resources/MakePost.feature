Feature: Make Post
	Scenario: User makes a new post with valid data
    Given the user is authenticated
    When the user makes a POST request to create a new post with valid data
    Then the response status code should be 201
    And the post ID should be a positive integer
    
	Scenario: User makes a new post without a title
    Given the user is authenticated
    When the user makes a POST request to create a new post without a title
    Then the response status code should be 400
    And the response should contain an error message indicating the missing title field

	Scenario: User makes a new post without a body
    Given the user is authenticated
    When the user makes a POST request to create a new post without a body
    Then the response status code should be 400
    And the response should contain an error message indicating the missing body field

	Scenario: User makes a new post with an empty title and body
    Given the user is authenticated
    When the user makes a POST request to create a new post with an empty title and body
    Then the response status code should be 400
    And the response should contain error messages indicating the missing title and body fields

	Scenario: User makes a new post with excessively long title and body
    Given the user is authenticated
    When the user makes a POST request to create a new post with very long title and body
    Then the response status code should be 400
    And the response should contain error messages indicating the title and body length constraints

	Scenario: Unauthorized user tries to make a new post
    Given the user is not authenticated
    When an unauthorized user makes a POST request to create a new post
    Then the response status code should be 401
    And the response should contain an error message indicating the user needs to authenticate

  Scenario: User makes a new post with additional unauthorized data
    Given the user is authenticated
    When the user makes a POST request to create a new post with additional unauthorized data
    Then the response status code should be 201

	Scenario: User makes a post with special characters in the title and body
    Given the user is authenticated
    When the user makes a POST request to create a new post with special characters in the title and body
    Then the response status code should be 201