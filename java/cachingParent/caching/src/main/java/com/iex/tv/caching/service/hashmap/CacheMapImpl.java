package com.iex.tv.caching.service.hashmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iex.tv.caching.service.CacheImpl;

public class CacheMapImpl extends CacheImpl<Map<Object, CacheMapImpl.CacheElement>> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private CacheMapConfig cacheMapConfig;

    private CacheMapCacheManagerImpl cacheManager;

    private CacheMapEventListener eventListener;
    
    private transient ScheduledFuture<?> scheduledFuture;
    
    private long firstObjectLastAccessedTime = 0;
    private long lastObjectLastAccessedTime = 0;
    
    public CacheMapImpl(CacheMapConfig cacheMapConfig, CacheMapCacheManagerImpl cacheManager)
    {
    	super();
        this.cacheMapConfig = cacheMapConfig;
        setName(this.cacheMapConfig.getName());
        Map<Object, CacheElement> rawCache = new HashMap<Object, CacheElement>();
        setCache(Collections.synchronizedMap(rawCache));
        this.cacheManager = cacheManager;
    }


    
	@Override
	public Object getValue(Object key) {
		Object value = null;

        // Only return a non-null value if it's not expired
        CacheElement cacheElement = (CacheElement)cache.get(key);
        if (cacheElement != null)
        {
        	if(hasExpired(cacheElement)) {
        		remove(key);
        	} 
        	else {
	            value = cacheElement.getValue();
	
	            // If we're not forcing the value to expire, update its last access time
	            if (cacheMapConfig.isForceExpiration())
	            {
	            	lastObjectLastAccessedTime = cacheElement.touch();
	            }
        	}
        }
        return value;
	}

	@Override
	public void put(Object key, Object value) {
		put(key, value, false);
	}

	@Override
	public void put(Object key, Object value,
			boolean doNotNotifyCacheReplicators) {
		assert key != null : "key must be not null";
		assert value != null : "value must be not null";
		CacheElement newObj = new CacheElement(key, value);
		if(this.firstObjectLastAccessedTime <= 0) {
			this.firstObjectLastAccessedTime = newObj.getLastAccessedTime();
		}
		
		if(this.lastObjectLastAccessedTime <= 0) {
			this.lastObjectLastAccessedTime = this.firstObjectLastAccessedTime;
		}
		CacheElement oldElement = cache.put(key, newObj);
        if (oldElement == null) // Key not found...value added
        {
            startExpireMonitor();
        }		
        
        if(!doNotNotifyCacheReplicators) {
        	if(this.eventListener != null) {
        		if(oldElement == null) {
        			this.eventListener.notifyElementPut(this, newObj);
        		} else {
        			this.eventListener.notifyElementUpdated(this, newObj);
        		}
        	}
        }
	}
	
	@Override
	public void remove(Object key) {
		remove(key, false);
	}

	@Override
	public void remove(Object key, boolean doNotNotifyCacheReplicators) {
		CacheElement element = null;
		synchronized(cache) {
			element = cache.remove(key);
		}
		
		if(!doNotNotifyCacheReplicators) {
			if(this.eventListener != null) {
				if(element == null) {
					element = new CacheElement(key, null);
				}
				this.eventListener.notifyElementRemoved(this, element);
			}
		}
	}

	@Override
	public void clear(boolean doNotNotifyCacheReplicators) {
        synchronized (cache)
        {		
        	if(cacheMapConfig.isForceExpiration()) {
        		stopExpireMonitor();
        	}
        	cache.clear();
        }
        
		if(!doNotNotifyCacheReplicators) {
			if(this.eventListener != null) {
				this.eventListener.notifyRemoveAll(this);;
			}
		}
	}
	
	@Override
	public void clear() {
		clear(false);
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	@Override
	public int size() {
		return this.cache.size();
	}
	
	public CacheElement getCacheElement(Object key) {
        CacheElement cacheElement = (CacheElement)cache.get(key);
        return cacheElement;
	}
	
	public List<?> getKeys() {
		return new ArrayList<Object>(super.getNativeCache().keySet());
	}
	
    private void startExpireMonitor()
    {
    	if(cacheMapConfig.isForceExpiration() && scheduledFuture == null) {
	        synchronized (cache)
	        {
	            // If not running, create/start the cache monitor
	            if (scheduledFuture == null)
	            {
	                scheduledFuture = this.cacheManager.getScheduledThreadPool().scheduleAtFixedRate(new RemoveExpiredEntriesTask(), 
	                		cacheMapConfig.getPollSeconds(), cacheMapConfig.getPollSeconds(), TimeUnit.SECONDS);
	            }
	        }
    	}
    }

    public boolean isScheduleRunning() {
    	if(this.scheduledFuture == null) {
    		return false;
    	}
    	
    	if(this.scheduledFuture.isCancelled()) {
    		return false;
    	}
    	
    	if(this.scheduledFuture.isDone()) {
    		return false;
    	}
    	return true;
    }
    
    /*
     * this method must be called inside synchronized block
     */
    private void stopExpireMonitor()
    {
    	// Stop the cache monitor thread
    	this.firstObjectLastAccessedTime = 0;
    	this.lastObjectLastAccessedTime = 0;
    	if (scheduledFuture != null)
    	{
    		try {
    			scheduledFuture.cancel(true);
    		} catch(Exception e) {
    			logger.warn("Shutting down CacheMap CacheManager error.", e);
    		}
    		scheduledFuture = null;
    	}
    }	
    
    /**
     * @param value CacheElement non-null
     * @return boolean
     */
    private boolean hasExpired(CacheElement element)
    {
        // No null check for performance
    	long now = System.currentTimeMillis();
    	long duration = now - element.getLastAccessedTime();
        return (duration > cacheMapConfig.getExpireMillis());
    }
    
    private int removeExpiredElement(long now) {
    	int removedCount = 0;
    	firstObjectLastAccessedTime = lastObjectLastAccessedTime;
    	
		Iterator<Map.Entry<Object, CacheElement>> iter = cache.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Object, CacheElement> entry = iter.next();

			// Remove the entry if it hasn't been accessed within expireMillis
			CacheElement cacheElement = entry.getValue();
			if (now - cacheElement.getLastAccessedTime() > cacheMapConfig.getExpireMillis()) {
				iter.remove();
				removedCount++;
			} else {
				firstObjectLastAccessedTime = Math.min(firstObjectLastAccessedTime, cacheElement.getLastAccessedTime());
			}
		}   
		return removedCount;
    }
    
    private void logStats()
    {
        // Since this is an expensive operation, only do it if the info will be logged
        if (logger.isDebugEnabled())
        {
            try
            {
                synchronized (cache)
                {
                    for (Map.Entry<Object, CacheElement> entry : cache.entrySet())
                    {
                        logger.debug("k:" + entry.getKey() + ", v:" + entry.getValue());
                    }
                }
            }
            catch (Exception except)
            {
                logger.debug(except);
            }
        }
    }
    
    public CacheMapConfig getCacheMapConfig() {
    	return cacheMapConfig;
    }
    
    public void dispose() {
    	this.clear(true);
    	if(this.eventListener != null) {
    		this.eventListener.dispose();
    	}
    }
    
	@SuppressWarnings("serial")
	public class CacheElement implements Comparable<CacheElement>, Serializable
    {
		private Object key;
        /**
         * Encapsulated object to track in the cache.
         */
        private Object value;

        /**
         * Last time, in milliseconds since UTC, since this object was accessed.
         */
        private long lastAccessedTime;

        private long hits;
        
        /**
         * Stores a reference to the value and sets the last access time of the value.
         * 
         * @param value Object to wrap with an expired time.
         */
        public CacheElement(Object key, Object valueParm)
        {
            this.value = valueParm;
            this.key = key;
            touch();
            this.hits = 0;
        }

        /**
         * Uses the underlying value's equals() method
         * 
         * @param obj The reference object with which to compare.
         * @return true if the underlying value's are the same, false otherwise.
         */
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            else if ((obj == null) || !(CacheElement.class.isAssignableFrom(obj.getClass())))
            {
                return false;
            }

            Object otherValue = ((CacheElement) obj).getValue();

            if (value == otherValue)
            {
                return true;
            }
            else if (value == null)
            {
                return false;
            }
            else
            {
                return value.equals(otherValue);
            }
        }	
        /**
         * Compares CacheElement's based on last accessed time.
         * 
         * @param obj Reference object with which to compare to.
         * @return a negative, zero, or positive integer if this object is less than, equal, or greater then the
         *         specified object.
         */
        // Comparable:START
        public int compareTo(CacheElement other)
        {
            int returnValue = 1;

            long thatVal = other.lastAccessedTime;
            long thisVal = lastAccessedTime;
            returnValue = (thisVal < thatVal ? -1 : (thisVal == thatVal ? 0 : 1));

            return returnValue;
        }

        // Comparable:END

        /**
         * Returns a reference to the underlying value.
         * 
         * @return Object passed in during creation.
         */
        public Object getValue()
        {
            return value;
        }

        /**
         * Returns a reference to the underlying key.
         * 
         * @return key passed in during creation.
         */        
        public Object getKey() {
        	return key;
        }
        /**
         * Returns the last time this object was accessed. Accessed means created or touched. NOTE: getElement() does
         * not currently update this time.
         * 
         * @return Number of milliseconds since UTC.
         */
        public long getLastAccessedTime()
        {
            return lastAccessedTime;
        }

        /**
         * Resets the last accessed time to the current time.
         */
        public long touch()
        {
            lastAccessedTime = System.currentTimeMillis();
            hits += 1;
            return this.lastAccessedTime;
        }

        public long getHits() {
        	return hits;
        }
        @Override
        public String toString()
        {
            StringBuffer buf = new StringBuffer();

            buf.append('[');
            buf.append("time:");
            buf.append(lastAccessedTime);
            buf.append(", val:");
            buf.append(value);
            buf.append(']');

            return buf.toString();
        }
    }

	private class RemoveExpiredEntriesTask implements Runnable {
		/**
		 * Called when the expire check timer expires. This will iterate through
		 * the entire map, removing expired entries along the way.
		 * 
		 * @see Runnable
		 */
		@Override
		public void run() {
			long now = System.currentTimeMillis();
			if (firstObjectLastAccessedTime != 0 && (now - firstObjectLastAccessedTime > cacheMapConfig.getExpireMillis())) {
				logStats();
				synchronized (cache) {
					if (firstObjectLastAccessedTime != 0 && (now - firstObjectLastAccessedTime > cacheMapConfig.getExpireMillis())) {
						int removedCount = removeExpiredElement(now);
						if(logger.isDebugEnabled()) {
							if(removedCount == 0) {
								logger.debug("No Object is removed");
							} else {
								logger.debug(removedCount + " objects are removed from cache.");
							}
						}
					}
				}
			}
			
			// If no more entries, stop the cache monitor thread
			if (cache.size() == 0) {
				synchronized (cache) {
					if (cache.size() == 0) {
						stopExpireMonitor();
					}
				}
			}
		}
	}
}
