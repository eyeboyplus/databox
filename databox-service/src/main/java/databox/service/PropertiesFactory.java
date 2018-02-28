package databox.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import databox.exception.ConfigException;
import databox.exception.ConfigNotFoundException;

public class PropertiesFactory {
	public static Properties getProperties(InputStream in) {
		Properties properties = new Properties();
		try {
			properties.load(new BufferedInputStream(in));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static Properties getProperties(String fileName) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			properties.load(new BufferedInputStream(new FileInputStream(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
