package com.iex.tv.core.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.AbstractDataSource;

public abstract class AbstractRoutingDataSource extends AbstractDataSource {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private IConfigurableDataSourceFactory configurableDataSourceFactory;

	@Resource(name = "customerContextHolder")
	private CustomerContextHolder customerContextHolder;

	private Map<Long, DataSource> resolvedDataSources;
	
	protected abstract ICustomerContext determineCurrentLookupKey();
	
	public AbstractRoutingDataSource() {
		resolvedDataSources = new HashMap<Long, DataSource>(10);
	}
	
	protected DataSource determineTargetDataSource() {
		ICustomerContext customerContext = determineCurrentLookupKey();
		Long lookupKey = customerContext.getCustomerId();
		
		DataSource dataSource = this.resolvedDataSources.get(lookupKey);
		
		if (dataSource == null) {
			dataSource = resolveSpecifiedDataSource(customerContext);
		}

		if (dataSource == null) {
			String msg = "Cannot determine target DataSource for customer [" + lookupKey + "]";
			logger.error(msg);
			throw new IllegalStateException(msg);
		}
		return dataSource;
	}
	
	protected DataSource resolveSpecifiedDataSource(ICustomerContext customerContext) throws IllegalArgumentException {
		try {
			DataSource dataSource = null;
			synchronized(customerContext) {
				dataSource = configurableDataSourceFactory.createDataSource(customerContext);
				this.resolvedDataSources.put(customerContext.getCustomerId(), dataSource);
			}
			return dataSource;
			
		} catch (Exception e) {
			String msg = "Unable to create DataSource for customer [" + customerContext.getCustomerId() + "]";
			logger.error(msg, e);
			throw new IllegalArgumentException(msg, e);	
		} 
	}	

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return determineTargetDataSource().getConnection(username, password);
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return determineTargetDataSource().getConnection();
	}

	@PostConstruct
	public void init() {
		Collection<ICustomerContext> customers = customerContextHolder.getCustomerContexts();
		for(ICustomerContext customerContext : customers) {
			try {
				DataSource dataSource = configurableDataSourceFactory.createDataSource(customerContext);
				resolvedDataSources.put(customerContext.getCustomerId(), dataSource);
			} catch (Exception e) {
				logger.error("Unable to create DataSource for customer [" + customerContext.getCustomerId() + "]", e);
			}			
		}
	}
}
