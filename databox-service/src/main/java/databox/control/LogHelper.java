package databox.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LogHelper {
	public static String logToJson(Log log) {
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
		String json = gson.toJson(log);  
		return json;
	}
}