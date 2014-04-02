package com.e2.cxf.restful;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Transaction")
public class Transaction implements java.io.Serializable {
	private String result;
	private int amount;
	private String userName;
	
	public Transaction() {

	}
	
	public Transaction(String result, int amount, String userName) {
		super();
		this.result = result;
		this.amount = amount;
		this.userName = userName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	
}
