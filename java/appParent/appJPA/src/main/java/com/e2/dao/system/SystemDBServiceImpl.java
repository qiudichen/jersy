package com.e2.dao.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.e2.dao.GenericDao;
import com.e2.dao.QueryParameter;
import com.e2.domain.sys.SubSystemType;
import com.e2.domain.sys.SystemProperty;

@Repository("systemDBServiceImpl")
public class SystemDBServiceImpl implements SystemDBService {

	@Autowired
	@Qualifier("sysPropDAO")	
	private GenericDao<SystemProperty, String> sysPropDAO;
	
	@Override
	public List<SystemProperty> getAllSystemProperties() {
		return sysPropDAO.findAll();
	}

	@Override
	public List<SystemProperty> getSystemPropertiesBySubType(SubSystemType type) {
		QueryParameter p = new QueryParameter(type, "type");
		return sysPropDAO.findByNamedQuery(SystemProperty.NamedQuery.QUERY_FIND_BY_SUBTYPE, p);
	}
}
