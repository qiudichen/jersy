package com.iex.tv.dao.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.iex.tv.domain.Employee;
import com.iex.tv.domain.Employee.Gender;

@Repository("employeeDaoImpl")
public class EmployeeDaoImpl implements EmployeeDao {

	@PersistenceContext(name="emDemo")
	protected EntityManager em;
	
	@Override
	public long addEmployee(String lastName, String firstName, Gender gender) {
		Employee employee = new Employee(firstName, lastName, gender, new Date(), new Date());
		em.persist(employee);
		em.flush(); //Option
		return employee.getEmpNum();
	}

	@Override
	public Employee findByPk(long empNum) {
		Employee e1 = em.find(Employee.class, empNum);
		//em.detach(e1);
		Employee e = em.find(Employee.class, empNum);
		//em.clear();
		Employee e2 = em.find(Employee.class, empNum);
		return em.find(Employee.class, empNum);
	}

	@Override
	public List<Employee> getEmployees() {
    	TypedQuery<Employee> q = em.createQuery("SELECT e FROM Employee e", Employee.class);
    	List<Employee> result = q.getResultList();
    	return result;
	}

	@Override
	public void updateEmployee(long empNum, String lastName, String firstName) {
		Employee employee = em.getReference(Employee.class, empNum);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
	}

	@Override
	public void deleteEmployee(long empNum) {
		Employee employee = em.getReference(Employee.class, empNum);
		em.remove(employee);
		em.flush();
	}

}
