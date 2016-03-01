package com.e2.web.jms;

import java.io.Serializable;

public class SimpleObject implements Serializable {
	private long sendTime;
	
	private String buf;
	
	public SimpleObject() {
		
	}

	public SimpleObject(long sendTime, String buf) {
		super();
		this.sendTime = sendTime;
		this.buf = buf;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public String getBuf() {
		return buf;
	}

	public void setBuf(String buf) {
		this.buf = buf;
	}
}
