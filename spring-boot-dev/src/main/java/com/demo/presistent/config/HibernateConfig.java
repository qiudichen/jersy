package com.demo.presistent.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@ComponentScan(basePackages = {"com.eem.persistent.dao.hibernate"})
public class HibernateConfig extends BaseDBConfig {

	@Bean
	public LocalSessionFactoryBean iexSessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(iexDataSource());
		sessionFactory.setPackagesToScan("com.demo.persistent.domain");
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	public PlatformTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		SessionFactory obj = iexSessionFactory().getObject();
		transactionManager.setSessionFactory(obj);
		return transactionManager;
	}	
}
