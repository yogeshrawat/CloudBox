package com.app.dynamoDb;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class DynamoFilesURL {
	
	public String FileURL;
	public String Owner;
	AmazonDynamoDBClient dynamoDB;
	AWSCredentials credentials;
	public DynamoFilesURL(){
	   
	    credentials = new ProfileCredentialsProvider("default").getCredentials();
	    dynamoDB = new AmazonDynamoDBClient(credentials);
	    Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	    dynamoDB.setRegion(usWest2);	
	    
	}
	
	public String getFileURL() {
		return FileURL;
	}
	public void setFileURL(String fileURL) {
		FileURL = fileURL;
	}
	public String getOwner() {
		return Owner;
	}
	public void setOwner(String owner) {
		Owner = owner;
	}
	
	public void insert(String Owner, String FileURL){
		
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("FileURL", new AttributeValue(FileURL));
        item.put("Owner", new AttributeValue(Owner));
        
        PutItemRequest putItemRequest = new PutItemRequest("FilesURL", item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);

		
	}
	
	public void read(String FileURL){
		
		ScanRequest scanRequest = new ScanRequest("FilesURL");

        Map<String, Condition> scanFilter = new HashMap<String, Condition>();
        scanFilter.put("FileURL", new Condition().withAttributeValueList(new AttributeValue(FileURL)).withComparisonOperator(ComparisonOperator.EQ));

        scanRequest.setScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);

        for(Map<String, AttributeValue> item : scanResult.getItems()) {
            System.out.println(item.get("UserID"));
        }
		}
	
	public void remove(){
		
	}

}
