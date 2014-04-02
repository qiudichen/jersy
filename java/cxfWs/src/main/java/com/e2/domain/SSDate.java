package com.e2.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "day", "month", "year" })
public class SSDate implements Serializable
{
	@XmlElement(required = true)
	protected String day;

    @XmlElement(required = true)
    protected String month;
    
    @XmlElement(required = true)
    protected String year;

    /**
     * Required default CTOR
     */
    public SSDate()
    {
        super();
    }

    /**
     * CTOR for tests
     * 
     * @param dayParm
     * @param monthParm
     * @param yearParm
     */
    public SSDate(String dayParm, String monthParm, String yearParm)
    {
        super();
        day = dayParm;
        month = monthParm;
        year = yearParm;
    }

    /**
     * Gets the value of the day property.
     * 
     * @return possible object is {@link String }
     */
    public String getDay()
    {
        return day;
    }

    /**
     * Sets the value of the day property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setDay(String value)
    {
        this.day = value;
    }

    /**
     * Gets the value of the month property.
     * 
     * @return possible object is {@link String }
     */
    public String getMonth()
    {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setMonth(String value)
    {
        this.month = value;
    }

    /**
     * Gets the value of the year property.
     * 
     * @return possible object is {@link String }
     */
    public String getYear()
    {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setYear(String value)
    {
        this.year = value;
    }

    public boolean isBlank()
    {
        return false;
    }
}