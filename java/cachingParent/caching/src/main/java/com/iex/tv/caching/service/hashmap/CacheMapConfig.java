package com.iex.tv.caching.service.hashmap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import java.lang.Cloneable;

@XmlRootElement(name = "cache")
//@XmlAccessorType (XmlAccessType.FIELD)
public class CacheMapConfig implements Cloneable {
    /**
     * Default number of seconds that a map entry will expire.
     */
    private static final long EXPIRE_SECONDS_DEFAULT = 120;

    /**
     * Minimum number of seconds between expired entry checks.
     */
    private static final long POLL_SECONDS_MINIMUM = 30;
    
    
	private String name;
	
    /**
     * Number of milliseconds when an entry expires.
     */
    
    private long expireSeconds;
    
    //@XmlTransient
    private long expireMillis = -1;

    /**
     * Flag indicating whether a map entry will ALWAYS expire regardless of it's last access time.
     */

    private boolean forceExpiration;

    /**
     * Number of milliseconds that the <c>cacheMonitor</c> will sleep before invoking the <c>timerExpired()</c>
     * method.
     */
    
    private long pollSeconds;

    public CacheMapConfig() {

    }
    
	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name", required=true)
	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "expireSeconds", required=true)
	public void setExpireSeconds(long expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public boolean isForceExpiration() {
		return forceExpiration;
	}
	
    @XmlAttribute(name = "forceExpiration", required=true)
	public void setForceExpiration(boolean forceExpiration) {
		this.forceExpiration = forceExpiration;
	}

	public long getPollSeconds() {
		if(this.pollSeconds <= 0) {
			setPollSeconds(getExpireSeconds() / 2);
		} 
		return pollSeconds;
	}
	
	@XmlAttribute(name = "pollSeconds")
	public void setPollSeconds(long pollSeconds) {
		this.pollSeconds = Math.max(pollSeconds, POLL_SECONDS_MINIMUM);	
	}

	private long getExpireSeconds() {
		if(this.expireSeconds <= 0) {
			setExpireSeconds(EXPIRE_SECONDS_DEFAULT);
		}		
		return this.expireSeconds;
	}
	
	public long getExpireMillis() {
		if(this.expireMillis < 0) {
			this.expireMillis = getExpireSeconds() * 1000;
		} 
		return expireMillis;
	}
	
	public CacheMapConfig clone() {
        try {
            return (CacheMapConfig) super.clone();
        } catch (CloneNotSupportedException e) {        
            throw new RuntimeException();
        }
    }
}
