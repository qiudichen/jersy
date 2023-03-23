package com.tomcat.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertyLoader {

	public String load(String path) {
		
		Properties prop = new Properties();
		StringBuilder builder = new StringBuilder();
		try {
			InputStream in = getClass().getResourceAsStream(path);
			if(in == null) {
				builder.append("No tvconfig/tv.default.properties file found in classpath.");
			} else {
				prop.load(in);
		       for (Object key: prop.keySet()) {
		    	   builder.append(key + ": " + prop.getProperty(key.toString()));
		        }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
}
