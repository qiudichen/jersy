package com.demo.webservice.cxf.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="person")
public class Person {
	private String firstName;
	private String lastName;
	
	public Person() {
		
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
}
