package com.iex.tv.caching.service.hashmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class CacheMapConfigFactgoryBean {
	private static final String DEFAULT_CONFIG = "cachemap-config-context.xml";
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private Resource configLocation;
	
	private Resource schemaLocation;
	
	private boolean shared = false;

	private String cacheManagerName;

	private CacheMapConfigs configs;
	
	private CacheMapCacheManagerImpl cacheManager;
	
	public CacheMapConfigFactgoryBean() {
		
	}
	
	public void setConfigLocation(Resource configLocation) {
		String path = configLocation.getFilename();
		if(path == null || path.startsWith("$")) {
			this.configLocation = new ClassPathResource(DEFAULT_CONFIG);			
			logger.warn("env property cachemapConfigLocation is not set. Use default CacheMap configuration");
		} else {
			this.configLocation = configLocation;
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Cache Map File Name is " + this.configLocation.getFilename());
		}
	}
	
	@PostConstruct
	public void init() throws IOException, JAXBException {
		logger.info("Initializing EHCache CacheManager");

		
		if (this.configLocation != null) {
			if(logger.isDebugEnabled()) {
				String msg = "Cache Map File Location is " + this.configLocation.getURI().toString();
				logger.debug(msg);
			}

			InputStream is = this.configLocation.getInputStream();
			try {
				configs = loadConfigs(is);
			}
			finally {
				is.close();
			}
		}

		if(this.configs != null) {
			this.cacheManager = createCacheMapCacheManagerImpl(configs);
		}
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
		Schema schema = geteSchema();
		if(schema != null) {
			unmarshaller.setSchema(schema);
		}
		CacheMapConfigs cacheMapConfigs = (CacheMapConfigs) unmarshaller.unmarshal(is);
		return cacheMapConfigs;
	}
	
	private Schema geteSchema() {
		try {
			if(this.schemaLocation == null) {
				return null;
			}
			
			URI uri = this.schemaLocation.getURI();
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(uri));
			return schema;
		} catch(Throwable e) {
			return null;
		}
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
	
	public void setSchemaLocation(Resource schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	public static void main(String[] argv) {
		ClassPathResource configLocation = new ClassPathResource("cachemap-config-context.xml");
		ClassPathResource schemaLocation = new ClassPathResource("cachemap.xsd");
		
		CacheMapConfigFactgoryBean bean = new CacheMapConfigFactgoryBean();
		bean.setConfigLocation(configLocation);
		bean.setSchemaLocation(schemaLocation);
		
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
