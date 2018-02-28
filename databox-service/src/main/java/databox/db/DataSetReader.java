package databox.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class DataSetReader {
	private JsonReader reader;
	
	public void load(String fileName) throws UnsupportedEncodingException, FileNotFoundException{
		reader = new JsonReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
		
	}
	
	public List<DataSet> getAllDataSet() throws IOException{
		List<DataSet> res = new ArrayList<DataSet>();
		
		reader.beginArray();
		while (reader.hasNext()){
			res.add(readDataSet(reader));
		}
		reader.endArray();
		
		reader.close();
		
		return res;
	}
	
	public DataSet readDataSet(JsonReader reader) throws IOException{
		String name = null;
		String description = null;
		List<Table> tables = null;
		
		reader.beginObject();
		while(reader.hasNext()){
			String key = reader.nextName();
			if (key.equals("name")){
				name = reader.nextString();
			}
			else if (key.equals("description")){
				description  = reader.nextString();
			}
			else if (key.equals("table")){
				tables = readTableList(reader);
			}
		}
		reader.endObject();
		return new DataSet(name,description,tables);
	}
	
	public List<Table> readTableList(JsonReader reader) throws IOException{
		List<Table> tables= new ArrayList<Table>();
		
		reader.beginArray();
		while (reader.hasNext()){
			tables.add(readTable(reader));
		}
		reader.endArray();
		return tables;	
	}
	
	public Table readTable(JsonReader reader) throws IOException{
		String name = null;
		String localName = null;
		String description = null;
		List<String> pks = null;
		List<String> fks = null;
		List<Field> fields = null;
		
		reader.beginObject();
		while(reader.hasNext()){
			String key = reader.nextName();
			if (key.equals("name")){
				name = reader.nextString();
			}
			else if (key.equals("localname")){
				localName = reader.nextString();
			}
			else if (key.equals("description")){
				description = reader.nextString();
			}
			else if (key.equals("pk")&&reader.peek() != JsonToken.NULL){
				pks = readStringArray(reader);
			}
			else if (key.equals("fk")&& reader.peek() != JsonToken.NULL){
				fks = readStringArray(reader);
			}
			else if (key.equals("field")){
				fields = readFields(reader);
			}
		}
		reader.endObject();
		return new Table(name, localName,description,pks,fks,fields);
	}

	private List<Field> readFields(JsonReader reader) throws IOException {
		List<Field> fields = new ArrayList<Field>();
		
		reader.beginArray();
		while(reader.hasNext()){
			fields.add(readField(reader));
		}
		 reader.endArray();
		 return fields;
	}

	private Field readField(JsonReader reader) throws IOException {
		String name =null;
		String localName=null;
		String type=null;
		
		reader.beginObject();
		while (reader.hasNext()){
			String key = reader.nextName();
			if (key.equals("name")){
				name = reader.nextString();
			}
			else if (key.equals("localname")){
				localName = reader.nextString();
			}
			else if(key.equals("type")){
				type = reader.nextString();
			}
		}
		reader.endObject();
		return new Field(name,localName,type);
	}

	private List<String> readStringArray(JsonReader reader) throws IOException {
		List<String> string = new ArrayList<String>();
		
		reader.beginArray();
		while (reader.hasNext()){
			string.add(reader.nextString());
		}
		reader.endArray();
		return string;
	}
	
}

