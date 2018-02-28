package databox.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import databox.service.DataBoxService;

public class TestDataBoxServivce {

	private DataBoxService service;
	@Before
	public void setUp() throws Exception {
		service = new DataBoxService();
	}

	@Test
	public void testProjection() {
		List<String> fields = new ArrayList<String>();
		fields.add("TSH");
		System.out.println(service.projections("diagnosis", fields));
		fields.add("FT3");
		System.out.println(service.projections("diagnosis", fields));
		fields.add("FT4");
		System.out.println(service.projections("diagnosis", fields));
	}
}
