package com.e2.jersy.core;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class JerseyRestClient {

	public static void main(String[] args) {
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource webResource = client.resource(getBaseURI());
		
		//webResource.path("rest").path("payment").path("save");
		
		ClientResponse response = webResource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		
		int i = response.getStatus();
		String output = response.getEntity(String.class);
		System.out.println(output);
		 

	}

	public static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/webservice/rest/access/hello/").build();
	}
}
