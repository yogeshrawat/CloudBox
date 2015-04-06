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
 * Servlet implementation class FBLoginProcessServlet
 */
public class FBLoginProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		
		String fbUserName = null;
		String fbUserID = null;
		String fbUserEmail = null;
		
	    /////////////////////
	    /*InputStream is = request.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[32];
        int r=0;
        while( r >= 0 ) {
            r = is.read(buf);
            if( r >= 0 ) os.write(buf, 0, r);
        }
        String s = new String(os.toByteArray(), "UTF-8");
        System.out.println(s); 
	    Map<String, String> data = makeQueryMap(s);
	    
	    fbUserName = data.get("UserName");
		fbUserID = data.get("Id");
		fbUserEmail = data.get("Email");*/
	    //////////////////////
		
		fbUserName = request.getParameter("UserName");
		fbUserID = request.getParameter("Id");
		fbUserEmail = request.getParameter("Email");
		
		System.out.println(fbUserName+","+fbUserID+","+fbUserEmail);
		
		/*JSONArray friends = null;
		HashMap<String, String> friendList = new HashMap<String, String>();	
		try {
			
			friends = new JSONArray(request.getParameter("userInfo[Friends]"));
			for(int i =0; i<friends.length(); i++)
			{
				JSONObject jsonobject = friends.getJSONObject(i);
			    String name = jsonobject.getString("name");
			    String id = jsonobject.getString("id");
			    
				friendList.put(name, id);
			}
			
			System.out.println(friendList);
			
		} catch (JSONException e) {
			
			System.err.println("Parse friends list error with msg:"+e.getMessage());
		}*/
		
		if(fbUserName != null && fbUserID !=null)
		{
			session.setAttribute("isFBLoggedIn", true);
			
			JSONObject redirectURL = new JSONObject();
			try {
				
				redirectURL.put("redirectURL", "CloudBoxHome.jsp");
			} catch (JSONException e) {
				
				System.err.println("Parse response redirect URL error with msg:"+e.getMessage());
			}
			System.out.println(redirectURL.toString());
			response.setContentType("application/json");
			response.getWriter().write(redirectURL.toString());
		}
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
