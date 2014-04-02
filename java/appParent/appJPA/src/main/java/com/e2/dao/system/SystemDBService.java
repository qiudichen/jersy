package com.e2.dao.system;

import java.util.List;

import com.e2.domain.sys.SubSystemType;
import com.e2.domain.sys.SystemProperty;

public interface SystemDBService {
	public List<SystemProperty> getAllSystemProperties();
	
	public List<SystemProperty> getSystemPropertiesBySubType(SubSystemType type);
}
