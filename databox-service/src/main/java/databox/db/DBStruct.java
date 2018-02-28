package databox.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBStruct {
	private DataSetReader dataSetReader;
	public DBStruct(String fileName) throws UnsupportedEncodingException, FileNotFoundException {
		dataSetReader = new DataSetReader();
		dataSetReader.load(fileName);
	}
	
	public Map<String, String> getPKMap() throws IOException {
		Map<String, String> res = new HashMap<String, String>();
		List<DataSet> datasets = dataSetReader.getAllDataSet();
		for(DataSet dataSet : datasets) {
			List<Table> tables = dataSet.getTables();
			for (Table table : tables) {
				String pk = table.getPrimaryKeys().get(0);
				res.put(table.getName(), pk);
			}
		}
		return res;
	}
}
