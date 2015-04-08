package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.amazonS3.S3Folder;
import com.app.dynamoDb.DynamoFacebookUsers;

/**
 * Servlet implementation class FBLoginProcessServlet
 */
public class FBLoginProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	S3Folder s3Obj = new S3Folder();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		
		String fbUserName = "";
		String fbUserID = "";
		String fbUserEmail = "";
		
		fbUserName = request.getParameter("UserName");
		fbUserID = request.getParameter("Id");
		fbUserEmail = request.getParameter("Email");
		
		System.out.println(fbUserName+","+fbUserID+","+fbUserEmail);
		
		if(!fbUserName.isEmpty() && !fbUserID.isEmpty())
		{	
			DynamoFacebookUsers dynamoFbUser = new DynamoFacebookUsers();
			
			if(!dynamoFbUser.isExist(fbUserID))
			{
				dynamoFbUser.insert(fbUserID, fbUserName, fbUserEmail);
				
				s3Obj.createRootBucket(fbUserID);
			}	
			
			session.setAttribute("userID", fbUserID);
			session.setAttribute("isFBLoggedIn", true);
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/*private static Map<String, String> makeQueryMap(String query) throws UnsupportedEncodingException {
	    String[] params = query.split("&");
	    Map<String, String> map = new HashMap<String, String>();
	    for( String param : params ) {
	        String[] split = param.split("=");
	        map.put(URLDecoder.decode(split[0], "UTF-8"), URLDecoder.decode(split[1], "UTF-8"));
	    }
	    return map;
	}*/
}
