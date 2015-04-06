package com.app.amazonS3;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

public class AmazonS3ClientUtil {

	
	private static AmazonS3Client s3client = null;
	private final static Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	
	/**
	 * @param s3 the AmazonClient for each user
	 */
	static{
		s3client = new AmazonS3Client(new AWSCredentialsProviderChain(new InstanceProfileCredentialsProvider(),
				new ClasspathPropertiesFileCredentialsProvider()));
		s3client.setRegion(usWest2);
	}
	/**
	 * @return the Amazon s3 Client instance for this user
	 */
	public AmazonS3Client getS3Client() {
		return s3client;
	}



}
