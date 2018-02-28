package databox.task.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dom4j.DocumentException;

import databox.util.FileHelper;
import databox.task.TaskConfig;
import databox.task.TaskInfo;
import databox.task.TaskListXmlParser;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String fileName;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected boolean downloadFile(HttpServletRequest request, String tmpDir, String desDir) {
    	//fileName = UUID.randomUUID().toString() + ".xyz";
    	File downloadFile = new File(desDir);
    	if(!downloadFile.exists())
    		downloadFile.mkdirs();
    	
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
    	
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){  
			DiskFileItemFactory factory = new DiskFileItemFactory();               
			factory.setSizeThreshold(1024*1024);              
			factory.setRepository(new File(tmpDir));   
			//factory.setRepository(new File("media"));
			ServletFileUpload upload = new ServletFileUpload(factory);              
			upload.setFileSizeMax(-1);                 
			upload.setSizeMax(50 * 1024 * 1024);     
			upload.setHeaderEncoding("UTF-8");               
			List<FileItem> items = null;                 
			try {  
				items = upload.parseRequest(request);  
			} catch (Exception e) {  
				e.printStackTrace();  
				return false;
			}
			if(items!=null){  
				Iterator<FileItem> iter = items.iterator();  
				while (iter.hasNext()) {  
					FileItem item = iter.next();                    
					if (item.isFormField()) {   
						String name = item.getFieldName();              
						String value = item.getString();               
					}else {  //������ϴ��ļ�
							String fieldName = item.getFieldName();                
							//fileName = item.getName();  
							fileName = UUID.randomUUID().toString() + "." +FileHelper.getFileNameSuffix(item.getName());
							//fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
							try {  
									item.write(new File(desDir, fileName));  
								} catch (Exception e) {  
									e.printStackTrace();  
									return false;
								}  
						  } 
				}  
			}  
	}  
    	
    	return true;
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		
		String tmpDir = TaskConfig.tempPath;
		String downloadDir = TaskConfig.downloadPath;
		String taskDir = TaskConfig.taskRootPath;
		
		System.out.println(downloadFile(request, tmpDir, downloadDir));
		
		FileHelper.unZip(downloadDir + "/" + fileName, downloadDir + "/" + FileHelper.getFileName(fileName));
		
		TaskListXmlParser userXmlParser = null;
		TaskListXmlParser serverXmlParser = null;
		try {
			userXmlParser = new TaskListXmlParser(downloadDir + File.separator + FileHelper.getFileName(fileName) + "/tasklist.xml");
			//serverXmlParser = new TaskListXmlParser(taskDir + "/tasklist.xml");
			serverXmlParser = (TaskListXmlParser) this.getServletContext().getAttribute("tasklist");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendRedirect("index.html?status=1");
			//out.println("{status: 1, error: 'can't find tasklist.xml file'}");
			return;
		}
		
		List<String> taskGroupNames = userXmlParser.getAllTaskGroupName();
		for(Iterator<String> it = taskGroupNames.iterator(); it.hasNext();) {
			String name = it.next();
			if(!serverXmlParser.containsTaskGroup(name))
				continue;
			else {
				response.sendRedirect("index.html?status=2");
				//out.println("{status: 1, error: 'the box has existed a \"" + name + "\" group'}");
				return;
			}
		}
		
		Iterator<String> it = taskGroupNames.iterator();
		if(it.hasNext()) {
			String taskGroupName = it.next();
			if(!FileHelper.copy(downloadDir + File.separator + FileHelper.getFileName(fileName), taskDir + File.separator + taskGroupName)) {
				response.sendRedirect("index.html?status=3");
				//out.println("{stauts:1, error: 'can't deploy the task.'}");
				return;
			}
		}	
		
		//add to server tasklist
		it = taskGroupNames.iterator();
		while(it.hasNext()) {
			String groupName = it.next();
			List<TaskInfo> infos = userXmlParser.getTaskInfoByGroupName(groupName);
			serverXmlParser.insertTaskInfoByGroupName(groupName, infos);
		}

//		String uploadPath="D:\\Class\\upload";
//		//String uploadPath="my_data";
//		File uploadFile = new File(uploadPath);
//		if(!uploadFile.exists()) {
//			uploadFile.mkdirs();
//		}
//		
//		request.setCharacterEncoding("utf-8");  
//		response.setCharacterEncoding("utf-8");
//		
//		String fileName = null;
//		
//		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//		if(isMultipart){  
//			DiskFileItemFactory factory = new DiskFileItemFactory();               
//			factory.setSizeThreshold(1024*1024);              
//			factory.setRepository(new File("D:\\Class\\temp"));   
//			//factory.setRepository(new File("media"));
//			ServletFileUpload upload = new ServletFileUpload(factory);              
//			upload.setFileSizeMax(-1);                 
//			upload.setSizeMax(50 * 1024 * 1024);     
//			upload.setHeaderEncoding("UTF-8");               
//			List<FileItem> items = null;                 
//			try {  
//				items = upload.parseRequest(request);  
//			} catch (Exception e) {  
//				e.printStackTrace();  
//			}
//			if(items!=null){  
//				Iterator<FileItem> iter = items.iterator();  
//				while (iter.hasNext()) {  
//					FileItem item = iter.next();                    
//					if (item.isFormField()) {   
//						String name = item.getFieldName();              
//						String value = item.getString();               
//					}else {  //������ϴ��ļ�  
//							String fieldName = item.getFieldName();                
//							fileName = item.getName();  
//							fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//							try {  
//									item.write(new File(uploadPath, fileName));  
//								} catch (Exception e) {  
//									e.printStackTrace();  
//								}  
//						  } 
//				}  
//			}  
//	}  
	
		//Utils.unZip(uploadPath+ "\\" + fileName, uploadPath);
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		response.sendRedirect("index.html?status=0");
		//out.write("{status: 0}");
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
