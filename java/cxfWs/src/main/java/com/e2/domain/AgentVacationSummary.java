package com.e2.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "com.iex.tv.smartsync.ws.agent.vacationimport", name = "agentVacationSummary", propOrder = { "agentVacation" })
@XmlRootElement(name = "agentVacationSummary")
public class AgentVacationSummary {

	@XmlElement(required = true)
	protected List<AgentVacation> agentVacation;

	public List<AgentVacation> getAgentVacation() {
		if (agentVacation == null) {
			agentVacation = new ArrayList<AgentVacation>();
		}
		return this.agentVacation;
	}

	public void addAgent(AgentVacation vacAgentToAdd) {
		if (agentVacation == null) {
			agentVacation = new ArrayList<AgentVacation>();
		}

		agentVacation.add(vacAgentToAdd);
	}

	/**
	 * @param agentVacationParm
	 *            The agentVacation to set.
	 */
	public void setAgentVacation(List<AgentVacation> agentVacationParm) {
		agentVacation = agentVacationParm;
	}

	/**
	 * Returned the errored vacation in a new <code>AgentVacationSummary</code>
	 * 
	 * @return
	 */
	public AgentVacationSummary getErroredAgents() {
		AgentVacationSummary returnList = new AgentVacationSummary();
		return returnList;
	}
}
