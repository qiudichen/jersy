package com.iex.tv.caching.test.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.core.io.Resource;

@XmlRootElement(name = "cachedata")
@XmlAccessorType (XmlAccessType.FIELD)
public class CacheTestData {
	@XmlElement(name = "cachedata")
	private List<CacheData> cachedatas;	
		
	@XmlTransient
	private Map<String, CacheData> cachedataSet;
	
	public CacheTestData() {
		
	}

	public List<CacheData> getCachedatas() {
		return cachedatas;
	}

	public void setCachedatas(List<CacheData> cachedatas) {
		this.cachedatas = cachedatas;
	}

	protected Map<String, CacheData> getCachedataSet() {
		if(cachedataSet == null && cachedatas != null) {
			cachedataSet = new HashMap<String, CacheData>(cachedatas.size());
			for(CacheData cacheData : cachedatas) {
				cachedataSet.put(cacheData.getName(), cacheData);
			}
		}
		return cachedataSet;
	}

	public CacheData getCacheData(String name) {
		return getCachedataSet().get(name);
	}
	
	public Set<String> getCacheDataNames() {
		return this.getCachedataSet().keySet();
	}
	
 	public void setCachedataSet(Map<String, CacheData> cachedataSet) {
		this.cachedataSet = cachedataSet;
	}

	public static CacheTestData createInstance (Resource resource) throws JAXBException, IOException {
		CacheTestData cacheTestData = XMLDataLoader.loader(resource, CacheTestData.class);
		return cacheTestData;
	}
}
