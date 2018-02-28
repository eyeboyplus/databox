package databox.control;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestLogAnalysis {

	private LogAnalysis logAnalysis;
	
	@Before
	public void setUp() throws Exception {
		logAnalysis = new LogAnalysis("127.0.0.1", 5000);
		//logAnalysis = new LogAnalysis("192.168.254.139", 5000);
	}

	@Test
	public void testInit() {
		System.out.println("init");
		assertTrue(logAnalysis.init());
	}
		
	@Test
	public void testControl() {
		System.out.println("control");
		
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add("TSH");
		fieldNames.add("FT3");
		fieldNames.add("FT4");
		String serviceName = "getData";
		List<String> pk = new ArrayList<String>();
		pk.add("123");
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> fk = new ArrayList<String>();
		fk.add("123");
		map.put("FT3", fk);
		Log log = new Log(serviceName, "diagnosis",
				fieldNames, pk, map, new Date());
		
		List<String> fieldNames1 = new ArrayList<String>();
		fieldNames1.add("TSH");
		fieldNames1.add("FT3");
		fieldNames1.add("FT4");
		String serviceName1 = "getAllValue";
		List<String> pk1 = new ArrayList<String>();
		pk1.add("TSH");
		Map<String, List<String>> map1 = new HashMap<String, List<String>>();
		List<String> fk1 = new ArrayList<String>();
		fk1.add("123");
		map1.put("FT3", fk1);
		Log log1 = new Log(serviceName1, "diagnosis", 
				fieldNames1, pk1, map1, new Date());
		
		logAnalysis.control(log);
		logAnalysis.control(log);
		logAnalysis.control(log1);
	}

}
