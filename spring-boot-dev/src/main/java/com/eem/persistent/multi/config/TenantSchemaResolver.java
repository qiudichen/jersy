package com.eem.persistent.multi.config;

public interface TenantSchemaResolver {
	String getTenantSchema(String tenantId);
	String getDefaultSchema();
}
