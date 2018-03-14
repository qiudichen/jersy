package com.demo.webservice.jersey;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component
@Path("/foo")
public class CalloutRest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> bar() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("hello", "world");
		return map;
	}
}
