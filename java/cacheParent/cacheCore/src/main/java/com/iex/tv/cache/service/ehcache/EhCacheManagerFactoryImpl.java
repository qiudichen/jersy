package com.iex.tv.cache.service.ehcache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.ehcache.Ehcache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.iex.tv.cache.spi.Cache;
import com.iex.tv.cache.spi.CacheManager;
import com.iex.tv.cache.spi.CacheManagerFactory;

public class EhCacheManagerFactoryImpl implements CacheManagerFactory<Ehcache> {
private static final String DEFAULT_CONFIG = "ehcache-config-context.xml";
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final ConcurrentMap<String, EhCacheManagerImpl> cacheManagers = new ConcurrentHashMap<String, EhCacheManagerImpl>();	
	/**
	 * configuration file location
	 */
	private Resource configLocation;
	
	/**
	 * 
	 */
	private boolean shared = false;
	
	/**
	 * true indicates single cache manager environment;
	 * false indicates multiple cache manager environment;
	 */
	private boolean single = false;
	
	
	
	public Resource getConfigLocation() {
		if(this.configLocation == null) {
			this.configLocation = new ClassPathResource(DEFAULT_CONFIG);			
			logger.warn("env property cachemapConfigLocation is not set. Use default CacheMap configuration");			
		}
		return configLocation;
	}



	public void setConfigLocation(Resource configLocation) {
		String path = configLocation.getFilename();
		if(path != null && !path.startsWith("$")) {
			this.configLocation = configLocation;
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Cache Map File Name is " + this.configLocation.getFilename());
		}
	}



	public boolean isShared() {
		return shared;
	}



	public void setShared(boolean shared) {
		this.shared = shared;
	}



	public boolean isSingle() {
		return single;
	}



	public void setSingle(boolean single) {
		this.single = single;
	}



	@Override
	public CacheManager<Ehcache> createCacheManager() {
		// TODO Auto-generated method stub
		return null;
	}


	@PostConstruct
	public void init() {
		logger.info("Initializing EHCache CacheManager");
		Resource resource = getConfigLocation();
		if(!this.isSingle()) {
			//TODO prepare for multiple cache environment
			
		}
	}
	
	@Override
	public boolean remove(String cacheManagerName) {
		EhCacheManagerImpl cacheManager = this.cacheManagers.remove(cacheManagerName);
		if(cacheManager != null) {
			cacheManager.shutdown();
			return true;
		}
		return false;
	}
	
	@PreDestroy
	public void shutdown() {
		logger.info("Shutting down EHCache CacheManager");
		for(EhCacheManagerImpl cacheManager : cacheManagers.values()) {
			cacheManager.shutdown();
		}
	}	
}
