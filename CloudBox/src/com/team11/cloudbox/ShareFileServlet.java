package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.dynamoDb.DynamoSharedURL;

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
			
			return;
		}
		else
		{
		    String targetUser= request.getParameter("User");
			String fileName= request.getParameter("FileName");
		    String version= request.getParameter("Version");
		    
		    if(targetUser !=null && fileName != null && version != null)
		    {   
		    	//Share file via copy file to another target user S3 bucket
			    //TBD
			    String curFolder= (String) session.getAttribute("currentDir");
			    System.out.println("UserID:"+userID+", TargetUser:"+targetUser+", FilePartialURL:"+curFolder+fileName+", Version:"+version);
			    return;
		    }
			else
		    {
		    	 response.sendError(HttpServletResponse.SC_NO_CONTENT, "No important parameter");
		    	 return;
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
