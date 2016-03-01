package com.e2.jersy.core;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;

import org.apache.http.HttpStatus;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;

import com.demo.domain.data.SessionLocator;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.thoughtworks.xstream.XStream;

public class JerseyRestClient {

	public static void main(String[] args) {
		try {
			SessionLocator result0 = getXML(SessionLocator.class, "http://localhost:8099/app1/rest/create/123", null, null);
			
			SessionLocator result = getXML(SessionLocator.class, "http://localhost:8099/app1Queue/rest/create/123", null, null);
			SessionLocator result1 = postXML(SessionLocator.class, "http://localhost:8099/app1Queue/rest/addSession", result);

			SessionLocator result2 = getJson(SessionLocator.class, "http://localhost:8099/app1Queue/rest/create/123", null, null);
			SessionLocator result3 = postJson(SessionLocator.class, "http://localhost:8099/app1Queue/rest/addSession", result2);
			
			//SessionLocator result = getJson(SessionLocator.class, "http://localhost:8099/app1/rest/create/123", null, null);
			System.out.println(result2);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			SessionLocator result = getXML(SessionLocator.class, "http://localhost:8099/app1/rest/create/123", null, null);
			SessionLocator result1 = postXML(SessionLocator.class, "http://localhost:8099/app1/rest/addSession", result);

			SessionLocator result2 = getJson(SessionLocator.class, "http://localhost:8099/app1/rest/create/123", null, null);
			SessionLocator result3 = postJson(SessionLocator.class, "http://localhost:8099/app1/rest/addSession", result2);
			
			//SessionLocator result = getJson(SessionLocator.class, "http://localhost:8099/app1/rest/create/123", null, null);
			System.out.println(result2);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void test() {
		XStream xstream = new XStream();
    	xstream.setMode(XStream.NO_REFERENCES);
    	xstream.autodetectAnnotations(true);
    	xstream.processAnnotations(SessionLocator.class);	
    	SessionLocator a = new SessionLocator("a", "1");
    	String v = xstream.toXML(a);
    	System.out.println(v);
	}
	
	public static <T> T getJson(Class<T> responseEntityClass, String url, String[] names, Object[] values) {
		return get(responseEntityClass, url, names, values, MediaType.APPLICATION_JSON);
	}
	
	public static <T> T getXML(Class<T> responseEntityClass, String url, String[] names, Object[] values) {
		return get(responseEntityClass, url, names, values, MediaType.APPLICATION_XML);
	}
	
	private static <T> T get(Class<T> responseEntityClass, String url, String[] names, Object[] values, String mediaType) {
		ClientConfig clientConfig = new ClientConfig();
		
		if(responseEntityClass != null) {
			clientConfig.register(responseEntityClass);
		}
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget webTarget = client.target(url);

		if(names != null && names.length > 0) {
			for(int i = 0; i < names.length; i++) {
				webTarget.queryParam(names[i], values[i]);
			}
		}
		
		Invocation.Builder invocationBuilder =	webTarget.request(mediaType);
		
		Response response = invocationBuilder.get();
		
		if(response.getStatus() != HttpStatus.SC_OK) {
			System.out.print("Request Failed: " + response.getStatus());			
			return null;
		}
		
		T result = response.readEntity(responseEntityClass);
		return result;
	}	
	
	public static <T> T postJson(Class<T> responseEntityClass, String url, Object requestEntity) {
		return post(responseEntityClass, url, requestEntity, MediaType.APPLICATION_JSON);
	}
	
	public static <T> T postXML(Class<T> responseEntityClass, String url, Object requestEntity) {
		return post(responseEntityClass, url, requestEntity, MediaType.APPLICATION_XML);
	}
	
	private static <T> T post(Class<T> responseEntityClass, String url, Object requestEntity, String mediaType) {
		ClientConfig clientConfig = new ClientConfig();
		
		//clientConfig.getFeatures().puJacksonFeaturet(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);     
		//clientConfig.register(JacksonFeature.class);
		
		if(responseEntityClass != null) {
			clientConfig.register(responseEntityClass);
		}
		
//		final Map<String, String> namespacePrefixMapper = new HashMap<String, String>();
//		namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
//		 
//		final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig()
//		            .setNamespacePrefixMapper(namespacePrefixMapper)
//		            .setNamespaceSeparator(':');
//		 
//		final ContextResolver<MoxyJsonConfig> jsonConfigResolver = moxyJsonConfig.resolver();

		
		Client client = ClientBuilder.newClient(clientConfig); //.register(JacksonFeature.class).register(jsonConfigResolver).register(JacksonJsonProvider.class);
		
		WebTarget webTarget = client.target(url);
		
		Invocation.Builder invocationBuilder =	webTarget.request(mediaType);

		Entity.entity(requestEntity, mediaType);
		
		Response response = invocationBuilder.post(Entity.entity(requestEntity, mediaType));
		
		if(response.getStatus() != HttpStatus.SC_OK) {
			System.out.print("Request Failed: " + response.getStatus());			
			return null;
		}
		
		T result = response.readEntity(responseEntityClass);
		return result;
	}		
}
