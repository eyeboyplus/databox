package databox.db;

//import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import com.google.gson.stream.JsonReader;

import databox.db.DataSet;
import databox.db.DataSetReader;

public class TestDataSetReader {

	@Test
	public void test() throws IOException {
		DataSetReader sample =new DataSetReader();
		sample.load("C:/Users/youbei/Desktop/sample-info.json");
		List<DataSet> datasets = sample.getAllDataSet();
		int size = datasets.size();
		//assertEquals(2, size);
	}
}
