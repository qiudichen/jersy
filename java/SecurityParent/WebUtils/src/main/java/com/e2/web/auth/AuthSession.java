package com.e2.web.auth;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AuthSession implements Serializable {
	public final static String SESSION_KEY = AuthSession.class.getSimpleName();
	
	private String token;
	
	private String endPoint;
	
	private String tenantId;
	
	private String userId;
	
	private long lastAccessTime;
	
	public AuthSession(String token, String endPoint) {
		this.token = token;
		this.endPoint = endPoint;
		this.lastAccessTime = System.currentTimeMillis();
	}
	
	void touch() {
		this.lastAccessTime = System.currentTimeMillis();
	}
	
	boolean isExpired(long maxExpiredDuration) {
		long currentTime = System.currentTimeMillis();
		return this.lastAccessTime + maxExpiredDuration < currentTime;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public String getToken() {
		return token;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "AuthSession [token=" + token + ", endPoint=" + endPoint
				+ ", tenantId=" + tenantId + ", userId=" + userId
				+ ", lastAccessTime=" + lastAccessTime + "]";
	}	
}
