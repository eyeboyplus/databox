package databox.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import databox.Variant;
import databox.control.Log;

public class DataBoxMongoDB implements IDataBoxDatabase {
	private MongoClient client = null;
	private MongoDatabase db = null;

	private Map<String, String> pkMap = null;
	private Map<String, List<String>> fkMap = null;
	
	private List<String> getFields(Document doc) {
		List<String> res = new ArrayList<String>();
		Set<String> set = doc.keySet();
		res.addAll(set);
		return res;
	}
	
	public DataBoxMongoDB(String ip, int port, String dbName, 
			Map<String, String> primaryKeyMap, 
			Map<String, List<String>> forignKeyMap) {
		client = new MongoClient(ip, port);
		db = client.getDatabase(dbName);	
		this.pkMap = primaryKeyMap;
		this.fkMap = forignKeyMap;
	}
	
	public RecordedData getData(final String collectionName) {
		
		List<Document> data = new ArrayList<Document>();
		MongoCursor<Document> cursor = getMongoCursor(collectionName);
		
		//! log :serviceName
		String serviceName = "getData";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			data.add(doc);
			
			//! log : relDataList
			String pk = pkMap.get(collectionName);
			if(pk != null) {
				Object pkObj = doc.get(pk);
				Variant variant = new Variant(pkObj);
				relDataList.add(variant.toString());
			}
			
			//! log : fkList
			List<String> fields = getFields(doc);
			for(String field : fields) {
				List<String> fks = this.fkMap.get(collectionName);
				if(fks != null && fks.contains(field)) {
					List<String> fkValues = fkList.get(field);
					if(fkValues == null) {
						fkValues = new ArrayList<String>();
						fkList.put(field, fkValues);
					}
					fkValues.add(new Variant(doc.get(field)).toString());
				}
			}
		}
		
