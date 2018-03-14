package com.demo.presistent.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.presistent.config.DatabaseConfig.C3P0Config;
import com.demo.web.WelcomeController;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class C3P0DataSourceUtil {
	private final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	
	private static final int DEFAULT_POOL_SIZE = 10;
	
	public C3P0DataSourceUtil() {
		
	}
	
	public DataSource createDataSource(String driver, String url, String username, String password, Properties c3pProps) {
		ComboPooledDataSource datasource = new ComboPooledDataSource();
		
		try {
			datasource.setDriverClass(driver);
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e.getMessage());
		}

		datasource.setJdbcUrl(url);
		datasource.setUser(username);
		datasource.setPassword(password);

		//set properties
		String value = c3pProps.getProperty(C3P0Config.PREFERREDTESTQUERY);
		if(value != null) {
			datasource.setPreferredTestQuery(value);			
		} else {
			String sql = getPreferredTestQuery(driver);
			if(sql != null) {
				datasource.setPreferredTestQuery(sql);
			}
		}
		
		value = c3pProps.getProperty(C3P0Config.IDLECONNECTIONTESTPERIOD);
		int intVal = getInt(value, 30);
		datasource.setIdleConnectionTestPeriod(intVal);
		
		value = c3pProps.getProperty(C3P0Config.MAXSTATEMENTSPERCONNECTION);
		intVal = getInt(value, 15);
		datasource.setMaxStatementsPerConnection(intVal);
		
		value = c3pProps.getProperty(C3P0Config.TESTCONNECTIONONCHECKIN);
		if(!"false".equals(value)) {
			datasource.setTestConnectionOnCheckin(true);
		}
		
		value = c3pProps.getProperty(C3P0Config.MAXPOOLSIZE);
		int maxPoolSize = getInt(value, DEFAULT_POOL_SIZE);
		datasource.setMaxPoolSize(maxPoolSize);
		
		value = c3pProps.getProperty(C3P0Config.ACQUIREINCREMENT);
		int acquireIncrement = getInt(value, 0);
		if(acquireIncrement > 0) {
			datasource.setAcquireIncrement(acquireIncrement);
		}
		
		value = c3pProps.getProperty(C3P0Config.INITIALPOOLSIZE);
		int initialPoolSize = getInt(value, 0);
		if(initialPoolSize > 0) {
			datasource.setInitialPoolSize(initialPoolSize);
		}
		
		value = c3pProps.getProperty(C3P0Config.MAXIDLETIME);
		int maxIdleTime = getInt(value, 0);
		if(maxIdleTime > 0) {
			datasource.setMaxIdleTime(maxIdleTime);
		}
		
		value = c3pProps.getProperty(C3P0Config.MINPOOLSIZE);
		int minPoolSize = getInt(value, 0);
		if(minPoolSize > 0) {
			datasource.setMinPoolSize(minPoolSize);
		}		

		value = c3pProps.getProperty(C3P0Config.MAXCONNECTIONAGE);
		int maxConnectionAge = getInt(value, 0);
		if(maxConnectionAge > 0) {
			datasource.setMaxConnectionAge(maxConnectionAge);
		}

		value = c3pProps.getProperty(C3P0Config.MAXIDLETIMEEXCESSCONNECTIONS);
		int maxIdleTimeExcessConnections = getInt(value, 0);
		if(maxIdleTimeExcessConnections > 0) {
			datasource.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
		}

		String automaticTestTable = c3pProps.getProperty(C3P0Config.AUTOMATICTESTTABLE);
		if(automaticTestTable != null) {
			datasource.setAutomaticTestTable(automaticTestTable);
		}
		
		String connectionTesterClassName = c3pProps.getProperty(C3P0Config.CONNECTIONTESTERCLASSNAME);
		if(connectionTesterClassName != null) {
			try {
				datasource.setConnectionTesterClassName(connectionTesterClassName);
			} catch (PropertyVetoException e) {
				logger.error("error to set ConnectionTesterClassName " + connectionTesterClassName, e);
			}
		}

		String testConnectionOnCheckout = c3pProps.getProperty(C3P0Config.TESTCONNECTIONONCHECKOUT);
		if("true".equals(testConnectionOnCheckout)) {
			datasource.setTestConnectionOnCheckout(true);
		}

		value = c3pProps.getProperty(C3P0Config.MAXSTATEMENTS);
		int maxStatements = getInt(value, 0);
		if(maxStatements > 0) {
			datasource.setMaxStatements(maxStatements);
		}
		
		value = c3pProps.getProperty(C3P0Config.STATEMENTCACHENUMDEFERREDCLOSETHREADS);
		int statementCacheNumDeferredCloseThreads = getInt(value, 0);
		if(statementCacheNumDeferredCloseThreads > 0) {
			datasource.setStatementCacheNumDeferredCloseThreads(statementCacheNumDeferredCloseThreads);
		}
		
		value = c3pProps.getProperty(C3P0Config.ACQUIRERETRYATTEMPTS);
		int acquireRetryAttempts = getInt(value, 0);
		if(acquireRetryAttempts > 0) {
			datasource.setAcquireRetryAttempts(acquireRetryAttempts);
		}

		value = c3pProps.getProperty(C3P0Config.ACQUIRERETRYDELAY);
		int acquireRetryDelay = getInt(value, 0);
		if(acquireRetryDelay > 0) {
			datasource.setAcquireRetryDelay(acquireRetryDelay);
		}

		String autoCommitOnClose = c3pProps.getProperty(C3P0Config.AUTOCOMMITONCLOSE);
		if("true".equals(autoCommitOnClose)) {
			datasource.setAutoCommitOnClose(true);
		}

		value = c3pProps.getProperty(C3P0Config.CHECKOUTTIMEOUT);
		int checkoutTimeout = getInt(value, 0);
		if(checkoutTimeout > 0) {
			datasource.setCheckoutTimeout(checkoutTimeout);
		}

		String connectionCustomizerClassName = c3pProps.getProperty(C3P0Config.CONNECTIONCUSTOMIZERCLASSNAME);
		if(connectionCustomizerClassName != null) {
			datasource.setConnectionCustomizerClassName(connectionCustomizerClassName);
		}

		String dataSourceName = c3pProps.getProperty(C3P0Config.DATASOURCENAME);
		if(dataSourceName != null) {
			datasource.setDataSourceName(dataSourceName);
		}

		String debugUnreturnedConnectionStackTraces = c3pProps.getProperty(C3P0Config.DEBUGUNRETURNEDCONNECTIONSTACKTRACES);
		if("true".equals(debugUnreturnedConnectionStackTraces)) {
			datasource.setDebugUnreturnedConnectionStackTraces(true);
		}

		String factoryClassLocation = c3pProps.getProperty(C3P0Config.FACTORYCLASSLOCATION);
		if(factoryClassLocation != null) {
			datasource.setFactoryClassLocation(factoryClassLocation);
		}

		String forceIgnoreUnresolvedTransactions = c3pProps.getProperty(C3P0Config.FORCEIGNOREUNRESOLVEDTRANSACTIONS);
		if("true".equals(forceIgnoreUnresolvedTransactions)) {
			datasource.setForceIgnoreUnresolvedTransactions(true);
		}

		String forceUseNamedDriverClass = c3pProps.getProperty(C3P0Config.FORCEUSENAMEDDRIVERCLASS);
		if("true".equals(forceUseNamedDriverClass)) {
			datasource.setForceUseNamedDriverClass(true);
		}

		value = c3pProps.getProperty(C3P0Config.NUMHELPERTHREADS);
		int numHelperThreads = getInt(value, 0);
		if(numHelperThreads > 0) {
			datasource.setNumHelperThreads(numHelperThreads);
		}

		String overrideDefaultUser = c3pProps.getProperty(C3P0Config.OVERRIDEDEFAULTUSER);
		if(overrideDefaultUser != null) {
			datasource.setOverrideDefaultUser(overrideDefaultUser);
		}

		String overrideDefaultPassword = c3pProps.getProperty(C3P0Config.OVERRIDEDEFAULTPASSWORD);
		if(overrideDefaultPassword != null) {
			datasource.setOverrideDefaultPassword(overrideDefaultPassword);
		}

		String privilegeSpawnedThreads = c3pProps.getProperty(C3P0Config.PRIVILEGESPAWNEDTHREADS);
		if("true".equals(privilegeSpawnedThreads)) {
			datasource.setPrivilegeSpawnedThreads(true);
		}

		value = c3pProps.getProperty(C3P0Config.PROPERTYCYCLE);
		int propertyCycle = getInt(value, 0);
		if(propertyCycle > 0) {
			datasource.setPropertyCycle(propertyCycle);
		}

		value = c3pProps.getProperty(C3P0Config.UNRETURNEDCONNECTIONTIMEOUT);
		int unreturnedConnectionTimeout = getInt(value, 0);
		if(unreturnedConnectionTimeout > 0) {
			datasource.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
		}

		String breakAfterAcquireFailure = c3pProps.getProperty(C3P0Config.BREAKAFTERACQUIREFAILURE);
		if("true".equals(breakAfterAcquireFailure)) {
			datasource.setBreakAfterAcquireFailure(true);
		}
		
		return datasource;
	}

	private static int getInt(String value, int defaultInt) {
		if (value == null) {
			return defaultInt;
		}
		
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return defaultInt;
		}
	}
	
	private String getPreferredTestQuery(String driver) {
		if(driver == null || driver.trim().isEmpty()){
			throw new RuntimeException("No JDBC driver specified");
		}
		
		if(driver.equals("oracle.jdbc.driver.OracleDriver") || driver.toLowerCase().contains("oracle")) {
			return "SELECT 1 FROM DUAL";
		}
		
		if(driver.equals("com.mysql.jdbc.Driver") || driver.toLowerCase().contains("mysql.")) {
			return "SELECT 1";
		}

		return "";
	}	
	
	public static void main(String argv[]) {
		ComboPooledDataSource datasource = new ComboPooledDataSource();
		System.out.println("=========");
	}
}
