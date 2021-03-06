package com.app.amazonS3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.app.dynamoDb.DynamoFilesURL;
import com.app.dynamoDb.DynamoUser;

public class S3Operations{

	static AmazonS3User s3user = null;
	private static final String SUFFIX = "/";
	AmazonS3ClientUtil util;
	private int inputStreamLength = 0;
	
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
	/**
	 * 
	 * @param bucketName - bucketname of the user eg. 1010sdarwin 
	 * @param folderName - exact path eg. sdarwin/subfolder1
	 * Note - sdarwin is a big folder created by default.
	 */
	public void createFolder(String bucketName, String folderName) {
		// TODO validate foldername 

		// Create metadata for your folder & set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// Create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// Create a PutObjectRequest passing the foldername suffixed by /
		PutObjectRequest putObjectRequest =
				new PutObjectRequest(bucketName,  folderName + S3Operations.SUFFIX,
						emptyContent, metadata);

		// Send request to S3 to create folder
		s3client.putObject(putObjectRequest);
	}
	/*public void uploadFile(String bucketName, String folderLocation){
		File file = new File(folderLocation);
		PutObjectRequest pr = new PutObjectRequest(bucketName, userID, file);
        
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
	            .withBucketName(bucketName);
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
		
		String delimiter = "/";
	    /*if (!prefix.endsWith(delimiter)) {
	        prefix += delimiter;
	    }*/
	    
	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            .withBucketName(bucketName).withPrefix(prefix).withDelimiter(delimiter);
	    ObjectListing objects = s3client.listObjects(listObjectsRequest);
		List<String> str = objects.getCommonPrefixes();
		ArrayList<Folders> folders = new ArrayList<Folders>();
		Folders temp ;
		for(String s : str){
			System.out.println(s);
			s = s.replaceAll(prefix, "").trim();
			temp = new Folders(bucketName, s.replaceAll("\\W", "").trim().toLowerCase());
			folders.add(temp);

		}
		return folders;
	}
	
	
	/**
	 * 
	 * @param bucketName
	 * @param prefix - for bucket level should be the bucketname itself
	 * @return List of file objects that are present in the bucket level
	 */
	public ArrayList<Files> getFiles(String bucketName, String prefix){
		//String bucketName = getBucketNameFromUserID(userId);
		ArrayList<Files> files = new ArrayList<Files>();
		Files temp ;
		String delimiter = "/";
	    /*if (!prefix.endsWith(delimiter)) {
	        prefix += delimiter;
	    }*/

	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            .withBucketName(bucketName).withPrefix(prefix).withDelimiter(delimiter);
	    ObjectListing objects = s3client.listObjects(listObjectsRequest);
	    
	    List<S3ObjectSummary> temporary = objects.getObjectSummaries();
		for ( S3ObjectSummary s : temporary){
			String key = s.getKey();
			if(prefix.length()>0)
				key = s.getKey().replaceAll(prefix, "").trim();
				
				if(key.length()>0 && !key.contains("/") && Character.isDigit(key.charAt(0))){
					//System.out.println(key);
					String t = key;
					key = key.substring(1);
					int ver = (Character.getNumericValue(t.charAt(0)));
					temp = new Files(key,ver,s.getSize());
					files.add(temp);
				}
		}
		ArrayList<Files> ret = new ArrayList<Files>();
		Set<String> hs = new HashSet<String>();
		
		DynamoFilesURL df = new DynamoFilesURL();
		for(Files s : files){
			hs.add(s.getFileName());
		}
		for(String str : hs){
			
			String version = df.read(prefix+str);
			System.out.println("Version from dynamo "+version+" filename from hashset "+str);
			for(Files t: files)
			{
				System.out.println("Inside loop : original files "+t.getFileName()+ " version "+t.getVersion());
				if(t.getFileName().compareTo(str)==0 && (""+t.getVersion()).compareTo(version)==0)
				{
					
					ret.add(new Files(t.getFileName(),Integer.parseInt(version),t.getSize()));
				}
			}
			
			
		}
		return ret;
	}

	/**
	 * This method will 
	 * @param bucketName
	 * @param filePath - the locaiton from where the file needs to be downloaded
	 * @return - public URL of the file
	 */
	public S3ObjectInputStream downloadFile(String bucketName, String filePath,String fileName, String version){
		String finalPath;
		//if(filePath.length()>0)
		 finalPath= filePath+version+fileName;
		//else
		//	finalPath = SUFFIX + version + fileName;
		
		S3Object object = s3client.getObject(new GetObjectRequest(bucketName, finalPath));
		
		setInputStreamLength((int)object.getObjectMetadata().getContentLength());
		System.out.println(getInputStreamLength());
		return object.getObjectContent();
		/*File file = new File("C:\\my.txt");
		s3client.getObject(new GetObjectRequest(bucketName, filePath) , file);
		
		GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, filePath);
		return s3client.generatePresignedUrl(req);*/
		
	}

	/**
	 * @return the inputStreamLength
	 */
	public int getInputStreamLength() {
		return inputStreamLength;
	}

	/**
	 * @param inputStreamLength the inputStreamLength to set
	 */
	public void setInputStreamLength(int inputStreamLength) {
		this.inputStreamLength = inputStreamLength;
	}
	
	public boolean removeFile(String bucketName, String filePath, String fileName, String version){
		String finalPath= filePath+version+fileName;
		s3client.deleteObject(bucketName, finalPath);
		return true;
	}
	
	public void shareFile( String sourceBucketName, String sourcefilePath, String sourcefileName,
			String version,
			String destinationBucketName){
		
		String finalPath= sourcefilePath+version+sourcefileName;
		String destinationFileName = version+sourcefileName;
		s3client.copyObject(sourceBucketName, finalPath, destinationBucketName, destinationFileName);
	}
	
	
}
