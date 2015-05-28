package com.e2.jersy.core;

import java.net.URI;
import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class JerseyRestClient {

	public static void main(String[] args) {
		
		try {			
            System.out.println(URLEncoder.encode("String with spaces", "UTF-8"));
            System.out.println(URLEncoder.encode("special chars: &%*", "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

		
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource webResource = client.resource(getBaseURI());
		
		WebResource saveResource = webResource.path("jhon").path("/200");
		ClientResponse response = saveResource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		
		if(response.getStatus() == 200) {
			String output = response.getEntity(String.class);
			System.out.println(output);
		}
		
		 
		WebResource getResource = webResource.path("rest").path("access").path("hello");
		getResource = getResource.path("test");
		response = getResource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		if(response.getStatus() == 200) {
			String output = response.getEntity(String.class);
			System.out.println(output);		
		}

	}

	public static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/cxfrest/rest/payment/transaction").build();
	}
}
