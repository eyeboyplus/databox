package databox.sample.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.conversions.Bson;
import org.nustaq.serialization.FSTConfiguration;

import databox.Variant;
import databox.service.DataBoxService;

public class GetMinServlet extends HttpServlet {

	public GetMinServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String collectionName = request.getParameter("collectionName");
		String fieldName = request.getParameter("fieldName");
		
		Bson filter = null;
		ServletInputStream in = request.getInputStream();
		if(in.available() > 0)
			filter = BsonHelper.getBsonFromStream(in);
		
		DataBoxService service = new DataBoxService();
		Variant doc = null;
		doc = service.getMin(collectionName, fieldName, filter);
			
		FSTConfiguration fstConfig = FSTConfiguration.createJsonConfiguration();
		out.write(fstConfig.asJsonString(doc));
	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
