package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.amazonS3.S3Operations;
import com.app.dynamoDb.DynamoSharedURL;
import com.app.dynamoDb.DynamoUser;

/**
 * Servlet implementation class ShareFileServlet
 */
public class ShareFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		String userID = session.getAttribute("userID").toString();
		
		String URl= request.getParameter("URL");
		if(URl != null)
		{
			//Add file link to database for access restriction
			System.out.println("URl:"+URl+", UserID:"+userID);
			
			DynamoSharedURL  dynamoSharetable = new DynamoSharedURL();
			dynamoSharetable.insert(URl);
		}
		else
		{
		    String targetUser= request.getParameter("User");
			String fileName= request.getParameter("FileName");
		    String version= request.getParameter("Version");
		    
		    if(targetUser !=null && fileName != null && version != null)
		    {   
		    	//Share file via copy file to another target user S3 bucket-S7
		    	DynamoUser  user= new DynamoUser();
		    	String targetId = user.getUserIDfromUserName(targetUser);
		    	
		    	S3Operations s3Operations = new S3Operations();
		    	String from = s3Operations.getBucketNameFromUserID(userID);
		    	String to = s3Operations.getBucketNameFromUserID(targetId);
		    	
			    String curFolder= (String) session.getAttribute("currentDir");
			    System.out.println("From:"+from+", To:"+to+", FilePartialURL:"+curFolder+", File:"+fileName+", Version:"+version);
		    
			    s3Operations.shareFile(from, curFolder, fileName, version, to);
		    }
			else
		    {
		    	 response.sendError(HttpServletResponse.SC_NO_CONTENT, "No important parameter");
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
