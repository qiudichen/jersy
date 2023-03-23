package com.nice.wfm.security.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;


public class JMXServer {
    public static final String name = "defaultName";
    public static final int number = 100;
    
	public static void main(String[] args) {
    	try {
	        //get MBean Server
	        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	        //register MBean
	        ImplementationMBean mBean = new Implementation(name, number);
	        ObjectName name = new ObjectName("com.bard.mbean:type=ConcreteImplementation");
	
	        mbs.registerMBean(mBean, name);
	
	        do{
	            Thread.sleep(60000);
	            System.out.println("Name = " + mBean.getName() + ", number = " + mBean.getNumber());
	        } while(mBean.getKilled() == false);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
