package com.iex.tv.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.iex.tv.domain.BaseEntity;

public class SystemDaoImpl <E extends BaseEntity, PK extends Serializable> extends GenericDaoImpl<E, PK> {
	@PersistenceContext(name="emSystem") 
	protected EntityManager em;
	
	public SystemDaoImpl() {
		super();
	}

	public SystemDaoImpl(Class<E> entityClass) {
		super(entityClass);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
