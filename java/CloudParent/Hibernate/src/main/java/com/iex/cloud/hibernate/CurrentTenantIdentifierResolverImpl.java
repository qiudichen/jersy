package com.iex.cloud.hibernate;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements
	CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		 return "1";
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
