package com.e2.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace="com.iex.tv.smartsync",name = "id", propOrder = {
    "value"
})
@XmlRootElement(name = "id")
public class Id implements Serializable{

    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String acdID;
    @XmlValue
    protected String value;

    /**
     * Gets the value of the acdID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcdID() {
        return acdID;
    }

    /**
     * Sets the value of the acdID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcdID(String value) {
        this.acdID = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getvalue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setvalue(String value) {
        this.value = value;
    }
}
