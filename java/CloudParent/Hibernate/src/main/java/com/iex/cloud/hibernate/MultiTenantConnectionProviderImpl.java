package com.iex.cloud.hibernate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.cfg.AvailableSettings;
//import org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider;
//import org.hibernate.service.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
//import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

@SuppressWarnings("serial")
public class MultiTenantConnectionProviderImpl extends
		AbstractMultiTenantConnectionProvider {

	protected static Logger log = Logger.getLogger(MultiTenantConnectionProviderImpl.class);
	
    private final Map<String, ConnectionProvider> connectionProviders = new HashMap<String, ConnectionProvider>();
    
	@Override
	protected ConnectionProvider getAnyConnectionProvider() {
		System.out.println("barfoo");
        Properties properties = getConnectionProperties();

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://127.0.0.1:3306/test");
        ds.setUsername("root");
        ds.setPassword("");

        InjectedDataSourceConnectionProvider defaultProvider = new InjectedDataSourceConnectionProvider();
        defaultProvider.setDataSource(ds);
        defaultProvider.configure(properties);

        return (ConnectionProvider) defaultProvider;
	}

	@Override
	protected ConnectionProvider selectConnectionProvider(
			String tenantIdentifier) {
		
        System.out.println("foobar");
        Properties properties = getConnectionProperties();
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://127.0.0.1:3306/test2");
        ds.setUsername("root");
        ds.setPassword("");
        InjectedDataSourceConnectionProvider defaultProvider = new InjectedDataSourceConnectionProvider();
        defaultProvider.setDataSource(ds);
        defaultProvider.configure(properties);
        return (ConnectionProvider) defaultProvider;
	}

    private Properties getConnectionProperties() {
        Properties properties = new Properties();
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver");
        properties.put(AvailableSettings.URL, "jdbc:mysql://127.0.0.1:3306/test");
        properties.put(AvailableSettings.USER, "root");
        properties.put(AvailableSettings.PASS, "");

        return properties;
    }	
}
