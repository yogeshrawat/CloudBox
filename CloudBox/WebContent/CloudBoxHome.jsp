<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%
	boolean fBLogin=true;
	if(session.getAttribute("UserName")== null || session.getAttribute("UserName").equals(""))
	{
	//	response.sendRedirect("Login.jsp");
	  	fBLogin = false;
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
	<title>CloudBox</title>
	<link rel="icon" type="image/png" href="res/favicon.png">
	<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
	<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
	<link type="text/css" rel="stylesheet" href="css/homePageStyle.css" />
</head>
<body>
<script type="text/javascript">
      window.fbAsyncInit = function() {
        FB.init({
          appId      : '421083851380864',
		  cookie     : true,
          xfbml      : true,
          version    : 'v2.2'
        });
      };

      (function(d, s, id){
         var js, fjs = d.getElementsByTagName(s)[0];
         if (d.getElementById(id)) {return;}
         js = d.createElement(s); js.id = id;
         js.src = "//connect.facebook.net/en_US/sdk.js";
         fjs.parentNode.insertBefore(js, fjs);
       }(document, 'script', 'facebook-jssdk'));
	   
</script>
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
		<a id="modal_trigger" href="#modal" class="btn">View your Stuff on CloudBox</a>
	</div>
	
	<div id="fbShareLike">
		<div class="fb-like" data-href="#" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>
	</div>
	
	<div id="logout_btns">
		<div class="one_half"><a href="#" id="logout" class="btn">Logout</a></div>
    	<div class="fb-login-button" data-max-rows="1" data-size="large" data-show-faces="true" data-auto-logout-link="true" onlogin="checkLoginState();" scope="public_profile,email,user_friends"></div>
	</div>
</div>


</body>
</html>
