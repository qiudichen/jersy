package com.demo.persistent.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "devcallout")


@NamedQueries({ @NamedQuery(name = "findAgentcalloutByServicename", query = "from Agentcallout e where e.servicename = :servicename") })

public class Agentcallout implements java.io.Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;	
	
	@Column(name = "agentcalloutid", length=10)
    private String agentcalloutid;

	@Column(name = "servicename", length=20)
    private String servicename;

    public Agentcallout() {
    	
    }

	public Agentcallout(String agentcalloutid, String servicename) {
		super();
		this.agentcalloutid = agentcalloutid;
		this.servicename = servicename;
	}

	public String getAgentcalloutid() {
		return agentcalloutid;
	}

	public void setAgentcalloutid(String agentcalloutid) {
		this.agentcalloutid = agentcalloutid;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public Long getId() {
		return id;
	}
}
