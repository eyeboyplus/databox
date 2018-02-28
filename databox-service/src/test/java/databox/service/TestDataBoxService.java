package databox.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.model.Filters;

import databox.Variant;
import databox.service.DataBoxService;

public class TestDataBoxService {
	private DataBoxService service;

	@Before
	public void setUp() throws Exception {
		service = new DataBoxService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAverage() {
		System.out.println(service.getAverage("diagnosis", "TSH", Filters.exists("TSH")));
	}
	
	@Test
	public void testGetAllValue() {
		System.out.println(service.getAllValue("diagnosis", "TSH"));
	}
	
	@Test
	public void testGetCondictionCount() {
		long cnt = service.getConditionCount("diagnosis", Filters.exists("TSH"));
		System.out.println(cnt);
	}
	
	@Test
	public void testGetData() {
		List<Document> res = service.getData("diagnosis");
		System.out.println(res);
	}
	
	@Test
	public void testDataCount() {
		long cnt = service.dataCount("diagnosis");
		System.out.println(cnt);
	}
	
	@Test
	public void testGetFilterData() {
		System.out.println(service.getFilterData("diagnosis", Filters.eq("TSH", 30)));
	}
	
	@Test
	public void testProjections() {
		List<String> fields = new ArrayList<String>();
		fields.add("TSH");
		fields.add("FT3");
		System.out.println(service.projections("diagnosis", fields));
		//System.out.println(service.projections("diagnosis", fields, Filters.eq("TSH", 30)));
	}
	
	@Test
	public void testGetDistinctValue() {
		Document doc = service.getDistinctValue("diagnosis", "TSH");
		Document doc1 = service.getDistinctValue("diagnosis", "TSH", Filters.lt("TSH", 20));
		System.out.println(doc);
		System.out.println(doc1);
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
	
	private String getDiagnosis(int id) {
		switch(id) {
		case 0: return "֧���ܷ���";
		case 1: return "��״�ٽ��";
		case 2: return "��״����";
		}
		return "";
	}
	
	public void testInsert() {
		int ageMax = 80;
		Random ageRandom = new Random();
		Random diagnosisRandom = new Random();
		Random hgbRandom = new Random();
		
		List<Document> docs = new ArrayList<Document>();
		
		for(int i = 0; i < 100000; ++i) {
			Document doc = new Document();
			doc.append("name", "test" + i);
			doc.append("age", ageRandom.nextInt(ageMax) + 1);
			doc.append("diagnosis", getDiagnosis(diagnosisRandom.nextInt(2)));
			doc.append("hgb", 100 + hgbRandom.nextDouble() * 140);
			docs.add(doc);
		}
		
		service.insert("test", docs);
	}
	
	
}
