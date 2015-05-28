package com.iex.cloud.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import com.iex.cloud.domain.BaseEntity;

public class HibernateDaoImpl<E extends BaseEntity, PK extends Serializable> extends BaseHibernateDaoImpl
implements HibernateDao<E, PK> {
	
	protected static Logger log = Logger.getLogger(HibernateDaoImpl.class.getName());
	
	protected Class<E> entityClass;

	protected Class<PK> keyClass;
	
	protected String className;
	
	protected SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public HibernateDaoImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		
		this.className = this.getClass().getName();

		Type[] classes = genericSuperclass.getActualTypeArguments();
		if(classes != null) {
			if(classes.length > 0 ) {
				this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
			}
			
			if(classes.length > 1) {
				this.keyClass = (Class<PK>) genericSuperclass.getActualTypeArguments()[1];
			}
		}
		
		log.debug(className);
		log.debug(entityClass);
		log.debug(keyClass);
		
	}
    
    public HibernateDaoImpl(final Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	protected Class<E> getEntityClass() {
		return entityClass;
	}
	
	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    private String findAllSQL;
    private String getFindAllSQL() {
    	if(findAllSQL == null) {
    		findAllSQL = getFindAllSQL(this.entityClass);
    	}
    	return findAllSQL;
    }
    
	@Override
	public E findByPk(PK id) {
		return findByPk(entityClass, id);
	}

	@Override
	public void remove(PK pk) {
		super.remove(entityClass, pk);
	}

	@Override
	public List<E> find(String query, int... rowStartIdxAndCount) {
		return super.find(query, this.entityClass, rowStartIdxAndCount);
	}

	@Override
	public List<E> find(String query, QueryParameter... queryParameters) {
		return super.find(query, this.entityClass, queryParameters);
	}

	@Override
	public List<E> find(String query, List<QueryParameter> queryParameters, int... rowStartIdxAndCount) {
		return  find(query, queryParameters, this.entityClass, rowStartIdxAndCount);
	}

	@Override
	public List<E> findAll(int... rowStartIdxAndCount) {
		return find(getFindAllSQL(), rowStartIdxAndCount);
	}

	@Override
	public List<E> findByNamedQuery(String queryName) {
		return super.findByNamedQuery(queryName, this.entityClass) ;
	}

	@Override
	public List<E> findByNamedQuery(String queryName,
			int... rowStartIdxAndCount) {
		return super.findByNamedQuery(queryName, this.entityClass, rowStartIdxAndCount);
	}

	@Override
	public List<E> findByNamedQuery(String queryName, QueryParameter... queryParameters) {
		return super.findByNamedQuery(queryName, this.entityClass, queryParameters);
	}

	@Override
	public List<E> findByNamedQuery(String queryName, List<QueryParameter> queryParameters) {
		return super.findByNamedQuery(queryName, this.entityClass, queryParameters);
	}

	@Override
	public List<E> findByNamedQuery(String queryName,
			List<QueryParameter> queryParameters, int... rowStartIdxAndCount) {
		return super.findByNamedQuery(queryName, this.entityClass, queryParameters, rowStartIdxAndCount);
	}
}
