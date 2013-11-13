package com.e2.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.e2.web.model.Customer;
import com.e2.web.service.CustomerBo;
import com.e2.web.util.SpringUtil;

@SuppressWarnings("serial")
public class ActionServlet extends HttpServlet {

	private CustomerBo customerBo;
	
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		List<Customer> list = customerBo.findAllCustomer();
		CustomerBo customerBo1 = SpringUtil.getSpringBean("customerBoImpl", CustomerBo.class);
		System.out.println(list);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		doGet(arg0, arg1);
	}

	@Override
	public void init(ServletConfig sconfig) throws ServletException {
		super.init(sconfig);
		String value = sconfig.getInitParameter("urlPrefix");
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		customerBo = (CustomerBo) ctx.getBean("customerBoImpl");		
	}
}
