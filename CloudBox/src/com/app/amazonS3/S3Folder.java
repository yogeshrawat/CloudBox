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
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.app.dynamoDb.DynamoUser;

public class S3Folder {
	private static final String BUCKET = "s3-bucket-location";
	private final static String FOLDER_SUFFIX = "/";
	private final static String FILE_NAME_PREFIX = "cbox";
	//final static AmazonS3Client client;
	private static AmazonS3Client s3client = null;
	private final static Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	static S3Operations s3oprnObj = new S3Operations();


	static {
		// Create S3 Client object using AWS KEY & SECRET
//		client = new AmazonS3Client(
//				new BasicAWSCredentials(AWS_KEY, AWS_SECRET));
		
		s3client = new AmazonS3Client(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		s3client.setRegion(usWest2);
	}
	
	public void uploadFile(String userId, String folderLocation,String exactLocation){
		File file = new File(folderLocation);
		System.out.println(file.getName());
		//String fileName = "myfolder" + FOLDER_SUFFIX + "userauthority.png";
		PutObjectRequest pr = new PutObjectRequest(s3oprnObj.getBucketNameFromUserID(userId),
				exactLocation + FOLDER_SUFFIX, file);
        
      s3client.putObject(pr);
	}
	
	public void createRootBucket(String userID){
		DynamoUser du = new DynamoUser();
		String name = du.getUserName(userID).replaceAll("\\W", "").trim().toLowerCase();
		s3client.createBucket(userID + name);
		s3oprnObj.createFolder(s3oprnObj.getBucketNameFromUserID(userID),userID);
	}

	public static void main(String[] args) {
		S3Folder s3Folder = new S3Folder();
		S3Operations oprn = new S3Operations();
		//System.out.println(oprn.getFolders("1001syogesh", "cboxfoo/testing").size());
		//s3Folder.createRootBucket("1001");
		s3oprnObj.createFolder("1010sdarwin","sdarwin/subfolder1");
	//	s3Folder.uploadFile("1001", "C:\\mytext.txt","1001syogesh/subfolder");
	//	s3client.deleteBucket("1005spratikbidkar");
		
	//	System.out.println(s3oprnObj.listKeysInDirectory("1001syogesh", FILE_NAME_PREFIX));
		
//		ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest().
//			    withBucketName("1001syogesh"));
		System.out.println(oprn.getFilesFromBucket("1001", "1001syogesh").size());
		//for (final S3ObjectSummary objectSummary: s3oprnObj.listKeysInDirectory("unitedawesome", "unitedawesome/myfolder")){
		 //   final String key = objectSummary.getKey();
		     //if(key.contains(FOLDER_SUFFIX))
		    	 //System.out.println(key);
		     
		  //  System.out.println(key);
		//}
		    
		    /*
		    //if (S3Asset.isImmediateDescendant(prefix, key)) {
		 //       final String relativePath = getRelativePath("j", key);
		}*/
		
		
	}
}

