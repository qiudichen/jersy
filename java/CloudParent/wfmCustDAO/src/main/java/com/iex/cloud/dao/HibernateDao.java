package com.iex.cloud.dao;

import java.io.Serializable;
import java.util.List;

import com.iex.cloud.domain.BaseEntity;

public interface HibernateDao<E extends BaseEntity, PK extends Serializable> extends BaseDao {
    public void flush();
    
	public E findByPk(PK id); 

	public void remove(PK pk);
	    
    public int update(String query, final List<QueryParameter> queryParameters);

    public int update(String query, QueryParameter... queryParameters);
    
    public int updateByNativeQuery(String queryName, QueryParameter... queryParameters);    
    
    public List<E> find(String query, final int... rowStartIdxAndCount);

    public List<E> find(String query, final QueryParameter... queryParameters);

    public List<E> find(String query, List<QueryParameter> queryParameters, final int... rowStartIdxAndCount);    
    
    public List<E> findAll(final int... rowStartIdxAndCount);

    public List<E> findByNamedQuery(String queryName);

    public List<E> findByNamedQuery(String queryName, final int... rowStartIdxAndCount);
    
    public List<E> findByNamedQuery(String queryName, final QueryParameter... queryParameters);    
    
    public List<E> findByNamedQuery(String queryName, List<QueryParameter> queryParameters);

    public List<E> findByNamedQuery(String queryName, List<QueryParameter> queryParameters, final int... rowStartIdxAndCount);
}
