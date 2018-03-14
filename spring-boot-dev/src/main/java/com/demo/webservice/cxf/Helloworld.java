package com.demo.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.demo.webservice.cxf.data.Greet;
import com.demo.webservice.cxf.data.Person;

@WebService(name = "Helloworld", targetNamespace = "http://www.demo.com/namespace/helloworld/")
public interface Helloworld {

    @WebMethod(operationName = "sayHello", action = "http://www.demo.com/namespace/helloworld/sayHello")
    @WebResult(name = "greeting", targetNamespace="http://www.demo.com/types")
    public Greet sayHello(
    		@WebParam(name="person", targetNamespace="http://www.demo.com/types") 
    		Person person);
}
