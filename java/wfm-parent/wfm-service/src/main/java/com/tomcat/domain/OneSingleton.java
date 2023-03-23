package com.tomcat.domain;

public class OneSingleton {
	private static OneSingleton oneSingleton;
	
	private OneDomain oneDomain;
	
	public static OneSingleton getInstance() {
		if(oneSingleton == null) {
			oneSingleton = new OneSingleton();
		}
		return oneSingleton;
	}
	
	private OneSingleton() {
		oneDomain = new OneDomain();
	}
	
	public String foo() {
		BaseSingletion baseSingleton = BaseSingletion.getInstance();
		System.out.println(" baseSingleton.hashCode() -- " + baseSingleton.hashCode());
		return oneDomain.foo();
	}
}
