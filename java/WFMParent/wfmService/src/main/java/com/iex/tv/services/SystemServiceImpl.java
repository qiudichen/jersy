package com.iex.tv.services;

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
	@Transactional(readOnly=true)
	public List<SystemProperty> getAllSystemProperties()
			throws ServiceException {
		return sysPropDAO.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<SystemProperty> getSystemPropertiesBySubType(SubSystemType type)
			throws ServiceException {
		QueryParameter p = new QueryParameter(type, "type");
		return sysPropDAO.findByNamedQuery(SystemProperty.NamedQuery.QUERY_FIND_BY_SUBTYPE, p);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=ServiceException.class)
	@Override
	public void addSystemProperty(SystemProperty systemProperty)
			throws ServiceException {
		sysPropDAO.persist(systemProperty);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=ServiceException.class)
	@Override
	public void deleteSystemProperty(String oid) throws ServiceException {
		sysPropDAO.remove(oid);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=ServiceException.class)
	@Override
	public void updateSystemProperty(SystemProperty systemProperty)
			throws ServiceException {
		sysPropDAO.merge(systemProperty);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=ServiceException.class)
	@Override
	public void updateSystemProperty(String oid, String value)
			throws ServiceException {
		SystemProperty entity = sysPropDAO.findByPk(oid);
		entity.setValue(value);
	}
}
