package com.iex.tv.services;

import java.util.List;

import com.iex.tv.domain.system.SubSystemType;
import com.iex.tv.domain.system.SystemProperty;

public interface SystemService {
	public List<SystemProperty> getAllSystemProperties() throws ServiceException;
	
	public List<SystemProperty> getSystemPropertiesBySubType(SubSystemType type) throws ServiceException;
	
	public List<SystemValue> getSystemValueBySubType(SubSystemType type) throws ServiceException;
	
	public SystemProperty addSystemProperty(String name, String value, SubSystemType type) throws ServiceException;
	
	public void deleteSystemProperty(String oid) throws ServiceException;
	
	public void updateSystemProperty(SystemProperty systemProperty) throws ServiceException;
	
	public void updateSystemProperty(String oid, String value) throws ServiceException;
	
	public List<SystemProperty> getSystemPropertiesByRank() throws ServiceException;
}
