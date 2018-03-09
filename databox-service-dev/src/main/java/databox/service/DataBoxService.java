package databox.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.bson.Document;
import org.bson.conversions.Bson;

import databox.Variant;

public class DataBoxService implements IDataBoxService {

	private DataBoxHttpClient httpClient = null;
	
	public DataBoxService() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("databox-service.properties");
		Properties properties = PropertiesFactory.getProperties(in);
		String ip = properties.getProperty("sample.server.ip");
		int port = Integer.valueOf(properties.getProperty("sample.server.port"));
		httpClient = new DataBoxHttpClient(ip, port);
	}

	public void putResult(String key, Object value) {
		System.out.println(key + " : " + value);
	}

	public List<Document> getData(String collectionName) {
		List<Document> docs = new ArrayList<Document>();
		
		CloseableHttpResponse response = httpClient.get("/databox-sample/GetDataServlet?collectionName=" + collectionName);
		docs = (List<Document>) httpClient.readObject(response);
		return docs;
	}
	
	public List<Document> getAllValue(String collectionName, String fieldName) {
		CloseableHttpResponse response = httpClient.get("/databox-sample/GetAllValueServlet?collectionName=" + collectionName + "&fieldName=" + fieldName);
		return (List<Document>) httpClient.readObject(response);
	}

	public double getAverage(String collectionName, String fieldName, Bson filter) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/GetAverageServlet?collectionName=" + collectionName + "&fieldName=" + fieldName, filter);
		return httpClient.readDouble(response);
	}

	public long getConditionCount(String collectionName, Bson filter) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/GetConditionCountServlet?collectionName=" + collectionName, filter);
		return httpClient.readLong(response);
	}

	public long dataCount(String collectionName) {
		CloseableHttpResponse response = httpClient.get("/databox-sample/DataCountServlet?collectionName=" + collectionName);
		return httpClient.readLong(response);
	}

	public List<Document> getFilterData(String collectionName, Bson filter) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/GetFilterDataServlet?collectionName=" + collectionName, filter);
		return (List<Document>)httpClient.readObject(response);
	}

	public Variant getMax(String collectionName, String fieldName, Bson bson) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/GetMaxServlet?collectionName=" + collectionName + "&fieldName=" + fieldName, bson);
		return (Variant) httpClient.readObject(response);
	}

	public Variant getMin(String collectionName, String fieldName, Bson bson) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/GetMinServlet?collectionName=" + collectionName + "&fieldName=" + fieldName, bson);
		return (Variant) httpClient.readObject(response);
	}

	public Document getDistinctValue(String collectionName, String fieldName) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/GetDistinctValueServlet?collectionName=" + collectionName + "&fieldName=" + fieldName);
		return (Document) httpClient.readObject(response);
	}

	public Document getDistinctValue(String collectionName, String fieldName, Bson bson) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/GetDistinctValueServlet?collectionName=" + collectionName + "&fieldName=" + fieldName, bson);
		return (Document) httpClient.readObject(response);
	}

	public List<Document> projections(String collectionName, List<String> fieldName) {
		CloseableHttpResponse response = httpClient.post("/databox-sample/ProjectionsServlet?collectionName=" + collectionName, fieldName);
		return (List<Document>) httpClient.readObject(response);
	}

	public List<Document> projections(String collectionName, List<String> fieldName, Bson bson) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fieldName", fieldName);
		map.put("filter", bson);
		CloseableHttpResponse response = httpClient.post("/databox-sample/ProjectionsServlet?collectionName=" + collectionName, map);
		return (List<Document>) httpClient.readObject(response);
	}

}
