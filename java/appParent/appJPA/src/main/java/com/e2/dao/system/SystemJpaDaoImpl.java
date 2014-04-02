package com.e2.dao.system;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.e2.dao.GenericDaoImpl;
import com.e2.domain.BaseEntity;

public class SystemJpaDaoImpl<E extends BaseEntity, PK extends Serializable> extends GenericDaoImpl<E, PK> {
	@PersistenceContext(name="emSystem") 
	protected EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
