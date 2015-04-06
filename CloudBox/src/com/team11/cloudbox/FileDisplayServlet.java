package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		    String userRoot= "/home/leon/Documents/SOEN6441/";
		    String curFolder= (String) session.getAttribute("currentDir");
		    
		    String subFolderName= request.getParameter("folder");
		    if(subFolderName==null)
		    {
		    	 session.setAttribute("currentDir", userRoot);
		    }
		    else
		    {		    	 
		    	 session.setAttribute("currentDir", curFolder + subFolderName + "/");
		    }
		    
		}else{
		    nextPage = "Login.jsp";
		}
		
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "must-revalidate");
		response.setDateHeader("Expires",0);
		response.sendRedirect(nextPage);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	}

}