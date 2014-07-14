package com.iex.tv.caching.ws;

import java.util.Collection;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

@WebService(targetNamespace = "com.iex.tv.caching", name = "CachingService")
public interface CachingWSService {
	@WebMethod(operationName = "getCachingNames")
	public Collection<String> getCachingNames();

	@WebMethod(operationName = "removeObject")
	public void removeObject(
			@WebParam(name = "cacheName", mode = Mode.IN) String cacheName,
			@WebParam(name = "keyValue", mode = Mode.IN) String keyValue,
			@WebParam(name = "keyType", mode = Mode.IN) KeyType keyType);
	
	@WebMethod(operationName = "clear")
	public void clear(
			@WebParam(name = "cacheName", mode = Mode.IN) String cacheName);
}
