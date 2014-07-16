package com.iex.tv.caching.ws.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.iex.tv.caching.spi.CacheManager;
import com.iex.tv.caching.ws.CachingWSService;
import com.iex.tv.caching.ws.KeyType;

@Service("cachingWSServiceImpl")
@WebService(endpointInterface = "com.iex.tv.caching.ws.CachingWSService")
public class CachingWSServiceImpl implements CachingWSService {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired(required = false)
	@Qualifier("cacheManager")
	private CacheManager<?> cacheManager;
	
	@Override
	public Collection<String> getCachingNames() {
		List<String> names = new ArrayList<String>();
		names.add("test1");
		names.add("test2");
		names.add("test3");
		names.add("test4");
		return names;
	}

	@Override
	public void removeObject(String cacheName, String keyValue,
			KeyType keyType) {
		assert cacheName != null: "cahce Name cannot be null.";
		assert keyValue != null: "keyValue cannot be null.";
		assert keyType != null: "keyType cannot be null.";
		if(checkCacheManager()) {
			this.cacheManager.evict(cacheName, keyType.getValue(keyValue));
		}
	}

	@Override
	public void clear(String cacheName) {
		assert cacheName != null: "cahce Name cannot be null.";
		if(checkCacheManager()) {
			this.cacheManager.clear(cacheName);
		}
	}

	private boolean checkCacheManager() {
		if(this.cacheManager == null) {
			logger.warn("CacheManager is not binded to this service.");
			return false;
		}
		return true;
	}
}
