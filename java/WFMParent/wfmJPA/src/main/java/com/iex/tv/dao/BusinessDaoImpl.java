package com.iex.tv.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.iex.tv.domain.BaseEntity;

public class BusinessDaoImpl <E extends BaseEntity, PK extends Serializable> extends GenericDaoImpl<E, PK> {
	@PersistenceContext(name="emBusiness") 
	protected EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
