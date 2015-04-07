/**
 * Javascript file for userMain.jsp page
 */

//Global variable
var curFocusedFile;
var curFocuedFileVersion;

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
				  tempAlert("About to open folder:"+subFolder, 2500, "green");
				  location.reload();
				}
		);
	});
	
	$("#newfolder").click(function(){
		$('.ui.modal.folder')
		.modal('show');
	});
	
	$(".ui.button.modelfolder").click(function(){
		var folderName = $(".input.folder").children("input:first-child").val();
		$.get("CreateFolderServlet", 
				{NewFolder: folderName}, 
				function(){ 
				  tempAlert("Created new folder:"+folderName, 2500, "green");
				  location.reload();
				}
		);		
	});
	
	//TBD-Place marker for upload
	
	$(".ui.button.download").click(function(){
		//TBD
	});
	
	$(".item.version.number").click(function(){
		var targetVersion = $(this).closest("div").text();
		var div= $(this).closest("td").find(".file.version")[0];
		console.log("Version is:" + div.getAttribute("data-version"));
		div.dataset.version = targetVersion;
		console.log("Current version is:" + div.dataset.version);
		
		var file = $(this).closest("tr").children("td:nth-child(2)").text();
		var fbShare = $(this).closest("td").next().find(".fb-share-button")[0];
		var updatedURL="";
		$.get("FileURlParseServlet", 
				{FileName: file, Version: targetVersion}, 
				function(data){ 
					if(data != null) updatedURL=data.url;
					
					//Set the actual file link to FB share					
					console.log("href is:" + fbShare.getAttribute("data-href"));
					fbShare.dataset.href = updatedURL;
					console.log("Current href is:" + fbShare.dataset.href);
				}
		);
		
	});
	
	$(".ui.button.CBShare").click(function(){
		$('.ui.modal.share')
		  .modal('show');
		
		curFocusedFile = $(this).closest("tr").children("td:nth-child(2)").text();
		console.log("File focused:"+curFocusedFile);
		curFocuedFileVersion = $(this).closest("tr").find(":nth-child(4)").find(".file.version")[0].dataset.version;		
		console.log("File version focused:"+curFocuedFileVersion);
	});
	
	$(".ui.button.modelshare").click(function(){
		//Obtain target client
		var sharetoUser = $(".input.share").children("input:first-child").val();
		
		//Share file via copy file to another target user S3 bucket
		$.get("ShareFileServlet", 
				{User: sharetoUser, FileName: curFocusedFile, Version: curFocuedFileVersion}, 
				function(){ 
				  tempAlert("Shared a file:"+curFocusedFile, 2500, "green");
				}
		);
		
	});
	
	$(".ui.button.mainshare").click(function(){
		//Get the file URL that will be shared
		var fbShare = $(this).find(".fb-share-button")[0];
		var fileURl = fbShare.dataset.href;
		console.log("FB will share href="+fileURl);
		
		//Add file link to database for access restriction
		$.get("ShareFileServlet", 
				{URL: fileURl}, 
				function(){ 
				  tempAlert("Track a file sharing", 2500, "green");
				}
		);
	});
		
	$(".ui.button.trash").click(function(){
		var td = $(this).closest("tr").children("td:nth-child(2)");
		var a = td.children("a:first-child");
		if(td.find("a")[0] == null)
			{
			  var fileName = td.text();
			  $.get("RemoveServlet", 
						{FileName: fileName}, 
						function(){ 
						  tempAlert("Removed file:"+folderName, 2500, "green");
						  location.reload();
						}
				);
			}
		else
			{
				var folderName = a.text();
				$.get("RemoveServlet", 
						{Folder: folderName}, 
						function(){ 
						  tempAlert("Removed folder:"+folderName, 2500, "green");
						  location.reload();
						}
				);
			}

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

