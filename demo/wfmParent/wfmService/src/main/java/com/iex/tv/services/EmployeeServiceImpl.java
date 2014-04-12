package com.iex.tv.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.dao.service.EmployeeDao;
import com.iex.tv.domain.Employee;
import com.iex.tv.domain.Employee.Gender;

@Service("employeeServiceImpl")
@Transactional(value="demoTransactionManager", propagation = Propagation.REQUIRED)
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	@Qualifier("employeeDaoImpl")	
	private EmployeeDao employeeDao;
	
	@Override
	@Transactional(value="demoTransactionManager", readOnly=true)
	public List<Employee> getEmployees() {
		return employeeDao.getEmployees();
	}

	@Override
	public Employee getEmployee(long empNum) {
		return employeeDao.findByPk(empNum);
	}

	@Override
	public Employee addEmployee(String lastName, String firstName, Gender gender) {
		Employee emp = employeeDao.addEmployee(lastName, firstName, gender);
		return emp;
	}

	@Override
	public void deleteEmployee(long empNum) {
		employeeDao.deleteEmployee(empNum);
	}

	@Override
	public Employee updateEmployee(long empNum, String lastName, String firstName) {
		Employee employee = employeeDao.updateEmployee( empNum, lastName, firstName);
		return employee;
	}

	@Override
	public Employee updateEmployee(Employee employee, String lastName,
			String firstName) {
		return employeeDao.updateEmployee(employee, lastName, firstName);
	}

}
