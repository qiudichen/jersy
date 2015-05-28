package com.iex.cloud.dao.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.iex.cloud.dao.BaseDaoTest;
import com.iex.cloud.dao.ServiceCallBack;

public class EmployeeDaoImplTest extends BaseDaoTest {
	
	@Resource(name="employeeDaoImpl")
	private EmployeeDao employeeDao;
	
	@Test
	public void addEmployee() {
		try {
			Object result = this.service.run(new ServiceCallBack() {
				@Override
				public Object execute() throws Exception {
					// TODO Auto-generated method stub
					return null;
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
