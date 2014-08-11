package com.iex.tv.cache.service.ehcache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.ehcache.Ehcache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.iex.tv.cache.spi.CacheManager;
import com.iex.tv.cache.spi.CacheManagerFactory;
import com.iex.tv.cache.spi.ScopeResolver;
import com.iex.tv.cache.util.xml.PropertyPlaceholderXMLConfigure;

public class EhCacheManagerFactoryImpl implements CacheManagerFactory<Ehcache> {
	private static final String DEFAULT_CONFIG_NAME = "defaultCacheManager";
	
	private static final String DEFAULT_CONFIG = "ehcache-config-context.xml";
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final ConcurrentMap<String, EhCacheManagerImpl> cacheManagers = new ConcurrentHashMap<String, EhCacheManagerImpl>();	
	
	private ScopeResolver scopeResolver;
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
	
	private PropertyPlaceholderXMLConfigure parser = null;
	
	private Resource getConfigLocation() {
		if(this.configLocation == null) {
			this.configLocation = new ClassPathResource(DEFAULT_CONFIG);			
			logger.warn("env property cachemapConfigLocation is not set. Use default CacheMap configuration");			
		}
		return configLocation;
	}

	protected PropertyPlaceholderXMLConfigure getPropertyPlaceholderXMLConfigure() {
		if(this.parser == null) {
			Resource resource = getConfigLocation();
			this.parser = new PropertyPlaceholderXMLConfigure(resource);
		}
		return this.parser;
	}

	public void setScopeResolver(ScopeResolver scopeResolver) {
		this.scopeResolver = scopeResolver;
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
		String cacheManagerName = this.getCacheManagerName();
		
		if(contains(cacheManagerName)) {
			return this.cacheManagers.get(cacheManagerName);
		}
		
		InputStream is = null;
		try {
			if(this.scopeResolver != null) {
				is = this.getPropertyPlaceholderXMLConfigure().process(this.scopeResolver.getCacheProperties());
			} else {
				is = this.getPropertyPlaceholderXMLConfigure().process(new Properties());
			}
			return addCacheManager(is, cacheManagerName);
		} catch (IOException e) {
			logger.error("Unable to create Ehcache Manager for " + cacheManagerName, e);
		}
		return null;
	}

	private String getCacheManagerName() {
		String cacheManagerName = null;
		if(this.scopeResolver != null) {
			cacheManagerName = this.scopeResolver.getConversationId();
		} else {
			cacheManagerName = DEFAULT_CONFIG_NAME;
		}
		return cacheManagerName;
	}
	
	private CacheManager<Ehcache> addCacheManager(InputStream is, String cacheManagerName) throws IOException {
		logger.info("create EHCache CacheManager: " + cacheManagerName);
		
		net.sf.ehcache.CacheManager cacheManager = null;
		if (is != null) {
			try {
				cacheManager = (this.shared ? net.sf.ehcache.CacheManager.create(is) : new net.sf.ehcache.CacheManager(is));
			}
			finally {
				is.close();
			}
		}
		else {
			cacheManager = (this.shared ? net.sf.ehcache.CacheManager.create() : new net.sf.ehcache.CacheManager());
		}
		
		if (cacheManagerName != null) {
			cacheManager.setName(cacheManagerName);
		}	
		
		EhCacheManagerImpl cacheManagerWraper = new EhCacheManagerImpl(cacheManagerName, cacheManager);
		cacheManagers.put(cacheManagerName, cacheManagerWraper);
		return cacheManagerWraper;
	}

	@PostConstruct
	public void init() {
		logger.info("Initializing EHCache CacheManager");
		getPropertyPlaceholderXMLConfigure();
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
	
	private boolean contains(String cacheManagerName) {
		return this.cacheManagers.containsKey(cacheManagerName);
	}
}
