package com.iex.tv.caching.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iex.tv.caching.util.CacheServiceUtil;
import com.iex.tv.caching.ws.KeyType;

public class JMSClient {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("cache-activemq-context.xml", "cxf-jmsclient-context.xml");

		CacheServiceUtil serviceUtil = (CacheServiceUtil)appContext.getBean("cacheServiceUtil");
		
		String cacheName = null;
		String keyValue = null;
		
		KeyType keyType = KeyType.StringType;
		
		serviceUtil.removeObject(cacheName, keyValue, keyType);
		serviceUtil.clear(cacheName);
		appContext.close();
	}
}
