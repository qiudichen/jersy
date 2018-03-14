package com.demo.presistent.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.demo.presistent.config.DatabaseConfig.C3P0Config;
import com.demo.presistent.config.DatabaseConfig.DataSourceConfig;

public class BaseDBConfig {
	public final static String JPA_SOURCE= "jpa.properties";
	public final static String JPA_EXT_SOURCE= "jpa-ext.properties";

	@Autowired
    protected Environment env;   
	
	@Bean("iexdatasource")
    public DataSource iexDataSource() {
		C3P0DataSourceUtil util = new C3P0DataSourceUtil();

		Properties prop =  EnvUtils.getPrefixProperties(env, JPA_SOURCE, JPA_EXT_SOURCE, "iex.");
		
		String driver = prop.getProperty(DataSourceConfig.DRIVER);
		String url = prop.getProperty(DataSourceConfig.URL);
		String username = prop.getProperty(DataSourceConfig.USER);
		String password = prop.getProperty(DataSourceConfig.PASSWORD);
		Properties c3pProps = EnvUtils.getPrefixProperties(C3P0Config.CONFIG_PREFIX, prop);
		DataSource dataSource = util.createDataSource(driver, url, username, password, c3pProps);
        return dataSource;
    }
	
	protected Properties hibernateProperties() {
		Properties prop =  EnvUtils.getPrefixProperties(env, JPA_SOURCE, JPA_EXT_SOURCE, "iex.");
		
        Properties hibernateProperties = new Properties();
        
        for(HibernateConst.Config config : HibernateConst.Config.values()) {
        		String key = config.getValue();
        		if(prop.containsKey(key)) {
        			hibernateProperties.setProperty(key, prop.getProperty(key));
        		}
        }        

		for (HibernateConst.ConnectProp connectProp : HibernateConst.ConnectProp.values()) {
			String key = connectProp.getValue();
			if (prop.containsKey(key)) {
				hibernateProperties.setProperty(key, prop.getProperty(key));
			}
		}   
		
		for (HibernateConst.CacheProp cacheProp : HibernateConst.CacheProp.values()) {
			String key = cacheProp.getValue();
			if (prop.containsKey(key)) {
				hibernateProperties.setProperty(key, prop.getProperty(key));
			}
		}   
        return hibernateProperties;
    }		
}
