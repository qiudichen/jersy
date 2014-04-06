package com.iex.tv.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.dao.GenericDao;
import com.iex.tv.dao.QueryParameter;
import com.iex.tv.domain.system.SubSystemType;
import com.iex.tv.domain.system.SystemProperty;

@Service("systemServiceImpl")
public class SystemServiceImpl implements SystemService {

	@Autowired
	@Qualifier("systemPropertyDAO")	
	private GenericDao<SystemProperty, String> sysPropDAO;
	
	@Override
	@Transactional(value="systemTransactionManager", readOnly=true)
	public List<SystemProperty> getAllSystemProperties()
			throws ServiceException {
		return sysPropDAO.findAll();
	}

	@Override
	@Transactional(value="systemTransactionManager", readOnly=true)
	public List<SystemProperty> getSystemPropertiesBySubType(SubSystemType type)
			throws ServiceException {
		QueryParameter p = new QueryParameter(type, "type");
		
		Number count = (Number)sysPropDAO.findSingleResultByNamedQuery(SystemProperty.NamedQuery.QUERY_GET_COUNT, p);
		
		if(count.intValue() > 0) {
			return sysPropDAO.findByNamedQuery(SystemProperty.NamedQuery.QUERY_FIND_BY_SUBTYPE, p);
		}
		
		return Collections.emptyList();
	}

	@Override
	@Transactional(value="systemTransactionManager", readOnly=true)
	public List<SystemProperty> getSystemPropertiesByRank() throws ServiceException {
		List<SystemProperty> results = sysPropDAO.findByNamedQuery(SystemProperty.NamedQuery.QUERY_GET_SECOND);
		return results;
	}
	
	@Transactional(value="systemTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=ServiceException.class)
	@Override
	public SystemProperty addSystemProperty(String name, String value, SubSystemType type)
			throws ServiceException {
		SystemProperty systemProperty = new SystemProperty(name, value, type);
		sysPropDAO.persist(systemProperty);
		return systemProperty;
	}

	@Transactional(value="systemTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=ServiceException.class)
	@Override
	public void deleteSystemProperty(String oid) throws ServiceException {
		sysPropDAO.remove(oid);
	}

	@Transactional(value="systemTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=ServiceException.class)
	@Override
	public void updateSystemProperty(SystemProperty systemProperty)
			throws ServiceException {
		sysPropDAO.merge(systemProperty);
	}

	@Transactional(value="systemTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=ServiceException.class)
	@Override
	public void updateSystemProperty(String oid, String value)
			throws ServiceException {
		SystemProperty entity = sysPropDAO.findByPk(oid);
		entity.setValue(value);
	}

	@Override
	public List<SystemValue> getSystemValueBySubType(SubSystemType type)
			throws ServiceException {
		final String queryStr = "SELECT NEW  com.iex.tv.services.SystemValue(s.name, s.value) FROM SystemProperty AS s WHERE s.subsystem = :type";
		QueryParameter p = new QueryParameter(type, "type");
		List<SystemValue> result = sysPropDAO.find(queryStr, SystemValue.class, p);
		return result;
	}
}
