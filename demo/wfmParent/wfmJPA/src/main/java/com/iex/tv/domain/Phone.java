package com.iex.tv.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="PHONE")
public class Phone extends BaseEntity {
	@Id 
	@Column(name="ID")
	@SequenceGenerator(name="seqPhoneId", sequenceName="SEQ_PHONE_ID", allocationSize = 5, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqPhoneId")
	private long id;
	
	@Column(name="PHONENUM", nullable = false, length = 40)
	private String phone;
	
	@Column(name="AGENT", nullable = false)
	private long agentId;

	public Phone() {
		super();
	}
	
	public Phone(String phone) {
		super();
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public long getId() {
		return id;
	}
	
	
}