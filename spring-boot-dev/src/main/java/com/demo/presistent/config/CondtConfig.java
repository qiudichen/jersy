package com.demo.presistent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.demo.persistent.dao.AgentcalloutDAO;
import com.eem.persistent.dao.hibernate.AgentcalloutDAOImpl;
import com.eem.persistent.dao.jpa.AgentcalloutDAOJpaImpl;

@Configuration
public class CondtConfig {
	@Autowired
    protected Environment env;  
	// ==== DAO =================
	
//	@Bean(name = "agentcalloutDAO")
//	//@ConditionalOnBean(LocalContainerEntityManagerFactoryBean.class)
//	@ConditionalOnMissingBean(name = "agentcalloutDAO")
//	public AgentcalloutDAO agentcalloutJPADAO() {
//		AgentcalloutDAO dao  = new AgentcalloutDAOJpaImpl();
//		return dao;
//	}	
	
	//========conditional test====
	
	@Bean("tt")
	public String beanTest() {
		String t = env.getProperty("usemysql");
		return "beanTest";
	}

	@Bean
	@ConditionalOnResource(resources = "classpath:jpa1.properties")
	public String otherBean(@Qualifier("tt") String beanTest) {
		String t = env.getProperty("usemysql");
		return "other: " + beanTest;
	}

	@Bean(name = "tenantType")
	@ConditionalOnProperty(name = "usemysql", havingValue = "MT")
	public String multipleTenant() {
		String t = env.getProperty("usemysql");
		return "multipleTenant";
	}

	@Bean(name = "tenantType")
	@ConditionalOnMissingBean(name = "tenantType")
	public String singleTenant() {
		String t = env.getProperty("usemysql");
		return "singleTenant";
	}
}
