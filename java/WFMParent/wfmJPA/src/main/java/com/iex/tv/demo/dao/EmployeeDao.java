package com.iex.tv.demo.dao;

import java.util.List;

import com.iex.tv.domain.training.Employee;

public interface EmployeeDao {
	public List<Employee> getEmployees();
	
	public Employee addEmployee(String lastName, String firstName, Employee.Gender gender);
	
	public Employee findByPk(long empNum);
	
	public void deleteEmployee(long empNum);
	
	public void deleteEmployee(Employee employee);
	
	public Employee updateEmployee(Employee employee, String lastName, String firstName);
}
