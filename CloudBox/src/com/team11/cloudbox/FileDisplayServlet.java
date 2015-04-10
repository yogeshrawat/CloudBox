package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.amazonS3.S3Operations;

/**
 * Servlet implementation class FileDisplayServlet
 */
public class FileDisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isCredentialValid;
		HttpSession session = request.getSession(true);
		
		if((session.getAttribute("isFBLoggedIn")!=null && session.getAttribute("isFBLoggedIn").equals(true))
				||(session.getAttribute("isCBLoggedIn") != null && session.getAttribute("isCBLoggedIn").equals(true)))
		{
			isCredentialValid = true;
		}
		else
		{
			isCredentialValid = false;
		}
		
		String nextPage ="";
		
		if(isCredentialValid){
		    nextPage = "UserMain.jsp";
		    
		   /*String userId = (String) session.getAttribute("userID");
		    S3Operations s3AccessObject = new S3Operations();
		    String userRoot = s3AccessObject.getBucketNameFromUserID(userId);*/
		    String userRoot ="";
		    
		    String curFolder= (String) session.getAttribute("currentDir");
		    
		    String subFolderName= request.getParameter("folder");
		    if(subFolderName==null)
		    {
		    	 session.setAttribute("currentDir", userRoot);
		    }
		    else
		    	if(subFolderName.compareTo("/")==0)
		    	{
		    		session.setAttribute("currentDir", userRoot);
		    		return;
		    	}
			    else
			    {	if(curFolder.isEmpty())
				    {
			    		session.setAttribute("currentDir", subFolderName);
				    }
				    else
				    {
				    	session.setAttribute("currentDir", curFolder +"/"+ subFolderName);
				    }
			    
			    	return;
			    }
		    
		}
		else
		{
		    nextPage = "Login.jsp";
		}
		
		response.sendRedirect(nextPage);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	}
}
