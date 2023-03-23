package com.tomcat.domain;

public class WebDomain {

	private BaseDomain baseDomain;
	
	public WebDomain() {
		baseDomain = new BaseDomain();
	}
	
	public String foo() {
		return "WebDomain " + this.baseDomain.foo();
	}
}
