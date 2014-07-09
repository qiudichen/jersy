package com.iex.tv.core.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("customerContextHolder")
public class CustomerContextHolder implements ICustomerContextHolder {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static ThreadLocal<ICustomerContext> conversation = new ThreadLocal<ICustomerContext>();

	private Map<Long, ICustomerContext> resolvedCustomerContexts = new HashMap<Long, ICustomerContext>(10);
	
	@Resource
	private ICustomerContextResolver customerContextResolver;
	
	public CustomerContextHolder() {
		
	}
	
	@Override
	public void switchIn(Long customerId) throws ConfigException {
		if (customerId == null) {
			switchOut();
		} else {
			ICustomerContext customerContext = resolvedCustomerContexts.get(customerId);
            if (customerContext == null) {
            	synchronized (resolvedCustomerContexts) {
            		customerContext = resolvedCustomerContexts.get(customerId);
            		if (customerContext == null) {
            			customerContext = getICustomerContext(customerId);
            			resolvedCustomerContexts.put(customerId, customerContext);
            		}
            	}
            }			
			conversation.set(customerContext);
		}
	}
	
	private ICustomerContext getICustomerContext(Long customerId) throws ConfigException {
		ICustomerContext customerContext = customerContextResolver.getCustomerContext(customerId);
		if(customerContext == null) {
			String msg = "Invalid Customer [" + customerId + "]";
			logger.error(msg);
			throw new ConfigException(msg);
		}
		return customerContext;
	}
	
	@Override
	public void switchOut() {
		conversation.remove();
	}

	@Override
	public Long getCustomerId() {
		ICustomerContext customerContext = conversation.get();
		if(customerContext != null) {
			return customerContext.getCustomerId();
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		Collection<ICustomerContext> customers = customerContextResolver.getCustomerContexts();
		for(ICustomerContext customerContext : customers) {
			resolvedCustomerContexts.put(customerContext.getCustomerId(), customerContext);
		}		
	}

	@Override
	public Collection<ICustomerContext> getCustomerContexts() {
		return resolvedCustomerContexts.values();
	}

	@Override
	public ICustomerContext getCustomerContext(Long customerId) throws ConfigException {
		return customerContextResolver.getCustomerContext(customerId);
	}
}
