package databox.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.model.Filters;

import databox.db.DataBoxMongoDB;
import databox.db.RecordedData;

public class TestDataBoxMongoDB {

	private DataBoxMongoDB db;
	
	@Before
	public void setUp() throws Exception {
		//db = new DataBoxMongoDB("localhost", 27017);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetData() {
		RecordedData data = db.getData("test");
	}
	
	@Test
	public void testGetAverage() {
		RecordedData data = db.getAverage("diagnosis", "TSH", Filters.exists("TSH"));
		System.out.println(data);
	}

}
