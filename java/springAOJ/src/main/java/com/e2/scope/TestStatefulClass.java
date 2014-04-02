package com.e2.scope;


public class TestStatefulClass {
	String path;
	public TestStatefulClass() {
		System.out.println("TestStatefulClass.TestStatefulClass()");
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void display() {
		System.out.println(this.path);
	}
	
	public void close() {
		System.out.println("close");
	}
}
