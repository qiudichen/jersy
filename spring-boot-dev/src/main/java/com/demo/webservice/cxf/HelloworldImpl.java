package com.demo.webservice.cxf;

import javax.jws.WebService;

import com.demo.webservice.cxf.data.Greet;
import com.demo.webservice.cxf.data.Person;

@WebService(endpointInterface = "com.demo.webservice.cxf.Helloworld", wsdlLocation="META-INF/wsdl/helloworld.wsdl")
public class HelloworldImpl implements Helloworld {

	@Override
	public Greet sayHello(Person person) {
		Greet greet = new Greet("Hello, " + person.getFirstName() + " "  + person.getLastName() + ", welcome");
		return greet;
	}

}
