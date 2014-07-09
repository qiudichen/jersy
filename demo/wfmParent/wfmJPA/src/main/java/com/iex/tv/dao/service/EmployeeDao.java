package com.iex.tv.dao.service;

import java.util.List;

import com.iex.tv.domain.Employee;

public interface EmployeeDao {
	public long addEmployee(String lastName, String firstName, Employee.Gender gender);
	
	public Employee findByPk(long empNum);
	
	public List<Employee> getEmployees();
	
	public void updateEmployee(long empNum, String lastName, String firstName);
	
	public void deleteEmployee(long empNum);
}
