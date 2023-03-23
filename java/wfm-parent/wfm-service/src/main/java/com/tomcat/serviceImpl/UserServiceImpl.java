package com.tomcat.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class UserServiceImpl implements UserService {
	
	private Map<String, String> userMap = new HashMap<>();

	public UserServiceImpl() {
		userMap.put("agent1", "agent1");
		userMap.put("agent2", "agent2");
		userMap.put("agent3", "agent3");
		userMap.put("agent4", "agent4");
		userMap.put("agent5", "agent5");
	}
	
	@Override
	public boolean loing(String user, String password) {
		if(userMap.containsKey(user)) {
			return password.equals(userMap.get(user));
		}
		return false;
	}

	@Override
	public void mbean() {
		// TODO Auto-generated method stub
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	    Set<ObjectInstance> instances = server.queryMBeans(null, null);
        
        Iterator<ObjectInstance> iterator = instances.iterator();
         
        //Class Name:torg.apache.catalina.mbeans.ConnectorMBean
        //Object Name:tCatalina:type=Connector,port=8080
        
        while (iterator.hasNext()) {
            ObjectInstance instance = iterator.next();
            ObjectName obj = instance.getObjectName();
            System.out.println("MBean Found:");
            System.out.println("Class Name:t" + instance.getClassName());
            System.out.println("Object Name:t" + instance.getObjectName());
            System.out.println("****************************************");
        }
        
        try {
        	Set<ObjectName> objs = server.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
        	for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
        		ObjectName obj = i.next();
        		String port = obj.getKeyProperty("port");
        		System.out.print(port);
        	}
			ObjectName objectName=new ObjectName("Catalina:type=Connector,port=8080");
			Object port = server.getAttribute(objectName, "port");
			System.out.print(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Override
	public void foo() {
		String msg = "change at UserService 13";
		System.out.println(msg);
	
		//java.util.Properties properties = System.getProperties();
		//java.util.SortedSet<Object> keys = new java.util.TreeSet<>(properties.keySet());
		
		String path = "tvconfig/tv.default.properties";
		
		
		StringBuilder builder = new StringBuilder();
		try {
			System.out.println("========= open stream====================");
			Properties prop = openStream(path);
		    for (Object key: prop.keySet()) {
		    	builder.append(key + ": " + prop.getProperty(key.toString()));
		    }
		    System.out.println("=======" + builder.toString());

		    System.out.println("========= open Resource====================");
		    builder = new StringBuilder();
		    Properties prop1 = openResource(path);
		    for (Object key: prop1.keySet()) {
		    	builder.append(key + ": " + prop.getProperty(key.toString()));
		    }
		    System.out.println("=======" + builder.toString());
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Properties openStream(String path) throws Exception {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ClassLoader loader1 = UserServiceImpl.class.getClassLoader();
		
		InputStream in0 = loader.getResourceAsStream(path);

		InputStream in = loader1.getResourceAsStream(path);
		if(in != null) {
			prop.load(in);
		}
		return prop;
	}
	
	private Properties openResource(String path) throws Exception {
		Resource resource = new ClassPathResource(path);
		Properties props = new Properties();
		StringBuilder builder = new StringBuilder();

		String fileEncoding = null;
		PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
		
		fillProperties(
				props, new EncodedResource(resource, fileEncoding), propertiesPersister);
		return props;
	}
	
	private static final String XML_FILE_EXTENSION = ".xml";
	static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister)
			throws IOException {
		
		InputStream stream = null;
		Reader reader = null;
		try {
			String filename = resource.getResource().getFilename();
			if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
				stream = resource.getInputStream();
				persister.loadFromXml(props, stream);
			}
			else if (resource.requiresReader()) {
				reader = resource.getReader();
				persister.load(props, reader);
			}
			else {
				stream = resource.getResource().getInputStream();
				persister.load(props, stream);
			}
		}
		finally {
			if (stream != null) {
				stream.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public static void main(String argv[]) {
		UserServiceImpl userService = new UserServiceImpl();
		userService.foo();
	}
}
