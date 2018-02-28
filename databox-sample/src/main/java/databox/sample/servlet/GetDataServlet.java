package databox.sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.nustaq.serialization.FSTConfiguration;

import databox.service.DataBoxService;

public class GetDataServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public GetDataServlet() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String collectionName = request.getParameter("collectionName");
		
		DataBoxService service = new DataBoxService();
		List<Document> docs = service.getData(collectionName);
		FSTConfiguration fstConfig = FSTConfiguration.createJsonConfiguration();
			
		out.write(fstConfig.asJsonString(docs));
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void init() throws ServletException {
	}

}
