package com.e2.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.e2.web.model.Customer;
import com.e2.web.service.CustomerBo;

@SuppressWarnings("serial")
public class ActionServletWired extends HttpServlet {

	@Autowired
	private CustomerBo customerBo;
	
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		List<Customer> list = customerBo.findAllCustomer();
		System.out.println(list);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		doGet(arg0, arg1);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String value = config.getInitParameter("urlPrefix");
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

}
