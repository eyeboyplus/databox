package databox.db;

import java.util.List;

public class Table {
	private String name;
	private String localName;
	private String description;
	private List<String> primaryKeys;
	private List<String> foreignKeys;
	private List<Field>  fields;
	
	public Table(String name, String localName, String description, List<String> pks, List<String> fks,
			List<Field> fields) {
		this.name =name;
		this.localName = localName;
		this.description = description;
		this.primaryKeys =pks;
		this.foreignKeys = fks;
		this.fields = fields;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(List<String> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public List<String> getForeignKeys() {
		return foreignKeys;
	}
	public void setForeignKeys(List<String> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
}

