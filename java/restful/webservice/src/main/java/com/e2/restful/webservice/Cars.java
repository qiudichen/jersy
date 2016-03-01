package com.e2.restful.webservice;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cars")
public class Cars {
	private List<Car> list;
	
	
	public Cars() {
	}
	
	public Cars(List<Car> list) {
		this.list = list;
	}

	public List<Car> getList() {
		return list;
	}

	public void setList(List<Car> list) {
		this.list = list;
	}
	
	
}
