package databox.db;

public class Field {
	private String name;
	private String localName;
	private String type;
	
	public Field(String name, String localName, String type) {
		this.name =name;
		this.localName = localName;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

