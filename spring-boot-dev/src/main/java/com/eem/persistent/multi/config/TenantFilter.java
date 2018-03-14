package com.eem.persistent.multi.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(TenantFilter.class);
	
	private static final String 	TENANT_ID = "tenant-id";
	
	private FilterConfig filterConfig;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String tenantId = httpRequest.getHeader(TENANT_ID);
		
		if(tenantId == null || tenantId.isEmpty()) {
			tenantId = httpRequest.getParameter(TENANT_ID);
		}
		
		if(tenantId == null) {
			tenantId = "";
		}
		TenantTracker.setTenantId(tenantId);
		chain.doFilter(request, response);
		TenantTracker.clear();
	}

	@Override
	public void destroy() {
		logger.debug("TenantFilter destory");
	}

}
