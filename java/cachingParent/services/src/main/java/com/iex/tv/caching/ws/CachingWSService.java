package com.iex.tv.caching.ws;

import java.util.Collection;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService(targetNamespace = "com.iex.tv.caching", name = "CachingService")
@SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL,parameterStyle=ParameterStyle.BARE)
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
