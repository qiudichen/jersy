package com.eem.persistent.multi.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

	private final Logger logger = LoggerFactory.getLogger(CurrentTenantIdentifierResolverImpl.class);

	public void setValidateSession(boolean validateSession) {
		this.validateSession = validateSession;
	}

	private boolean validateSession;
	
	private TenantTrackerProvider tenantTrackerProvider;
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenantId = getTenantTrackerProvider().getTenantId();
		if(logger.isDebugEnabled()) {
			logger.debug("TenantId = " + tenantId);
		}
		return tenantId;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return validateSession;
	}

	public void setTenantTrackerProvider(TenantTrackerProvider tenantTrackerProvider) {
		this.tenantTrackerProvider = tenantTrackerProvider;
	}

	public TenantTrackerProvider getTenantTrackerProvider() {
		if(tenantTrackerProvider == null) {
			if(logger.isDebugEnabled()) {
				logger.debug("TenantTrackerProvider is not set. Use build-in TenantTrackerProviderImpl.");
			}
			tenantTrackerProvider = new TenantTrackerProviderImpl();
		}
		return tenantTrackerProvider;
	}	
}
