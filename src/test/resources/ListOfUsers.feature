Feature: Users List
	Scenario: Get list of users successfully
		Given the user is authenticated
		When the user makes a GET request to fetch the list of users
		Then the response status code should be 200
		And the response should contain a non-empty list of users
		And each user in the list should have the following attributes:
    | id   | name       | username   | email             |
    | int  | string     | string     | string (valid email) |
    
  Scenario: Unauthorized user tries to get list of users
  	Given the user is not authenticated
		When an unauthorized user makes a GET request to fetch the list of users
		Then the response status code should be 401
		
	Scenario: Get List of users with pagination
		Given the user is authenticated
		When the user makes a GET request to fetch the list of users with page number 2 and page size 20
		Then the response status code should be 200
		And the response should contain a non-empty list of users
		And the number of users in the response should be equal to the specified page size
		And the response headers should contain the total count of users
		And each user in the list should have the following attributes:
    	| id   | name       | username   | email             |
    	| int  | string     | string     | string (valid email) |
    
  Scenario: Get List of Users with Invalid Page Number
  	Given the user is authenticated
		When the user makes a GET request to fetch the list of users with invalid page number 500
		Then the response status code should be 404
		And the response should contain an error message indicating that the page is not found
		
	Scenario: Get list of users with invalid page size
		Given the user is authenticated
		When the user makes a GET request to fetch the list of users with page size 200
		Then the response status code should be 400
		And the response should contain an error message indicating that the page size is not valid