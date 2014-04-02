package com.e2.dao.cust;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.e2.dao.GenericDaoImpl;
import com.e2.domain.BaseEntity;

public class CustJpaDaoImpl<E extends BaseEntity, PK extends Serializable> extends GenericDaoImpl<E, PK> {
	@PersistenceContext(name="emCustomer") 
	protected EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
