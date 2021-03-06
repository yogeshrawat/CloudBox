<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  
<%
	if((session.getAttribute("isFBLoggedIn")!=null && session.getAttribute("isFBLoggedIn").equals(true))
			||(session.getAttribute("isCBLoggedIn") != null && session.getAttribute("isCBLoggedIn").equals(true)))
	{
		response.sendRedirect("CloudBoxHome.jsp");
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
	<title>CloudBox Login</title>
	<link rel="icon" type="image/png" href="res/favicon.png">
	<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="js/jquery.leanModal.min.js"></script>
	<script type="text/javascript" src="js/gen_validatorv4.js"></script>
	<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
	<link type="text/css" rel="stylesheet" href="css/loginStyle.css" />
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
      
  // This is called with the results from from FB.getLoginStatus().
  function statusChangeCallback(response) {
    console.log(response);
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // fr FB.getLoginStatus().
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      fetchUserFBInfo();
    }
  }

  // This function is called when someone finishes with the Login
  // Button.  See the onlogin handler attached to it in the sample
  // code below.
  function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
  }
  
  var FBUserName, FbUserID, FBUserEmail;
  // Send user name and id as well as friends list to server
  function fetchUserFBInfo() {
    FB.api('/me', function(response) {
	  console.log(JSON.stringify(response));
	  //Set var userName, fbID, userEmail
	  FBUserName = response.name;
	  FbUserID = response.id;
	  FBUserEmail = response.email;
	  console.log(FBUserName+","+FbUserID+","+FBUserEmail);
	  
		$.get("FBLoginProcessServlet", 
				{UserName:FBUserName, Id:FbUserID,Email:FBUserEmail}, 
				function(){ 
					window.location.replace("CloudBoxHome.jsp");
				}
		);
	  
    });
    
	/* FB.api(
		"/me/friends",
		function (response) {
		  if (response && !response.error) {
			console.log(JSON.stringify(response.data));
			
			//Fetch each Friend-Name and ID send to server - ajax
			  var sendInfo = {
							UserName:FBUserName, 
							Id:FbUserID, 
							Email:FBUserEmail
						   }; 
			  $.ajax({
	                url:"FBLoginProcessServlet",
	                type:"POST",
	                dataType:'json',
	                data:sendInfo,
	                success:function(data){
	                	window.location.replace(data.redirectURL);
	                }
	               }); 	
		  }
		}
	); */
  }   
</script>

<div class="container">
	<div id="main">
		<span class="icon"><img class="logo" src="res/favicon.png"></img></span>
		<span class="subtitle">CloudBox<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;Your stuff, anywhere</span>
		<a id="modal_trigger" href="#modal" class="btn">Click here to Login or register</a>
	</div>
	
	<div id="modal" class="popupContainer" style="display:none;">
		<header class="popupHeader">
			<span class="header_title">Login</span>
			<span class="modal_close"><i class="fa fa-times"></i></span>
		</header>
		
		<section class="popupBody">
			<!-- Social Login -->
			<div class="social_login">
				<div class="">
					<a href="#" id="fb_form" class="social_box fb">
						<span class="icon"><i class="fa fa-facebook"></i></span>
						<span class="icon_title">Connect with Facebook</span>	
					</a>
				</div>

				<div class="centeredText">
					<span>Or use your Email address</span>
				</div>

				<div class="action_btns">
					<div class="one_half"><a href="#" id="login_form" class="btn">Login</a></div>
					<div class="one_half last"><a href="#" id="register_form" class="btn">Sign up</a></div>
				</div>
			</div>
			
			<!-- Username & Password Login form -->
			<div class="user_login">
				<form name="LoginForm" action="CBLoginServlet" method="post">
					<label>Email / Username</label>
					<input type="text" name="EmailOrUsername" />
					<br />
					<label>Password</label>
					<input type="password" name="Password" />
					<br />
					<div class="action_btns">
						<div class="one_half"><a href="#" class="btn back_btn"><i class="fa fa-angle-double-left"></i> Back</a></div>
						<div class="one_half last"><a href="javascript: submitLogin()" class="btn btn_red">Login</a></div>
					</div>
				</form>
			</div>

			<!-- Register Form -->
			<div class="user_register">
				<form name="RegisterForm" action="CBRegServlet" method="post">
					<label>Full Name</label>
					<input type="text" name="UserName" />
					<br />
					<label>Email Address</label>
					<input type="email" name="Email" />
					<br />
					<label>Password</label>
					<input type="password" name="Password" />
					<br />
					<div class="action_btns">
						<div class="one_half"><a href="#" class="btn back_btn"><i class="fa fa-angle-double-left"></i> Back</a></div>
						<div class="one_half last"><a href="javascript: submitRegister()" class="btn btn_red">Register</a></div>
					</div>
				</form>
			</div>
			
			<!--Facebook login-->
			<div id="fb-root" class="fb_login">
				<form>
					<div class="fb-login-button" data-max-rows="1" data-size="large" data-show-faces="true" data-auto-logout-link="true" onlogin="checkLoginState();" scope="public_profile,email,user_friends"></div>
					<div class="action_btns">
						<div class="one_half"><a href="#" class="btn back_btn"><i class="fa fa-angle-double-left"></i> Back</a></div>
					</div>
				</form>
			</div>
			
		</section>
	</div>
</div>

<script type="text/javascript">
	var loginFormValidator = new Validator("LoginForm");
	loginFormValidator.addValidation("EmailOrUsername","req", 
	"Please enter the full name or email");
	loginFormValidator.addValidation("Password","req", 
			"Please enter the password");
	loginFormValidator.addValidation("Password", "alnum", 
			"Password can only include alpha-number");
	
	function submitLogin()
	{
	  if(document.LoginForm.onsubmit())
	  {
	    document.LoginForm.submit();
	  }
	}

	var registerformValidator = new Validator("RegisterForm");
	registerformValidator.addValidation("UserName","req", 
			"Please enter the full name");
	registerformValidator.addValidation("UserName", "alnum_s", 
			"Full name can only include alpha-number and a space");
	registerformValidator.addValidation("Email","req", 
			"Please enter the email address");
	registerformValidator.addValidation("Email", "email", 
			"Email should follow email format");
	registerformValidator.addValidation("Password","req", 
			"Please enter the password");
	registerformValidator.addValidation("Password", "alnum", 
			"Password can only include alpha-number");
	
	function submitRegister()
	{
	  if(document.RegisterForm.onsubmit())
	  {
	    document.RegisterForm.submit();
	  }
	}
</script>

<script type="text/javascript">
	$("#modal_trigger").leanModal({top : 200, overlay : 0.6, closeButton: ".modal_close" });
	
	$(function(){
		// Calling Login Form
		$("#login_form").click(function(){
			$(".social_login").hide();
			$(".user_login").show();
			return false;
		});

		// Calling Register Form
		$("#register_form").click(function(){
			$(".social_login").hide();
			$(".user_register").show();
			$(".header_title").text('Register');
			return false;
		});

		// Going back to Social Forms
		$(".back_btn").click(function(){
			$(".user_login").hide();
			$(".user_register").hide();
			$(".fb_login").hide();
			$(".social_login").show();
			$(".header_title").text('Login');
			return false;
		});
		
		// Calling Facebook Login
		$("#fb_form").click(function(){
			$(".social_login").hide();
			$(".fb_login").show();
			return false;			
		});
	})
</script>

</body>
</html>
