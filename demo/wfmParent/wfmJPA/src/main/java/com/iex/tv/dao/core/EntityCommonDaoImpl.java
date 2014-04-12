package com.iex.tv.dao.core;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.iex.tv.domain.BaseEntity;

public abstract class EntityCommonDaoImpl<E extends BaseEntity, PK extends Serializable> extends CommonDaoImpl implements EntityCommonDao<E, PK> {
	protected Class<E> entityClass;

	@SuppressWarnings("unchecked")
	protected EntityCommonDaoImpl() {
		super();
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];		
	}

	public EntityCommonDaoImpl(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
	
	@Override
	public E getReference(PK primaryKey) {
		return super.getReference(entityClass, primaryKey);
	}

	@Override
	public E findByPk(PK key) {
		return super.findByPk(entityClass, key);
	}

	@Override
	public List<E> findAll(int... rowStartIdxAndCount) {
		return super.findAll(entityClass, rowStartIdxAndCount);
	}

	@Override
	public List<E> find(String queryString) {
		return super.find(entityClass, queryString);
	}

	@Override
	public List<E> find(String queryString, int... rowStartIdxAndCount) {
		return super.find(entityClass, queryString, rowStartIdxAndCount);
	}

	@Override
	public List<E> find(String queryString, QueryParameter... parameters) {
		return super.find(entityClass, queryString, parameters);
	}

	@Override
	public List<E> find(String queryString, List<QueryParameter> parameters,
			int... rowStartIdxAndCount) {
		return super.find(entityClass, queryString, parameters, rowStartIdxAndCount);
	}

	@Override
	public List<E> findByNamedQuery(String namedQuery) {
		return super.findByNamedQuery(entityClass, namedQuery);
	}

	@Override
	public List<E> findByNamedQuery(String namedQuery,
			int... rowStartIdxAndCount) {
		return super.findByNamedQuery(entityClass, namedQuery, rowStartIdxAndCount);
	}

	@Override
	public List<E> findByNamedQuery(String namedQuery,
			QueryParameter... parameters) {
		return super.findByNamedQuery(entityClass, namedQuery, parameters);
	}

	@Override
	public List<E> findByNamedQuery(String namedQuery,
			List<QueryParameter> parameters, int... rowStartIdxAndCount) {
		return super.findByNamedQuery(entityClass, namedQuery, parameters, rowStartIdxAndCount);
	}

	@Override
	public List<E> findByNativeQuery(String sqlString) {
		return super.findByNativeQuery(entityClass, sqlString);
	}

	@Override
	public List<E> findByNativeQuery(String sqlString,
			int... rowStartIdxAndCount) {
		return super.findByNativeQuery(entityClass, sqlString, rowStartIdxAndCount);
	}

	@Override
	public List<E> findByNativeQuery(String sqlString, QueryParameter... parameters) {
		return super.findByNativeQuery(entityClass, sqlString, parameters);
	}

	@Override
	public List<E> findByNativeQuery(String sqlString,
			List<QueryParameter> parameters, int... rowStartIdxAndCount) {
		return super.findByNativeQuery(entityClass, sqlString, parameters, rowStartIdxAndCount);
	}

	@Override
	public void remove(PK primaryKey) {
		super.remove(entityClass, primaryKey);
	}
}
