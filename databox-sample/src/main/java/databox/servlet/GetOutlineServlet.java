package databox.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetXMLServlet
 */
public class GetOutlineServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;UTF-8");
		
		String type = request.getParameter("type");
		String fileName = null;
		if("service".equals(type)) {
			fileName = this.getClass().getClassLoader().getResource("/").getPath() + "databox/outline/service-info.json";
		} else if("dataset".equals(type)) {
			fileName = this.getClass().getClassLoader().getResource("/").getPath() + "databox/outline/dataset-info.json";
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
		PrintWriter out = response.getWriter();
		String res = in.readLine();
		while(res != null) {
			out.write(res);
			res = in.readLine();
		}
		in.close();
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
