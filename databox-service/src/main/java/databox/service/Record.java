package databox.service;

import java.util.HashMap;
import java.util.Map;

import databox.Variant;

public class Record {
	private Map<String,String> map = null;
	
	public Record() {
		this.map = new HashMap<String, String>();
	}
	
	public void setValue(String key,String value){
		map.put(key, value);
	}
	
	public Variant getValue(String key){
		return new Variant(map.get(key));
	}
}
