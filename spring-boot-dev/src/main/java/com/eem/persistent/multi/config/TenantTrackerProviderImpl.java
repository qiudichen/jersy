package com.eem.persistent.multi.config;

public class TenantTrackerProviderImpl implements TenantTrackerProvider {

	private boolean multipleTenant;
	
	public TenantTrackerProviderImpl() {
		multipleTenant = true;
	}

	public TenantTrackerProviderImpl(boolean multipleTenant) {
		this.multipleTenant = multipleTenant;
	}
	
	@Override
	public String getTenantId() {
		String tenantId = TenantTracker.getTenantId();
		if(isSingleTenantId(tenantId)) {
			return "";
		}
		return tenantId;
	}
	
	@Override
	public boolean isRunningMultiTenanted() {
		return multipleTenant;
	}

	public void setMultipleTenant(boolean multipleTenant) {
		this.multipleTenant = multipleTenant;
	}
}
