package com.app.amazonS3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.app.dynamoDb.DynamoUser;

public class S3Folder {
	private static final String BUCKET = "s3-bucket-location";
	private final static String FOLDER_SUFFIX = "/";
	//final static AmazonS3Client client;
	private static AmazonS3Client s3client = null;
	private final static Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	S3Operations s3oprnObj = new S3Operations();


	static {
		// Create S3 Client object using AWS KEY & SECRET
//		client = new AmazonS3Client(
//				new BasicAWSCredentials(AWS_KEY, AWS_SECRET));
		
		s3client = new AmazonS3Client(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		s3client.setRegion(usWest2);
	}
	
	public void uploadFile(String userID, String folderLocation){
		File file = new File(folderLocation);
		System.out.println(file.getName());
		//String fileName = "myfolder" + FOLDER_SUFFIX + "userauthority.png";
		PutObjectRequest pr = new PutObjectRequest(s3oprnObj.getBucketFromID(userID).getName(), "user", file);
        
      s3client.putObject(pr);
	}
	
public void createRootBucket(String userID){
		DynamoUser du = new DynamoUser();
		
		s3client.createBucket(userID + du.getUserName(userID).replaceAll("\\W", "").trim().toLowerCase());
	}

	public void create(String foldername, String userID) {
		// TODO validate foldername 

		// Create metadata for your folder & set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// Create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// Create a PutObjectRequest passing the foldername suffixed by /
		PutObjectRequest putObjectRequest =
				new PutObjectRequest("unitedawesome", foldername + FOLDER_SUFFIX,
						emptyContent, metadata);

		// Send request to S3 to create folder
		s3client.putObject(putObjectRequest);
	}
	


	public static void main(String[] args) {
		S3Folder s3Folder = new S3Folder();
		//s3Folder.createRootBucket("1001");
		//s3Folder.create("myfolder","1001");
		s3Folder.uploadFile("1001", "C:\\mytext.txt");
	}
}
