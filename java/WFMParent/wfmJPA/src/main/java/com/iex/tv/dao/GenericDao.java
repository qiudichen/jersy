package com.iex.tv.dao;

import java.io.Serializable;
import java.util.List;

import com.iex.tv.domain.BaseEntity;

public interface GenericDao<E extends BaseEntity, PK extends Serializable> {
    public void flush();
    
    public void refresh(E entity);

	public E findByPk(PK id); 

	public void persist(E entity);
	
	public void remove(PK pk);
	
    public void remove(E entity);
    
    public E merge(E entity);	
    
    public int update(String query, final List<QueryParameter> args);

    public int update(String query, QueryParameter... args);
    
    public int updateByNativeQuery(String queryName, QueryParameter... args);    
    
    public List<E> find(String query, final int... rowStartIdxAndCount);
    
    public List<E> find(String query, final QueryParameter... queryParameters);
    
    public List<E> find(String query, List<QueryParameter> args, final int... rowStartIdxAndCount);    
    
    public List<E> findAll(final int... rowStartIdxAndCount);

    public List<E> findAll(boolean cached, final int... rowStartIdxAndCount);     
    
    public List<E> findByNamedQuery(String queryName);
    
    public List<E> findByNamedQuery(String queryName, QueryParameter... args);    
    
    public List<E> findByNamedQuery(String queryName, List<QueryParameter> args);
    
    public List<E> findByNativeQuery(String queryName);
    
    public List<E> findByNativeQuery(String queryName, QueryParameter... args);    
    
    public List<E> findByNativeQuery(String queryName, List<QueryParameter> args);
    
	public List<?> findAnyBySQL(String sql);

	public List<E> findEntitiesBySQL(String sql, final int... rowStartIdxAndCount);

	public List<E> findEntitiesBySQL(String sql, List<QueryParameter> parms, final int... rowStartIdxAndCount);

	public <T extends BaseEntity> List<T> findEntitiesBySQL(String sql, Class<T> entityClass, final int... rowStartIdxAndCount);    
}
