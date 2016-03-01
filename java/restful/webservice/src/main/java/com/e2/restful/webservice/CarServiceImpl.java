package com.e2.restful.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/car")
public class CarServiceImpl {

	@GET
	@Path("/list")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Cars getCars(@QueryParam("list") final List<String> idList) {
		List<Car> list = new ArrayList<Car>();
		Car car = new Car();
		car .setColor("red");
		car.setMiles(1000);
		car.setVin("vin1234");
		list.add(car);
		return new Cars(list);
	}
	
	@POST
	@Path("/addcar")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Car createPodcast(Car car) {
		car.setColor("Red");
		car.setMiles(1000);
		car.setVin("11111111");
		return car; 	
	}
		
	@POST
	@Path("/addcars")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createPodcast(List<Car> cars) {
		return Response.status(204).build(); 	
	}
		
}
