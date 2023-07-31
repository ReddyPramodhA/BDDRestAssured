Feature: Add Comment
	Scenario: User comments on a post with valid data
		Given the user is authenticated
		And there is an existing post with ID 1
		When the user comments on the post
		Then the comment should be successfully added with a unique ID
		And the response status code should be 201
		
	Scenario: User comments on a post with empty message
		Given the user is authenticated
		And there is an existing post with ID 1
		When the user comments on the post with an empty message
		Then the response status code should be 400
		And the response should contain an error message indicating the missing comment message field
		
	Scenario: User comments on a non-existing post
		Given the user is authenticated
		And there is no post with ID 999
		When the user comments on the non-existing post
		Then the response status code should be 404
		And the response should contain an error message indicating the post was not found
		
	Scenario: User comments on a post with excessively long message
		Given the user is authenticated
		And there is an existing post with ID 1
		When the user comments on the post with a very long message
		Then the response status code should be 400
		And the response should contain an error message indicating the message length constraint
		
	Scenario: Unauthorized user tries to comment on a post
		Given the user is not authenticated
		And there is an existing post with ID 1
		When an unauthorized user tries to comment on the post
		Then the response status code should be 401
		And the response should contain an error message indicating the user needs to authenticate
		
	Scenario: User comments on a post with special characters in the message
		Given the user is authenticated
		And there is an existing post with ID 1
		When the user comments on the post with a message containing special characters
		Then the response status code should be 201
		
	Scenario: User comments on a post with HTML tags in the message
		Given the user is authenticated
		And there is an existing post with ID 1
		When the user comments on the post with a message containing HTML tags:
		"""
        <strong>This is a <em>test</em> comment.</strong>
    """
		Then the response status code should be 201
		And the comment should be successfully added with the HTML tags preserved and not interpreted as markup