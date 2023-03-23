package com.tomcat.domain;

public class BaseSingletion {

	private static BaseSingletion baseSingletion;
	
	public static BaseSingletion getInstance() {
		if(baseSingletion == null) {
			baseSingletion = new BaseSingletion();
		}
		return baseSingletion;
	}
	
	private BaseDomain baseDomain;
	
	private BaseSingletion() {
		baseDomain = new BaseDomain();
	}
	
	public String foo() {
		return baseDomain.foo();
	}
}
