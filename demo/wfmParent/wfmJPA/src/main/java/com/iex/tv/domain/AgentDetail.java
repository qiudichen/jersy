package com.iex.tv.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="AGENT_DETAIL")
public class AgentDetail extends BaseEntity {
	
	@Id 
	@Column(name="AGENT_DETAIL_ID")
	private long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "AGENT_DETAIL_ID", referencedColumnName="AGENT_ID")    
	private Agent agent;
	
	@Column(name="DESCRIPTION", length = 255 )
	private String description;
	
	public AgentDetail() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
