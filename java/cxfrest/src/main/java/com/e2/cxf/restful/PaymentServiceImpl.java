package com.e2.cxf.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

@Component
public class PaymentServiceImpl implements PaymentService {

	public PaymentServiceImpl() {
		System.out.println();
	}
	
	@GET
	@Path("/save/{userName}")
	@Produces(MediaType.TEXT_PLAIN)
	public String savePayment(@PathParam("userName")String userName, @QueryParam("amount")int amount) {
		String result = "successful";
		return result;
	}	 
	
	@GET
	@Path("/transaction/{userName}/{amount}")
	@Produces(MediaType.APPLICATION_XML)
	public Transaction transaction(@PathParam("userName")String userName, @PathParam("amount")int amount) {
		String result = "successful";
		return new Transaction(result, amount,  userName);
	}	 	
}