<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%
	if(session.getAttribute("downloadUrl") != null)
	{
		response.sendRedirect("download");
	}

	boolean fBLogin=false;
	if(session.getAttribute("isFBLoggedIn")!=null && session.getAttribute("isFBLoggedIn").equals(true))
	{
	  	fBLogin = true;
	}
	else if(session.getAttribute("isCBLoggedIn")==null || !session.getAttribute("isCBLoggedIn").equals(true))
	{
		response.sendRedirect("Login.jsp");
	}
%>
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
	<title>CloudBox User-Home</title>
	<link rel="icon" type="image/png" href="res/favicon.png">
	<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="js/fblogout.js"></script>
	<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
	<link type="text/css" rel="stylesheet" href="css/homePageStyle.css" />
</head>
<body>
<script type="text/javascript">
	$(document).ready(function(){
		var fBLogin = <%= fBLogin%>;
    	if(fBLogin){
			$(".fb-login-button").show();
			$(".one_half").hide();
		}
    	else{
    		$(".fb-login-button").hide();
			$(".one_half").show();
    	}
  	});
</script>

<div class="container">
	<div id="main">
		<span class="icon"><img class="logo" src="res/favicon.png"></img></span>
		<span class="subtitle">CloudBox<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;Your stuff, anywhere</span>
		<a id="modal_trigger" href="FileDisplayServlet" class="btn">View your Stuff on CloudBox</a>
	</div>
	
	<div id="fbShareLike">
		<div class="fb-like" data-href="http://localhost:8080/CloudBox/CloudBoxHome.jsp" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>
	</div>
	
	<div id="logout_btns">
		<div class="one_half"><a href="Logout.jsp" id="logout"><i class="fa fa-sign-out"></i></a></div>
    	<div class="fb-login-button" data-max-rows="1" data-size="large" data-auto-logout-link="true"></div>
	</div>
</div>

</body>
</html>
