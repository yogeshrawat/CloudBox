package com.app.dynamoDb;
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


public class DynamoFacebookUsers
{

	public String UserID;
	public String UserName;
	public String Email;

	public AmazonDynamoDBClient dynamoDB;
	public AWSCredentials credentials;
	public DynamoFacebookUsers(){
	   
	 //   credentials = new ProfileCredentialsProvider("default").getCredentials();
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

	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String email) {
		Email = email;
	}




	public void insert(String UserID, String UserName, String Email)
    {
    	 Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
         item.put("UserID", new AttributeValue(UserID));
         item.put("UserName", new AttributeValue(UserName));
         item.put("Email", new AttributeValue(Email));
         System.out.println("inserted");
         
         PutItemRequest putItemRequest = new PutItemRequest("FacebookUsers", item);
         PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);

    }
	
	public boolean isExist(String UserID)
	{
		ScanRequest scanRequest = new ScanRequest("FacebookUsers");

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("UserID", new Condition().withAttributeValueList(new AttributeValue(UserID)).withComparisonOperator(ComparisonOperator.EQ));

		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
		if(!(item.isEmpty()))
			return true;
		}
		return false;
	}
	
	public String getUserName(String UserID)
	{
		String result = "";
		ScanRequest scanRequest = new ScanRequest("FacebookUsers");

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
    

	public AttributeValue getUserID(String UserName)
    {
    	 
        ScanRequest scanRequest = new ScanRequest("FacebookUsers");

        Map<String, Condition> scanFilter = new HashMap<String, Condition>();
        scanFilter.put("Email", new Condition().withAttributeValueList(new AttributeValue(UserName)).withComparisonOperator(ComparisonOperator.EQ));
        scanRequest.setScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);

        for(Map<String, AttributeValue> item : scanResult.getItems()) {
            return item.get("UserID");
    	
        }
        return null;
    }
 
}
