package com.e2.client.webservice;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.domain.data.AuthRequest;
import com.demo.domain.data.AuthResponse;

public class RestClient {
	private static final String BaseUrl = "http://localhost:8099/";
	private static final String App1 = "app1";
	private static final String App2 = "app2";
	
	private RestTemplate restTemplate;
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext(SpringConfig.class);
		
		RestTemplate restTemplate = beanFactory.getBean(RestTemplate.class, "restTemplate");
		RestClient restClient = new RestClient(restTemplate);
		restClient.testApp1OneWay();
		restClient.testAuthApp2();
		restClient.testAuth();
		restClient.testAuthJSonL();	
		restClient.testAuthxml();
		beanFactory.close();
	}

	public RestClient(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

    protected <T, K> T exchange(String uri, HttpMethod httpMethod, Class<T> cls, K request, MediaType mediaType, Object... urlVariables) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Arrays.asList(mediaType));
    	headers.setContentType(mediaType);
    	
    	HttpEntity entity = request != null ? new HttpEntity<K>(request, headers) : new HttpEntity(headers) ;
    	ResponseEntity<T> response = restTemplate.exchange(BaseUrl + uri, httpMethod, entity, cls, urlVariables);
    	T responseBody = response.getBody();
    	return responseBody;
    }

    public void testAuth() {
    	final String uri = App1 + "/rest/login";
    	AuthRequest authRequest = new AuthRequest("name", "password");

    	//XML request, XML String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST, String.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//XML request, XML AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}    
    	
    	//JSON request, JSON String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST,String.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//JSON request, JSON AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}      	
    }

    public void testAuthApp2() {
    	final String uri = App2 + "/rest/auth";
    	AuthRequest authRequest = new AuthRequest("name", "password");

    	//XML request, XML String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST,String.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//XML request, XML AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}    
    	
    	//JSON request, JSON String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST,String.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//JSON request, JSON AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}      	
    }
    
    public void testAuthJSonL() {
    	final String uri = App1 + "/rest/json/login";
    	AuthRequest authRequest = new AuthRequest("name", "password");
    	
    	//JSON request, JSON String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST,String.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//JSON request, JSON AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//XML request, XML String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST,String.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//XML request, XML AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}    
    }
    
	public void testApp1OneWay() {
		final String uri = App1 + "/rest/oneway/{id}";
		
		String authResponse = exchange(uri, HttpMethod.GET, String.class, null, MediaType.APPLICATION_XML, "test1");
		System.out.println(authResponse);
	}
	
    public void testAuthxml() {
    	final String uri = App1 + "/rest/xml/login";
    	AuthRequest authRequest = new AuthRequest("name", "password");

    	//XML request, XML String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST,String.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//XML request, XML AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_XML);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}     
    	
    	//JSON request, JSON String response
    	try {
	    	String authResponse = exchange(uri, HttpMethod.POST,String.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}   
    	
    	//JSON request, JSON AuthResponse object response
    	try {
    		AuthResponse authResponse = exchange(uri, HttpMethod.POST,AuthResponse.class, authRequest, MediaType.APPLICATION_JSON);
	    	System.out.println(authResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}    	
    }
}
