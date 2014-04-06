package com.iex.tv.dao;

import java.io.Serializable;
import java.util.List;

import com.iex.tv.domain.BaseEntity;

public interface GenericDao<E extends BaseEntity, PK extends Serializable> extends BaseGenericDao {
    public void flush();
    
	public E findByPk(PK id); 

	public void remove(PK pk);
	    
    public int update(String query, final List<QueryParameter> args);

    public int update(String query, QueryParameter... args);
    
    public int updateByNativeQuery(String queryName, QueryParameter... args);    
    
    public List<E> find(String query, final int... rowStartIdxAndCount);

    public List<E> find(String query, final QueryParameter... queryParameters);

    public List<E> find(String query, List<QueryParameter> args, final int... rowStartIdxAndCount);    
    
    public List<E> findAll(final int... rowStartIdxAndCount);

    public List<E> findByNamedQuery(String queryName);

    public List<E> findByNamedQuery(String queryName, final int... rowStartIdxAndCount);
    
    public List<E> findByNamedQuery(String queryName, final QueryParameter... args);    
    
    public List<E> findByNamedQuery(String queryName, List<QueryParameter> args);

    public List<E> findByNamedQuery(String queryName, List<QueryParameter> args, final int... rowStartIdxAndCount);

    public List<E> findByNativeQuery(String query);
    
    public List<E> findByNativeQuery(String query, QueryParameter... parms);

    public List<E> findByNativeQuery(String query, List<QueryParameter> args);
    
    public List<E> findByNativeQuery(String sql, final int... rowStartIdxAndCount);
    
    public List<E> findByNativeQuery(String sql, List<QueryParameter> parms, final int... rowStartIdxAndCount);    
}
