/**
 * Javascript file for userMain.jsp page
 */
$(document).ready(function(){
	$("#logout").click(function(){
		window.location.replace("Logout.jsp");
	});
	
	$("#home").click(function(){
		$.get("FileDisplayServlet", 
				{folder: "/"}, 
				function(){ location.reload();}
		);
	});
});

//function updateQueryStringParameter(uri, key, value) {
//	  var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
//	  var separator = uri.indexOf('?') !== -1 ? "&" : "?";
//	  if (uri.match(re)) {
//	    return uri.replace(re, '$1' + key + "=" + value + '$2');
//	  }
//	  else {
//	    return uri + separator + key + "=" + value;
//	  }
//}

