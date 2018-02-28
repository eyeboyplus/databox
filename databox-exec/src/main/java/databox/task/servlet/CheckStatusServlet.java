package databox.task.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import databox.task.AbstractTask;
import databox.task.TaskInfo;

public class CheckStatusServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public CheckStatusServlet() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String taskGroupName = request.getParameter("taskGroupName");
		String taskName = request.getParameter("taskName");
		String key = taskGroupName + "/" + taskName;
		Map<String, AbstractTask> workingTask = (Map<String, AbstractTask>) getServletContext().getAttribute("workingTask");
		
		PrintWriter out = response.getWriter();
		
		if(taskGroupName!=null && taskName!=null) {
			if(workingTask != null) {
				AbstractTask task = workingTask.get(key);
				if(task == null) {
					out.println("{}");
					return;
				}
				boolean isAlive = task.isAlive();
				JsonObject res = new JsonObject();
				res.addProperty("taskGroupName", taskGroupName);
				res.addProperty("taskName", taskName);
				res.addProperty("status", isAlive ? "running":"stop");
				
				out.println(new Gson().toJson(res));
			}
		} else {
			JsonArray res = new JsonArray();
			if(!workingTask.isEmpty()) {
				for(Iterator<String> it = workingTask.keySet().iterator(); it.hasNext();) {
					String k = it.next();
					AbstractTask task = workingTask.get(k);
					if(task == null) {
						continue;
					}
					boolean isAlive = task.isAlive();
					JsonObject obj = new JsonObject();
					TaskInfo info = task.getTaskInfo();
					obj.addProperty("taskGroupName", info.getTaskGroupName());
					obj.addProperty("taskName", info.getTaskName());
					obj.addProperty("status", isAlive ? "running":"stop");
					res.add(obj);
				}
			}
			out.println(new Gson().toJson(res));
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.doGet(request, response);
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}
