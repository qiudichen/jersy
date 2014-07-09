package com.iex.tv.dao.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.iex.tv.domain.Employee;

public class EmployeeJDBCService {
	private String dbURL = "jdbc:postgresql://localhost:5432/demo";
	private String dbUser = "tvsys";
	private String dbPassword = "Iex.prm$";

	
	public EmployeeJDBCService() {
		
	}
	
	protected Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		return conn;
	}
	
	public long addEmployee(Employee employee) throws SQLException {
		final String insertSQL = 
				"INSERT INTO EMPLOYEE " + 
				"(EMPNUM, BIRTHDATE, FIRSTNAME, LASTNAME, GENDER, HIREDATE) " +
				" VALUES (nextval('SEQ_EMP_ID'), ?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(insertSQL);
			ps.setDate(1, new java.sql.Date(employee.getBirthDate().getTime()));
			ps.setString(2, employee.getFirstName());
			ps.setString(3, employee.getLastName());
			ps.setString(4, employee.getGender().toString());
			ps.setDate(5, new java.sql.Date(employee.getHireDate().getTime()));
			ps.executeUpdate();
		} finally {
			close(ps);
			close(conn);
		}     
		return 0;
	}
	
	private void close(Connection conn) {
		try {
			if(conn != null) conn.close();
		} catch(Exception e) {}		
	}
	
	private void close(Statement s) {
		try {
			if(s != null) s.close();
		} catch(Exception e) {}		
	}
}
