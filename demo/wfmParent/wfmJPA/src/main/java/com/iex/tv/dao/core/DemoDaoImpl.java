package com.iex.tv.dao.core;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.iex.tv.domain.BaseEntity;

public class DemoDaoImpl<E extends BaseEntity, PK extends Serializable> extends EntityCommonDaoImpl<E, PK> {
	
	@PersistenceContext(name="emDemo")
	protected EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public DemoDaoImpl() {
		super();
	}

	public DemoDaoImpl(Class<E> entityClass) {
		super(entityClass);
	}
}
