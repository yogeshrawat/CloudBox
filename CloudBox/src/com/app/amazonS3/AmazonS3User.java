package com.app.amazonS3;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * 
 * 
 * @author pratik bidkar
 *
 */
public class AmazonS3User {
	
	private String name = "";
	private String email = "";
	private String uid = "";
	
	
	
	public AmazonS3User(String uid){
		
		
		setUid(uid);
		
	}

	
	
	/**
	 * user name to store
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * user email to set
	 * 
	 */
	public void setEmail(String email){
		this.email = email;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the uid for the user
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the usWest2
	 */
	public Region getUsWest2() {
		return usWest2;
	}

}
