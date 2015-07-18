package com.iex.hibernate.cloud;

import org.hibernate.service.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;


@SuppressWarnings("serial")
public class MultiTenantConnectionProvider extends
AbstractMultiTenantConnectionProvider {

	@Override
	protected ConnectionProvider getAnyConnectionProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
