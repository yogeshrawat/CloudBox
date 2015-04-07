package com.app.dynamoDb;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class CommunicateDynamoDb {
	
	public static void main(String args[])
	{
		DynamoSharedURL fb = new DynamoSharedURL();
		fb.insert("www.gmai.com");
		//fb.remove("www.gmai.com");
	}

}
