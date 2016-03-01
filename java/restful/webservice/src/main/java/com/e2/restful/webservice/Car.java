package com.e2.restful.webservice;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "car")
public class Car {
	private String vin;
	private String color;
	private Integer miles;
	
	public Car() {
		
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getMiles() {
		return miles;
	}

	public void setMiles(Integer miles) {
		this.miles = miles;
	}
}
