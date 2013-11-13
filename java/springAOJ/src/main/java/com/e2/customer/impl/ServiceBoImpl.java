package com.e2.customer.impl;

import com.e2.aspect.PerformanceLogable;
import com.e2.customer.bo.CustomerBo;
import com.e2.customer.bo.ServiceBo;

public class ServiceBoImpl implements ServiceBo {

	private CustomerBo customer;
	
	
	public void setCustomer(CustomerBo customer) {
		this.customer = customer;
	}


	@Override
	@PerformanceLogable
	public void callService(String name) {
		System.out.println(name);
		customer.addCustomer();
		System.out.println("end");
	}

}
