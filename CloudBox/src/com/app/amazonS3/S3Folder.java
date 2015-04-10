package com.app.amazonS3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
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
	public boolean uploadFile(String bucketName, String fileName, InputStream input,String locationOnS3 ,
			ObjectMetadata metadata) throws FileNotFoundException{
		DynamoFilesURL dfu = new DynamoFilesURL();
		//ArrayList<Files> temp = s3oprnObj.getFiles(bucketName, locationOnS3);
		//int version = 0;
		String versionFileName = dfu.read(locationOnS3+fileName);
		if(versionFileName=="0"){
		
			dfu.insert((locationOnS3+fileName),"1");
			PutObjectRequest pr = new PutObjectRequest(bucketName, locationOnS3 + ("1"+fileName), 
					input,metadata);
			s3client.putObject(pr);
			return true;
		}
		else{
			versionFileName = ""+(Integer.parseInt(versionFileName)+1);
			dfu.insert((locationOnS3+fileName),versionFileName);
			PutObjectRequest pr = new PutObjectRequest(bucketName, locationOnS3 + (versionFileName+fileName), 
					input,metadata);
			s3client.putObject(pr);
			return true;
		}
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
		
		//"10vcredist".substring(0,"abc.def.ghi".indexOf(c)-1);
		
	//	System.out.println(oprn.downloadFile("1001syogesh","cboxfoo/cboximage"));
	//	System.out.println(oprn.getFolders("1001syogesh", "1001syogesh").size());
		//s3Folder.createRootBucket("1001");
	//	s3oprnObj.createFolder("1011syogesh1","new/pratik");
		
		File file = new File("E:\\vcredist.bmp");
		FileInputStream input = new FileInputStream(file);
		
		ObjectMetadata m = new ObjectMetadata();
		
		m.setContentLength(file.length());
		
		//s3Folder.uploadFile("1001syogesh", file.getName(),input,"cboxfoo/testing/",m);
	
		
		S3ObjectInputStream s3 = s3oprnObj.downloadFile("1001syogesh", "cboxfoo/testing/", "vcredist.bmp", "1");
		
		InputStream inputStream = null;
		OutputStream outputStream = null;
	 
		/*try {
			// read this file into InputStream
			inputStream = s3;
	 
			// write the inputStream to a FileOutputStream
			outputStream = 
	                    new FileOutputStream(new File("/Users/mkyong/Downloads/holder-new.js"));
	 
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
	 
			System.out.println("Done!");
	 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}
		}*/
		//	s3client.deleteBucket("1005spratikbidkar");
		/*ArrayList<Files> f = s3oprnObj.getFiles("1001syogesh", "");
		for(Files fr : f){
			System.out.println(fr.getFileName());
			System.out.println(fr.getVersion());
		}*/
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

