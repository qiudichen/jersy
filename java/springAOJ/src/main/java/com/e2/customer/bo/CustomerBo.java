package com.e2.customer.bo;

public interface CustomerBo {
	void addCustomer();
	 
	String addCustomerReturnValue();
 
	void addCustomerThrowException() throws Exception;
 
	void addCustomerAround(String name);
	
	void anotationMethod(String name);

}
