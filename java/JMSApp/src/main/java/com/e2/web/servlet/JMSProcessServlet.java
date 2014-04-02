package com.e2.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.e2.web.service.ServiceBo;

@SuppressWarnings("serial")
public class JMSProcessServlet extends HttpServlet {
	@Autowired
	private ServiceBo service;
	private String message;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String msg = request.getParameter("msg");
		if(msg == null || msg.isEmpty()) {
			msg = message;
		}
		service.process(message);
		System.out.println(message);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		doGet(arg0, arg1);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		message = config.getInitParameter("urlPrefix");
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}
}
