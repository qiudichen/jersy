package com.iex.tv.demo.services;

import java.util.List;

import com.iex.tv.domain.training.Employee;

public interface EmployeeService {
	public List<Employee> getEmployees();
	
	public Employee getEmployee(long empNum);
	
	public Employee addEmployee(String lastName, String firstName, Employee.Gender gender) throws DBRollback;
	
	public void deleteEmployee(long empNum) throws DBRollback;
	
	public Employee updateEmployee(long empNum, String lastName, String firstName) throws DBRollback;
	
	public Employee updateEmployee(Employee employee, String lastName, String firstName) throws DBRollback;
}
