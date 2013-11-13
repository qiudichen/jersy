package com.e2.web.dao;

import java.util.List;

import com.e2.web.model.Customer;

public interface CustomerDao {
	void addCustomer(Customer customer);
	
	List<Customer> findAllCustomer();
}
