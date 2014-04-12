package com.iex.tv.services;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.tv.domain.Employee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
//@Ignore
public class EmployeeServiceTest {
	
	@Resource (name="employeeServiceImpl")
	private EmployeeService employeeService;
	
	@Before
	public void setup() {
		try {
			System.out.println(" Start Test ");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void release() {

	}	
		
	@Test
	public void getAllEmployees() {
		try {
			List<Employee> employees = employeeService.getEmployees();
			Assert.assertNotNull(employees);
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void fullCycleTestCase() {
		try {
			Employee employee = employeeService.addEmployee("Ethan", "Allen", Employee.Gender.M);
			Assert.assertNotNull(employee);
			
			employee = employeeService.getEmployee(employee.getEmpNum());
			Assert.assertNotNull(employee);
			
			employeeService.updateEmployee(employee.getEmpNum(), "LastName-Changed", "FirstName-Changed");
			employee = employeeService.getEmployee(employee.getEmpNum());
			Assert.assertTrue("LastName-Changed".equals(employee.getLastName()));
			Assert.assertTrue("FirstName-Changed".equals(employee.getFirstName()));
			
			employeeService.updateEmployee(employee.getEmpNum(), "Again-Changed", "Again-Changed");
			employee = employeeService.getEmployee(employee.getEmpNum());
			Assert.assertTrue("Again-Changed".equals(employee.getLastName()));
			Assert.assertTrue("Again-Changed".equals(employee.getFirstName()));

			employeeService.deleteEmployee(employee.getEmpNum());
			employee = employeeService.getEmployee(employee.getEmpNum());
			Assert.assertNull(employee);
			
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
