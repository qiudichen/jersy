package com.iex.tv.utils;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component("exceptionUtils")
public class ExceptionUtils {
	public class Error {
		Throwable rootCause;
		String message;
		public Error(Throwable rootCause, String message) {
			super();
			this.rootCause = rootCause;
			this.message = message;
		}
		public Throwable getRootCause() {
			return rootCause;
		}
		public String getMessage() {
			return message;
		}
		
		public void printStackTrace() {
			this.rootCause.printStackTrace();
		}
	}
	
	public class SQLError extends Error {
		int errorCode;
		String sqlState;
		
		private SQLError(Throwable rootCause, String message, int errorCode, String sqlState) {
			super(rootCause, message);
			this.errorCode = errorCode;
			this.sqlState = sqlState;
		}

		public int getErrorCode() {
			return errorCode;
		}

		public String getSqlState() {
			return sqlState;
		}
	}
	
	public Error getCausedError(Throwable e) {
		Throwable casuedException = getCauseException(e);
		if(casuedException instanceof SQLException) {
			SQLException sqlException = getCauseSQLException((SQLException)casuedException);
			int errorCode = sqlException.getErrorCode();
			String sqlState = sqlException.getSQLState();
			return new SQLError(sqlException, sqlException.getMessage(), errorCode, sqlState);
		} else {
			return new Error(casuedException, casuedException.getMessage());
		}
	}
	
	private SQLException getCauseSQLException(SQLException e) {
		SQLException nextException = e.getNextException();
		if(nextException == null) {
			return e;
		}
		
		if(nextException == e) {
			return e;
		}

		return getCauseSQLException(nextException);
	}
	
	private Throwable getCauseException(Throwable e) {
		Throwable nextException = e.getCause();
		if(nextException == null) {
			return e;
		}
		
		if(nextException == e) {
			return e;
		}

		return getCauseException(nextException);
	}	
}
