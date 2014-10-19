package com.iex.tv.cache.test.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Agent implements Serializable {
	private String oid;
	private String firstName;
	private String lastName;
	private long id;
	private int rank;
	private String descritpion;
	
	public Agent() {
		
	}

	public Agent(String oid, String firstName, String lastName, long id, int rank,
			String descritpion) {
		super();
		this.oid = oid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.rank = rank;
		this.descritpion = descritpion;
	}

	public String getOid() {
		return oid;
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
}
