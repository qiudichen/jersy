package com.iex.cloud.dao.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.iex.cloud.dao.CustDaoImpl;
import com.iex.cloud.domain.cust.Employee;
import com.iex.cloud.domain.cust.Gender;

@Repository("employeeDaoImpl")
public class EmployeeDaoImpl extends CustDaoImpl<Employee, Long> implements EmployeeDao {
	@Override
	public List<Employee> getEmployees() {
		return super.findAll();
	}

	@Override
	public Employee addEmployee(String lastName, String firstName, Gender gender) {
		Employee employee = new Employee(firstName, lastName, gender, new Date(), new Date());
		persist(employee);
		flush();
		return employee;
	}

	@Override
	public Employee findByPk(long empNum) {
		return super.findByPk(empNum);
	}

	@Override
	public void deleteEmployee(long empNum) {
		super.remove(empNum);
	}

	@Override
	public void deleteEmployee(Employee employee) {
		this.getSession().delete(employee);
	}

	@Override
	public Employee updateEmployee(Employee employee, String lastName,
			String firstName) {
		Employee managedEmployee = merge(employee);
		managedEmployee.setFirstName(firstName);
		managedEmployee.setLastName(lastName);
		return managedEmployee;
	}

	@Override
	public Employee updateEmployee(long empNum, String lastName,
			String firstName) {
		Employee employee =  (Employee) this.getSession().load(Employee.class, empNum);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		return employee;
	}

}
