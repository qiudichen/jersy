package com.iex.tv.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class Person implements Serializable {
	
	@Column(name="FIRSTNAME", nullable = false, length = 40)
	private String firstName;
	
	@Column(name="MIDDLENAME", nullable = true, length = 40)
	private String middleName;
	
	@Column(name="LASTNAME", nullable = false, length = 40)
	private String lastName;
	
	public Person() {
		super();
	}

	public Person(String firstName, String middleName, String lastName) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}