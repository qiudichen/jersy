package com.tomcat.supv;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.tomcat.serviceImpl.ServiceLocator;

public class ServletContextListenerServiceLocator implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServiceLocator.setServletContext(null);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("============Supv===================");
		ServletContext servletContext = event.getServletContext();
		ServiceLocator.setServletContext(servletContext);
	}
}
