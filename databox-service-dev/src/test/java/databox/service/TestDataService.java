package databox.service;

import com.mongodb.client.model.Filters;
import databox.Variant;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestDataService {
	private DataBoxService service;

	@Before
	public void setUp() throws Exception {
		service = new DataBoxService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetData() {
		List<Document> docs = service.getData("diagnosis");
		System.out.println(docs.size());
	}
	
	@Test
	public void testGetAllData() {
		List<Document> docs = service.getAllValue("diagnosis", "TSH");
		System.out.println(docs.size());
	}
	
	@Test
	public void testGetAverage() {
		double value = service.getAverage("diagnosis", "TSH", Filters.exists("TSH"));
		System.out.println(value);
	}
	
	@Test
	public void testGetConditionCount() {
		long count = service.getConditionCount("test", Filters.exists("age"));
		System.out.println(count);
	}
	
	@Test
	public void testGetFilterData() {
		List<Document> doc = service.getFilterData("test", Filters.exists("age"));
		System.out.println(doc);
	}
	
	@Test
	public void testGetDistinctValueServlet() {
		Document doc = service.getDistinctValue("test", "age");
		System.out.println(doc);
		Document doc1 = service.getDistinctValue("test", "age", Filters.exists("age"));
		System.out.println(doc1);
	}
	
	@Test
	public void testProjections() {
		List<String> fields = new ArrayList<String>();
		fields.add("age");
//		List<Document> doc1 = service.projections("test", fields);
//		System.out.println(doc1);
		List<Document> doc2 = service.projections("test", fields, Filters.exists("age"));
		System.out.println(doc2);
	}
	
	@Test
	public void testGetMax() {
		Variant var = service.getMax("diagnosis", "TSH", Filters.exists("TSH"));
		System.out.println(var);
	}
	
	@Test
	public void testGetMin() {
		Variant var = service.getMin("diagnosis", "TSH", Filters.exists("TSH"));
		System.out.println(var);
	}
}
