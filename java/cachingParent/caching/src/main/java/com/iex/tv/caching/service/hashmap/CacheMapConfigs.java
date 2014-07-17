package com.iex.tv.caching.service.hashmap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cacheMapConfigs")
@XmlAccessorType (XmlAccessType.FIELD)
public class CacheMapConfigs {

	@XmlAttribute(name = "threadPoolSize")
	private int threadPoolSize;
	
	@XmlElement(name = "cache")
	private List<CacheMapConfig> configs = null;
	
	public CacheMapConfigs() {
		
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public List<CacheMapConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<CacheMapConfig> configs) {
		this.configs = configs;
	}
}
