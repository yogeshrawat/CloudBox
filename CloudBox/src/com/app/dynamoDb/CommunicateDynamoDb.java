package com.app.dynamoDb;

public class CommunicateDynamoDb {
	
	public static void main(String args[])
	{
		DynamoFacebookUsers fb = new DynamoFacebookUsers();
		//fb.insert("1010", "james", "james@gmail.com");
		fb.getUserID("james@gmail.com");
	}

}
