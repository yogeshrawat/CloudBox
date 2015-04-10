package com.team11.cloudbox;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.amazonS3.S3Operations;

/**
 * Servlet implementation class RemoveServlet
 */
public class RemoveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    String curFolder= (String) session.getAttribute("currentDir");
	    
	    /*String FolderName= request.getParameter("Folder");
	    System.out.println("FolderName=" + FolderName);*/
	    String FileName = request.getParameter("FileName");
	    System.out.println("FileName=" + FileName);
	    String FileVersion = request.getParameter("FileVersion");
	    System.out.println("FileVersion=" + FileVersion);

	    if(FileName != null && !FileName.isEmpty())
	    {
	    	/*try
	    	{
		        Path path = FileSystems.getDefault().getPath(curFolder, FileName); 	       
		        boolean result = Files.deleteIfExists(path);
		        if(result)
		        {
		        	return;
		        }
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println("Failed to delete file:"+e.getMessage());
		    	response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Exception happens on server");
	    	}*/
	    		
	    		String userId = (String) session.getAttribute("userID");	    	
		    	S3Operations s3Operations = new S3Operations();
		    	String s3BucketHome = s3Operations.getBucketNameFromUserID(userId);
		    	
		    	s3Operations.removeFile(s3BucketHome, curFolder, FileName, FileVersion);
		    	
		    	response.getWriter().write("Removed File:"+FileName);
				response.setHeader("Refresh","1");        
	    }
	    /*else 
	    	if(FolderName != null && !FolderName.isEmpty())
	    	{
		    	try
		    	{
		    		Path path = FileSystems.getDefault().getPath(curFolder, FolderName);		       
					System.out.println("Deleting recursivey : "+path);
				    Files.walkFileTree(path, new SimpleFileVisitor<Path>()
		    	    {
		    	        @Override
		    	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
		    	                throws IOException
		    	        {
		    	            Files.delete(file);
		    	            return FileVisitResult.CONTINUE;
		    	        }

		    	        @Override
		    	        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
		    	        {
		    	            // try to delete the file anyway, even if its attributes
		    	            // could not be read, since delete-only access is
		    	            // theoretically possible
		    	            Files.delete(file);
		    	            return FileVisitResult.CONTINUE;
		    	        }

		    	        @Override
		    	        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
		    	        {
		    	            if (exc == null)
		    	            {
		    	                Files.delete(dir);
		    	                return FileVisitResult.CONTINUE;
		    	            }
		    	            else
		    	            {
		    	                // directory iteration failed; propagate exception
		    	                throw exc;
		    	            }
		    	        }
		    	    });
					
		    	}
		    	catch(Exception e)
		    	{
		    		System.err.println("Failed to delete file:"+e.getMessage());
			    	response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Exception happens on server");
		    	}
	    	}
	    	else
	    	{
	    		response.sendError(HttpServletResponse.SC_NO_CONTENT, "No important parameter");
	    	}*/

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
