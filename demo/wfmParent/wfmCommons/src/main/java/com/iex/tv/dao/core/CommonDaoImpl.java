package com.iex.tv.dao.core;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.domain.BaseEntity;

public abstract class CommonDaoImpl implements CommonDao {
	protected Logger logger = Logger.getLogger(this.getClass());
	protected abstract EntityManager getEntityManager();
	
	public CommonDaoImpl() {
		
	}
	
	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public <T extends BaseEntity> void persist(T entity, boolean flush) {
		getEntityManager().persist(entity);
		if(flush) {
			flush();
		}
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public <T extends BaseEntity> void persist(T entity) {
		this.persist(entity, false);
	}

	@Override
	public void flush() {
		getEntityManager().flush();
	}

	@Override
	public <T extends BaseEntity> boolean contains(T entity) {
		return this.getEntityManager().contains(entity);
	}

	@Override
	public <T extends BaseEntity> void detach(T entity) {
		getEntityManager().detach(entity);
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public <T extends BaseEntity> void remove(T entity) {
		if(entity != null) {
			EntityManager entityManager = getEntityManager();
			entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
		}		
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public <T extends BaseEntity, PrimaryKey extends Serializable> void remove(
			Class<T> entityClass, PrimaryKey pk) {
		T managedEntity = getEntityManager().getReference(entityClass, pk);
		getEntityManager().remove(managedEntity);
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public <T extends BaseEntity> T merge(T entity) {
		return getEntityManager().merge(entity);
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public int update(String queryString, List<QueryParameter> parameters) {
    	Query query = createQuery(queryString);
    	populateQueryNamedParameter(query, parameters);
    	int ret = query.executeUpdate();
    	return ret;		
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public int update(String queryString, QueryParameter... parameters) {
    	Query query = createQuery(queryString);
    	populateQueryNamedParameter(query, parameters);
    	int ret = query.executeUpdate();
    	return ret;	
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public int updateByNamedQuery(String namedQuery, List<QueryParameter> parameters) {
    	Query query = createNamedQuery(namedQuery);
    	populateQueryNamedParameter(query, parameters);
    	int ret = query.executeUpdate();
    	return ret;
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public int updateByNamedQuery(String namedQuery, QueryParameter... parameters) {
    	Query query = createNamedQuery(namedQuery);
    	populateQueryNamedParameter(query, parameters);
    	int ret = query.executeUpdate();
    	return ret;
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public int updateByNativeQuery(String sqlString, List<QueryParameter> parameters) {
    	Query query = getEntityManager().createNativeQuery(sqlString);
    	populateQueryNamedParameter(query, parameters);
    	int ret = query.executeUpdate();
    	return ret;
	}

	@Override
	@Transactional( propagation = Propagation.MANDATORY )
	public int updateByNativeQuery(String sqlString, QueryParameter... parameters) {
    	Query query = getEntityManager().createNativeQuery(sqlString);
    	populateQueryNamedParameter(query, parameters);
    	int ret = query.executeUpdate();
    	return ret;
	}

	@Override
	public <T extends BaseEntity, PrimaryKey extends Serializable> T getReference(Class<T> entityClass, PrimaryKey primaryKey) {
		return getEntityManager().getReference(entityClass, primaryKey);
	}

	@Override
	public <T extends BaseEntity, PrimaryKey extends Serializable> T findByPk(Class<T> entityClass, PrimaryKey key) {
		return getEntityManager().find(entityClass, key);
	}

	@Override
	public <T extends BaseEntity, PrimaryKey extends Serializable> T findByPk(Class<T> entityClass, PrimaryKey key, javax.persistence.CacheRetrieveMode retrieveModeMode) {
		if(retrieveModeMode == null) {
			return findByPk(entityClass, key);
		}
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(RETRIEVEMODE_KEY, retrieveModeMode);
		return getEntityManager().find(entityClass, key, properties);
	}

	
	@Override
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, int... rowStartIdxAndCount) {
		return this.find(entityClass, getFindAllSQL(entityClass), rowStartIdxAndCount);
	}

	@Override
	public <T extends BaseEntity> List<T> find(Class<T> entityClass, String queryString) {
    	return find(entityClass, queryString, (int[])null);
	}

	@Override
	public <T extends BaseEntity> List<T> find(Class<T> entityClass, String queryString, int... rowStartIdxAndCount) {
    	return find(entityClass, queryString, null, rowStartIdxAndCount) ;
	}

	@Override
	public <T extends BaseEntity> List<T> find(Class<T> entityClass, String queryString, QueryParameter... parameters) {
    	TypedQuery<T> query = createQuery(queryString, entityClass);
    	populateQueryNamedParameter(query, parameters);
    	List<T> result = findUsingRowIndex(query);
    	return result;   
	}

	@Override
	public <T extends BaseEntity> List<T> find(Class<T> entityClass, String queryString, List<QueryParameter> parameters, int... rowStartIdxAndCount) {
    	TypedQuery<T> query = createQuery(queryString, entityClass);
    	populateQueryNamedParameter(query, parameters);
    	List<T> result = findUsingRowIndex(query, rowStartIdxAndCount);
    	return result;   
	}

	@Override
	public <T extends BaseEntity> List<T> findByNamedQuery(Class<T> entityClass, String namedQuery) {
		return findByNamedQuery(entityClass, namedQuery, (int[]) null); 
	}

	@Override
	public <T extends BaseEntity> List<T> findByNamedQuery(Class<T> entityClass, String namedQuery, int... rowStartIdxAndCount) {
		return findByNamedQuery(entityClass, namedQuery, null, rowStartIdxAndCount) ; 
	}

	@Override
	public <T extends BaseEntity> List<T> findByNamedQuery(Class<T> entityClass, String namedQuery, QueryParameter... parameters) {
		TypedQuery<T> query = createNamedQuery(namedQuery, entityClass);
		populateQueryNamedParameter(query, parameters);
		List<T> results = query.getResultList();
		return results; 
	}

	@Override
	public <T extends BaseEntity> List<T> findByNamedQuery(Class<T> entityClass, String namedQuery,
			List<QueryParameter> parameters, int... rowStartIdxAndCount) {
		TypedQuery<T> query = createNamedQuery(namedQuery, entityClass);
		populateQueryNamedParameter(query, parameters);
		List<T> results = findUsingRowIndex(query, rowStartIdxAndCount);
		return results;
	}

	@Override
	public <T extends BaseEntity> List<T> findByNativeQuery(Class<T> entityClass, String sqlString) {
		return findByNativeQuery(entityClass, sqlString, (int[])null) ;
	}

	@Override
	public <T extends BaseEntity> List<T> findByNativeQuery(Class<T> entityClass, String sqlString, int... rowStartIdxAndCount) {
		return findByNativeQuery(entityClass, sqlString, null, rowStartIdxAndCount) ;
	}

	@Override
	public <T extends BaseEntity> List<T> findByNativeQuery(Class<T> entityClass, String sqlString, QueryParameter... parameters) {
		Query query = getEntityManager().createNativeQuery(sqlString, entityClass);
		populateQueryNamedParameter(query, parameters);
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>)query.getResultList();
		return result;
	}

	@Override
	public <T extends BaseEntity> List<T> findByNativeQuery(Class<T> entityClass, String sqlString,
			List<QueryParameter> parameters, int... rowStartIdxAndCount) {
		Query query = getEntityManager().createNativeQuery(sqlString, entityClass);
		populateQueryNamedParameter(query, parameters);
		@SuppressWarnings("unchecked")
		List<T> results = (List<T>) findUsingRowIndex(query, rowStartIdxAndCount);
		return results;
	}

	@Override
	public Object findSingleResult(String queryString, QueryParameter... parameters) {
    	Query query = createQuery(queryString);
    	populateQueryNamedParameter(query, parameters);    	
    	return query.getSingleResult();
	}

	@Override
	public Object findSingleResultByNamedQuery(String nameQuery, QueryParameter... parameters) {
    	Query query = createNamedQuery(nameQuery);
    	populateQueryNamedParameter(query, parameters);    	
    	return query.getSingleResult();
	}

	@Override
	public Object findSingleResultByNativeQuery(String sqlString, QueryParameter... parameters) {
    	Query query = getEntityManager().createNativeQuery(sqlString);
    	populateQueryNamedParameter(query, parameters);    	
    	return query.getSingleResult();
	}

	@Override
	public List<?> findAnyBySQL(String sql) {
    	Query q = getEntityManager().createNativeQuery(sql);
    	return q.getResultList();
	}

	protected CriteriaBuilder getCriteriaBuilder() {
		return this.getEntityManager().getCriteriaBuilder();
	}

	protected void populateQueryNamedParameter(Query query, List<QueryParameter> parameters) {
    	if(parameters == null || parameters.isEmpty()) {
    		return;
    	}
    	
    	for (QueryParameter queryParameter : parameters) {
    		queryParameter.populateQueryNamedParameter(query);
    	}
    }
    
    protected void populateQueryNamedParameter(Query query, QueryParameter... parameters) {
    	if(parameters == null || parameters.length == 0) {
    		return;
    	}
    	
    	for (QueryParameter queryParameter : parameters) {
    		queryParameter.populateQueryNamedParameter(query);
    	}
    }
    
    @SuppressWarnings("unchecked")
    protected <T extends BaseEntity> List<T> findUsingRowIndex(TypedQuery<T> query, final int... rowStartIdxAndCount) {
    	return (List<T>)findUsingRowIndex((Query)query, rowStartIdxAndCount);
	}    
    
    /**
     * Execute a SELECT query and return the query results
	 * as an untyped List with the position of the first result to retrieve
	 * and the maximum number of results to retrieve
     * @param query  SELECT query
     * @param rowStartIdxAndCount the position of the first(start with 0) and and the maximum number 
     * 			of result to retrieve. 
     */	
    protected List<?> findUsingRowIndex(Query query, final int... rowStartIdxAndCount) {
    	if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
    		int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
    		if (rowStartIdx > 0) {
    			query.setFirstResult(rowStartIdx);
    		}
    		
    		if (rowStartIdxAndCount.length > 1) {
    			int rowCount = Math.max(0, rowStartIdxAndCount[1]);
    			if (rowCount > 0) {
    				query.setMaxResults(rowCount);
    			}
    		}
    	}
    	return query.getResultList();
	}  
    
    protected String getFindAllSQL(Class<?> entityClass) {
    	return (new StringBuilder("SELECT o FROM ")).append(entityClass.getSimpleName()).append(" o").toString();
    }
        
	protected  <T extends Serializable> TypedQuery<T> createQuery(CriteriaQuery<T> cp) {
		TypedQuery<T> query = this.getEntityManager().createQuery(cp);
		return query;
	}
	
	protected <T extends BaseEntity> TypedQuery<T> createQuery(String queryString, Class<T> entityClass) {
		TypedQuery<T> query = getEntityManager().createQuery(queryString, entityClass);
		return query;
	}

	protected <T extends BaseEntity> TypedQuery<T> createQuery(String queryString, Class<T> entityClass, Map<String, Object> hints) {
		TypedQuery<T> query = getEntityManager().createQuery(queryString, entityClass);
		if(hints != null && !hints.isEmpty()) {
			for(Map.Entry<String, Object> hint : hints.entrySet()) {
				query.setHint(hint.getKey(), hint.getValue());
			}
		}
		return query;
    }
	
	protected Query createQuery(String queryString) {
		Query query = getEntityManager().createQuery(queryString);
		return query;
	}
	
	protected Query createQuery(String queryString, Map<String, Object> hints) {
		Query query = getEntityManager().createQuery(queryString);
		if(hints != null && !hints.isEmpty()) {
			for(Map.Entry<String, Object> hint : hints.entrySet()) {
				query.setHint(hint.getKey(), hint.getValue());
			}
		}
		return query;
	}
	
	protected <T extends BaseEntity> TypedQuery<T> createNamedQuery(String nameQuery, Class<T> entityClass) {
		TypedQuery<T> query = getEntityManager().createNamedQuery(nameQuery, entityClass);
		return query;
	}
	
	protected <T extends BaseEntity> TypedQuery<T> createNamedQuery(String nameQuery, Class<T> entityClass, Map<String, Object> hints) {
		TypedQuery<T> query = getEntityManager().createNamedQuery(nameQuery, entityClass);
		if(hints != null && !hints.isEmpty()) {
			for(Map.Entry<String, Object> hint : hints.entrySet()) {
				query.setHint(hint.getKey(), hint.getValue());
			}
		}
		return query;
	}
	
	protected Query createNamedQuery(String nameQuery) {
		Query query = getEntityManager().createNamedQuery(nameQuery);
		return query;
	}
	
	protected Query createNamedQuery(String nameQuery, Map<String, Object> hints) {
		Query query = getEntityManager().createNamedQuery(nameQuery);
		if(hints != null && !hints.isEmpty()) {
			for(Map.Entry<String, Object> hint : hints.entrySet()) {
				query.setHint(hint.getKey(), hint.getValue());
			}
		}
		return query;
	}
	
	@Override
	public void showStatistics() {
		final String nativeClass = "org.hibernate.jpa.internal.EntityManagerFactoryImpl"; 
				
		EntityManagerFactory factory = this.getEntityManager().getEntityManagerFactory();
		try {
			Class<?> factoryClass = Class.forName(nativeClass);
			if(factory.getClass().isAssignableFrom(factoryClass)) {
				Method method = factory.getClass().getMethod("getSessionFactory");
				Object session = method.invoke(factory);
				if(session.getClass().isAssignableFrom(Class.forName("org.hibernate.internal.SessionFactoryImpl"))) {
					Method method1 = session.getClass().getMethod("getStatistics");
					Object result  = method1.invoke(session);
					logger.info(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
