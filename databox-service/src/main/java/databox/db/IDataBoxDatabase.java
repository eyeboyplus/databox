package databox.db;

import java.util.List;

import org.bson.conversions.Bson;

public interface IDataBoxDatabase {
	public RecordedData getData(final String collectionName);
	public RecordedData getAllValue(final String collectionName, final String fieldName);
	public RecordedData getAverage(final String collectionName, final String fieldName, final Bson filter);
	public RecordedData getConditionCount(final String collectionName, final Bson filter);
	public RecordedData dataCount(final String collectionName);
	public RecordedData getFilterData(final String collectionName, final Bson filter);
	public RecordedData projections(final String collectionName, final List<String> fieldName);
	public RecordedData projections(final String collectionName, final List<String> fieldName, final Bson filter);
	public RecordedData getMax(final String collectionName, final String fieldName, final Bson filter);
	public RecordedData getMin(final String collectionName, final String fieldName, final Bson filter);
	public RecordedData getDistinctValue(final String collectionName, final String fieldName);
	public RecordedData getDistinctValue(final String collectionName, final String fieldName, final Bson filter);
 
}
