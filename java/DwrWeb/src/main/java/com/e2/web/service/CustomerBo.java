package com.e2.web.service;

import java.util.List;

import com.e2.web.model.Customer;

public interface CustomerBo {
	void addCustomer(Customer customer);
	
	List<Customer> findAllCustomer();
}
