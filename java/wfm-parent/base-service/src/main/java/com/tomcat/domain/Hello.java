package com.tomcat.domain;

public class Hello implements HelloMBean {
    private String message = "Hello World";
    
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void sayHello() {
        System.out.println(message);
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
