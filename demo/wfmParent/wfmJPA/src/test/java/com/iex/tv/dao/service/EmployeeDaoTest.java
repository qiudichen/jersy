package com.iex.tv.dao.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.domain.Employee;

@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
@Transactional
public class EmployeeDaoTest {
	
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
			Employee employee = employeeDao.addEmployee("Ethan", "Allen", Employee.Gender.M);
			Assert.assertNotNull(employee);
			
			employee = employeeDao.findByPk(employee.getEmpNum());
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
