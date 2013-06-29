package com.e2.restful.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Response;

@Path("/access")
public class GatewayServiceImpl {

	@GET
	@Path("/hello/{msg}")
	//@Produces(MediaType.APPLICATION_JSON)
	public Response getMsg(@DefaultValue("test") @PathParam("msg") String msg) {
		String output = "Jersy say: " + msg;
		return Response.status(200).entity(output).build();
	}
}
