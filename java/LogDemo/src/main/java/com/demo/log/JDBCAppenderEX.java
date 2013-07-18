package com.demo.log;

import java.sql.SQLException;

import org.apache.log4j.Layout;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;

public class JDBCAppenderEX extends JDBCAppender {
	
	protected void execute(String sql) throws SQLException {
		System.out.println(sql);
		super.execute(sql);
	}

	protected String getLogStatement(LoggingEvent event) {
		Layout layout = getLayout();
		String sql = layout.format(event);
		return sql;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
}
