package com.app.amazonS3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.app.dynamoDb.DynamoFilesURL;
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
		s3client = new AmazonS3Client(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		s3client.setRegion(usWest2);
	}
	
	/**
	 * Uploads a file to the specified location by the parameters. This also takes care about the 
	 * file versioning.
	 * @param bucketName - which bucket the files needs to be uploaded to
	 * @param fileName - file name (eg. samplefile.txt , image.jpg)
	 * @param locationOnS3 - path where the file has to be stored
	 * @throws FileNotFoundException
	 */
	public void uploadFile(String bucketName, String fileName, File file,String locationOnS3 ) throws FileNotFoundException{
		DynamoFilesURL dfu = new DynamoFilesURL();
		//ArrayList<Files> temp = s3oprnObj.getFiles(bucketName, locationOnS3);
		//int version = 0;
		String versionFileName = null;
		if(dfu.read(locationOnS3+fileName)=="1"){
		
			dfu.insert((locationOnS3+fileName),"1");
		}
		String ver = dfu.read(locationOnS3+fileName);
		PutObjectRequest pr = new PutObjectRequest(bucketName, locationOnS3 + (ver+fileName), 
				file);
		s3client.putObject(pr);
		/*for(Files f : temp){
			if(f.getFileName().contains(fileName)){
				//f.setFileName(f.getFileName() + "1");
				 version = Character.getNumericValue(f.getFileName().charAt(0));
				 version++;
				//s3client.copyObject(bucketName, f.getFileName(), bucketName+"/"+"cboxfoo", "yogi");
//				//s3client.deleteObject(bucketName, f.getFileName());
			}
			else{
				 versionFileName = "1"+fileName;
				 
				 break;
			}
		}
		versionFileName = ""+version+fileName;
		System.out.println("path"+(locationOnS3+versionFileName));
		PutObjectRequest pr = new PutObjectRequest(bucketName, locationOnS3 + versionFileName, 
				file);
		s3client.putObject(pr);*/
	}
	
	/**
	 * Creates a root bucket for a user when he is registered to system.
	 * A bucket for the user gets created on S3.
	 * @param userID - the userID of user
	 */
	public void createRootBucket(String userID){
		DynamoUser du = new DynamoUser();
		String name = du.getUserName(userID).replaceAll("\\W", "").trim().toLowerCase();
		s3client.createBucket(userID + name);
	}

	public static void main(String[] args) throws FileNotFoundException {
		S3Folder s3Folder = new S3Folder();
		S3Operations oprn = new S3Operations();
	//	System.out.println(oprn.downloadFile("1001syogesh","cboxfoo/cboximage"));
	//	System.out.println(oprn.getFolders("1001syogesh", "1001syogesh").size());
		//s3Folder.createRootBucket("1001");
	//	s3oprnObj.createFolder("1011syogesh1","new/pratik");
		File file = new File("E:\\vcredist.bmp");
		s3Folder.uploadFile("1001syogesh", file.getName(),file,"cboxfoo/testing/");
	//	s3client.deleteBucket("1005spratikbidkar");
	//	s3oprnObj.getFiles("1001syogesh", "cboxfoo/testing");
	//	System.out.println(s3oprnObj.listKeysInDirectory("1001syogesh", FILE_NAME_PREFIX));
		
//		ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest().
//			    withBucketName("1001syogesh"));
//		System.out.println(oprn.getFiles("1001", "1001syogesh").size());
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

