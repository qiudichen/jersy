package com.iex.tv.caching.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iex.tv.caching.util.CacheServiceUtil;
import com.iex.tv.caching.ws.KeyType;

public class WSClient {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("cxf-wsclient-context.xml");
		CacheServiceUtil serviceUtil = (CacheServiceUtil)appContext.getBean("cacheServiceUtil");
		
		String cacheName = null; //"agent";
		String keyValue = "agent.1";
		KeyType keyType = KeyType.StringType;
		
		serviceUtil.removeObject(cacheName, keyValue, keyType);
		serviceUtil.clear(cacheName);
		appContext.close();
	}
}
