package com.e2.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e2.web.annotation.TransactionMethod;
import com.e2.web.dao.CustomerDao;
import com.e2.web.model.Customer;
import com.e2.web.service.CustomerBo;

@Service("customerBoImpl")
public class CustomerBoImpl implements CustomerBo {
	
	@Autowired
	private CustomerDao customerDao;
	
	@TransactionMethod(readOnly=true, timeout=100)
	public void addCustomer(Customer customer) {
		System.out.println("<--- customer is addes.");
		customerDao.addCustomer(customer);
	}

	public List<Customer> findAllCustomer() {
		return customerDao.findAllCustomer();
	}
}
