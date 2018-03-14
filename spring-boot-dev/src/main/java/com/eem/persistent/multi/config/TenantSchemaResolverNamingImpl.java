package com.eem.persistent.multi.config;

public class TenantSchemaResolverNamingImpl implements TenantSchemaResolver {
	
	private String schemaPrefix;
	
	private String defaultSchema;
	
	public TenantSchemaResolverNamingImpl() {
		
	}

	private String getSchemaPrefix() {
		if(schemaPrefix == null && schemaPrefix.isEmpty()) {
			return "custschema";
		}
		return schemaPrefix;
	}


	public void setSchemaPrefix(String schemaPrefix) {
		this.schemaPrefix = schemaPrefix;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	@Override
	public String getTenantSchema(String tenantId) {
		return getSchemaPrefix() + tenantId;
	}

	@Override
	public String getDefaultSchema() {
		if(defaultSchema == null && defaultSchema.isEmpty()) {
			return getSchemaPrefix();
		}
		return defaultSchema;
	}

}
