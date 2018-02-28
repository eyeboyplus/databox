package databox.task.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import databox.task.AbstractTask;

public class GetResultServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public GetResultServlet() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String taskGroupName = request.getParameter("taskGroupName");
		String taskName = request.getParameter("taskName");
		String key = taskGroupName + "/" + taskName;
		Map<String, AbstractTask> workingTask = (Map<String, AbstractTask>) getServletContext().getAttribute("workingTask");
		PrintWriter out = response.getWriter();
		if(workingTask != null) {
			AbstractTask task = workingTask.get(key);
			if(task != null) {
				if(!task.isAlive()) {
					JsonObject obj = new JsonObject();
					obj.addProperty("result", task.getMessage());
					obj.addProperty("error", task.getError());
					out.println(new Gson().toJson(obj));
				} else {
					out.println(new Gson().toJson(new JsonObject()));
				}
			} else {
				out.println(new Gson().toJson(new JsonObject()));
			}
		}
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
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
