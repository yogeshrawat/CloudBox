package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.app.amazonS3.S3Operations;

/**
 * Servlet implementation class FileURlParseServlet
 */
public class FileURlParseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected String downloadEntry = null;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		this.downloadEntry = config.getInitParameter("downloadEntry");
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		String userId = (String) session.getAttribute("userID");	    	
    	S3Operations s3Operations = new S3Operations();
    	String s3BucketHome = s3Operations.getBucketNameFromUserID(userId);
	    
	    String fileName= request.getParameter("FileName");
	    String version= request.getParameter("Version");
	    if(fileName==null || version==null)
	    {
	    	 response.sendError(HttpServletResponse.SC_NO_CONTENT, "No important parameter");
	    	 return;
	    }

	    String curFolder= (String) session.getAttribute("currentDir");
	    String folderUrl = "";
	    if(!curFolder.isEmpty())
	    	 folderUrl = urlSpecialCharReplace(curFolder);
	    String fileUrl = urlSpecialCharReplace(fileName);
	    String verUrl = urlSpecialCharReplace(version);
	    
	    String URL="";
	    if(!curFolder.isEmpty())
	    	URL = downloadEntry+"?"+"loc="+folderUrl+"&name="+fileUrl+"&ver="+verUrl+"&defaultDir="+s3BucketHome;
	    else
	    	URL = downloadEntry+"?"+"name="+fileUrl+"&ver="+verUrl+"&defaultDir="+s3BucketHome;
	    
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
	
	private String urlSpecialCharReplace(String rawUrl)
	{
		String result="";
		StringBuilder sb = new StringBuilder(rawUrl);
		
		for (int index = 0; index < sb.length(); index++) {
			
		    switch(sb.charAt(index))
		    {
		    case '/':
		    	sb.replace(index, index+1, "%2F");
		    	break;
		    case ' ':
		    	sb.replace(index, index+1, "%20");
		    	break;
		    case '.':
		    	sb.replace(index, index+1, "%2E");
		    	break;
		    case '?':
		    	sb.replace(index, index+1, "%3F");
		    	break;
		    case '&':
		    	sb.replace(index, index+1, "%26");
		    	break;
		    }
		}
		
		result = sb.toString();
		
		return result;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
