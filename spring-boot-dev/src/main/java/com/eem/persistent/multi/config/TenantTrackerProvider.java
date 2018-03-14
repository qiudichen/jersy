package com.eem.persistent.multi.config;

public interface TenantTrackerProvider {
	public final static String defaultSingleTenantId = "SINGLE_TENANT";
	
	public String getTenantId();
	
	public boolean isRunningMultiTenanted();
	
	public default boolean isSingleTenantId(String tenantId) {
		return defaultSingleTenantId.equals(tenantId);
	}
}
