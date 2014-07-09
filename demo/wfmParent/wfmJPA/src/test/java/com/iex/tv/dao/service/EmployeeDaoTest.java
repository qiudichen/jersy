package com.iex.tv.dao.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.iex.tv.domain.Employee;

public class EmployeeDaoTest extends BaseDaoTest {
	
	@Resource
	private EmployeeDao employeeDao;

	@Test
	public void getAllEmployees() {
		try {
			List<Employee> employees = employeeDao.getEmployees();
			Assert.assertNotNull(employees);
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}	
	
	@Test
	public void fullCycleTestCase() {
		try {
			long id  = employeeDao.addEmployee("Ethan", "Allen", Employee.Gender.M);
			Assert.assertTrue(id > 0);
			
			Employee employee = employeeDao.findByPk(id);
			Assert.assertNotNull(employee);
			
			employeeDao.updateEmployee(employee.getEmpNum(), "LastName-Changed", "FirstName-Changed");
			employee = employeeDao.findByPk(employee.getEmpNum());
			Assert.assertTrue("LastName-Changed".equals(employee.getLastName()));
			Assert.assertTrue("FirstName-Changed".equals(employee.getFirstName()));
			
			employeeDao.updateEmployee(employee.getEmpNum(), "Again-Changed", "Again-Changed");
			employee = employeeDao.findByPk(employee.getEmpNum());
			Assert.assertTrue("Again-Changed".equals(employee.getLastName()));
			Assert.assertTrue("Again-Changed".equals(employee.getFirstName()));

			employeeDao.deleteEmployee(employee.getEmpNum());
			employee = employeeDao.findByPk(employee.getEmpNum());
			Assert.assertNull(employee);
			
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}	
}
