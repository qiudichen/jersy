package com.iex.tv.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.dao.service.EmployeeDao;
import com.iex.tv.domain.Employee;
import com.iex.tv.domain.Employee.Gender;

@Service("employeeServiceImpl")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	@Qualifier("employeeDaoImpl")	
	private EmployeeDao employeeDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Employee> getEmployees() {
		return employeeDao.getEmployees();
	}

	@Override
	@Transactional(readOnly=true)
	public Employee getEmployee(long empNum) {
		return empNum > 0 ? employeeDao.findByPk(empNum) : null;
	}

	@Override
	public long addEmployee(String lastName, String firstName, Gender gender) {
		if(lastName != null && firstName != null) {
			return employeeDao.addEmployee(lastName, firstName, gender);
		} else {
			return 0;
		}
	}

	@Override
	public void deleteEmployee(long empNum) {
		if(empNum > 0) 
			employeeDao.deleteEmployee(empNum);
	}

	@Override
	public void updateEmployee(long empNum, String lastName, String firstName) {
		employeeDao.updateEmployee(empNum, lastName, firstName);
	}

}
