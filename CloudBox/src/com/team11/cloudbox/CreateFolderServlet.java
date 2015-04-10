package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.amazonS3.S3Operations;

/**
 * Servlet implementation class CreateFolderServlet
 */
public class CreateFolderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    String curFolder= (String) session.getAttribute("currentDir");
	    
	    String newFolderName= request.getParameter("NewFolder");
	    if(newFolderName==null)
	    {
	    	 response.sendError(HttpServletResponse.SC_NO_CONTENT, "No important parameter");
	    }
	    else
	    {
	    	/*File newSubFolder = new File(curFolder+newFolderName);
	    	if(!newSubFolder.exists())
	    	{
	    		if(newSubFolder.mkdir())//Add a new sub folder to S3 under current folder shown-Parameter-relative path to root-S1
	    		{
	    			return;
	    		}
	    		else
	    		{
	    			response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Folder cannot be created");
	    		}
	    	}
	    	else
	    	{
		    	 response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Folder already exist");
	    	}*/
	    	String userId = (String) session.getAttribute("userID");	    	
	    	S3Operations s3Operations = new S3Operations();
	    	String s3BucketHome = s3Operations.getBucketNameFromUserID(userId);
	    	
	    	if(curFolder.isEmpty())
	    	{
		    	System.out.println("s3BucketHome:"+s3BucketHome+", new subfolder to create:"+newFolderName.toLowerCase());
	    		s3Operations.createFolder(s3BucketHome, newFolderName.toLowerCase());
	    	}
	    	else
	    	{
		    	System.out.println("s3BucketHome:"+s3BucketHome+", new subfolder to create:"+curFolder+":"+newFolderName.toLowerCase());
		    	s3Operations.createFolder(s3BucketHome, curFolder+"/"+newFolderName.toLowerCase());	    		
	    	}

	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
