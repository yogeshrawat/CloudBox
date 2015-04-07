package com.team11.cloudbox;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	    	File newSubFolder = new File(curFolder+newFolderName);
	    	if(!newSubFolder.exists())
	    	{
	    		if(newSubFolder.mkdir())
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
