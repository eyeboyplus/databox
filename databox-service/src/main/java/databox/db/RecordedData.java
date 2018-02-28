package databox.db;

import databox.control.Log;

public class RecordedData {
	private Log log;
	private Object object;
	private boolean isAllowed;
	
	public RecordedData(Log log, Object data) {
		this.log = log;
		object = data;
		isAllowed = true;
	}
	
	public boolean isAllowed() {
		return this.isAllowed;
	}
	
	public void setAllowFlag(boolean flag) {
		this.isAllowed = flag;
	}
	
	public Log getLog() {
		return log;
	}
	
	public void setLog(Log log) {
		this.log = log;
	}
	
	public Object getData() {
		return object;
	}
	public void setData(Object object) {
		this.object = object;
	}
}
