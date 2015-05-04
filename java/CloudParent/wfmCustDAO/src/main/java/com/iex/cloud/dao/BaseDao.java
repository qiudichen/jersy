package com.iex.cloud.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockOptions;

public interface BaseDao {
    public void flush();

    public <T, Key extends Serializable> T findByPk(Class<T> entityClass, Key key);
    
	public <T> void refresh(T entity);

	public <T> void lock(T entity, LockOptions lockMode);
	
	public void clear();

	public void close();
	
	public <T> boolean contains(T entity);
	
	public <T> void detach(T entity);

	public <T> void persist(T entity);

    public <T, Key extends Serializable> void remove(Class<T> entityClass, Key pk);

    public <T> T merge(T entity);

    public int update(String query, final List<QueryParameter> args);

    public int update(String query, QueryParameter... args);
    
    public int updateByNativeQuery(String queryName, QueryParameter... args);    
       
    public <T> List<T> find(String query, Class<T> classEntity, final int... rowStartIdxAndCount);
    
    public <T> List<T> find(String query, Class<T> classEntity, final QueryParameter... queryParameters);
        
    public <T> List<T> find(String query, final List<QueryParameter> queryParameters, Class<T> classEntity, final int... rowStartIdxAndCount);
    
    public <T> List<T> findAll(Class<T> classEntity);      

    public <T> List<T> findAll(Class<T> classEntity, final int... rowStartIdxAndCount);      
        
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity);

    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, final int... rowStartIdxAndCount);
        
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, final QueryParameter... args);
    
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, List<QueryParameter> args);

    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, List<QueryParameter> args, final int... rowStartIdxAndCount);
    
    public List<?> findByNativeQuery(String query);
    
    public List<?> findByNativeQuery(String query, QueryParameter... parms);
    
    public List<?> findByNativeQuery(String query, List<QueryParameter> args);
    
    public List<?> findByNativeQuery(String sql, final int... rowStartIdxAndCount);
    
    public List<?> findByNativeQuery(String sql, List<QueryParameter> parms, final int... rowStartIdxAndCount);
    
    public Object findSingleResult(String query, final QueryParameter... queryParameters);
    
    public Object findSingleResult(String query);
    
    public Object findSingleResultByNamedQuery(String queryName);
    
    public Object findSingleResultByNamedQuery(String queryName, QueryParameter... args);  
}
