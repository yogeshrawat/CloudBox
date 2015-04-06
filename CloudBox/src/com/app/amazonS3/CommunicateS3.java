package com.app.amazonS3;

import com.app.dynamoDb.DynamoUser;

public interface CommunicateS3 {
	
	public boolean validatedUser(DynamoUser user);

	void createFolder(String userID, String folderName, AmazonS3User client);

}
