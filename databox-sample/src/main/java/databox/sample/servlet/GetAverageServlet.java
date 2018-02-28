package databox.sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.conversions.Bson;

import databox.service.DataBoxService;

public class GetAverageServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public GetAverageServlet() {
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
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String collectionName = request.getParameter("collectionName");
		String fieldName = request.getParameter("fieldName");
		
		Bson filter = BsonHelper.getBsonFromStream(request.getInputStream());
		
		DataBoxService service = new DataBoxService();
		double value = service.getAverage(collectionName, fieldName, filter);
			
		out.write(String.valueOf(value));
			
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
