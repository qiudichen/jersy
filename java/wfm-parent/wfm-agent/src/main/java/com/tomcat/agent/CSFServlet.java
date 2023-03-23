package com.tomcat.agent;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;

import com.tomcat.domain.BaseSingletion;
import com.tomcat.domain.WebSingleton;

public class CSFServlet extends HttpServlet {
    @Override
    public void destroy()
    {
    }
    
    @Override
    public void service(ServletRequest arg0Parm, ServletResponse arg1Parm) throws IOException, ServletException
    {
    	System.out.println("============Agent===================");
        HttpServletRequest request = (HttpServletRequest) arg0Parm;
        HttpServletResponse response = (HttpServletResponse) arg1Parm;
        
        try {
        	WebSingleton webDomain = WebSingleton.getInstance();
        	System.out.println(webDomain.foo());
        } catch(Throwable e) {
        	e.printStackTrace();
        }
        
        try {
        	BaseSingletion baseDomain = BaseSingletion.getInstance(); 
        	System.out.println(baseDomain.foo());
        } catch(Throwable e) {
        	e.printStackTrace();
        }        
    }
    
    @Override
    public void init(ServletConfig arg0Parm) throws ServletException
    {
    	XPath xpath = null;
    	
    }
}
