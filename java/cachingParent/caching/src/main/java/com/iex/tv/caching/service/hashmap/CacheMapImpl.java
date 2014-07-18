package com.iex.tv.caching.service.hashmap;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iex.tv.caching.service.CacheImpl;

public class CacheMapImpl extends CacheImpl<Map<Object, CacheMapImpl.CacheElement>> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private CacheMapConfig cacheMapConfig;

    private CacheMapCacheManagerImpl cacheManager;

    private transient ScheduledFuture<?> scheduledFuture;
    
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
        if ((cacheElement != null) && (!hasExpired(cacheElement)))
        {
            value = cacheElement.getValue();

            // If we're not forcing the value to expire, update its last access time
            if (!cacheMapConfig.isForceExpiration())
            {
                cacheElement.touch();
            }
        }
        return value;
	}

	@Override
	public void put(Object key, Object value) {
		assert key != null : "key must be not null";
		assert value != null : "value must be not null";
		CacheElement oldElement = cache.put(key, new CacheElement(value));
        if (oldElement == null) // Key not found...value added
        {
            startExpireMonitor();
        }		
	}

	@Override
	public void remove(Object key) {
		cache.remove(key);
	}

	@Override
	public void clear() {
        stopExpireMonitor();
        cache.clear();
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
	
    private void startExpireMonitor()
    {
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
    
    private void stopExpireMonitor()
    {
        synchronized (cache)
        {
            // Stop the cache monitor thread
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
    }	
	
    /**
     * @param value CacheElement non-null
     * @return boolean
     */
    private boolean hasExpired(CacheElement element)
    {
        // No null check for performance
        return ((System.currentTimeMillis() - element.getLastAccessedTime()) > cacheMapConfig.getExpireMillis());
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
    
	@SuppressWarnings("serial")
	public class CacheElement implements Comparable<CacheElement>, Serializable
    {
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
        public CacheElement(Object valueParm)
        {
            this.value = valueParm;
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
        public void touch()
        {
            lastAccessedTime = System.currentTimeMillis();
            hits += 1;
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
			int numBefore = cache.size();

			// For expiration, 0 means never expire
			if ((cacheMapConfig.getExpireMillis() > 0) && (numBefore > 0)) {
				logStats();
				long now = System.currentTimeMillis();

				Set<Entry<Object, CacheElement>> entries = cache.entrySet();
				 
				synchronized (cache) {
					Iterator<Map.Entry<Object, CacheElement>> iter = entries.iterator();
					while (iter.hasNext()) {
						Map.Entry<Object, CacheElement> entry = iter.next();

						// Remove the entry if it hasn't been accessed within
						// expireMillis
						CacheElement cacheElement = entry.getValue();
						
						if (now - cacheElement.getLastAccessedTime() > cacheMapConfig.getExpireMillis()) {
							iter.remove();
						}
					}

					int numAfter = cache.size();
					if (logger.isDebugEnabled()) {
						int numRemoved = numBefore - numAfter;
						logger.debug(getName() + ":Removed " + numRemoved + "/" + numBefore + " entries");
					}

					// If no more entries, stop the cache monitor thread
					if (cache.size() == 0) {
						stopExpireMonitor();
					}
				}
			}
		}
	}
}
