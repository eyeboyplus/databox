package databox.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import databox.DataBox;
import databox.Variant;
import databox.control.LogAnalysis;
import databox.control.LogAnalysisProxy;
import databox.db.DataBoxMongoDB;
import databox.db.IDataBoxDatabase;
import databox.db.RecordedData;

public class DataBoxService implements IDataBoxService {

	private MongoClient client = null;
	private MongoDatabase db = null;
	
	private IDataBoxDatabase databoxDB = null;
	private LogAnalysis logAnalysis = null;
	
	public DataBoxService() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("databox/service/databox-service.properties");
		Properties properties = PropertiesFactory.getProperties(in);
		String ip = properties.getProperty(DataBox.KEY_DATA_DB_IP, DataBox.DEFAULT_DATA_DB_IP);
		int port = Integer.valueOf(properties.getProperty(DataBox.KEY_DATA_DB_PORT, DataBox.DEFAULT_DATA_DB_PORT));
		String dbName = properties.getProperty(DataBox.KEY_DATA_DB_DEFAULT_DB_NAME, DataBox.DEFAULT_DATA_DB_NAME);
		
		// pk map
		String dataSetInfoFileName = properties.getProperty(DataBox.KEY_DATA_SET_INFO_EXTERNAL_FILE_PATH);
		Map<String, String> pkMap = null;
		Map<String, List<String>> fkMap = null;
		//DBStruct dbStruct = new DBStruct(dataSetInfoFileName);
		//pkMap = dbStruct.getPKMap();
		//! 需要修改，从配置文件加载
		pkMap = new HashMap<String, String>();
		pkMap.put("diagnosis", "TSH");
		fkMap = new HashMap<String, List<String>>();
		List<String> fk = new ArrayList<String>();
		fk.add("FT3");
		fkMap.put("diagnosis", fk);
		
		String logAnalysisIp = "127.0.0.1";
		int logAnalysisPort = 5000;
		logAnalysis = new LogAnalysis(logAnalysisIp, logAnalysisPort);
		
		String controlSwitch = properties.getProperty(DataBox.KEY_LOG_CONTROL_SWITCH);
		databoxDB = new DataBoxMongoDB(ip, port, dbName, pkMap, fkMap);
		if("true".equals(controlSwitch.toLowerCase()))
			databoxDB = (IDataBoxDatabase) LogAnalysisProxy.getProxy(databoxDB, logAnalysis);
		
//		client = new MongoClient(ip, port);
//		db = client.getDatabase(dbName);
	}

	public List<Document> getData(final String collectionName) {
		RecordedData recordedData = databoxDB.getData(collectionName);
		if(recordedData.isAllowed())
			return (List<Document>) recordedData.getData();
		
		return new ArrayList<Document>();
	}
	
	public void putResult(String key, Object value) {
		System.out.println(key + " : " + value);
	}
	
	public void insert(String collectionName, List<Document> docs) {
		db.getCollection(collectionName).insertMany(docs);
	}
	
	public List<Document> getFilterData(String collectionName, Bson filters) {
		RecordedData data = databoxDB.getFilterData(collectionName, filters);
		if(data.isAllowed())
			return (List<Document>) data.getData();
		return new ArrayList<Document>();
	}

	public List<Document> projections(String collectionName, List<String> fieldName) {
		RecordedData data = databoxDB.projections(collectionName, fieldName);
		if(data.isAllowed())
			return (List<Document>) data.getData();
		
		return new ArrayList<Document>();
	}

	public List<Document> projections(String collectionName, List<String> fieldName, Bson filters) {
		RecordedData data = databoxDB.projections(collectionName, fieldName, filters);
		if(data.isAllowed())
			return (List<Document>) data.getData();
		
		return new ArrayList<Document>();
	}

