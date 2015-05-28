package com.iex.cloud.dao.service;

import java.util.List;

import com.iex.cloud.domain.cust.Employee;
import com.iex.cloud.domain.cust.Gender;

public interface EmployeeDao {
	public List<Employee> getEmployees();
	
	public Employee addEmployee(String lastName, String firstName, Gender gender);
	
	public Employee findByPk(long empNum);
	
	public void deleteEmployee(long empNum);
	
	public void deleteEmployee(Employee employee);
	
	public Employee updateEmployee(Employee employee, String lastName, String firstName);
	
	public Employee updateEmployee(long empNum, String lastName, String firstName);
}
