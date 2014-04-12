package com.iex.tv.dao.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.iex.tv.domain.Employee;
import com.iex.tv.domain.Employee.Gender;

@Repository("employeeDaoImpl")
public class EmployeeDaoImpl implements EmployeeDao {
	
	@PersistenceContext(name="emDemo")
	protected EntityManager em;
	
	@Override
	public List<Employee> getEmployees() {
    	TypedQuery<Employee> q = em.createQuery("SELECT e FROM Employee e", Employee.class);
    	List<Employee> result = q.getResultList();
    	return result;
	}

	@Override
	public Employee findByPk(long empNum) {
		return em.find(Employee.class, empNum);
	}

	@Override
	public Employee addEmployee(String lastName, String firstName, Gender gender) {
		Employee employee = new Employee(firstName, lastName, gender, new Date(), new Date());
		em.persist(employee);
		em.flush();
		return employee;
	}

	@Override
	public void deleteEmployee(long empNum) {
		Employee employee = em.getReference(Employee.class, empNum);
		em.remove(employee);
		em.flush();
	}

	@Override
	public void deleteEmployee(Employee employee) {
		if(employee != null) {
			em.remove(em.contains(employee) ? employee : em.merge(employee));
		}
	}
	
	@Override
	public Employee updateEmployee(Employee employee, String lastName, String firstName) {
		Employee managedEmployee = em.merge(employee);
		managedEmployee.setFirstName(firstName);
		managedEmployee.setLastName(lastName);
		return managedEmployee;
	}	
	
	@Override
	public Employee updateEmployee(long empNum, String lastName, String firstName) {
		Employee employee = em.getReference(Employee.class, empNum);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		return employee;
	}		
}
