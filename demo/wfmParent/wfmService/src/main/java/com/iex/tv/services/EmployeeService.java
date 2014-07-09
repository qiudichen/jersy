package com.iex.tv.services;

import java.util.List;

import com.iex.tv.domain.Employee;

public interface EmployeeService {
	public List<Employee> getEmployees();
	
	public Employee getEmployee(long empNum);
	
	public long addEmployee(String lastName, String firstName, Employee.Gender gender);
	
	public void deleteEmployee(long empNum);
	
	public void updateEmployee(long empNum, String lastName, String firstName);
}
