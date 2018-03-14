package com.eem.persistent.multi.config;

public class TenantTracker {
    private static ThreadLocal<String> tenantId = new ThreadLocal<String>(){
    	@Override
        protected String initialValue() {
            return "";
        }
    };
	
	public static String getTenantId() {
		return tenantId.get();
	}

	public static void setTenantId(String id) {
		tenantId.set(id);
	}

	public static void clear(){
		tenantId.remove();
	}
	
	public static boolean isTenantIdSet(){
		String id = getTenantId();
		return (id == null || id.isEmpty());
	}
}
