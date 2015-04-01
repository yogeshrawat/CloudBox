package com.team11.cloudbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CBRegServlet
 */
public class CBRegServlet extends HttpServlet {
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
		
		String cbUserName = request.getParameter("UserName");
		String cbUserPwd= request.getParameter("Password");
		String cbUserEmail = request.getParameter("Email");
		
		System.out.println(cbUserName+","+cbUserPwd+","+cbUserEmail);
		
		if(cbUserName!=null && cbUserPwd !=null && cbUserEmail!= null)
		{
			session.setAttribute("isCBLoggedIn", true);
			response.sendRedirect("CloudBoxHome.jsp");
		}
	}

}
