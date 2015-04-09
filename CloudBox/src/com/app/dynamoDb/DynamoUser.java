package com.app.dynamoDb;
import com.app.amazonS3.CommunicateS3;


/*
 * Copyright 2012-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;


public class DynamoUser {


	public String UserID;
	public String UserName;
	public String Password;
	public String Email;

	public AmazonDynamoDBClient dynamoDB;
	public AWSCredentials credentials;
	public DynamoUser(){

		//credentials = new ProfileCredentialsProvider("default").getCredentials();
		dynamoDB = new AmazonDynamoDBClient(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);		    
	}


	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}


	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	@SuppressWarnings("null")
	public  void insert(String UserName, String Password, String Email)
	{

		ScanRequest scanRequest = new ScanRequest("Users");
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		int s[]=new int[100];
		int i =0;

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
			s[i] = Integer.valueOf(item.get("UserID").getS());
			i++;
		}       
		Arrays.sort(s);
		int max= s[s.length-1];
		String userid = String.valueOf(max+1);        

		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("UserID", new AttributeValue(userid));
		item.put("UserName", new AttributeValue(UserName));
		item.put("Password", new AttributeValue(Password));
		item.put("Email", new AttributeValue(Email));
		this.setUserID(userid);
		this.setUserName(UserName);
		this.setPassword(Password);
		this.setEmail(Email);

		PutItemRequest putItemRequest = new PutItemRequest("Users", item);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);

	}

	public  boolean validateEmail(String Email, String Password){


		ScanRequest scanRequest = new ScanRequest("Users");
		scanRequest.setConditionalOperator(ConditionalOperator.AND);

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("Email", new Condition().withAttributeValueList(new AttributeValue(Email)).withComparisonOperator(ComparisonOperator.EQ));
		scanFilter.put("Password", new Condition().withAttributeValueList(new AttributeValue(Password)).withComparisonOperator(ComparisonOperator.EQ));

		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
			//System.out.println(item);

			if(!item.isEmpty())    	
				return true;
		}
		return false;

	}
	
	public  boolean validateName(String UserName, String Password){


		ScanRequest scanRequest = new ScanRequest("Users");
		scanRequest.setConditionalOperator(ConditionalOperator.AND);

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("UserName", new Condition().withAttributeValueList(new AttributeValue(UserName)).withComparisonOperator(ComparisonOperator.EQ));
		scanFilter.put("Password", new Condition().withAttributeValueList(new AttributeValue(Password)).withComparisonOperator(ComparisonOperator.EQ));

		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
			//System.out.println(item);

			if(!item.isEmpty())    	
				return true;
		}
		return false;


	}


	public String getUserIDfromUserName(String UserName)
	{
		String result = "";
		ScanRequest scanRequest = new ScanRequest("Users");
		//scanRequest.setConditionalOperator(ConditionalOperator.OR);

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("UserName", new Condition().withAttributeValueList(new AttributeValue(UserName)).withComparisonOperator(ComparisonOperator.EQ));
		//scanFilter.put("Password", new Condition().withAttributeValueList(new AttributeValue(UserName)).withComparisonOperator(ComparisonOperator.EQ));

		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
			System.out.println(item.get("UserID"));
		
			result = item.get("UserID").toString();
		}
		return result;
	}
	
	public String getUserIDfromEmail(String Email)
	{
		String result = "";
		ScanRequest scanRequest = new ScanRequest("Users");

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("Email", new Condition().withAttributeValueList(new AttributeValue(Email)).withComparisonOperator(ComparisonOperator.EQ));

		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
			System.out.println(item.get("UserID"));
		
			result = item.get("UserID").toString();
		}
		return result;
	}

	public String getUserName(String UserID)
	{
		String result = "";
		ScanRequest scanRequest = new ScanRequest("Users");

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("UserID", new Condition().withAttributeValueList(new AttributeValue(UserID)).withComparisonOperator(ComparisonOperator.EQ));

		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
			System.out.println(item.get("UserName"));
		
			result = item.get("UserName").toString();
		}
		return result;
	}


}
