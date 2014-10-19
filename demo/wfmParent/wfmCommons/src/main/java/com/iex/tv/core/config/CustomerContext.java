package com.iex.tv.core.config;

import java.util.Arrays;
import java.util.List;

public class CustomerContext implements ICustomerContext {
	
	private Long customerId;
	
	private String customerName;

	private String dataSourceClassName;
	private String dbDriver;
	private String dbURL;
	private String dbUser;
	private String dbPassword;	
	
	private String dbJNDI;

	public CustomerContext(Long customerId) {
		this(customerId, "default");
	}
	
	public CustomerContext(Long customerId, String customerName) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
	}

	@Override
	public Long getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbJNDI() {
		return dbJNDI;
	}

	public void setDbJNDI(String dbJNDI) {
		this.dbJNDI = dbJNDI;
	}
}
