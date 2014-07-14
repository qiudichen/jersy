package com.iex.tv.caching.ws.services;

import java.util.Collection;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.iex.tv.caching.ws.CachingWSService;
import com.iex.tv.caching.ws.KeyType;

@Service("cachingWSServiceImpl")
@WebService(endpointInterface = "com.iex.tv.caching.ws.CachingWSService")
public class CachingWSServiceImpl implements CachingWSService {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public Collection<String> getCachingNames() {
		return null;
	}

	@Override
	public void removeObject(String cacheName, String keyValue,
			KeyType keyType) {

	}

	@Override
	public void clear(String cacheName) {
		
	}

}
