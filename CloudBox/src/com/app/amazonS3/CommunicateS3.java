package com.app.amazonS3;

import com.app.dynamoDb.DynamoUser;

public interface CommunicateS3 {
	
	public boolean validatedUser(DynamoUser user);


}
