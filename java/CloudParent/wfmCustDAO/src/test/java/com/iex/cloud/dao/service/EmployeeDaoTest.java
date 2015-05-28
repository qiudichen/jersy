package com.iex.cloud.dao.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.iex.cloud.dao.BaseDaoTest;
import com.iex.cloud.domain.cust.Employee;
import com.iex.cloud.test.service.CallbackService;

@ContextConfiguration(locations = { "classpath:/dao/dao-service-context.xml"})

@SuppressWarnings("unchecked")
public class EmployeeDaoTest extends BaseDaoTest {
	
	@Resource(name="employeeDaoImpl")
	private EmployeeDao employeeDaoImpl;
	
	@Test
	public void getEmployee() {
		try {
			List<Employee> result = (List<Employee>)this.execute(new CallbackService() {
				@Override
				public Object execute() throws Exception {
					List<Employee> empList = employeeDaoImpl.getEmployees();
					return empList;
				}
				
			});
			
			Assert.assertNotNull(result);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
