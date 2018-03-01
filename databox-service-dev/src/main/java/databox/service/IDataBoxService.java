package databox.service;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import databox.Variant;

public interface IDataBoxService {
	public void putResult(String key, Object value);
	
	public List<Document> getData(final String collectionName);
	
	public List<Document> getAllValue(final String collectionName, final String fieldName);
	
	public double getAverage(final String collectionName, final String fieldName, final Bson filter);
	
	public long getConditionCount(final String collectionName, final Bson filter);
	
	public long dataCount(final String collectionName);
	
	public List<Document> getFilterData(final String collectionName, final Bson filter);
	
	public Variant getMax(final String collectionName, final String fieldName, final Bson filter);
	
	public Variant getMin(final String collectionName, final String fieldName, final Bson filter);
	
	public Document getDistinctValue(final String collectionName, final String fieldName);
	 
	public Document getDistinctValue(final String collectionName, final String fieldName, final Bson filter);
 
	public List<Document> projections(final String collectionName, final List<String> fieldName);
 	
	public List<Document> projections(final String collectionName, final List<String> fieldName, final Bson filter);
}

