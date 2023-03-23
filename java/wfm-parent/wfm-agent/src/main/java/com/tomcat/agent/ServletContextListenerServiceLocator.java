package com.tomcat.agent;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.tomcat.serviceImpl.ServiceLocator;

//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletContextEvent;
//import jakarta.servlet.ServletContextListener;

public class ServletContextListenerServiceLocator implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServiceLocator.setServletContext(null);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("============Agent===================");
		ServletContext servletContext = event.getServletContext();
		ServiceLocator.setServletContext(servletContext);
	}
}
