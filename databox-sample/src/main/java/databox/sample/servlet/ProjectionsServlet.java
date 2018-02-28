package databox.sample.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.nustaq.serialization.FSTConfiguration;

import databox.exception.ConfigException;
import databox.service.DataBoxService;

public class ProjectionsServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public ProjectionsServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String collectionName = request.getParameter("collectionName");
		
		Object obj = null;
		ServletInputStream in = request.getInputStream();
		if(in.available() > 0) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
		    int n = 0;
		    while (-1!=(n=in.read(buffer))) {
		        output.write(buffer, 0, n);
		    }
		    byte[] bytes=output.toByteArray();
		    FSTConfiguration configuration=FSTConfiguration.createDefaultConfiguration();
		    obj = configuration.asObject(bytes);
		    
		}
		
		DataBoxService service = new DataBoxService();
		List<Document> doc = null;
			
		if(obj instanceof List)
			doc = service.projections(collectionName, (List<String>)obj);
		else if(obj instanceof Map) {
			Map<String, Object> map = (Map<String, Object>)obj;
			List<String> fieldName = (List<String>)map.get("fieldName");
			Bson filter = (Bson)map.get("filter");
			doc = service.projections(collectionName, fieldName, filter);
		}
			
		FSTConfiguration fstConfig = FSTConfiguration.createJsonConfiguration();
		out.write(fstConfig.asJsonString(doc));
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
