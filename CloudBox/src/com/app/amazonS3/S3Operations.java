package com.app.amazonS3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.app.dynamoDb.DynamoUser;

public class S3Operations implements CommunicateS3{

	static AmazonS3User s3user = null;
	private static final String SUFFIX = "/";
	AmazonS3ClientUtil util;
	DynamoUser du = new DynamoUser();
	private static AmazonS3Client s3client = null;
	private final static Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	
	public S3Operations(){
		
	}
	
	@Override
	public boolean validatedUser(DynamoUser user) {
		
		s3user = new AmazonS3User("1001");
		return false;
	}
	
	
	public void createRootBucket(String userID){
		
		util.getS3Client().createBucket(userID);
	}
	
	@Override
	public void createFolder(String userID, String folderName, AmazonS3User client ) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(userID,
		folderName + SUFFIX, emptyContent, metadata);
		// send request to S3 to create folder
		util.getS3Client().putObject(putObjectRequest);
		}
	
	public void uploadFile(String userID, String folderLocation){
		File file = new File(folderLocation);
		PutObjectRequest pr = new PutObjectRequest(getBucketFromID(userID).getName(), userID, file);
        
        util.getS3Client().putObject(pr);
	}

	public Bucket getBucketFromID(String userID) {
		s3client = new AmazonS3Client(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		s3client.setRegion(usWest2);
		
		List<Bucket> blist = s3client.listBuckets();
		Bucket retBucket=null;
		String tempBucketName = userID+du.getUserName(userID).replaceAll("\\W", "").trim().toLowerCase();
		for(Bucket b : blist){
			if(b.getName().equalsIgnoreCase(tempBucketName))
				retBucket = b;
		}
		return retBucket;
	}
	
	public void getFileFromBucket(String userID){
		s3client = new AmazonS3Client(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		s3client.setRegion(usWest2);
		
		File localFile = new File("localFilename");

		ObjectMetadata object = s3client.getObject(new GetObjectRequest("bucket", "s3FileName"), localFile);
	}

}