		//! log : fieldNames
		if(!data.isEmpty())
			fieldNames = getFields(data.get(0));
		
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		
		return new RecordedData(log, data);
	}

	@Override
	public RecordedData getAllValue(String collectionName, String fieldName) {
		List<Document> data = new ArrayList<Document>();
		
		String serviceName = "getAllValue";
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add(fieldName);
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		RecordedData temp = projections(collectionName, fieldNames);
		data = (List<Document>) temp.getData();
		Log tempLog = temp.getLog();
		relDataList = tempLog.getRelDataList();
		fkList = tempLog.getFkList();
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		
		if(!data.isEmpty())
			fieldNames = getFields(data.get(0));
		
		return new RecordedData(log, relDataList);
	}
	
	@Override
	public RecordedData getAverage(String collectionName, String fieldName, Bson filter) {
		double res = 0;
		long count = 0;
		
		String serviceName = "getAverage";
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add(fieldName);
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		MongoCursor<Document> cursor = getMongoCursor(collectionName, filter);
		String pk = pkMap.get(collectionName);
		List<String> fkFields = fkMap.get(collectionName);
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			Object val = doc.get(fieldName);
			if(val instanceof Integer)
				res += (Integer)val;
			else if(val instanceof Double)
				res += (Double)val;
			else if(val instanceof Float)
				res += (Float)val;
			else if(val instanceof Long)
				res += (Long)val;
			else if(val instanceof Short)
				res += (Short)val;
			count ++;
			
			if(pk != null)
				relDataList.add(new Variant(doc.get(pk)).toString());
			
			if(fkFields != null && fkFields.contains(fieldName)) {
				List<String> fkValues = fkList.get(fieldName);
				if(fkValues == null) {
					fkValues = new ArrayList<String>();
					fkList.put(fieldName, fkValues);
				}
				fkValues.add(new Variant(val).toString());
			}
		}
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		return new RecordedData(log, res / count);
	}
	
	@Override
	public RecordedData getConditionCount(String collectionName, Bson filter) {
		MongoCursor<Document> cursor = getMongoCursor(collectionName, filter);
		
		String serviceName = "getConditionCount";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		long count = 0;
		
		while(cursor.hasNext()) {
			cursor.next();
			count ++;
		}
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList); 
		return new RecordedData(log, count);
	}
	
	@Override
	public RecordedData dataCount(String collectionName) {
		long size = 0;
		
		String serviceName = "dataCount";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		MongoCursor<Document> cursor = getMongoCursor(collectionName);
		while(cursor.hasNext()) {
			size ++;
			cursor.next();
		}
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		return new RecordedData(log, size);
	}
	
	@Override
	public RecordedData getFilterData(String collectionName, Bson filter) {
		List<Document> res = new ArrayList<Document>();
		MongoCursor<Document> cursor = getMongoCursor(collectionName, filter);
		
		String serviceName = "getFilterData";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		String pk = this.pkMap.get(collectionName);
		List<String> fkFields = this.fkMap.get(collectionName);
		
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			res.add(doc);
			
			if(pk != null)
				relDataList.add(new Variant(doc.get(pk)).toString());
			
			List<String> fields = getFields(doc);
			if(fkFields != null) {
				for(String field : fields) {
					if(fkFields.contains(field)) {
						List<String> fkValues = fkList.get(field);
						if(fkValues == null) {
							fkValues = new ArrayList<String>();
							fkList.put(field, fkValues);
						}
						fkValues.add(new Variant(doc.get(field)).toString());
					}
				}
			}
		}
		
		if(!res.isEmpty())
			fieldNames = getFields(res.get(0));
				
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		return new RecordedData(log, res);
	}
	
	@Override
	public RecordedData projections(String collectionName, List<String> fieldName) {
		List<Document> res = new ArrayList<Document>();
		
		String serviceName = "projections";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		fieldNames = fieldName;
		
		String pk = this.pkMap.get(collectionName);
		List<String> fkFields = this.fkMap.get(collectionName);
		
		MongoCursor<Document> cursor = getMongoCursorWithProjection(collectionName, fieldName);
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			res.add(doc);
			
			if(pk != null)
				relDataList.add(new Variant(doc.get(pk)).toString());
			
			List<String> fields = getFields(doc);
			if(fkFields != null) {
				for(String field : fields) {
					if(fkFields.contains(field)) {
						List<String> fkValues = fkList.get(field);
						if(fkValues == null) {
							fkValues = new ArrayList<String>();
							fkList.put(field, fkValues);
						}
						fkValues.add(new Variant(doc.get(field)).toString());
					}
				}
			}
		}
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		return new RecordedData(log, res);
	}

	@Override
	public RecordedData projections(String collectionName, List<String> fieldName, Bson filter) {
	List<Document> res = new ArrayList<Document>();
		
		String serviceName = "projections";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		fieldNames = fieldName;
		
		String pk = this.pkMap.get(collectionName);
		List<String> fkFields = this.fkMap.get(collectionName);
		
		MongoCursor<Document> cursor = getMongoCursorWithProjection(collectionName, fieldName, filter);
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			res.add(doc);
			
			if(pk != null)
				relDataList.add(new Variant(doc.get(pk)).toString());
			
			List<String> fields = getFields(doc);
			if(fkFields != null) {
				for(String field : fields) {
					if(fkFields.contains(field)) {
						List<String> fkValues = fkList.get(field);
						if(fkValues == null) {
							fkValues = new ArrayList<String>();
							fkList.put(field, fkValues);
						}
						fkValues.add(new Variant(doc.get(field)).toString());
					}
				}
			}
		}
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		return new RecordedData(log, res);
	}
	
	@Override
	public RecordedData getMax(String collectionName, String fieldName, Bson filter) {
		List<String> fields = new ArrayList<String>();
		fields.add(fieldName);
		List<Document> tmp = (List<Document>) projections(collectionName, fields, filter).getData();

		String serviceName = "getMax";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		
		if(tmp.isEmpty())
			return new RecordedData(log, new Variant(null));
		
		Object max = tmp.get(0).get(fieldName);
	
		for(Document doc : tmp) {
			Object value = doc.get(fieldName);
			if(max instanceof Comparable && value instanceof Comparable && ((Comparable) max).compareTo(value) < 0)
				max = value;
		}
		
		return new RecordedData(log, new Variant(max));
	}
	
	@Override
	public RecordedData getMin(String collectionName, String fieldName, Bson filter) {
		List<String> fields = new ArrayList<String>();
		fields.add(fieldName);
		List<Document> tmp = (List<Document>) projections(collectionName, fields, filter).getData();

		String serviceName = "getMin";
		List<String> fieldNames = new ArrayList<String>();
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		
		if(tmp.isEmpty())
			return new RecordedData(log, new Variant(null));
		
		Object min = tmp.get(0).get(fieldName);
	
		for(Document doc : tmp) {
			Object value = doc.get(fieldName);
			if(min instanceof Comparable && value instanceof Comparable && ((Comparable) min).compareTo(value) > 0)
				min = value;
		}
		
		return new RecordedData(log, new Variant(min));
	}
	
	@Override
	public RecordedData getDistinctValue(String collectionName, String fieldName) {
		String serviceName = "getDistinctValue";
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add(fieldName);
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		Document data = new Document();
		RecordedData temp = projections(collectionName, fieldNames);
		List<Document> tempData = (List<Document>)temp.getData();
		if(!tempData.isEmpty()) {
			Document doc = tempData.get(0);
			data = doc;
			String pk = this.pkMap.get(collectionName);
			List<String> fkFields = this.fkMap.get(collectionName);
			if(pk != null)
				relDataList.add(new Variant(doc.get(pk)).toString());
			
			if(fkFields != null && fkFields.contains(fieldName)) {
				List<String> fkValues = fkList.get(fieldName);
				if(fkValues == null) {
					fkValues = new ArrayList<String>();
					fkList.put(fieldName, fkValues);
				}
				fkValues.add(new Variant(doc.get(fieldName)).toString());
			}
		}
		
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		
		return new RecordedData(log, data);
	}

	@Override
	public RecordedData getDistinctValue(String collectionName, String fieldName, Bson filter) {
		String serviceName = "getDistinctValue";
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add(fieldName);
		List<String> relDataList = new ArrayList<String>();
		Map<String, List<String>> fkList = new HashMap<String, List<String>>();
		
		Document data = new Document();
		RecordedData temp = projections(collectionName, fieldNames, filter);
		List<Document> tempData = (List<Document>)temp.getData();
		if(!tempData.isEmpty()) {
			Document doc = tempData.get(0);
			data = doc;
			String pk = this.pkMap.get(collectionName);
			List<String> fkFields = this.fkMap.get(collectionName);
			if(pk != null)
				relDataList.add(new Variant(doc.get(pk)).toString());
			
			if(fkFields != null && fkFields.contains(fieldName)) {
				List<String> fkValues = fkList.get(fieldName);
				if(fkValues == null) {
					fkValues = new ArrayList<String>();
					fkList.put(fieldName, fkValues);
				}
				fkValues.add(new Variant(doc.get(fieldName)).toString());
			}
		}
		
		
		Log log = new Log(serviceName, collectionName, fieldNames, relDataList, fkList);
		
		return new RecordedData(log, data);
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
