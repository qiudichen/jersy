package com.e2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "com.iex.tv.smartsync.ws.agent.vacationimport", name = "agent", propOrder = {
		"agentID", "vacGroup", "vacYear" })
@XmlRootElement(name = "agent")
public class Agent {

	@XmlElement(required = true)
	protected Id agentID;
	@XmlElement(required = true)
	protected String vacGroup;
	@XmlElement(required = true)
	protected String vacYear;

	/**
	 * Gets the value of the agentID property.
	 * 
	 * @return possible object is {@link AgentID }
	 */
	public String getAgentID() {
		return agentID.getvalue();
	}

	/**
	 * Sets the value of the agentID property.
	 * 
	 * @param value
	 *            allowed object is {@link AgentID }
	 */
	public void setAgentID(String value) {
		if (agentID == null) {
			agentID = new Id();
		}

		this.agentID.setvalue(value);
	}

	/**
	 * Gets the value of the vacGroup property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getVacGroup() {
		return vacGroup;
	}

	/**
	 * Sets the value of the vacGroup property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setVacGroup(String value) {
		this.vacGroup = value;
	}

	/**
	 * Gets the value of the vacYear property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getVacYear() {
		return vacYear;
	}

	/**
	 * Sets the value of the vacYear property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setVacYear(String value) {
		this.vacYear = value;
	}

	/**
	 * @return Returns the acdId.
	 */
	public String getAcdID() {
		return agentID == null ? null : agentID.getAcdID();
	}

	/**
	 * @param acdIdParm
	 *            The acdId to set.
	 */
	public void setAcdID(String acdIdParm) {
		if (agentID == null) {
			agentID = new Id();
		}
		agentID.setAcdID(acdIdParm);
	}
}
