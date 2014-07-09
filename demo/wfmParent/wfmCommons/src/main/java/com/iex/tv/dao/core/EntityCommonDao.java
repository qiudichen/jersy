package com.iex.tv.dao.core;

import java.io.Serializable;
import java.util.List;

import com.iex.tv.domain.BaseEntity;

public interface EntityCommonDao <E extends BaseEntity, PK extends Serializable> extends CommonDao {

	public void remove(PK primaryKey);
	
	public E getReference(PK primaryKey);
		
	public E findByPk(PK key);

	public List<E> findAll(final int... rowStartIdxAndCount);
	
	public List<E> find(String queryString);

	public List<E> find(String queryString, final int... rowStartIdxAndCount);

	public List<E> find(String queryString, final QueryParameter... parameters);

	public List<E> find(String queryString, final List<QueryParameter> parameters, int... rowStartIdxAndCount);

	public List<E> findByNamedQuery(String namedQuery);
	
	public List<E> findByNamedQuery(String namedQuery, final int... rowStartIdxAndCount);

	public List<E> findByNamedQuery(String namedQuery, final QueryParameter... parameters);

	public List<E> findByNamedQuery(String namedQuery, final List<QueryParameter> parameters, int... rowStartIdxAndCount);

	public List<E> findByNativeQuery(String sqlString);

	public List<E> findByNativeQuery(String sqlString, final int... rowStartIdxAndCount);

	public List<E> findByNativeQuery(String sqlString, QueryParameter... parameters);

	public List<E> findByNativeQuery(String sqlString, List<QueryParameter> parameters, final int... rowStartIdxAndCount);
}
