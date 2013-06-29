package com.e2.restful.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.e2.restful.services.TransactionBo;

@Component
@Path("/payment")
public class PaymentService {
	@Autowired
	@Qualifier("TransactionBoImpl")
	TransactionBo transactionBo;
 
	public PaymentService() {
		System.out.println();
	}
	
	@GET
	@Path("/save")
	public Response savePayment() {
		String result = transactionBo.save();
		return Response.status(200).entity(result).build();
	}
}