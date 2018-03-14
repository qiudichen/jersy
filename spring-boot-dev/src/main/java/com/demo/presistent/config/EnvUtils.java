package com.demo.presistent.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

public class EnvUtils {
	
	 public static Map<String, Object> getEnvMap(Environment env, String sourceName, String extSourceName) {
		 Map<String, Object> map = new HashMap<String, Object>();
		 Map<String, Object> mapEx = new HashMap<String, Object>();
		 
		 AbstractEnvironment absEnv = (AbstractEnvironment) env;
		 
		 MutablePropertySources propertySources = absEnv.getPropertySources();

		 Iterator<PropertySource<?>> it = propertySources.iterator();

		 boolean flag = true;
		 boolean flagEx = true;
		 
         while(it.hasNext() && (flag || flagEx)) {
        	 	PropertySource<?> propertySource = it.next();
            if (propertySource instanceof MapPropertySource) {
            		MapPropertySource mapPropertySource = (MapPropertySource)propertySource;
            		if(mapPropertySource.getName().equals(sourceName)) { 	           		
            			map.putAll(mapPropertySource.getSource());
            			flag = false;
            		}
            		
            		if(mapPropertySource.getName().equals(extSourceName)) { 	           		
            			mapEx.putAll(mapPropertySource.getSource());
            			flagEx = false;
            		}
            } 
         }	
         
         if(!mapEx.isEmpty()) {
        	 	map.putAll(mapEx);
         }
         return map;
	 }
	 
	 public static Properties getPrefixProperties(Environment env, String sourceName, String extSourceName, String prefix) {
		 Map<String, Object> map = getEnvMap(env, sourceName, extSourceName);
		 return getPrefixProperties(prefix, map) ;
	 }
	 
	 public static Properties getPrefixProperties(String prefix, Properties propParent) {
		 return getPrefixProperties(prefix, propParent, false);
	 }
	 
	 public static Properties getPrefixProperties(String prefix, Properties propParent, boolean removePrefix) {
		 Properties prop = new Properties();

		 for(Map.Entry<Object, Object> entry : propParent.entrySet()) {
			 String key = (String)entry.getKey();
			 if(key.startsWith(prefix)) {
				 String subkey = removePrefix ? key.substring(prefix.length()) : key;
				 String value = entry.getValue().toString();
				 prop.setProperty(subkey, value);
			 }
		 }
		 return prop;
	 }
	 
	 public static Properties getPrefixProperties(String prefix, Map<String, Object> map) {
		 Properties prop = new Properties();
		 for(Map.Entry<String, Object> entry : map.entrySet()) {
			 String key = entry.getKey();
			 if(key.startsWith(prefix)) {
				 String subkey = key.substring(prefix.length());
				 String value = entry.getValue().toString();
				 prop.setProperty(subkey, value);
			 }
		 }
		 return prop;
	 }
}
