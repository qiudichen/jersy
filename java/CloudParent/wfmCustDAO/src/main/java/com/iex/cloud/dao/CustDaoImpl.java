package com.iex.cloud.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

import com.iex.cloud.domain.BaseEntity;

public class CustDaoImpl<E extends BaseEntity, PK extends Serializable> extends HibernateDaoImpl<E, PK> {
	protected SessionFactory sessionFactory;

	@Override
	@Resource(name="emSystem") 
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
