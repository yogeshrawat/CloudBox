/**
 * Javascript file for userMain.jsp page
 */
$(document).ready(function(){
	$("#logout").click(function(){
		window.location.replace("Logout.jsp");
	});
	
	$("tr").hover(
			function() {
				$(this).find(".ui.button").css("display", "block");
			}, 
			function() {
				$(this).find(".ui.button").css("display", "none");
			}
		);
	$('.dropdown')
  	  .dropdown({
  	    transition: 'drop',
  	  	duration: 500,
	  	delay: {
	  	  show: 200,
	  	  hide: 3000,
	  	  touch: 50
	  	}
  	  });
	
	$("#home").click(function(){
		$.get("FileDisplayServlet", 
				{folder: "/"}, 
				function(){ location.reload();}
		);
	});
	
	$(".traverseFolder").click(function(){
		var subFolder = $(this).closest("a").text();
		$.get("FileDisplayServlet", 
				{folder: subFolder}, 
				function(){ 
					tempAlert("Abou to open folder:"+subFolder, 2500, "green");
					location.reload();
					}
		);
	});
	
	$(".ui.button.CB.Share").click(function(){
		$('.ui.modal')
		  .modal('show')
		;
	});
});

function tempAlert(msg,duration, fontcolor)
{
     var el = document.createElement("div");
    el.setAttribute("style","position:absolute;top:0;left:20%;background-color:white;color:"+fontcolor+";");
     el.innerHTML = msg;
     setTimeout(function(){
      el.parentNode.removeChild(el);
     },duration);
     document.body.appendChild(el);
}

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

