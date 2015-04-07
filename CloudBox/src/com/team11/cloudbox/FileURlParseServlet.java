package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * Servlet implementation class FileURlParseServlet
 */
public class FileURlParseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    
	    String fileName= request.getParameter("FileName");
	    String version= request.getParameter("Version");
	    if(fileName==null || version==null)
	    {
	    	 response.sendError(HttpServletResponse.SC_NO_CONTENT, "No important parameter");
	    	 return;
	    }

	    //TBD-Use curFolder+fileName & version & userID to fetch URL from S3
	    String userID = session.getAttribute("userID").toString();
	    String curFolder= (String) session.getAttribute("currentDir");
	    String URL = "#"+version;
	    
		JSONObject returnURl = new JSONObject();
		try {
			
			returnURl.put("url", URL);
		} catch (JSONException e) {
			
			System.err.println("Parse URL error with msg:"+e.getMessage());
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found URL");
			return;
		}
		System.out.println(returnURl.toString());
		response.setContentType("application/json");
		response.getWriter().write(returnURl.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
