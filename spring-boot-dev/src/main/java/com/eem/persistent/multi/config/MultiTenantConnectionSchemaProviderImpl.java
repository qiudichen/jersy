package com.eem.persistent.multi.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class MultiTenantConnectionSchemaProviderImpl implements MultiTenantConnectionProvider, ServiceRegistryAwareService {
	private final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionSchemaProviderImpl.class);
	
	protected final static String HIBERNATE_PREFIX = "hibernate.";
	protected final static String HIBERNATE_MULTITENANCY = "hibernate.multiTenancy";
	protected final static String HIBERNATE_DIALECT = "hibernate.dialect";
	protected final static String DIALECT_PACKAGE = "org.hibernate.dialect.";
	
	private Map<String, Object> hibernateProperties = new  HashMap<String, Object>();
	
	private DataSource dataSource;
	
	private boolean aggressiveRelease;
	
	private TenantSchemaResolver tenantSchemaResolver;
	
	private String setSchemaSQL = null;
	/*
	 * implement hibernate ServiceRegistryAwareService to get hibernate properties
	 */
	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		try {
			
			Map settings = serviceRegistry.getService(ConfigurationService.class).getSettings();
			
			for(Object key : settings.keySet()) {
				if(key.toString().startsWith(HIBERNATE_PREFIX)) {
					hibernateProperties.put(key.toString(), settings.get(key));
				}
			}
			
			if(logger.isDebugEnabled()) {
				logger.debug("hibernate properties: ");
				for(Map.Entry<String, Object> entry : hibernateProperties.entrySet()) {
					logger.debug(entry.getKey() + " = " + entry.getValue());
				}
			}
		} catch(Exception e) {
			logger.error("Unable to get Service Setting", e);
			hibernateProperties = Collections.emptyMap();
		}
		validMultiTenancyType();
		getDialect();
	}

	private void getDialect() {
		 Object obj = this.hibernateProperties.get(HIBERNATE_DIALECT);
		 String dialectClassName = obj != null ? (String) obj : "org.hibernate.dialect.MySQLDialect";
		 Class<?> cls;
		try {
			cls = Class.forName(dialectClassName);
		} catch (ClassNotFoundException e) {
			logger.error("Unsupport Database Dialect");
			return;
		}
		 
		 if(org.hibernate.dialect.MySQLDialect.class.isAssignableFrom(cls))  {
			 setSchemaSQL = "USE <schema>";
		 } else if(org.hibernate.dialect.Oracle8iDialect.class.isAssignableFrom(cls)) {
			 setSchemaSQL = "ALTER SESSION SET CURRENT_SCHEMA = <schema>";
		 } else if(org.hibernate.dialect.PostgreSQL81Dialect.class.isAssignableFrom(cls)) {
			 setSchemaSQL = "SET search_path TO <schema>";
		 } else {
			 logger.error("Unsupport Database Dialect");
		 }
	}
	
	
	/*********************************************************************************
	 * 
	 * Utility mehtods to get hibernate configuation info
	 * 
	 *********************************************************************************/
	private Map<String, Object> getHibernateProperty() {
		if(this.hibernateProperties == null) {
			this.hibernateProperties = Collections.emptyMap();
		}
		return this.hibernateProperties;
	}
	
	private String getHibernateProperty(String key) {
		Object value = getHibernateProperty().get(key);
		if(value != null) {
			return value.toString();
		}
		return null;
	}
	
	private void validMultiTenancyType() {
		String value = getHibernateProperty(HIBERNATE_MULTITENANCY);
		MultiTenancy tenancyType = MultiTenancy.findByValue(value);
		if(tenancyType != MultiTenancy.SCHEMA) {
			logger.warn(HIBERNATE_MULTITENANCY + " is not defined as SCHEMA. Please verify the hibernate configuation for " + HIBERNATE_MULTITENANCY);
		}
	}

	/*********************************************************************************
	 * 
	 * Implement MultiTenantConnectionProvider below
	 * 
	 *********************************************************************************/
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean isUnwrappableAs(Class unwrapType) {
		return (MultiTenantConnectionProvider.class.equals(unwrapType)) || (MultiTenantConnectionSchemaProviderImpl.class.isAssignableFrom(unwrapType));
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T> T unwrap(Class<T> unwrapType) {
		if ( isUnwrappableAs( unwrapType ) ) {
			return (T) this;
		}
		else {
			throw new UnknownUnwrapTypeException( unwrapType );
		}
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		try {
			Connection connection = this.dataSource.getConnection();
			return connection;
		} catch (SQLException e) {
			throw new HibernateException("Unable to get connection from datasource. ", e);
		}
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		if(logger.isTraceEnabled()) {
			logger.trace("get connection for " + tenantIdentifier);
		}
		
		Connection connection = getAnyConnection();
		
		try {
			String tenantSchema = this.tenantSchemaResolver.getTenantSchema(tenantIdentifier);
			String setSQL = getSetSchemaSQL(tenantSchema);
			if(logger.isTraceEnabled()) {
				logger.trace("execute sql: " + setSQL);
			}
			connection .createStatement().execute(setSQL);
			return connection;
		}
		catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
		}	
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		if(logger.isTraceEnabled()) {
			logger.trace("release connection for " + tenantIdentifier);
		}		
		try {
			connection.createStatement().execute(getSetDefaultSchemaSQL());
		}
		catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema " + this.tenantSchemaResolver.getDefaultSchema(), e);
		}
		releaseAnyConnection(connection);
	}

	private String defaultSchemaSQL = null;
	private String getSetDefaultSchemaSQL() {
		if(this.defaultSchemaSQL == null) {
			this.defaultSchemaSQL = getSetSchemaSQL(this.tenantSchemaResolver.getDefaultSchema());
		}
		return this.defaultSchemaSQL;
	}
	
	private String getSetSchemaSQL(String schemaName) {
		String sql = setSchemaSQL.replace("<schema>", schemaName);
		return sql;
	}
	
	/*
	 * set methods
	 */
	public void setAggressiveRelease(boolean aggressiveRelease) {
		this.aggressiveRelease = aggressiveRelease;
	}	

	@Override
	public boolean supportsAggressiveRelease() {
		return aggressiveRelease;
	}

	public void setTenantSchemaResolver(TenantSchemaResolver tenantSchemaResolver) {
		this.tenantSchemaResolver = tenantSchemaResolver;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
