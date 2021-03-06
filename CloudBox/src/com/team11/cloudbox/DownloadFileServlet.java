package com.team11.cloudbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.amazonS3.S3Operations;
import com.app.dynamoDb.DynamoSharedURL;

/**
 * Servlet implementation class DownloadFileServlet
 */
public class DownloadFileServlet extends HttpServlet {
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
		boolean isCredentialValid;
		HttpSession session = request.getSession(true);
    	String s3BucketHome = "";
		
		if(((session.getAttribute("isFBLoggedIn")!=null && session.getAttribute("isFBLoggedIn").equals(true)))
				||((session.getAttribute("isCBLoggedIn") != null && session.getAttribute("isCBLoggedIn").equals(true))))
		{
			isCredentialValid = true;
		}
		else
		{
			isCredentialValid = false;
		}
				
		if(isCredentialValid)
		{
			String folder="";
			String file="";
			String version = "";
			if(session.getAttribute("downloadUrl") != null)
			{
				//Download from shared link
				String queryUrl=(String) session.getAttribute("downloadUrl");
				if(!queryUrl.isEmpty())
				{
					//Check if URL in sharedTable
					String URLOfSDownload = downloadEntry+"?"+queryUrl;
					DynamoSharedURL  dynamoSharetable = new DynamoSharedURL();
					if(!dynamoSharetable.isExist(URLOfSDownload))
					{
						session.removeAttribute("downloadUrl");
						return;
					}
					
					String[] parameters = queryUrl.split("&");
					if(parameters.length >= 4)
					{
						folder = urlSpecialCharRevert(parameters[0].substring(parameters[0].indexOf('=')+1));
						file  = urlSpecialCharRevert(parameters[1].substring(parameters[1].indexOf('=')+1));
						version = urlSpecialCharRevert(parameters[2].substring(parameters[2].indexOf('=')+1));
						s3BucketHome = urlSpecialCharRevert(parameters[3].substring(parameters[3].indexOf('=')+1));
					}
					else if(parameters.length >= 3)
						{
							file  = urlSpecialCharRevert(parameters[0].substring(parameters[0].indexOf('=')+1));
							version = urlSpecialCharRevert(parameters[1].substring(parameters[1].indexOf('=')+1));
							s3BucketHome = urlSpecialCharRevert(parameters[2].substring(parameters[2].indexOf('=')+1));
						}
						else
						{
							session.removeAttribute("downloadUrl");
					    	response.sendError(HttpServletResponse.SC_NO_CONTENT, "No enough parameters");
					    	return;
						}
				}
				else
				{
					session.removeAttribute("downloadUrl");
			    	response.sendError(HttpServletResponse.SC_NO_CONTENT, "No important parameter");
					return;
				}
				
				session.removeAttribute("downloadUrl");
			}
			else
			{
				//Normal download
				if(request.getParameter("loc") != null)
				{
					folder=(String) request.getParameter("loc");
				}
				file=(String) request.getParameter("name");
				version = (String) request.getParameter("ver");
				s3BucketHome = (String) request.getParameter("defaultDir");
				System.out.println(folder+","+file+","+version+","+s3BucketHome);
			}
			
			//Download logic
	    	S3Operations s3Operations = new S3Operations();
	    	InputStream inStream = s3Operations.downloadFile(s3BucketHome, folder, file, version);
	    	int fileLength = s3Operations.getInputStreamLength();
	        
			response.setContentType("application/octet-stream");	        
	        response.setContentLength(fileLength);
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"", file);
	        response.setHeader(headerKey, headerValue);
	        
	        OutputStream outStream = response.getOutputStream();
	         
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	         
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	         
	        inStream.close();
	        outStream.close();  
			
		}
		else
		{
			session.setAttribute("downloadUrl", request.getQueryString());
			System.out.println("Add download Url session attribute="+request.getQueryString());
		    String nextPage = "Login.jsp";
		    response.sendRedirect(nextPage);
		}
		
	}
	
	private String urlSpecialCharRevert(String rawUrl)
	{
		String result="";
		StringBuilder sb = new StringBuilder(rawUrl);
		
		revertAll(sb, "%2F", "/");
		revertAll(sb, "%20", " ");
		revertAll(sb, "%2E", ".");
		revertAll(sb, "%2E", "?");
		revertAll(sb, "%26", "&");
		
		result = sb.toString();
		
		return result;
	}
	
	private void revertAll(StringBuilder builder, String from, String to)
	{
	    int index = builder.indexOf(from);
	    while (index != -1)
	    {
	        builder.replace(index, index + from.length(), to);
	        index += to.length();
	        index = builder.indexOf(from, index);
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
