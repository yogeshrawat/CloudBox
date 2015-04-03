/**
 * Initialize FB JavaScript SDK and subscribe FB logout event
 */
      window.fbAsyncInit = function() {
        FB.init({
          appId      : '421083851380864',
		  cookie     : true,
          xfbml      : true,
          version    : 'v2.2'
        });
        
        //Additional initialization
  	  	FB.Event.subscribe("auth.logout", function() {
    	  window.location = 'Logout.jsp';
    	  });
        
  	    FB.getLoginStatus(function() {
  		  });
      };

      (function(d, s, id){
         var js, fjs = d.getElementsByTagName(s)[0];
         if (d.getElementById(id)) {return;}
         js = d.createElement(s); js.id = id;
         js.src = "//connect.facebook.net/en_US/sdk.js";
         fjs.parentNode.insertBefore(js, fjs);
       }(document, 'script', 'facebook-jssdk'));