package com.jkenoyer.kidtrackerapi;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public static String Get(String key) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("properties");
			prop.load(input);
			return prop.getProperty(key);

		} catch(Exception error) {
			System.out.println(error.getMessage());
		}
		
		return null;
	}
}
