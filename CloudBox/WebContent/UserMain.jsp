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
		<script type="text/javascript" src="js/fblogout.js"></script>
		<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
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
            Vector<String> l_Files = new Vector<String>(), l_Folders = new Vector<String>();
            GetDirectory("/home/leon/Documents/SOEN6441/", l_Files, l_Folders);
        %>
		<script type="text/javascript">
		function insertTable()
		{
		    var num_rows = <%=l_Folders.size()+l_Files.size()%>;
		    var num_cols = 4;
		    
		    var folder=new Array();
		    <% for (int i=0; i<l_Folders.size(); i++) { %>
		    	folder[<%= i %>] = "<%=l_Folders.elementAt(i).toString()%>"; 
		    <% } %>
		    
		    <% for (int j=l_Folders.size(); j<l_Files.size()+l_Folders.size(); j++) { %>
	    		folder[<%= j %>] = "<%=l_Files.elementAt(j-l_Folders.size()).toString()%>"; 
	    	<% } %>		 
	    	
		    var width = 100;
		    
		    var theader = "<table id='table1' width = ' "+ width +"% '>";
		    for(var j = 0; j < num_cols; j++)
		    {
			    
	        	switch(j)
	        	{
	        		case 0:
	        			theader += "<th>Type </th>";
			            break;
	        		case 1:
	        			theader += "<th>Name </th>";
			            break;
	        		case 2:
	        			theader += "<th>Share </th>";
			            break;
	        		case 3:
	        			theader += "<th>Delete </th>";
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
				            tbody += "<td>";
				            if(i < <%=l_Folders.size()%>)
				            	{
					       	     	tbody += "<a href='#'><i class='fa fa-folder'></i></a>";      	
				            	}
				            else
				            	{
					            	tbody += "<a href='#'><i class='fa fa-file'></i></a>";	            	
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
				            tbody += "<a href='#'><i class='fa fa-link'></i></a>";
				            tbody += "</td>";
				            break;
		        		case 3:
				            tbody += "<td>";
				            tbody += "<a href='#'><i class='fa fa-times'></i></a>";
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
				$(".one_half").hide();
			}
	    	else{
	    		$(".fb-login-button").hide();
				$(".one_half").show();
	    	}
	  	});
	</script>
		<div id="logout_btns">
			<div class="one_half"><a href="Logout.jsp" id="logout" class="btn">Logout</a></div>
    		<div class="fb-login-button" data-max-rows="1" data-size="large" data-auto-logout-link="true"></div>
		</div>
		<form name="tableForm" class="dynTblForm">
		   <!--  <button id="DisplayFiles" type="button" onclick="insertTable()">Create Table</button> -->
		    <div id="wrapper"></div>
		</form>
	</body>
</html>