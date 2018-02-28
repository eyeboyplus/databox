package databox.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import databox.control.Log;
import databox.control.LogHelper;
import org.junit.Before;
import org.junit.Test;

public class LogHelperTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String serviceName = "getData";
		String collectionName = "test";
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add("1231");
		List<String> relDataList = new ArrayList<String>();
		relDataList.add("12431");
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		List<String> fk1 = new ArrayList<String>();
		fk1.add("123");
		fk1.add("1234");
		fkList.put("124", fk1);
		List<String> fk2 = new ArrayList<String>();
		fk2.add("123");
		fk2.add("1234");
		fkList.put("1244", fk2);	
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		System.out.println(LogHelper.logToJson(log));
	}
}
