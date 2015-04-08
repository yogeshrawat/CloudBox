package com.app.amazonS3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.app.dynamoDb.DynamoUser;

public class S3Operations implements CommunicateS3{

	static AmazonS3User s3user = null;
	private static final String SUFFIX = "/";
	AmazonS3ClientUtil util;
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
		PutObjectRequest pr = new PutObjectRequest(getBucketFromID(userID), userID, file);
        
        util.getS3Client().putObject(pr);
	}

	private String getBucketFromID(String bucketName) {

		List<Bucket> blist = util.getS3Client().listBuckets();
		String r = null;
		for(Bucket b : blist){
			if(b.getName().equalsIgnoreCase(bucketName))
				r = b.getName();
		}
		return null;
	}

}
