package com.team11.cloudbox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.app.amazonS3.S3Folder;
import com.app.amazonS3.S3Operations;

/**
 * Servlet implementation class UploadFileServlet
 */
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean isMultipart;
	//private String filePath;
	private int maxFileSize = 50 * 1024;
	private int maxMemSize = 4 * 1024;
	//private File file;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        throw new ServletException("GET method used with " +
                getClass( ).getName( )+": POST method required.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		String userID = session.getAttribute("userID").toString();
	    String curFolder= (String) session.getAttribute("currentDir");
	    
	    S3Operations s3Operations = new S3Operations();
    	String s3BucketHome = s3Operations.getBucketNameFromUserID(userID);
	    
	    //filePath = curFolder;
	    
		// Check that we have a file upload request
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter();
		
		isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
	    	response.sendError(HttpServletResponse.SC_NO_CONTENT, "No File selected");
			return;
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("~/temp"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List<FileItem> fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator<FileItem> i = fileItems.iterator();

			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fileName = fi.getName();
					
					/*String fieldName = fi.getFieldName();
					boolean isInMemory = fi.isInMemory();*/
					
					String contentType = fi.getContentType();
					long sizeInBytes = fi.getSize();
					InputStream input =fi.getInputStream();
					
					// Write the file
					if (fileName.lastIndexOf("\\") >= 0) {
						//file = new File(fileName.substring(fileName.lastIndexOf("\\")));
						fileName = fileName.substring(fileName.lastIndexOf("\\"));
					} 
					else 
						if(fileName.lastIndexOf("/") >= 0)
						{
							//file = new File(fileName.substring(fileName.lastIndexOf("/")));
							fileName = fileName.substring(fileName.lastIndexOf("/"));
						}
					
					//fi.write(file);//Upload to S3 with path and name-S5
					
					S3Folder s3Folder = new S3Folder();
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(sizeInBytes);
					meta.setContentType(contentType);
					
					boolean complete = s3Folder.uploadFile(s3BucketHome, fileName, input, curFolder, meta);
					if(complete)
					{
						out.write("Uploaded Filename: " + fileName);
						response.setHeader("Refresh","2");
					}
				}
			}			
			
		} catch (Exception ex) {
			System.err.println("Save upload file error:"+ex.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Save upload file error");
		}
	}

}