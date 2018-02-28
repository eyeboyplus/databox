package databox.sample.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.nustaq.serialization.FSTConfiguration;

import databox.exception.ConfigException;
import databox.service.DataBoxService;

public class GetConditionCountServlet extends HttpServlet {

	public GetConditionCountServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String collectionName = request.getParameter("collectionName");
		
		Bson filter = BsonHelper.getBsonFromStream(request.getInputStream());
		
		DataBoxService service = new DataBoxService();
		long count = service.getConditionCount(collectionName, filter);
			
		out.write(String.valueOf(count));
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
