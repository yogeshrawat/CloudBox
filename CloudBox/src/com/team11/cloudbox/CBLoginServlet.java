package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.dynamoDb.DynamoUser;

/**
 * Servlet implementation class CBLoginServlet
 */
public class CBLoginServlet extends HttpServlet {
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
		
		String cbEmailOrUserName = request.getParameter("EmailOrUsername");
		String cbUserPwd= request.getParameter("Password");
		
		System.out.println(cbEmailOrUserName+","+cbUserPwd);

		if(cbEmailOrUserName!=null && cbUserPwd !=null)
		{
			DynamoUser  user= new DynamoUser();
			
			if(cbEmailOrUserName.contains("@"))
			{

				if(user.validateEmail(cbEmailOrUserName, cbUserPwd))
				{
					session.setAttribute("userID", user.getUserIDfromUserName(cbEmailOrUserName));
					session.setAttribute("isCBLoggedIn", true);
					response.sendRedirect("CloudBoxHome.jsp");
				}
				else
				{
					response.sendRedirect("Login.jsp");
				}
				
			}
			else
			{
				if(user.validateName(cbEmailOrUserName, cbUserPwd))
				{
					session.setAttribute("userID", user.getUserIDfromUserName(cbEmailOrUserName));
					session.setAttribute("isCBLoggedIn", true);
					response.sendRedirect("CloudBoxHome.jsp");
				}
				else
				{
					response.sendRedirect("Login.jsp");
				}

			}
		}
		else
		{
			response.sendRedirect("Login.jsp");
		}

	}

}
