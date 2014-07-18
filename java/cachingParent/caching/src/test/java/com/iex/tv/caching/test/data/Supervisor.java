package com.iex.tv.caching.test.data;

import java.io.Serializable;


public class Supervisor implements Serializable {
	enum Type {Manager, Leader, SVP };
	
	private String firstName;
	private String lastName;
	private long id;
	private int rank;
	private String descritpion;
	private Type type;
	
	public Supervisor() {
		
	}

	public Supervisor(String firstName, String lastName, long id, int rank,
			String descritpion, Type type) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.rank = rank;
		this.descritpion = descritpion;
		this.type = type;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDescritpion() {
		return descritpion;
	}

	public void setDescritpion(String descritpion) {
		this.descritpion = descritpion;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	
}
