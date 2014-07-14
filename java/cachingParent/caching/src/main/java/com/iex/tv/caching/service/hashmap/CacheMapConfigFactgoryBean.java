package com.iex.tv.caching.service.hashmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class CacheMapConfigFactgoryBean {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private Resource configLocation;
	
	private boolean shared = false;

	private String cacheManagerName;

	private CacheMapConfigs configs;
	
	private CacheMapCacheManagerImpl cacheManager;
	
	public CacheMapConfigFactgoryBean() {
		
	}
	
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}
	
	@PostConstruct
	public void init() throws IOException, JAXBException {
		logger.info("Initializing EHCache CacheManager");

		
		if (this.configLocation != null) {
			InputStream is = this.configLocation.getInputStream();
			try {
				CacheMapConfigs configs = loadConfigs(is);
				System.out.println(configs);
			}
			finally {
				is.close();
			}
		}

		this.cacheManager = createCacheMapCacheManagerImpl(configs);
	}
	
	private CacheMapCacheManagerImpl createCacheMapCacheManagerImpl(CacheMapConfigs configs) {
		CacheMapCacheManagerImpl cacheManager = new CacheMapCacheManagerImpl(configs);

		if (this.cacheManagerName != null) {
			cacheManager.setName(this.cacheManagerName);
		}
		
		cacheManager.init();
		return cacheManager;
	}
	
	private CacheMapConfigs loadConfigs(InputStream is) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(CacheMapConfigs.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		CacheMapConfigs cacheMapConfigs = (CacheMapConfigs) unmarshaller.unmarshal(is);
		return cacheMapConfigs;
	}
	
	private void outputConfigs(CacheMapConfigs cacheMapConfigs, OutputStream out) {
		try {
			JAXBContext jc = JAXBContext.newInstance(CacheMapConfigs.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(cacheMapConfigs, out);
		} catch(JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public CacheMapCacheManagerImpl getCacheManager() {
		if(this.shared) {
			return this.cacheManager;
		} else {
			return createCacheMapCacheManagerImpl(configs);
		}
	}		

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
	}
	
	public static void main(String[] argv) {
		ClassPathResource configLocation = new ClassPathResource("cachemap-config-context.xml");
		
		CacheMapConfigFactgoryBean bean = new CacheMapConfigFactgoryBean();
		bean.setConfigLocation(configLocation);
		
		
		try {
			bean.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CacheMapConfigs cacheMapConfigs = new CacheMapConfigs();
		cacheMapConfigs.setThreadPoolSize(10);
		
		List<CacheMapConfig> configs = new ArrayList<CacheMapConfig>();
		
		CacheMapConfig config = new CacheMapConfig();
		configs.add(config);
		config.setExpireSeconds(1000);
		config.setForceExpiration(true);
		config.setName("agent");
		config.setPollSeconds(2000);
		
		
		config = new CacheMapConfig();
		configs.add(config);
		config.setExpireSeconds(1001);
		config.setForceExpiration(true);
		config.setName("supervisor");
		config.setPollSeconds(2001);
		
		cacheMapConfigs.setConfigs(configs);
		
		bean.outputConfigs(cacheMapConfigs, System.out);
	}
}
