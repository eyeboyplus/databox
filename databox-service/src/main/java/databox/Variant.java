package databox;

import java.io.Serializable;

public class Variant implements Serializable {
	private Object obj;
	
	public Variant(Object obj) {
		this.obj = obj;
	}
	public Variant(int value) {
		obj = Integer.valueOf(value);
	}
	
	public Variant(double value) {
		obj = Double.valueOf(value);
	}
	
	public Variant(long value) {
		obj = Long.valueOf(value);
	}
	
	public Variant(short value) {
		obj = Short.valueOf(value);
	}
	
	public Variant(char c) {
		obj = Character.valueOf(c);
	}
	
	public Variant(String s) {
		obj = s;
	}
	
	public double toDouble() {
		if(obj instanceof Double)
			return (Double) obj;
		else if(obj instanceof Integer)
			return (Integer)obj;
		else if(obj instanceof Long)
			return (Long)obj;
		else if(obj instanceof Short)
			return (Short)obj;
		else if(obj instanceof Character)
			return (Character)obj;
		else if(obj instanceof String)
			return Double.valueOf((String)obj);
		
		return Double.NaN;
	}
	
	@Override
	public String toString() {
		return obj.toString();
	}
}
