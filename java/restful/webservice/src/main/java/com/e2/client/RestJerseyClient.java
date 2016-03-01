package com.e2.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import com.e2.restful.webservice.Car;
import com.e2.restful.webservice.Cars;

public class RestJerseyClient {
	private static Logger logger = Logger.getLogger(RestJerseyClient.class.getName());
	
	public static void main(String argv[]) {
		postAuth();
		// getXML();
		// getJSON();
		// postXML();
		 postJSON();
	}	
	
	private static void getXML() {
		ClientConfig clientConfig = new ClientConfig().register(Cars.class);
		
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget webTarget = client.target("http://localhost:8080/webservice/rest").path("car").path("list").queryParam("list", "123").queryParam("list", "222");

		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_XML);
		
		Response response = invocationBuilder.get();
		
		Cars list = response.readEntity(Cars.class);
		System.out.println(response.getStatus());
	}
	
	private static void getJSON() {
		ClientConfig clientConfig = new ClientConfig().register(Cars.class);
		
		Client client = ClientBuilder.newClient(clientConfig);
		client.register(new LoggingFilter(logger, true));
		
		WebTarget webTarget = client.target("http://localhost:8080/webservice/rest").path("car").path("list").queryParam("list", "123").queryParam("list", "222");

		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
		
		Response response = invocationBuilder.get();
		
		Cars list = response.readEntity(Cars.class);
		System.out.println(response.getStatus());
	}
	
	
	
	private static void postXML() {
		ClientConfig clientConfig = new ClientConfig().register(Car.class);
		
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget webTarget = client.target("http://localhost:8080/webservice/rest").path("car").path("addcar");
		
		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_XML);
		
		Car car = new Car();
		car.setColor("red");
		Response response = invocationBuilder.post(Entity.entity(car, MediaType.APPLICATION_XML));
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));		
	}
	
	private static void postAuth() {
		ClientConfig clientConfig = new ClientConfig().register(Car.class);
		
		Client client = ClientBuilder.newClient(clientConfig);
		
		//WebTarget webTarget = client.target("http://localhost:4433/esb/user/login");
		WebTarget webTarget = client.target("http://172.25.110.223:4433/esb/user/login");
		
		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_XML);
		
		String payload = "<com.merced.platform.auth.domain.AuthRequest><userName>ftony</userName><password>1234</password><properties/><locale></locale></com.merced.platform.auth.domain.AuthRequest>";
		
		Response response = invocationBuilder.post(Entity.entity(payload, MediaType.APPLICATION_XML));
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));		
	}

	
	private static void postJSON() {
		ClientConfig clientConfig = new ClientConfig().register(Car.class);
		
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget webTarget = client.target("http://localhost:8080/webservice/rest").path("car").path("addcar");
		
		Invocation.Builder invocationBuilder =	webTarget.request(MediaType.APPLICATION_JSON);
		
		Car car = new Car();
		car.setColor("red");
		Response response = invocationBuilder.post(Entity.entity(car, MediaType.APPLICATION_JSON));
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));		
	}	
}
