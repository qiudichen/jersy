package com.demo.webservice.cxf;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {
	
	// define URI of web service
    @Bean
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/cxf/*");
    }

    // define BUS
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    } 
    
    // define service bean
    @Bean
    public HelloworldImpl helloworldImpl() {
    		return new HelloworldImpl();
    }  
    
    // define end point of hellow web service
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), helloworldImpl());
        endpoint.publish("/hello");
        //endpoint.setWsdlLocation("Weather1.0.wsdl");
        return endpoint;
    }    
}
