package databox.control;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	private String serviceName;
	
	private String relCol;
	private List<String> relFields;
	private List<String> relDataList;
	private Map<String, List<String>> fkList;
	
	private String createdTime;
	
	public Log(
			final String serviceName,
			final String collectionName, 
			final List<String> fieldNames,
			final List<String> relDataList, 
			final Map<String, List<String>> fkList) {
		this.serviceName = serviceName;
		
		this.relCol = collectionName;
		this.relFields = fieldNames;
		this.relDataList = relDataList;
		this.fkList = fkList;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		this.createdTime = sdf.format(new Date());		
	}
	
	public Log(
			final String serviceName,
			final String collectionName, 
			final List<String> fieldNames,
			final List<String> relDataList, 
			final Map<String, List<String>> fkList,
			final Date date) {
		this.serviceName = serviceName;
		
		this.relCol = collectionName;
		this.relFields = fieldNames;
		this.relDataList = relDataList;
		this.fkList = fkList;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		this.createdTime = sdf.format(date);		
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getRelCol() {
		return relCol;
	}

	public void setRelCol(String relCol) {
		this.relCol = relCol;
	}

	public List<String> getRelFields() {
		return relFields;
	}

	public void setRelFields(List<String> relFields) {
		this.relFields = relFields;
	}

	public List<String> getRelDataList() {
		return relDataList;
	}

	public void setRelDataList(List<String> relDataList) {
		this.relDataList = relDataList;
	}

	public Map<String, List<String>> getFkList() {
		return fkList;
	}

	public void setFkList(Map<String, List<String>> fkList) {
		this.fkList = fkList;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
}
