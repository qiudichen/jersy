package com.iex.tv.cache.spi;

import java.util.Properties;

public interface ScopeResolver {
	String getConversationId();
	
	Properties getCacheProperties();
}
