package com.demo.presistent.config;

import java.util.Properties;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.eem.persistent.multi.config.CurrentTenantIdentifierResolverImpl;
import com.eem.persistent.multi.config.MultiTenantConnectionSchemaProviderImpl;
import com.eem.persistent.multi.config.TenantFilter;
import com.eem.persistent.multi.config.TenantSchemaResolver;
import com.eem.persistent.multi.config.TenantSchemaResolverNamingImpl;

@Configuration
@EnableTransactionManagement
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@ComponentScan(basePackages = {"com.eem.persistent.dao.hibernate"})
public class HibernateMultiTenancyConfig extends BaseDBConfig {
	
	@Bean(name="iexSessionFactory")
	public LocalSessionFactoryBean iexSessionFactory(@Qualifier("multiTenantConnectionSchemaProvider") MultiTenantConnectionSchemaProviderImpl multiTenantConnectionSchemaProviderImpl) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(iexDataSource());
		sessionFactory.setPackagesToScan("com.demo.persistent.domain");
		Properties hibernateProperties = getMultiTenantHibernateProp(currentTenantIdentifierResolver(), multiTenantConnectionSchemaProviderImpl);
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	@Bean
	public PlatformTransactionManager hibernateTransactionManager(@Qualifier("iexSessionFactory") LocalSessionFactoryBean sessionFactoryBean) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		SessionFactory obj = sessionFactoryBean.getObject();
		transactionManager.setSessionFactory(obj);
		return transactionManager;
	}
	
	@Bean(name="tenantSchemaResolver")
	public TenantSchemaResolver tenantSchemaResolver(@Value("${iex.schema.prefix:calloutapi}") String schemaPrefix) {
		TenantSchemaResolverNamingImpl resolver = new TenantSchemaResolverNamingImpl();
		resolver.setSchemaPrefix(schemaPrefix);
		resolver.setDefaultSchema(schemaPrefix);
		return resolver;
	}
	
	@Bean(name="multiTenantConnectionSchemaProvider")
	public MultiTenantConnectionSchemaProviderImpl multiTenantConnectionSchemaProvider(TenantSchemaResolver tenantSchemaResolver) {
		MultiTenantConnectionSchemaProviderImpl provider = new MultiTenantConnectionSchemaProviderImpl();
		provider.setDataSource(iexDataSource());
		provider.setTenantSchemaResolver(tenantSchemaResolver);
		return provider;
	}
	
	@Bean(name="currentTenantIdentifierResolver")
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		CurrentTenantIdentifierResolver resolver = new CurrentTenantIdentifierResolverImpl();
		return resolver;
	}
	
	/*
	 * Tenant Filter
	 */
	@Bean
	public FilterRegistrationBean someFilterRegistration() {
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(new TenantFilter());
	    registration.addUrlPatterns("/*");
	    registration.setDispatcherTypes(DispatcherType.REQUEST);
	    registration.addInitParameter("paramName", "paramValue");
	    registration.setName("tenantFilter");
	    registration.setOrder(1);
	    return registration;
	} 	
	
	private final Properties getMultiTenantHibernateProp(
			CurrentTenantIdentifierResolver currentTenantIdentifierResolver,
			MultiTenantConnectionSchemaProviderImpl multiTenantConnectionSchemaProviderImpl) {
		Properties hibernateProperties = super.hibernateProperties();
		hibernateProperties.remove(HibernateConst.Config.HIBERNATE_HBM2DDL_AUTO.getValue());
		hibernateProperties.setProperty(HibernateConst.MultTenantProp.HIBERNATE_MULTITENANCY.getValue(), "SCHEMA");
		hibernateProperties.put(HibernateConst.MultTenantProp.HIBERNATE_TENANT_IDENTIFIER_RESOLVER.getValue(), currentTenantIdentifierResolver);
		hibernateProperties.put(HibernateConst.MultTenantProp.HIBERNATE_MULTI_TENANT_CONNECTION_PROVIDER.getValue(), multiTenantConnectionSchemaProviderImpl);
		return hibernateProperties;
	}	
}
