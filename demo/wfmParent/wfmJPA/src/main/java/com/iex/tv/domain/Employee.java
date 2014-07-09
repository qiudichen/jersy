package com.iex.tv.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name="EMPLOYEE")
@Cacheable(true)
public class Employee implements Serializable {
	public enum Gender {
		M, F;
		public static Gender findGender(String gender) {
			return "M".equals(gender) ? Employee.Gender.M : Employee.Gender.F;
		}
	}
	
	@Id 
	@Column(name="EMPNUM")
	@SequenceGenerator(name="seqEmployeeId", sequenceName="SEQ_EMP_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqEmployeeId")	
	private long empNum;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDATE", nullable=false)
	private Date birthDate;
	
	@Column(name="FIRSTNAME", nullable = false, length = 40)
	private String firstName;
	
	@Column(name="LASTNAME", nullable = false, length = 40)
	private String lastName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="GENDER", nullable = false)
	private Gender gender;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "HIREDATE", nullable=false)
	private Date hireDate;
	
	public Employee() {
		
	}
	
	public Employee(long empNum, Date birthDate, String firstName,
			String lastName, Gender gender, Date hireDate) {
		super();
		this.empNum = empNum;
		this.birthDate = birthDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.hireDate = hireDate;
	}

	public Employee(long empNum) {
		super();
		this.empNum = empNum;
	}
	
	public Employee(String firstName, String lastName, Gender gender, Date birthDate, Date hireDate) {
		super();
		this.birthDate = birthDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.hireDate = hireDate;
	}
	
	public long getEmpNum() {
		return empNum;
	}

	public void setEmpNum(long empNum) {
		this.empNum = empNum;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
}