package com.demo.webservice.cxf.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="greet")
public class Greet {
	private String welcome;
	
	public Greet() {
		
	}
	
	public Greet(String welcome) {
		this.welcome = welcome;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}
}
