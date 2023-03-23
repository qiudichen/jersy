package com.tomcat.serviceImpl;

import javax.servlet.ServletContext;

//import jakarta.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServiceLocator {
	private static ServletContext servletContext;
	
	private static ApplicationContext applicationContext;

	public static void setServletContext(ServletContext servletContext) {
		ServiceLocator.servletContext = servletContext;
	}
	
    public static ApplicationContext getWebApplicationContext() throws Exception
    {
        if (servletContext != null)
        {
            return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        }
        else if (applicationContext != null)
        {
            return applicationContext;
        }
        else
        {
            throw new Exception(new Exception("ServletContext is not initialized"));
        }
    }	
    
    public static UserService getUserService() throws Exception
    {
    	return getBean(UserService.class);
    } 
    
    public static <T> T getBean(Class<T> requiredType) throws Exception {
    	return getWebApplicationContext().getBean(requiredType);
    }
}
