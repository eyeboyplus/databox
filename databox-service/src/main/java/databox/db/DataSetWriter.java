package databox.db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.gson.stream.JsonWriter;

public class DataSetWriter {
	private JsonWriter writer ;
	public void outPut(String fileName) throws UnsupportedEncodingException, FileNotFoundException{
		writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"));
		writer.setIndent("    ");
	}
	
	public void writeDataSets(List<DataSet> datasets) throws IOException{
		writer.beginArray();
		for(DataSet dataset : datasets){
			writeDataSet(dataset);
		}
		writer.endArray();
		
		writer.close();
	}

	private void writeDataSet(DataSet dataset) throws IOException {
		writer.beginObject();
		writer.name("name").value(dataset.getName());
		writer.name("description").value(dataset.getDescription());
		writer.name("tables");
		writeTables(dataset.getTables());
		writer.endObject();
		
	}

	private void writeTables(List<Table> tables) throws IOException {
		writer.beginArray();
		for (Table table : tables){
			writeTable(table);
		}
		writer.endArray();
	}

	private void writeTable(Table table) throws IOException {
		writer.beginObject();
		writer.name("name").value(table.getName());
		writer.name("localName").value(table.getLocalName());
		writer.name("description").value(table.getDescription());
		writer.name("primaryKeys");
		writeStringArrary(table.getPrimaryKeys());
		writer.name("foreignKeys");
		writeStringArrary(table.getForeignKeys());
		writer.name("fields");
		writeFields(table.getFields());
		writer.endObject();
		
	}

	private void writeFields(List<Field> fields) throws IOException {
		writer.beginArray();
		for(Field field : fields){
			writeField(field);
		}
		writer.endArray();
	}

private void writeField(Field field) throws IOException {
		writer.beginObject();
		writer.name("name").value(field.getName());
		writer.name("localName").value(field.getLocalName());
		writer.name("type").value(field.getType());
		writer.endObject();
	}

	private void writeStringArrary(List<String> values) throws IOException {
		writer.beginArray();
		for(String value : values){
			writer.value(value);
		}
		writer.endArray();
	}
	
	
}

