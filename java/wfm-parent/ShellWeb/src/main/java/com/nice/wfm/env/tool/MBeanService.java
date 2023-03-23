package com.nice.wfm.env.tool;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class MBeanService {

	private static MBeanService instance;

	MBeanServer mBeanServer;

	private MBeanService() {
		mBeanServer = ManagementFactory.getPlatformMBeanServer();
	}

	public static MBeanService getInstance() {
		if (instance == null) {
			instance = new MBeanService();
		}
		return instance;
	}

	public void listObjects(PrintWriter writer) {
		Set<?> mbeans = mBeanServer.queryNames(null, null);
		for (Object mbean : mbeans) {
			try {
				WriteAttributes(mBeanServer, (ObjectName) mbean, writer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void WriteAttributes(final MBeanServer mBeanServer, final ObjectName http, PrintWriter writer)
			throws InstanceNotFoundException, IntrospectionException, ReflectionException {
		MBeanInfo info = mBeanServer.getMBeanInfo(http);
		MBeanAttributeInfo[] attrInfo = info.getAttributes();

		writer.println(" ================Begin========================== ");
		writer.println("Attributes for object: " + http + ":\n");
		for (MBeanAttributeInfo attr : attrInfo) {
			
			try {
				Object v = mBeanServer.getAttribute(http, attr.getName());
				if(v != null) {
					writer.println("Attribute Name: " + attr.getName() + "; value: " + v.toString());
				} else {
					writer.println("Attribute Name: " + attr.getName() + "; value: NULL");					
				}
				
			} catch (Exception e) {
				writer.println(attr.getName());
			}
			
//			writer.print("name:  " + attr.getName() + "\n");
//			writer.println("type:  " + attr.getType() + "\n");
//			Descriptor descriptor = attr.getDescriptor();
//			if(descriptor != null) {
//				String[] fields = descriptor.getFields();
//				String[] fieldNames = descriptor.getFieldNames();
//				if(fieldNames.length > 0) {
//					Object[] objs = descriptor.getFieldValues(fieldNames);	
//					for(int i = 0; i < fieldNames.length; i++) {
//						writer.println("field Name: " + fieldNames[i] + "; field Value: " + objs[i]);										
//					}
//				}
//			}
		}
		writer.println(" ================end========================== ");
	}
}
