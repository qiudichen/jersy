package com.demo.log;


import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

public class Log4jJDBCExample {
	static Log log = LogFactory.getLog(Log4jJDBCExample.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String userName = "dchen";
		//String password = "ethan6803";

		//String url = "jdbc:sqlserver://localhost:1433;DatabaseName=AutopushUnity";

		try {
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//Connection conn = DriverManager.getConnection(url, userName, password);
			
			log.debug("d");
			log.info("Sample info message");
			log.error("Sample error message");
			log.fatal("Sample fatal message");			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
