<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %> 
<%@ page import="java.util.*" %> 
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
		<title>User-Main Page</title>
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
			/*
            out.println("<Folders>");
            for (int a = 0; a < l_Folders.size(); a++) {
                out.println("<Folder>" + l_Folders.elementAt(a).toString() + "</Folder>");
            }
            out.println("</Folders>");            
            out.println("<Files>");
            for (int a = 0; a < l_Files.size(); a++) {
                out.println("<file>" + l_Files.elementAt(a).toString() + "</file>");
            }
            out.println("</Files>"); 
            */
        %>
		<script type="text/javascript">
		function insertTable()
		{
			document.getElementById("DisplayFiles").style.display = 'none';
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
		</script>
		<style>
		#table1
		{
		    border:solid 1px;
		    border-collapse:collapse;
		}
		
		#table1 th
		{
		    border:solid 1px;
		    border-collapse:collapse;
		}
		
		#table1 td
		{
		    border:solid 1px;
		    vertical-align:top;
		}
		
		</style> 
	</head>
	<body>
		<form name="tableForm" class="dynTblForm">
		    <button id="DisplayFiles" type="button" onclick="insertTable()">Create Table</button>
		    <div id="wrapper"></div>
		</form>
	</body>
</html>