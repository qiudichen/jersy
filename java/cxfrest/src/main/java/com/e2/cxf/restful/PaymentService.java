package com.e2.cxf.restful;

import javax.ws.rs.core.Response;

public interface PaymentService {
	public String savePayment(String userName, int amount);
	public Transaction transaction(String userName, int amount);
}
