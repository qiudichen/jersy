package com.iex.tv.caching.test.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType (XmlAccessType.FIELD)
public class DataItem {
	@XmlAttribute(name = "key", required=true)
	private String key;
	
	@XmlElement(name = "value")
	private String value;
	
	public DataItem() {
		
	}

	public DataItem(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
