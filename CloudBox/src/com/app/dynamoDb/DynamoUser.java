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
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
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

   
    static AmazonDynamoDBClient dynamoDB;

    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */
    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\yogeshrawat\\.aws\\credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\yogeshrawat\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);
    }

    public static void main(String[] args) throws Exception {
        init();        

        try {
//            String tableName = "Users";


            // Describe our new table
//            DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
//            TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
//            System.out.println("Table Description: " + tableDescription);

            // Add an item
//            insert("1003", "Leon", "Leon123", "Leon@gmail.com");
//
//            // Add another item
//            insert("1004", "coen691p", "cloud_computing", "coen691p@gmail.com");

            validate("1002","Pratik");


        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
    

    private static void insert(String UserID, String UserName, String Password, String Email)
    {
    	 Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
         item.put("UserID", new AttributeValue(UserID));
         item.put("UserName", new AttributeValue(UserName));
         item.put("Password", new AttributeValue(Password));
         item.put("Email", new AttributeValue(Email));
         
         PutItemRequest putItemRequest = new PutItemRequest("Users", item);
         PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);

    }
    
    @SuppressWarnings("null")
	private static void validate(String UserID, String Password){
    	
        
        ScanRequest scanRequest = new ScanRequest("Users");
        scanRequest.setConditionalOperator(ConditionalOperator.AND);

        Map<String, Condition> scanFilter = new HashMap<String, Condition>();
        scanFilter.put("UserID", new Condition().withAttributeValueList(new AttributeValue(UserID)).withComparisonOperator(ComparisonOperator.EQ));
        scanFilter.put("Password", new Condition().withAttributeValueList(new AttributeValue(Password)).withComparisonOperator(ComparisonOperator.EQ));

        scanRequest.setScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);

        for(Map<String, AttributeValue> item : scanResult.getItems()) {
            System.out.println(item);
            if(!item.isEmpty()){
                CommunicateS3 validates3 = null;
            	validates3.validatedUser(null);
            	
            }
        }

    }
 
}
