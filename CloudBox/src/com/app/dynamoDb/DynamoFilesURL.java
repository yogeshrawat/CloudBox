package com.app.dynamoDb;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class DynamoFilesURL {

	public String FileURL;
	public String Version;
	AmazonDynamoDBClient dynamoDB;
	AWSCredentials credentials;
	public DynamoFilesURL(){

		//credentials = new ProfileCredentialsProvider("default").getCredentials();
		dynamoDB = new AmazonDynamoDBClient(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		dynamoDB.setRegion(usWest2);	

	}

	public String getFileURL() {
		return FileURL;
	}
	public void setFileURL(String fileURL) {
		FileURL = fileURL;
	}
	

	public void insert(String FileURL, String Version){

		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("FileURL", new AttributeValue(FileURL));
		item.put("Version", new AttributeValue(Version));


		PutItemRequest putItemRequest = new PutItemRequest("FilesURL", item);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);


	}

	public String read(String FileURL){

		String result = "0";
		ScanRequest scanRequest = new ScanRequest("FilesURL");

		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("FileURL", new Condition().withAttributeValueList(new AttributeValue(FileURL)).withComparisonOperator(ComparisonOperator.EQ));

		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		for(Map<String, AttributeValue> item : scanResult.getItems()) {
			//System.out.println(item.get("Version"));
		//System.out.println(item.get("Version").toString().replaceAll("\\W\\D", "").trim().toLowerCase());
			if(!(item.isEmpty()))
			result = item.get("Version").toString().replaceAll("\\W\\D", "").trim().toLowerCase();
			
			//result = result.replaceAll("\\D", "");
		}
		return result;
		
	}

	public void remove(String FileURL){

		ScanRequest scanRequest = new ScanRequest("FilesURL");
		Map<String, Condition> scanFilter = new HashMap<String, Condition>();
		scanFilter.put("FileURL", new Condition().withAttributeValueList(new AttributeValue(FileURL)).withComparisonOperator(ComparisonOperator.EQ));
		scanRequest.setScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		if(!(scanResult.getCount()==0)){
			HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put("FileURL", new AttributeValue(FileURL));
			DeleteItemRequest deleteRequest = new DeleteItemRequest().withTableName("FilesURL").withKey(key);
			dynamoDB.deleteItem(deleteRequest);
		}
		else
		{
			System.out.println("No item Found");
		}
	}

}
