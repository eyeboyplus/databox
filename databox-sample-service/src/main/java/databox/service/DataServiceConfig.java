package databox.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataServiceConfig {
	private Properties properties;
	
	public DataServiceConfig(InputStream in) {
		properties = new Properties();
		try {
			properties.load(new BufferedInputStream(in));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DataServiceConfig(String fileName) {
		properties = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(fileName));
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getDBIp() {
		return properties.getProperty("ip");
	}
	
	public int getDBPort() {
		return Integer.parseInt(properties.getProperty("port"));
	}
}
