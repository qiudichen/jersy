package com.iex.cloud.scope;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.NamedThreadLocal;

public class ScopeContextProvider {
	private static final Log logger = LogFactory.getLog(ScopeContextProvider.class);

	private static final ThreadLocal<String> threadScope = new NamedThreadLocal<String>("ScopeContextProviderImpl");
	
	public static void switchIn(String conversationId) {
		if(conversationId != null) {
			if(logger.isDebugEnabled()) {
				logger.debug("switchIn(" + conversationId + ")");
			}
			threadScope.set(conversationId);
		}
	}

	public static void switchOut() {
		if(logger.isDebugEnabled()) {
			String conversationId = getConversationId();
			logger.debug("switchOut(" + conversationId + ")");
		}		
		threadScope.remove();
	}

	public static String getConversationId() {
		String conversationId = threadScope.get();
		if(logger.isDebugEnabled()) {
			logger.debug("getConversationId() = " + conversationId);
		}			
		return conversationId;
	}
}
