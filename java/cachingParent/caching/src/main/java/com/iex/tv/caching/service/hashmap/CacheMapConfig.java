package com.iex.tv.caching.service.hashmap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cache")
@XmlAccessorType (XmlAccessType.FIELD)
public class CacheMapConfig {
    /**
     * Default number of seconds that a map entry will expire.
     */
    private static final long EXPIRE_SECONDS_DEFAULT = 120;

    /**
     * Minimum number of seconds between expired entry checks.
     */
    private static final long POLL_SECONDS_MINIMUM = 30;
    
    @XmlAttribute(name = "cacheName", required=true)
	private String name;
	
    /**
     * Number of milliseconds when an entry expires.
     */
    @XmlAttribute(name = "expireSeconds", required=true)
    private long expireMillis;

    /**
     * Flag indicating whether a map entry will ALWAYS expire regardless of it's last access time.
     */
    @XmlAttribute(name = "forceExpiration", required=true)
    private boolean forceExpiration;

    /**
     * Number of milliseconds that the <c>cacheMonitor</c> will sleep before invoking the <c>timerExpired()</c>
     * method.
     */
    @XmlAttribute(name = "pollSeconds")
    private long pollSeconds;

    public CacheMapConfig() {

    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getExpireMillis() {
		return expireMillis;
	}

	public void setExpireSeconds(long expireSeconds) {
		this.expireMillis = expireSeconds;
	}

	public boolean isForceExpiration() {
		return forceExpiration;
	}

	public void setForceExpiration(boolean forceExpiration) {
		this.forceExpiration = forceExpiration;
	}

	public long getPollSeconds() {
		return pollSeconds;
	}

	public void setPollSeconds(long pollSeconds) {
        this.pollSeconds = pollSeconds;
	}

	public void init() {
		if(this.expireMillis < 0) {
			setExpireSeconds(EXPIRE_SECONDS_DEFAULT);
		} 
		this.expireMillis = this.expireMillis * 1000;
		
		if(this.pollSeconds <= 0) {
			setPollSeconds(EXPIRE_SECONDS_DEFAULT / 2);
		} else {
			this.pollSeconds = Math.max(pollSeconds, POLL_SECONDS_MINIMUM);			
		}
	}
}
