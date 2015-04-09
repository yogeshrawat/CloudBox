package com.app.dynamoDb;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class CommunicateDynamoDb {
	
	public static void main(String args[])
	{
		DynamoUser fb = new DynamoUser();
		fb.getUserIDfromEmail("yogesh.rawat89@gmail.com");
		//fb.insert("leon","leon123","leon@gmail.com");
		//fb.remove("www.gmai.com");
		//fb.read();
	}

}
