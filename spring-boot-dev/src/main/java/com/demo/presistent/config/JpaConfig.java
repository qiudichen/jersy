package com.demo.presistent.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConditionalOnMissingBean(HibernateConfig.class)
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.eem.persistent.dao.jpa"})
public class JpaConfig extends BaseDBConfig {

	@Bean
    public JpaTransactionManager jpaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return transactionManager;
    }
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {

         LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
         entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
         entityManagerFactoryBean.setDataSource(iexDataSource());
         entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
         entityManagerFactoryBean.setPackagesToScan("com.demo.persistent.domain");             
         entityManagerFactoryBean.setJpaProperties(hibernateProperties());
         return entityManagerFactoryBean;
     }

    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
   }
    

}
