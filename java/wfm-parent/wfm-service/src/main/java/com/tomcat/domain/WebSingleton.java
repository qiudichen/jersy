package com.tomcat.domain;

public class WebSingleton {
	private static WebSingleton webSingleton;
	
	private WebDomain webDomain;
	
	public static WebSingleton getInstance() {
		if(webSingleton == null) {
			webSingleton = new WebSingleton();
		}
		return webSingleton;
	}
	
	private WebSingleton() {
		webDomain = new WebDomain();
	}
	
	public String foo() {
		return webDomain.foo();
	}
}
