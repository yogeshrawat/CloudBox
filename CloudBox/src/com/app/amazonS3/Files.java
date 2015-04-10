package com.app.amazonS3;

import java.util.HashSet;

public class Files {
	
	private String fileName;
	private HashSet<String> version = new HashSet<String>();
	private Long size;
	
	public Files(String name, int version, long l){
		setFileName(name);
		setSize(l);
		setVersion(version);
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the version
	 */
	public HashSet getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version.add(""+version);
	}
	/**
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
	}
	
	
	

}
