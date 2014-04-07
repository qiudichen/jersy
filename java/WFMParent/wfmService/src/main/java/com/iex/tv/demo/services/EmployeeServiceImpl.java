package com.iex.tv.demo.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.demo.dao.EmployeeDao;
import com.iex.tv.domain.training.Employee;
import com.iex.tv.domain.training.Employee.Gender;

@Service("employeeServiceImpl")
public class EmployeeServiceImpl implements EmployeeService { 
	private static List<Employee> employees = new ArrayList<Employee>();
	{ 
		employees.add(new Employee(1l, new Date(), "David", "Chen", Gender.M, new Date()));
		employees.add(new Employee(2l, new Date(), "David1", "Chen1", Gender.M, new Date()));
		employees.add(new Employee(2l, new Date(), "David2", "Chen2", Gender.M, new Date()));
	}

	@Autowired
	@Qualifier("employeeDaoImpl")	
	private EmployeeDao employeeDao;
	
	@Override
	@Transactional(value="demoTransactionManager", readOnly=true)
	public List<Employee> getEmployees() {
		return employeeDao.getEmployees();
	}
	
	@Override
	@Transactional(value="demoTransactionManager", readOnly=true)	
	public Employee getEmployee(long empNum) {
		return employeeDao.findByPk(empNum);
	}
	
	@Override
	@Transactional(value="demoTransactionManager", propagation = Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public Employee addEmployee(String lastName, String firstName, Gender gender) throws DBRollback {
		try {
			Employee emp = employeeDao.addEmployee(lastName, firstName, gender);
			if(lastName.equals("error")) {
				throw new DBRollback();
			}
			return emp;
		} catch(Exception e) {
			e.printStackTrace();
			throw new DBRollback(e);
		}
	}

	@Override
	@Transactional(value="demoTransactionManager", propagation = Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public void deleteEmployee(long empNum) throws DBRollback {
		try {
			employeeDao.deleteEmployee(empNum);
		} catch(Exception e) {
			throw new DBRollback(e);
		}
	}

	@Override
	@Transactional(value="demoTransactionManager", propagation = Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public Employee updateEmployee(long empNum, String lastName, String firstName) throws DBRollback {
		try {		
			Employee employee = employeeDao.findByPk(empNum);
			if(employee == null) {
				return null;
			}
			
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			return employee;
		} catch(Exception e) {
			throw new DBRollback(e);
		}
	}
	
	@Override
	@Transactional(value="demoTransactionManager", propagation = Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public Employee updateEmployee(Employee employee, String lastName, String firstName) throws DBRollback {
		try {
			return employeeDao.updateEmployee(employee, lastName, firstName);
		} catch(Exception e) {
			throw new DBRollback(e);
		}		
	}	
}
