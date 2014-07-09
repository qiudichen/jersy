package com.iex.tv.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="AGENT_DETAIL")
public class AgentDetail extends BaseEntity {
	
	@Id 
	@Column(name="AGENT_DETAIL_ID")
	@org.hibernate.annotations.GenericGenerator(name="agent-primarykey", strategy="foreign",
		parameters={@org.hibernate.annotations.Parameter(name="property", value="agent")
	})
	@GeneratedValue(generator = "agent-primarykey")
	private long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
	private Agent agent;
	
	@Column(name="DESCRIPTION", length = 255 )
	private String description;
	
	public AgentDetail() {
		super();
	}

	public AgentDetail(Agent agent, String description) {
		super();
		this.agent = agent;
		this.description = description;
	}

	public AgentDetail(String description) {
		super();
		this.description = description;
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
