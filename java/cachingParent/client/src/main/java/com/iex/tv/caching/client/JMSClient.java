package com.iex.tv.caching.client;

import java.util.Collection;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iex.tv.caching.ws.CachingWSService;

public class JMSClient {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("cxf-jmsclient-context.xml");
		CachingWSService service = (CachingWSService)appContext.getBean("cachingServiceClient");
		Collection<String> names = service.getCachingNames();		
		for(String name : names) {
			System.out.println(name);
		}
		appContext.close();
	}
}
