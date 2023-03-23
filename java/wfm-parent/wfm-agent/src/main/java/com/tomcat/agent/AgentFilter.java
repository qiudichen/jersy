package com.tomcat.agent;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.tomcat.domain.BaseDomain;
import com.tomcat.domain.CountUtil;
import com.tomcat.domain.OneDomain;
import com.tomcat.domain.OneSingleton;
import com.tomcat.domain.WebDomain;
import com.tomcat.serviceImpl.ServiceLocator;
import com.tomcat.serviceImpl.UserService;

public class AgentFilter implements Filter {
	private UserService userService = null;
	
	@Override
	public void init(FilterConfig cfg) throws ServletException {
		try {
			userService = ServiceLocator.getUserService();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		System.out.println("=== agent filter====");
		this.userService.loing("agent2", "agent2");
		int count = CountUtil.increase();
		System.out.println(count);
		try {
			WebDomain webDomain = new WebDomain();
			System.out.println(webDomain.foo());
        } catch(Throwable e) {
        	e.printStackTrace();
        }
        
		try {
			BaseDomain baseDomain = new BaseDomain(); 
			System.out.println(baseDomain.foo());
        } catch(Throwable e) {
        	e.printStackTrace();
        }
        
		try {
			OneSingleton baseDomain = OneSingleton.getInstance(); 
			System.out.println(baseDomain.foo());
		} catch(Throwable e) {
			e.printStackTrace();
		}
		
		try {
			OneDomain oneDomain = new OneDomain(); 
			System.out.println(oneDomain.foo());
		} catch(Throwable e) {
			e.printStackTrace();
		}
		
		String uri = req.getRequestURI();
		System.out.println(uri);
		filterChain.doFilter(request, response);
	}
	
}
