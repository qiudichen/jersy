package com.eem.persistent.multi.config;

public enum MultiTenancy {
	DATABASE, SCHEMA, PARTITION;
	public static MultiTenancy findByValue(String value) {
		if("DATABASE".equalsIgnoreCase(value)) {
			return DATABASE;
		} else if("SCHEMA".equals(value)) {
			return SCHEMA;
		} else if("PARTITION".equals(value)) {
			return PARTITION;
		}  
		return SCHEMA;
	}
}
