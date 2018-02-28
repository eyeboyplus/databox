package databox.db;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
	private String name;
	private String description;
	private List<Table> tables;
	
	public DataSet(String name, String description) {
		this.name = name;
		this.description = description;
		this.tables = new ArrayList<Table>();
	}
	
	public DataSet(String name, String description, List<Table> tables) {
		this.name = name;
		this.description = description;
		this.tables = tables;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addTable(Table table) {
		this.tables.add(table);
	}
	public List<Table> getTables() {
		return tables;
	}
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
}

