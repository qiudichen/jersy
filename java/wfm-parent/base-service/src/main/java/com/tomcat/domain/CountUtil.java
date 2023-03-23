package com.tomcat.domain;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

public class CountUtil {
	private static int count = 0;
	
	public static int increase() {
		count++;
		return count;
	}
	
	public static void main(String[] argv) {
		
		try {
	        String objectName = "com.javacodegeeks.snippets.enterprise:type=Hello";
	         
	        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	 
	        // Construct the ObjectName for the Hello MBean we will register
	        ObjectName mbeanName = new ObjectName(objectName);
	         
	        Hello mbean = new Hello();
	        Hello mbean1 = new Hello();
	         
	        server.registerMBean(mbean, mbeanName);
	        server.registerMBean(mbean1, mbeanName);
	         
	        Set<ObjectInstance> instances = server.queryMBeans(new ObjectName(objectName), null);
	         
	        ObjectInstance instance = (ObjectInstance) instances.toArray()[0];
	         
	        System.out.println("Class Name:t" + instance.getClassName());
	        System.out.println("Object Name:t" + instance.getObjectName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		List<String> jvmArgs = bean.getInputArguments();
		System.out.println(jvmArgs); 
	}	
}
