package com.e2.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

public class RestClient {
	private static final String BASE_URL = "http://localhost:8099/";
	
	private RestTemplate restTemplate = null;
	
	public RestClient(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext beanFactory =
			     new AnnotationConfigApplicationContext(SpringConfig.class);

		RestTemplate restTemplate = beanFactory.getBean(RestTemplate.class, "restTemplate");
		
		RestClient restClient = new RestClient(restTemplate);
		restClient.testOneWay();
		System.out.println(restTemplate);
		beanFactory.close();
	}

	public void testOneWay() {
		String result = getForObject("/app1/rest/oneway/{id}", String.class, "id-1");
		result = getForObject("/app1/rest/oneway/{id}", String.class, "id-2");
		
	}
	
    public <T> T getForObject(String url, Class<T> cls, Object... urlVariables) {
    	//getRestTemplate().getForObject(slashPath(restUrl)+"listportlets/{appName}/metadata/{dmsName}", DMSMeta.class, appName, dmsName);
        T result = restTemplate.getForObject(BASE_URL + url, cls, urlVariables);
        return result;
    }    
    
    public <T> T postForObject(String url, Class<T> cls, Object request, Object... urlVariables) {
    	//getRestTemplate().postForObject("listportlets/{appName}/data/{dmsName}/{entityId}", data, DMSData.class, appName, dmsName, entityId);
    	//postForObject(esbHost + "/auth/onceonlytoken/", locator, OnceOnlyToken.class);
        T result = restTemplate.postForObject(BASE_URL + url, request, cls, urlVariables);
        return result;
    }        
}