//	@Override
//	public List<Document> sort(List<Document> data, List<String> fieldName, String flag) {
//		Collections.sort(data, new Comparator<Document>() {
//
//			@Override
//			public int compare(Document o1, Document o2) {
//				for(String s : fieldName){
//					String s1=o1.getString(s);
//					String s2=o2.getString(s);
//					if(flag.equals("ASC")){
//						if(s1.compareTo(s2)>0){
//							return 1;
//						}else if(s1.compareTo(s2)<0){
//							return -1;
//						}
//					}else if(flag.equals("DESC")){
//						if(s1.compareTo(s2)>0){
//							return -1;
//						}else if(s1.compareTo(s2)<0){
//							return 1;
//						}
//					}
//					
//				}
//				return 0;
//			}
//		});
//		return data;
//
//	}
	
	public List<Document> getAllValue(String collectionName, String fieldName) {
		RecordedData data = databoxDB.getAllValue(collectionName, fieldName);
		if(data.isAllowed())
			return (List<Document>) data.getData();
		return new ArrayList<Document>();
	}

	public double getAverage(String collectionName, String fieldName, Bson bson) {
		RecordedData data = databoxDB.getAverage(collectionName, fieldName, bson);
		if(data.isAllowed())
			return (Double) data.getData();

		return Double.MAX_VALUE;
	}
	
	public long getConditionCount(String collectionName, Bson bson) {
		RecordedData data = databoxDB.getConditionCount(collectionName, bson);
		if(data.isAllowed())
			return (Long) data.getData();
		return Long.MAX_VALUE;
	}
	
	public long dataCount(String collectionName) {
		RecordedData data = databoxDB.dataCount(collectionName);
		if(data.isAllowed())
			return (Long) data.getData();
		return Long.MAX_VALUE;
	}

	public Variant getMax(String collectionName, String fieldName, Bson filter) {
		RecordedData data = databoxDB.getMax(collectionName, fieldName, filter);
		if(data.isAllowed())
			return (Variant) data.getData();
		return new Variant(null);
	}

	public Variant getMin(String collectionName, String fieldName, Bson filter) {
		RecordedData data = databoxDB.getMin(collectionName, fieldName, filter);
		if(data.isAllowed())
			return (Variant) data.getData();
		return new Variant(null);	
	}

	public Document getDistinctValue(String collectionName, String fieldName) {
		RecordedData data = databoxDB.getDistinctValue(collectionName, fieldName);
		if(data.isAllowed())
			return (Document) data.getData();
		return new Document();	
	}

	public Document getDistinctValue(String collectionName, String fieldName, Bson filter) {
		RecordedData data = databoxDB.getDistinctValue(collectionName, fieldName, filter);
		if(data.isAllowed())
			return (Document) data.getData();
		return new Document();	
	}
	
	private List<Document> getDocument(String collectionName, String fieldName) {
		List<Document> res = new ArrayList<Document>();
		
		MongoCursor<Document> cursor = getMongoCursor(collectionName);
		while(cursor.hasNext()) {
			res.add(cursor.next());
		}
		return res;
	}
	
	private List<Document> getDocument(String collectionName, Bson bson) {
		MongoCursor<Document> cursor = getMongoCursor(collectionName, bson);
		List<Document> res = new ArrayList<Document>();
		while(cursor.hasNext()) {
			res.add(cursor.next());
		}
		
		return res;
	}
	
	private MongoCursor<Document> getMongoCursor(String collectionName) {
		MongoCollection<Document> collection = db.getCollection(collectionName);
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		return mongoCursor;
	}
	
	private MongoCursor<Document> getMongoCursor(String collectionName, Bson filters) {
		MongoCollection<Document> collection = db.getCollection(collectionName);
		FindIterable<Document> findIterable = collection.find(filters);
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		return mongoCursor;
	}
	
	private MongoCursor<Document> getMongoCursorWithProjection(String collectionName, List<String> fieldNames) {
		MongoCollection<Document> collection = db.getCollection(collectionName);
		FindIterable<Document> findIterable = collection.find().projection(Projections.include(fieldNames));
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		return mongoCursor;	
	}
	
	private MongoCursor<Document> getMongoCursorWithProjection(String collectionName, List<String> fieldNames, Bson filters) {
		MongoCollection<Document> collection = db.getCollection(collectionName);
		FindIterable<Document> findIterable = collection.find(filters).projection(Projections.include(fieldNames));
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		return mongoCursor;	
	}


}
