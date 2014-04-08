package com.iex.tv.domain.training;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="ADDRESS")
public class Address implements Serializable {
	@Id 
	@Column(name="ADDRESS_ID")
	@SequenceGenerator(name="seqAddrId", sequenceName="SEQ_ADDR_ID", allocationSize = 5, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqAddrId")
	private long id;

	@Column(name="STREET", nullable = false, length = 40)
	private String street;
	
	@Column(name="CITY", nullable = false, length = 40)
	private String city;
	
	@Column(name="STATE", nullable = false, length = 2)
	private String state;
	
	@Column(name="COUNTRY", nullable = false, length = 40)
	private String country;
	
	@Column(name="ZIP", nullable = false, length = 5)
	private String zip;
	
	@ManyToOne
	@JoinColumn(name="AGENT", nullable=false, insertable=true, updatable=false)	
	private Agent agent;
	
	public Address() {
		super();
	}
	
	public Address(String street, String city, String state, String country,
			String zip) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public long getId() {
		return id;
	}
	
}
