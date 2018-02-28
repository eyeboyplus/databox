package databox;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class DataSetInfoReader {
	
	private static String TAG_NAME_TABLE_NAME = "table-name";
	private static String TAG_NAME_TABLE_PK = "pk";
	
	private static String TAG_NAME_TABLE_FK = "fk";
	
	
	private Document document;
	
	public DataSetInfoReader(String configFileName) throws DocumentException {
		SAXReader reader = new SAXReader();
		this.document = reader.read(new File(configFileName));
	}
	
	public Map<String, String> getPrimaryKeyMap() {
		Map<String, String> res = new HashMap<String, String>();
	
		List<Node> tableElements = document.selectNodes("//dataset/table");
		for(Node node : tableElements) {
			Element table = (Element) node;
			String tableName = null;
			String pk = null;
			for(Iterator<Element> it = table.elementIterator(); it.hasNext();) {
				Element childElement = it.next();
				if(TAG_NAME_TABLE_NAME.equals(childElement.getName()))
					tableName = childElement.getTextTrim();
				if(TAG_NAME_TABLE_PK.equals(childElement.getName()))
					pk = childElement.getTextTrim();
			}
			if(tableName != null && pk != null)
				res.put(tableName, pk);
		}
		return res;
	}
	
	public Map<String, List<String>> getForeignKeyMap() {
		Map<String, List<String>> res = new HashMap<String, List<String>>();
		
		List<Node> tableElements = document.selectNodes("//dataset/table");
		for(Node node : tableElements) {
			Element table = (Element) node;
			String tableName = null;
			List<String> fks = null;
			for(Iterator<Element> it = table.elementIterator(); it.hasNext();) {
				Element childElement = it.next();
				if(TAG_NAME_TABLE_NAME.equals(childElement.getName()))
					tableName = childElement.getTextTrim();
				if(TAG_NAME_TABLE_FK.equals(childElement.getName()))
					fks.add(childElement.getTextTrim());
			
			}
		}
		
		return res;
	}
}
