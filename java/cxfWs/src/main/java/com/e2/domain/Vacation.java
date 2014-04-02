package com.e2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "com.iex.tv.smartsync.ws.agent.vacationimport", name = "vacation", propOrder = {
		"from", "to", "earned", "credited", "debited", "selected", "taken",
		"minFullWeeks", "maxPartialHours", "coFrom", "coTo" })
@XmlRootElement(name = "vacation")
public class Vacation {

	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String type;
	protected SSDate from;
	protected SSDate to;
	protected String earned;
	protected String credited;
	protected String debited;
	protected String selected;
	protected String taken;
	protected String minFullWeeks;
	protected String maxPartialHours;
	protected String coFrom;
	protected String coTo;

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the from property.
	 * 
	 * @return possible object is {@link From }
	 */
	public SSDate getFrom() {
		return from;
	}

	/**
	 * Sets the value of the from property.
	 * 
	 * @param value
	 *            allowed object is {@link From }
	 */
	public void setFrom(SSDate value) {
		this.from = value;
	}

	/**
	 * Gets the value of the to property.
	 * 
	 * @return possible object is {@link To }
	 */
	public SSDate getTo() {
		return to;
	}

	/**
	 * Sets the value of the to property.
	 * 
	 * @param value
	 *            allowed object is {@link To }
	 */
	public void setTo(SSDate value) {
		this.to = value;
	}

	/**
	 * Gets the value of the earned property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getEarned() {
		return earned;
	}

	/**
	 * Sets the value of the earned property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setEarned(String value) {
		this.earned = value;
	}

	/**
	 * Gets the value of the credited property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getCredited() {
		return credited;
	}

	/**
	 * Sets the value of the credited property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setCredited(String value) {
		this.credited = value;
	}

	/**
	 * Gets the value of the debited property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getDebited() {
		return debited;
	}

	/**
	 * Sets the value of the debited property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setDebited(String value) {
		this.debited = value;
	}

	/**
	 * Gets the value of the selected property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getSelected() {
		return selected;
	}

	/**
	 * Sets the value of the selected property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setSelected(String value) {
		this.selected = value;
	}

	/**
	 * Gets the value of the taken property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getTaken() {
		return taken;
	}

	/**
	 * Sets the value of the taken property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setTaken(String value) {
		this.taken = value;
	}

	/**
	 * Gets the value of the minFullWeeks property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getMinFullWeeks() {
		return minFullWeeks;
	}

	/**
	 * Sets the value of the minFullWeeks property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setMinFullWeeks(String value) {
		this.minFullWeeks = value;
	}

	/**
	 * Gets the value of the maxPartialHours property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getMaxPartialHours() {
		return maxPartialHours;
	}

	/**
	 * Sets the value of the maxPartialHours property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setMaxPartialHours(String value) {
		this.maxPartialHours = value;
	}

	/**
	 * Gets the value of the coFrom property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getCoFrom() {
		return coFrom;
	}

	/**
	 * Sets the value of the coFrom property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setCoFrom(String value) {
		this.coFrom = value;
	}

	/**
	 * Gets the value of the coTo property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getCoTo() {
		return coTo;
	}

	/**
	 * Sets the value of the coTo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setCoTo(String value) {
		this.coTo = value;
	}
}
