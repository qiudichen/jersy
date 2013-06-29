package com.e2.restful.services;

import org.springframework.stereotype.Service;

//@Service("TransactionBoImpl")
public class TransactionBoImpl implements TransactionBo {
	
	public TransactionBoImpl() {
		System.out.println();
	}
	
	@Override
	public String save() {
		return "Jersey + Spring example";
	}
}
