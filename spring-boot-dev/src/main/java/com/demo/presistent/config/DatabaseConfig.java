package com.demo.presistent.config;

public interface DatabaseConfig {
	
	interface DataSourceConfig {
		String DRIVER ="database.connection.driver_class";
		String URL ="database.connection.url";
		String USER ="database.connection.username";
		String PASSWORD ="database.connection.password";		
	}
	
	interface HibernateConfig {
		String CONFIG_PREFIX = "hibernate.";
		
		String DIALECT ="hibernate.dialect";
		
		String DEFAULT_SCHEMA = "hibernate.default_schema";

		String SHOW_SQL ="hibernate.show_sql";
		
		String FORMAT_SQL ="hibernate.format_sql";
		
		String HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	}
	
	interface C3P0Config {
		
		String CONFIG_PREFIX="hibernate.c3p0.";
		
		String ACQUIREINCREMENT = CONFIG_PREFIX + "acquire_increment";

		String INITIALPOOLSIZE = CONFIG_PREFIX + "initial_pool_size";

		String MAXPOOLSIZE = CONFIG_PREFIX + "max_size";

		String MINPOOLSIZE = CONFIG_PREFIX + "min_size";

		String MAXIDLETIME = CONFIG_PREFIX + "maxIdleTime";
		
		String MAXCONNECTIONAGE = CONFIG_PREFIX + "max_connectionAge";
		
		String MAXIDLETIMEEXCESSCONNECTIONS = CONFIG_PREFIX + "max_idle_time_excess_connections";
		
		String AUTOMATICTESTTABLE = CONFIG_PREFIX + "automatic_test_table";
		
		String CONNECTIONTESTERCLASSNAME = CONFIG_PREFIX + "connection_tester_class_name";
		
		String IDLECONNECTIONTESTPERIOD = CONFIG_PREFIX + "idleConnectionTestPeriod";
		
		String PREFERREDTESTQUERY = CONFIG_PREFIX + "preferredTestQuery";
		
		String TESTCONNECTIONONCHECKIN = CONFIG_PREFIX + "testConnectionOnCheckin";
		
		String TESTCONNECTIONONCHECKOUT = CONFIG_PREFIX + "testConnectionOnCheckout";
		
		String MAXSTATEMENTS = CONFIG_PREFIX + "maxStatements";
		
		String MAXSTATEMENTSPERCONNECTION = CONFIG_PREFIX + "maxStatementsPerConnection";
		
		String STATEMENTCACHENUMDEFERREDCLOSETHREADS = CONFIG_PREFIX + "statementCacheNumDeferredCloseThreads";
		
		String ACQUIRERETRYATTEMPTS = CONFIG_PREFIX + "acquireRetryAttempts";
		
		String ACQUIRERETRYDELAY = CONFIG_PREFIX + "acquireRetryDelay";
		
		String AUTOCOMMITONCLOSE = CONFIG_PREFIX + "autoCommitOnClose";
		
		String CHECKOUTTIMEOUT = CONFIG_PREFIX + "checkoutTimeout";
		
		String CONNECTIONCUSTOMIZERCLASSNAME = CONFIG_PREFIX + "connectionCustomizerClassName";
		
		//String CONTEXTCLASSLOADERSOURCE = C3C3P0_CONFIG_PREFIX + "contextClassLoaderSource";
		
		String DATASOURCENAME = CONFIG_PREFIX + "dataSourceName";
		
		String DEBUGUNRETURNEDCONNECTIONSTACKTRACES = CONFIG_PREFIX + "debugUnreturnedConnectionStackTraces";
		
		//String EXTENSIONS = C3C3P0_CONFIG_PREFIX + "extensions";
		String FACTORYCLASSLOCATION = CONFIG_PREFIX + "factoryClassLocation";
		
		String FORCEIGNOREUNRESOLVEDTRANSACTIONS = CONFIG_PREFIX + "forceIgnoreUnresolvedTransactions";
		
		//String FORCESYNCHRONOUSCHECKINS = C3C3P0_CONFIG_PREFIX + "forceSynchronousCheckins";
		
		String FORCEUSENAMEDDRIVERCLASS = CONFIG_PREFIX + "forceUseNamedDriverClass";
		
		String NUMHELPERTHREADS = CONFIG_PREFIX + "numHelperThreads";
		
		String OVERRIDEDEFAULTUSER = CONFIG_PREFIX + "overrideDefaultUser";
		
		String OVERRIDEDEFAULTPASSWORD = CONFIG_PREFIX + "overrideDefaultPassword";
		
		String PRIVILEGESPAWNEDTHREADS = CONFIG_PREFIX + "privilegeSpawnedThreads";
		
		String PROPERTYCYCLE = CONFIG_PREFIX + "propertyCycle";
		
		String UNRETURNEDCONNECTIONTIMEOUT = CONFIG_PREFIX + "unreturnedConnectionTimeout";
		
		String BREAKAFTERACQUIREFAILURE = CONFIG_PREFIX + "breakAfterAcquireFailure";			
	}
}
