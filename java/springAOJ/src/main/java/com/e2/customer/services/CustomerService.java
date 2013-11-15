package com.e2.customer.services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.e2.customer.bo.CustomerBo;
import com.e2.customer.bo.ServiceBo;

public class CustomerService {
	private String name;
	private String url;
 
	public void setName(String name) {
		this.name = name;
	}
 
	public void setUrl(String url) {
		this.url = url;
	}
 
	public void printName() {
		System.out.println("Customer name : " + this.name);
	}
 
	public void printURL() {
		System.out.println("Customer website : " + this.url);
	}
 
	public void printThrowException() {
		throw new IllegalArgumentException();
	}
	
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "Spring-Customer.xml" });
 
		CustomerBo customer = (CustomerBo) appContext.getBean("customerBo");
		customer.anotationMethod("name");
//		ServiceBo service = (ServiceBo)appContext.getBean("serviceBo");
//
//		customer.anotationMethod("");
//		
//		service.callService("");
//		customer.addCustomer();
//		customer.addCustomerAround("");
//		customer.addCustomerReturnValue();
//		try {
//			customer.addCustomerThrowException();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		
		Object obj = appContext.getBean("customerBo");;
		CustomerBo cust = (CustomerBo) appContext.getBean("customerBo");
 
		System.out.println("*************************");
		customer.addCustomer();
		System.out.println("*************************");
		customer.addCustomer();
		System.out.println("*************************");
		try {
			customer.addCustomer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
