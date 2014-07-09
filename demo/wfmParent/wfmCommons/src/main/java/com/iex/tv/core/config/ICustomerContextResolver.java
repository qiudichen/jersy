package com.iex.tv.core.config;

import java.util.Collection;

public interface ICustomerContextResolver {
	public ICustomerContext getCustomerContext(Long customerId) throws ConfigException;
	
	public Collection<ICustomerContext> getCustomerContexts();
}
