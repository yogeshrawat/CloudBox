package com.app.dynamoDb;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class DynamoDbClient {

    static AmazonDynamoDBClient dynamoDB;
    static AWSCredentials credentials = null;
   
    static {
    credentials = new ProfileCredentialsProvider("default").getCredentials();
    dynamoDB = new AmazonDynamoDBClient(credentials);
    Region usWest2 = Region.getRegion(Regions.US_WEST_2);
    dynamoDB.setRegion(usWest2);	
    }
	/**
	 * @param s3 the AmazonClient for each user
	 */
	
	/**
	 * @return the Amazon s3 Client instance for this user
	 */
	public AmazonDynamoDBClient getDynamoDbClient() {
		return dynamoDB;
	}



}
