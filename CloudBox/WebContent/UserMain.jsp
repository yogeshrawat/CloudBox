<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %> 
<%@ page import="java.util.*" %>
<%@ page import="com.app.amazonS3.*" %>
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
     <%
     	 String userId = (String) session.getAttribute("userID");
    	 S3Operations s3Operations = new S3Operations();
    	 
    	 String S3BucketHome = s3Operations.getBucketNameFromUserID(userId);
    	 String S3BucketFolder = (String)session.getAttribute("currentDir");
    	 
    	 System.out.println("The user ID:"+userId+", bucket home:"+S3BucketHome+", current folder:"+S3BucketFolder);
    	 
     	 ArrayList<Files> files = s3Operations.getFiles(S3BucketHome, S3BucketFolder);
     	 ArrayList<Folders> folders = s3Operations.getFolders(S3BucketHome, S3BucketFolder);
 		 
     	System.out.println("Inside current folder. The files:"+files.size()+", folders:"+folders.size());
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

		<script type="text/javascript">
		function insertTable()
		{
		    var num_rows = <%=folders.size()+files.size()%>;
		    var num_cols = 7;
		    
		    var folder=new Array();
		    var fileSize=new Array();
		    var fileVersion=new Array();
		    <% for (int i=0; i<folders.size(); i++) { %>
		    	folder[<%= i %>] = '<%= folders.get(i).getName()%>'; 
		    <% } %>
		    
		    <% for (int j=folders.size(); j<files.size()+folders.size(); j++) { %>
	    		folder[<%= j %>] =  '<%=(files.get(j-folders.size()).getFileName())%>'; 
	    		fileSize[<%= j-folders.size() %>] =  <%=(files.get(j-folders.size()).getSize())%>;
	    		fileVersion[<%= j-folders.size() %>] =  <%=(files.get(j-folders.size()).getVersion())%>;
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
		    	if(i>= <%= folders.size()%>)
		    	{
		    		var loc= '<%=S3BucketFolder.isEmpty()?"/":S3BucketFolder%>';
		    		var defaultFileDir;
		    		if(loc != "/")
		    		{
		    			defaultFileDir = "http://travellogenv-5bwgdxfrha.elasticbeanstalk.com/download?"+
		    			 "loc="+loc+
						 "&name="+folder[i]+
 						 "&ver="+(fileVersion[i-<%= folders.size() %>]);
		    		}
		    		else
		    		{
		    			defaultFileDir = "http://travellogenv-5bwgdxfrha.elasticbeanstalk.com/download?"+
		    			 "name="+folder[i]+
 						 "&ver="+(fileVersion[i-<%= folders.size() %>]);
		    		}
		    	}
		    	
		        tbody += "<tr>";
		        for(var j = 0; j < num_cols; j++)
		        {
		        	switch(j)
		        	{
		        		case 0:
				            tbody += "<td class='collapsing' >";
				            if(i < <%=folders.size()%>)
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
				            if(i < <%=folders.size()%>)
			            	{
					            tbody += "<a class='traverseFolder'>"+folder[i]+"</a>";     	
			            	}
			          		else
			            	{
					            tbody += folder[i];	            	
			            	}
				            tbody += "</td>";
				            break;
		        		case 2:
		        			tbody += "<td>";
		        			 if(i >= <%=folders.size()%>)
		        			 {
						            tbody += "<a href='"+defaultFileDir+"' class='ui button download' style='display:none;'>"+
						            		 "	<i class='download blue icon'></i>"+
						            		 "</a>";    	
				             }
				            tbody += "</td>";
		        			break;
		        		case 3:
		        			tbody += "<td>";
		        			if(i >= <%=folders.size()%>)
		        			{
		        				var versionNum = fileVersion[i- <%= folders.size() %>];
		        				var latestVer= versionNum;
		        				var content= "";		        				
		        				for(var k=1; k<=versionNum;k++)
		        				{
		        					content += "<div class='item version number'>"+k+"</div>";
		        				}
		        				
				            	tbody += "<div class='ui left floating dropdown icon button file version' style='display:none;' data-version='"+latestVer+"'>"+
				            	            "<i class='undo icon'></i><p>Version</p>"+
				            	            "<div class='menu'>"+content+"</div>"+
				            	         "</div>";
				            }
				            tbody += "</td>";
		        			break;
		        		case 4:
				            tbody += "<td>";
				            if(i >= <%=folders.size()%>)
				            {
				            	tbody += "<div class='ui right pointing dropdown icon button mainshare' style='display:none;'>"+
				            	            "<i class='share alternate icon'></i><p>share</p>"+
				            	            "<div class='menu'>"+
				            	            "   <div class='item fb share'>"+
				            	            "     <div id='fb-root'>"+
				            	            "		<div class='fb-share-button' data-href='"+defaultFileDir+"' data-layout='button'>"+
				            	            "		</div>"+
				            	            "     </div>"+
				            	            "	</div>"+
				            	            "   <div class='item'>"+
				            	            "		<div class='mini ui button CBShare'>CB Share</div>"+
				            	            "	</div>"+
				            	            "</div>"+
				            	         "</div>";
				            }
				            tbody += "</td>";
				            break;
		        		case 5:
		        			tbody += "<td>";
		        			if(i >= <%=folders.size()%>)
		        			{
		        				var fileCapacity = fileSize[i- <%= folders.size() %>];
					            tbody += "<p>"+fileCapacity+"</p>";
	        				}
				            tbody += "</td>";
		        			break;
		        		case 6:
				            tbody += "<td class='right aligned collapsing'>";
				            if(i >= <%=folders.size()%>)
		        			{
					        	tbody += "<div class='ui button trash' style='display:none;'><i class='trash red icon'></i></div>";
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
		    	
		    	insertTable();
		  	});
		</script>
		<script type="text/javascript" src="js/userfilepage.js"></script>
	
		<div id="logout_btns">
			<div id="logout" class="ui animated button">
		  		<div class="visible content"><i class="sign out icon"></i></div>
		  		<div class="hidden content">Logout</div>
			</div>
			<div id="fb-root">
    			<div class="fb-login-button" data-max-rows="1" data-size="large" data-auto-logout-link="true"></div>
			</div>
		</div>
		
		<div id="home" class="ui animated fade button">
		  <div class="visible content"><i class="home icon"></i></div>
		  <div class="hidden content">Home Page</div>
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
		
		<div class="small ui modal folder">
		  <i class="close icon"></i>
		  <div class="header">
		    New folder name
		  </div>
		  <div class="content">
		    <div class="ui large fluid input folder">
		  	<input placeholder="Email/Full Name" type="text">
		    </div>
		  </div>
		  <div class="actions">
		    <div class="ui button">Cancel</div>
		    <div class="ui button modelfolder">OK</div>
		  </div>
		</div>		
		
		<div class="small ui modal share">
		  <i class="close icon"></i>
		  <div class="header">
		    Share to CloudBox user
		  </div>
		  <div class="content">
		    <div class="ui large fluid input share">
		  	<input placeholder="Email/Full Name" type="text">
		    </div>
		  </div>
		  <div class="actions">
		    <div class="ui button">Cancel</div>
		    <div class="ui button modelshare">OK</div>
		  </div>
		</div>
		
		<div class="ui modal upload">
			  <div class="header">
			    Upload a file to CloudBox
			  </div>
				<div class="ui fluid form segment upload" id="uploadForm" enctype="multipart/form-data" method="post">
				    <div class="field">
				      <label>First Name</label>
				      <input type="file" name="fileToUpload" id="fileToUpload">
				    </div>
				  <div class="ui submit button" onclick="uploadFile()">Upload</div>
				  <div id="progressNumber"></div>
				</div>
		      <div class="actions">
		         <div class="ui button approve green" data-value="easy">Close</div>
		      </div>
		</div>		
		
	</body>
</html>