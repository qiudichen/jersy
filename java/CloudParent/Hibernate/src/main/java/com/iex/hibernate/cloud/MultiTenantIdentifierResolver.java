package com.iex.hibernate.cloud;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class MultiTenantIdentifierResolver implements
		CurrentTenantIdentifierResolver {
	private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	
	public static void setTenantIdentifier(String tenantIdentifier) {
		threadLocal.set(tenantIdentifier);
	}
	
	public static void clearTenantIdentifier() {
		threadLocal.remove();
	}
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		return threadLocal.get();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		String tenantIdentifier = threadLocal.get();
		return (tenantIdentifier != null);
	}
}
