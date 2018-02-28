package databox;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import databox.DataSetInfoReader;

public class TestDataSetInfoReader {

	private DataSetInfoReader reader;
	
	@Before
	public void setUp() throws Exception {
		reader = new DataSetInfoReader("F:/workspaces/databox/databox-log-analysis/core/config/DataMeta.xml");
	}

	@Test
	public void testGetPrimaryKeyMap() {
		Map<String, String> pks = reader.getPrimaryKeyMap();
		System.out.println(pks);
	}

}
