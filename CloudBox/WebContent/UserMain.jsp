<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %> 
<%@ page import="java.util.*" %>
<%
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
		<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
		<script type="text/javascript" src="js/semantic.js"></script>
		<script type="text/javascript" src="js/fblogout.js"></script>
		<link type="text/css" rel="stylesheet" href="css/semantic.css" />
		<link type="text/css" rel="stylesheet" href="css/usermain.css" />
		<title>CloudBox User-Main</title>
		<%!        
			public void GetDirectory(String a_Path, Vector<String> a_files, Vector<String> a_folders) {
			    File l_Directory = new File(a_Path);
			    File[] l_files = l_Directory.listFiles();
			
			    for (int c = 0; c < l_files.length; c++) {
			        if (l_files[c].isDirectory()) {
			            a_folders.add(l_files[c].getName());
			        } else {
			            a_files.add(l_files[c].getName());
			        }
			    }
			}
		 %>
        <%
        	String S3BucketRoot = "/home/leon/Documents/SOEN6441/";
            Vector<String> l_Files = new Vector<String>(), l_Folders = new Vector<String>();
            GetDirectory(S3BucketRoot, l_Files, l_Folders);
        %>
		<script type="text/javascript">
		function insertTable()
		{
		    var num_rows = <%=l_Folders.size()+l_Files.size()%>;
		    var num_cols = 7;
		    
		    var folder=new Array();
		    <% for (int i=0; i<l_Folders.size(); i++) { %>
		    	folder[<%= i %>] = "<%=l_Folders.elementAt(i).toString()%>"; 
		    <% } %>
		    
		    <% for (int j=l_Folders.size(); j<l_Files.size()+l_Folders.size(); j++) { %>
	    		folder[<%= j %>] = "<%=l_Files.elementAt(j-l_Folders.size()).toString()%>"; 
	    	<% } %>		 
		    
		    var theader = "<table id='table1' class='ui celled striped table'>";
		    for(var j = 0; j < num_cols; j++)
		    {
			    
	        	switch(j)
	        	{
	        		case 0:
	        			theader += "<th></th>";
			            break; 
	        		case 1:
	        			theader += "<th><p>Name</p></th>";
			            break;
	        		case 2:
	        		case 3:
	        		case 4:
	        			theader += "<th></th>";
			            break;
	        		case 5:
	        			theader += "<th><p>Size</p></th>";
			            break;
	        		case 6:
	        			theader += "<th></th>";
			            break;    
	        	}
		    }
		
		    var tbody = "";
		    for(var i = 0; i < num_rows; i++)
		    {
		        tbody += "<tr>";
		        for(var j = 0; j < num_cols; j++)
		        {
		        	switch(j)
		        	{
		        		case 0:
				            tbody += "<td class='collapsing' >";
				            if(i < <%=l_Folders.size()%>)
				            	{
					       	     	tbody += "<i class='folder icon' ></i>";      	
				            	}
				            else
				            	{
					            	tbody += "<i class='file outline icon' ></i>";	            	
				            	}
				            tbody += "</td>";
				            break;
		        		case 1:
				            tbody += "<td>";
				            if(i < <%=l_Folders.size()%>)
			            	{
					            tbody += "<a href='#'>"+folder[i]+"</a>";     	
			            	}
			          		else
			            	{
					            tbody += folder[i];	            	
			            	}
				            tbody += "</td>";
				            break;
		        		case 2:
		        			tbody += "<td>";
		        			 if(i >= <%=l_Folders.size()%>)
		        			 {
						            tbody += "<div class='ui button' style='display:none;'><i class='download blue icon'></i></div>";    	
				             }
				            tbody += "</td>";
		        			break;
		        		case 3:
		        			tbody += "<td>";
		        			if(i >= <%=l_Folders.size()%>)
		        			{
				            	tbody += "<div class='ui floating dropdown icon button' style='display:none;'>"+
				            	            "<i class='undo icon'></i><p>Version</p>"+
				            	            "<div class='menu'>"+
				            	            "	<div class='header'>Versions</div>"+
				            	            "   <div class='item'>v1</div>"+
				            	            "   <div class='item'>v2</div>"+
				            	            "</div>"+
				            	         "</div>";
				            }
				            tbody += "</td>";
		        			break;
		        		case 4:
				            tbody += "<td>";
				            if(i >= <%=l_Folders.size()%>)
				            {
				            	tbody += "<div class='ui floating dropdown icon button' style='display:none;'>"+
				            	            "<i class='share alternate icon'></i><p>share</p>"+
				            	            "<div class='menu'>"+
				            	            "	<div class='header'>Share Method</div>"+
				            	            "   <div class='item'>FB</div>"+
				            	            "   <div class='item'>CB</div>"+
				            	            "</div>"+
				            	         "</div>";
				            }
				            tbody += "</td>";
				            break;
		        		case 5:
		        			tbody += "<td>";
		        			if(i >= <%=l_Folders.size()%>)
		        			{
					            tbody += "<p>3Mb</p>";
	        				}
				            tbody += "</td>";
		        			break;
		        		case 6:
				            tbody += "<td class='right aligned collapsing'>";
				            if(i >= <%=l_Folders.size()%>)
				            {
					            tbody += "<div class='ui button' style='display:none;'><i class='trash red icon'></i></div>";
			            	}
				            tbody += "</td>";
				            break;
		        	}

		        }
		        tbody += "</tr>";
		    }
		    
		    var tfooter = "</table>";
		    document.getElementById('wrapper').innerHTML = theader + tbody + tfooter;
		}
		
		$(document).ready(insertTable);
		</script>
	</head>
	<body>
	<script type="text/javascript">
		$(document).ready(function(){
			var fBLogin = <%= fBLogin%>;
	    	if(fBLogin){
				$(".fb-login-button").show();
				$("#logout").hide();
			}
	    	else{
	    		$(".fb-login-button").hide();
				$("#logout").show();
	    	}
	  	});
		
		$(document).ready(function(){
			$("tr").hover(
				function() {
					$(this).find(".ui.button").css("display", "block");
					//$(this).find("td:nth-child(3) a:first-child").css("display", "block");
				}, 
				function() {
					$(this).find(".ui.button").css("display", "none");
				}
			);
			
			$('.dropdown')
			  .dropdown({
			    transition: 'drop'
			  });
					
			$("#logout").click(function(){
				window.location.replace("Logout.jsp");
			});
		
		});
		
	</script>
	
		<div id="logout_btns">
			<div id="logout" class="ui animated button">
		  		<div class="visible content"><i class="sign out icon"></i></div>
		  		<div class="hidden content">Logout</div>
			</div>
    		<div class="fb-login-button" data-max-rows="1" data-size="large" data-auto-logout-link="true"></div>
		</div>
		
		<div id="newfolder" class="ui animated fade button">
		  <div class="visible content">New</div>
		  <div class="hidden content">Folder</div>
		</div>
		<div id="upload" class="ui animated button">
		  <div class="visible content"><i class="upload icon"></i></div>
		  <div class="hidden content">Select a File</div>
		</div>
		
		<form name="tableForm" class="dynTblForm">
		    <div id="wrapper"></div>
		</form>
		
	</body>
</html>