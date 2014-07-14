package com.iex.tv.caching.service.hashmap;

import java.io.Serializable;

public interface CacheMap extends Serializable {
	public Object getValue(Object key);

	public void put(Object key, Object value);

	public void remove(Object key);

	public void clear();

	public boolean containKey(Object key);
}
