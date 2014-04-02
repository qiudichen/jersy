package com.e2.customer.bo;

import java.util.Date;
import java.util.Calendar;

public class Query {
	public void setParameter(String name, Date date) {
		System.out.println("set date");
	}
	
	public void setParameter(String name, Calendar calendar) {
		System.out.println("set calendar");
	}	
	
	public void setParameter(String name, Object obj) {
		System.out.println("set object");
	}	
	
	public static void main(String argv[]) {
		Query query = new Query();
		
		Object obj = new String("");
		query.setParameter("abc", obj);
		obj = new Date();
		query.setParameter("abc", (Date)obj);
		
		obj = Calendar.getInstance();
		query.setParameter("abc", (Calendar)obj);
	}
}
