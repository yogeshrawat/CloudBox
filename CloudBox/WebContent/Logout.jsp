<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "must-revalidate");
	response.setDateHeader("Expires",0);
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>CloudBox Logout</title>
	<link rel="icon" type="image/png" href="res/favicon.png">
	<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
	<link type="text/css" rel="stylesheet" href="css/logoutStyle.css" />
</head>
<body>
	<%
		session.invalidate();
    %>
	<div id="main">
		<span class="icon"><img class="logo" src="res/favicon.png"></img></span>
		<span class="subtitle">CloudBox<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;Your stuff, anywhere</span>
		<a id="modal_trigger" href="Login.jsp" class="btn">Go-Back To Login Page</a>
	</div>	
	<h1><font color="#FF176D">You are Successfully logged out...</font></h1>
</body>
</html>