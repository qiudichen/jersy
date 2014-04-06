package com.iex.tv.domain.customer;

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
@Table(name="R_ADDR")
public class Address extends CreateDateEntity {
	@Id 
	@Column(name="C_ID")
	@SequenceGenerator(name="seqAddressId", sequenceName="SEQ_ADDR_ID", allocationSize = 5, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqAddressId")
	private long id;
	
	@Column(name="C_ADDRS1", nullable = false, length = 40)
	private String addrs1;
	
	@Column(name="C_ADDRS2", nullable = true, length = 40)
	private String addrs2;
	
	@Column(name="C_CITY", nullable = false, length = 40)
	private String city;
	
	@Column(name="C_STATE", nullable = false, length = 2)
	private String state;
	
	@Column(name="C_ZIP", nullable = true, length = 5)
	private String zip;
	
	@ManyToOne
	@JoinColumn(name="C_AGT")	
	private Agent agent;

	public Address() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddrs1() {
		return addrs1;
	}

	public void setAddrs1(String addrs1) {
		this.addrs1 = addrs1;
	}

	public String getAddrs2() {
		return addrs2;
	}

	public void setAddrs2(String addrs2) {
		this.addrs2 = addrs2;
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
}
