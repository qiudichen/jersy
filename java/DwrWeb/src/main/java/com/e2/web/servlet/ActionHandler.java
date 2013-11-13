package com.e2.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import com.e2.web.model.Customer;
import com.e2.web.service.CustomerBo;

@Component("actionHandler")
public class ActionHandler implements HttpRequestHandler {

	@Autowired
	private CustomerBo customerBo;

	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Customer> list = customerBo.findAllCustomer();
		System.out.println(list);
	}
}
