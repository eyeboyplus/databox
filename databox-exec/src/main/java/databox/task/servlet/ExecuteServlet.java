package databox.task.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import databox.task.*;

public class ExecuteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ExecuteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String taskGroupName = request.getParameter("taskGroupName");
		String taskName = request.getParameter("taskName");


		//TODO 导出配置文件
        String ip = "localhost";
        int port = 27017;
        String dbname = "tasklist";
        String collectioName = "task";
        TaskListMongoDB taskListMongoDB = new TaskListMongoDB(ip, port, dbname, collectioName);
        TaskInfo taskInfo = taskListMongoDB.getTaskInfo(taskGroupName, taskName);

		String key = taskGroupName + "/" + taskName;

		Map<String, AbstractTask> workingTask = (Map<String, AbstractTask>) this.getServletContext().getAttribute("workingTask");
		if(workingTask == null) {
			workingTask = new HashMap<String, AbstractTask>();
			this.getServletContext().setAttribute("workingTask", workingTask);
		}
//
//		TaskListXmlParser parser = (TaskListXmlParser) this.getServletContext().getAttribute("tasklist");
//		TaskInfo taskInfo = parser.getTaskInfo(taskGroupName, taskName);
		if(taskInfo == null) {
			System.out.println("gg");
			return;
		}
		AbstractTask task = new JTask(taskInfo);
		
		PrintWriter out = response.getWriter();
		if(task.execute()) {
			workingTask.put(key, task);
			out.println("{\"status\": 0}");
		} else {
			out.println("{\"status\": -1}");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
