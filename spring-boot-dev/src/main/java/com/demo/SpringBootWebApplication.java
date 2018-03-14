package com.demo;

import java.net.URL;
import java.net.URLConnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

import com.demo.presistent.config.BaseDBConfig;
import com.demo.presistent.config.HibernateConfig;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, 
//		HibernateJpaAutoConfiguration.class,
//		DataSourceTransactionManagerAutoConfiguration.class})

@PropertySource(name = BaseDBConfig.JPA_EXT_SOURCE, value = "classpath:"
		+ BaseDBConfig.JPA_EXT_SOURCE, ignoreResourceNotFound = true)
@PropertySource(name = BaseDBConfig.JPA_SOURCE, value = "classpath:"
		+ BaseDBConfig.JPA_SOURCE, ignoreResourceNotFound = true)
public class SpringBootWebApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootWebApplication.class);
	}
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootWebApplication.class, args);
	}
}
