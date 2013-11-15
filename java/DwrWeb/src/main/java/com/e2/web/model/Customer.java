package com.e2.web.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.e2.web.service.CustomerBo;

@Configurable
public class Customer implements java.io.Serializable {

	@Autowired
	@Qualifier("customerBoImpl")
	private CustomerBo customerBo;
	
	private long customerId;
	private String name;
	private String address;
	private Date createdDate;

	public Customer() {
	}

	public Customer(long customerId, String name, String address,
			Date createdDate) {
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.createdDate = createdDate;
	}

	public long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "/app-context1.xml" });
 
		Customer customer = new Customer();
		System.out.println(customer);
	}	
}

