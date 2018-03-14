package com.demo.presistent.config;

import java.util.Properties;

import javax.servlet.DispatcherType;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.eem.persistent.multi.config.CurrentTenantIdentifierResolverImpl;
import com.eem.persistent.multi.config.MultiTenantConnectionSchemaProviderImpl;
import com.eem.persistent.multi.config.TenantFilter;
import com.eem.persistent.multi.config.TenantSchemaResolver;
import com.eem.persistent.multi.config.TenantSchemaResolverNamingImpl;

@Configuration
@ConditionalOnMissingBean(HibernateConfig.class)
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.eem.persistent.dao.jpa"})
public class JpaMultiTenancyConfig extends BaseDBConfig {

	@Bean
    public JpaTransactionManager jpaTransactionManager(@Qualifier("iexEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
        return transactionManager;
    }
	
	@Bean(name="iexEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("multiTenantConnectionSchemaProvider") MultiTenantConnectionSchemaProviderImpl multiTenantConnectionSchemaProviderImpl) {
         LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
         entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
         entityManagerFactoryBean.setDataSource(iexDataSource());
         entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
         entityManagerFactoryBean.setPackagesToScan("com.demo.persistent.domain");    
         
         Properties hibernateProperties = getMultiTenantHibernateProp(currentTenantIdentifierResolver(), multiTenantConnectionSchemaProviderImpl);
         entityManagerFactoryBean.setJpaProperties(hibernateProperties);
         return entityManagerFactoryBean;
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
	
    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
   }

}
