package com.nice.wfm.security.jmx;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXClient {
	private final static String CONNECTION_URL = "service:jmx:rmi:///jndi/rmi://{REPLACE}/jmxrmi"; 
	
	public static void main(String[] args) {
		String host = "localhost";
		String port = "16301";
		String user = "tvsys";
		
		String password = "iex.prms";
		
		String urlString = CONNECTION_URL.replace("{REPLACE}", host + ":" + port);
		
        Map<String, String[]> environment = null;
        if(null != user && null != password) {
	        environment = new HashMap<String, String[]>();
	        String[] credentials = new String[] {user, password};
	        environment.put(JMXConnector.CREDENTIALS, credentials);
        }
        
        try {
	        JMXServiceURL serviceURL = new JMXServiceURL(urlString);
	        JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, environment);
	        MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
	        
	//        writeAttributes(mBeanServerConnection);
	        getWarDeployStatus(mBeanServerConnection);
	        jmxConnector.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        
	}

	protected static void getWarDeployStatus(MBeanServerConnection mbeanServerConn) throws Exception {
		
		
		ObjectName name = new ObjectName("com.bard.mbean:type=ConcreteImplementation");

		//get virtual host names:
		Set<ObjectName> names = mbeanServerConn.queryNames(name, null);
		
		for(ObjectName objName : names) {
			System.out.println(objName.getCanonicalKeyPropertyListString());
			System.out.println(objName.getCanonicalName());
			System.out.println(objName.getDomain());
			System.out.println(objName.getKeyPropertyListString());
			System.out.println(objName.getKeyPropertyList());
			System.out.println(objName.getCanonicalKeyPropertyListString());
			System.out.println(objName.getInstance(name));
			
			try {
				com.sun.jmx.remote.security.FileLoginModule f = null;
				
				Object attr1 = mbeanServerConn.getAttribute(objName, "name");
				System.out.println(attr1);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}	
	
	private static void writeAttributes(MBeanServerConnection mbeanServerConn)
	{
		try {
			String domains[] = mbeanServerConn.getDomains(); 
			for (int i = 0; i < domains.length; i++) { 
			      System.out.println("Domain[" + i + "] = " + domains[i]); 
			} 
			  
			String domain = mbeanServerConn.getDefaultDomain();    
			  
			Set<ObjectName> names = mbeanServerConn.queryNames(null, null); 
			  
			for (ObjectName objName : names ) { 
				  Hashtable<String, String> list = objName.getKeyPropertyList();
				  System.out.println(list);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
