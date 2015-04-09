package com.app.amazonS3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.app.dynamoDb.DynamoUser;

public class S3Operations{

	static AmazonS3User s3user = null;
	private static final String SUFFIX = "/";
	AmazonS3ClientUtil util;
	DynamoUser du = new DynamoUser();
	private static AmazonS3Client s3client = null;
	private final static Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	static {
		// Create S3 Client object using AWS KEY & SECRET
//		client = new AmazonS3Client(
//				new BasicAWSCredentials(AWS_KEY, AWS_SECRET));
		
		s3client = new AmazonS3Client(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		s3client.setRegion(usWest2);
	}
	public S3Operations(){
		
	}
	
	public boolean validatedUser(DynamoUser user) {
		
		s3user = new AmazonS3User("1001");
		return false;
	}
	
	
	public void createRootBucket(String userID){
		
		util.getS3Client().createBucket(userID);
	}
	
	
	/*public void uploadFile(String userID, String folderLocation){
		File file = new File(folderLocation);
		PutObjectRequest pr = new PutObjectRequest(getBucketFromID(userID).getName(), userID, file);
        
        util.getS3Client().putObject(pr);
	}*/

	public Bucket getBucketFromID(String userID) {
		
		
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
		
		
		File localFile = new File("localFilename");
		
		//s3client.getObject(new getObjectRequest("unitedawesome"," "),localFile);
		ObjectMetadata object = s3client.getObject(new GetObjectRequest("bucket", "s3FileName"), localFile);
	}
	
	public String getBucketNameFromUserID(String userId){
		String result = "";
		result = userId + du.getUserName(userId).replaceAll("\\W", "").trim().toLowerCase();
		return result;
	}

	/**
	 * 
	 * @param bucketName - bucket Name
	 * @param prefix - exact path about which you need the information
	 * @return List - Collection of items inside the given location
	 */
	public List<S3ObjectSummary> listKeysInDirectory(String bucketName, String prefix) {
		
	    String delimiter = "/";
	    if (!prefix.endsWith(delimiter)) {
	        prefix += delimiter;
	    }

	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            .withBucketName(bucketName).withPrefix(prefix).withDelimiter(delimiter);
	    ObjectListing objects = s3client.listObjects(listObjectsRequest);
	    
	     return objects.getObjectSummaries();
	}
	
	/**
	 * 
	 * @param bucketName
	 * @param prefix
	 * @return List of folders in a bucket
	 */
	public ArrayList<Folders> getFolders(String bucketName, String prefix){
		
		/*String delimiter = "/";
	    if (!prefix.endsWith(delimiter)) {
	        prefix += delimiter;
	    }

	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            .withBucketName(bucketName);
	    ObjectListing objects = s3client.listObjects(listObjectsRequest);
		List<S3ObjectSummary> str = objects.getObjectSummaries();*/
		
		ArrayList<Folders> folders = new ArrayList<Folders>();
		Folders temp ;
		List<S3ObjectSummary> temporary = listKeysInDirectory(bucketName.toLowerCase(), prefix);
		//for (final S3ObjectSummary objectSummary: str)
		//	System.out.println(objectSummary.getKey());
		for (final S3ObjectSummary objectSummary: temporary){
			String key = objectSummary.getKey();
			System.out.println(key);
			char lastChar = key.charAt(key.length()-1);
			if(lastChar=='/'){
				temp = new Folders(bucketName, key.replaceAll("\\W", "").trim().toLowerCase());
				folders.add(temp);
			}
			
		}
		return folders;
	}
	
	/**
	 * 
	 * @param bucketName
	 * @param prefix - the path upto the folder level eg. unitedawesome/myfolder
	 * @return - List of files from a subfolder inside a bucket
	 */
	public ArrayList<Files> getFilesFromFolder(String bucketName, String prefix){
		
		ArrayList<Files> files = new ArrayList<Files>();
		Files temp ;
		String delimiter = "/";
	    if (!prefix.endsWith(delimiter)) {
	        prefix += delimiter;
	    }

	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName)
	            .withPrefix(prefix).withDelimiter(delimiter);
	    ObjectListing objects = s3client.listObjects(listObjectsRequest);
	    
	    List<S3ObjectSummary> temporary = objects.getObjectSummaries();
		for (final S3ObjectSummary objectSummary: temporary){
			String key = objectSummary.getKey();
			
			String f = null;
			if(!key.contains("/")){
				 f = key.replaceAll(prefix, "").trim();
				 if(f.length()!=0)
				 {temp = new Files(f,1,objectSummary.getSize());
				 files.add(temp);
				 }
			}
			
		}
		return files;
		
	}

	/**
	 * 
	 * @param bucketName
	 * @param prefix - for bucket level should be the bucketname itself
	 * @return List of file objects that are present in the bucket level
	 */
	public ArrayList<Files> getFilesFromBucket(String userId, String prefix){
		String bucketName = getBucketNameFromUserID(userId);
		ArrayList<Files> files = new ArrayList<Files>();
		Files temp ;
		String delimiter = "/";
	    if (!prefix.endsWith(delimiter)) {
	        prefix += delimiter;
	    }

	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            .withBucketName(bucketName);
	    ObjectListing objects = s3client.listObjects(listObjectsRequest);
	    
	    List<S3ObjectSummary> temporary = objects.getObjectSummaries();
		for (final S3ObjectSummary objectSummary: temporary){
			String key = objectSummary.getKey();
			
			if(!key.contains("/")){
				 {temp = new Files(key,1,objectSummary.getSize());
				 files.add(temp);
				 }
			}
			
		}
		return files;
		
	}


	
	
}
