package com.iex.tv.caching.service.ehcache;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

public class EhCacheManagerFactoryBean {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private Resource configLocation;
	
	private boolean shared = false;

	private String cacheManagerName;

	private CacheManager cacheManager;
	
	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
	}
	
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}
	
	public CacheManager getCacheManager() {
		return this.cacheManager;
	}
	
	@PostConstruct
	public void init() throws IOException, CacheException {
		logger.info("Initializing EHCache CacheManager");
		
		if (this.configLocation != null) {
			InputStream is = this.configLocation.getInputStream();
			try {
				this.cacheManager = (this.shared ? CacheManager.create(is) : new CacheManager(is));
			}
			finally {
				is.close();
			}
		}
		else {
			this.cacheManager = (this.shared ? CacheManager.create() : new CacheManager());
		}
		
		if (this.cacheManagerName != null) {
			this.cacheManager.setName(this.cacheManagerName);
		}		
	}
	
	@PreDestroy
	public void shutdown() {
		logger.info("Shutting down EHCache CacheManager");
		this.cacheManager.shutdown();
	}
}
