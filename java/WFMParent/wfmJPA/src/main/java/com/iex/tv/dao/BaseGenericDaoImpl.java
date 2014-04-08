package com.iex.tv.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseGenericDaoImpl implements BaseGenericDao {
	protected abstract EntityManager getEntityManager();

    public BaseGenericDaoImpl() {

	}

	@Override
    public <T, Key extends Serializable> T findByPk(Class<T> entityClass, Key key) { 
    	return getEntityManager().find(entityClass, key); 
    }

	@Override
	public void flush() {
		getEntityManager().flush();
	}
	
	@Override
	public <T> void refresh(T entity) {
		getEntityManager().refresh(entity);
	}
	
	@Override
	public <T> void lock(T entity, LockModeType lockMode) {
		getEntityManager().lock(entity, lockMode);
	}
	
	@Override
	public void clear() {
		getEntityManager().clear();
	}

	@Override
	public void close() {
		getEntityManager().close();
	}
	
	@Override
	public <T> boolean contains(T entity) {
		return getEntityManager().contains(entity);
	}
	
	@Override
	public <T> void detach(T entity) {
		getEntityManager().detach(entity);
	}
	
    /**
     * Make an entity instance managed and persistent. 
     *
     * @param entity entity object
     * 
     * @throws EntityExistsException - if the entity already exists. (The EntityExistsException may be thrown when the persist operation is invoked, or the EntityExistsException or another PersistenceException may be thrown at flush or commit time.) 
	 * @throws IllegalStateException - if this EntityManager has been closed. 
	 * @throws IllegalArgumentException - if not an entity 
	 * @throws TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */	
	@Override
    @Transactional( propagation = Propagation.MANDATORY )	
    public <T> void persist(T entity) {
		getEntityManager().persist(entity);
    }
    
    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public <T> void remove(T entity) { 
    	if(entity == null) {
    		return ;
    	}
    	EntityManager em = getEntityManager();
		em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public <T, Key extends Serializable> void remove(Class<T> entityClass, Key pk) {
    	EntityManager em = getEntityManager();
    	T t = em.getReference(entityClass, pk);
    	if(t != null) {
    		em.remove(t);
    	}
    }    
    
    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public <T> T merge(T entity) {
        return getEntityManager().merge(entity);
    } 

    @Override
    @Transactional( propagation = Propagation.MANDATORY )    
    public int update(String query, final List<QueryParameter> args) {
    	Query q = getEntityManager().createQuery(query);
    	populateQueryNamedParameter(q, args);
    	int ret = q.executeUpdate();
    	return ret;
    }

    @Override
    @Transactional( propagation = Propagation.MANDATORY )    
    public int update(String query, QueryParameter... args) {
    	Query q = getEntityManager().createQuery(query);
    	populateQueryNamedParameter(q, args);
    	int ret = q.executeUpdate();
    	return ret;
    }

    @Override
    @Transactional( propagation = Propagation.MANDATORY )    
    public int updateByNativeQuery(String queryName, QueryParameter... queryParameter) {
    	Query q = getEntityManager().createNativeQuery(queryName);
    	populateQueryNamedParameter(q, queryParameter);
    	int ret = q.executeUpdate();
    	return ret;
    }
    
    @Override
    public <T> List<T> find(String query, Class<T> classEntity, final int... rowStartIdxAndCount) {
    	TypedQuery<T> q = getEntityManager().createQuery(query, classEntity);
    	List<T> result = findUsingRowIndex(q, rowStartIdxAndCount);
    	return result;
    }
    
    @Override
    public <T> List<T> find(String query, Class<T> classEntity, final QueryParameter... queryParameters) {
    	TypedQuery<T> q = getEntityManager().createQuery(query, classEntity);
    	populateQueryNamedParameter(q, queryParameters);
    	List<T> result = findUsingRowIndex(q);
    	return result;    	
    }
    
    @Override
    public <T> List<T> find(String query, final List<QueryParameter> queryParameters, Class<T> classEntity, final int... rowStartIdxAndCount) {
    	TypedQuery<T> q = getEntityManager().createQuery(query, classEntity);
    	populateQueryNamedParameter(q, queryParameters);
    	List<T> result = findUsingRowIndex(q, rowStartIdxAndCount);
    	return result;    	
    }

    @Override
    public <T> List<T> findAll(Class<T> classEntity) {
    	return findAll(classEntity, null);    	
    }

    @Override
    public <T> List<T> findAll(Class<T> classEntity, final int... rowStartIdxAndCount) {
    	return this.find(getFindAllSQL(classEntity), classEntity, rowStartIdxAndCount);
    }
    
    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity) {
		TypedQuery<T> q = getEntityManager().createNamedQuery(queryName, classEntity);
		List<T> results = q.getResultList();
		return results;    	
    }
    
    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, final int... rowStartIdxAndCount) {
		TypedQuery<T> q = getEntityManager().createNamedQuery(queryName, classEntity);
		List<T> results = findUsingRowIndex(q, rowStartIdxAndCount);
		return results;
    }
    
    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, List<QueryParameter> args, final int... rowStartIdxAndCount) {
    	TypedQuery<T> q = getEntityManager().createNamedQuery(queryName, classEntity);
    	populateQueryNamedParameter(q, args);
    	List<T> results = findUsingRowIndex(q, rowStartIdxAndCount);
    	return results;    	
    }

    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, final QueryParameter... args) {
    	TypedQuery<T> q = getEntityManager().createNamedQuery(queryName, classEntity);
    	populateQueryNamedParameter(q, args);
    	List<T> results =  q.getResultList();
    	return results;    	
    }

    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, List<QueryParameter> args) {
    	TypedQuery<T> q = getEntityManager().createNamedQuery(queryName, classEntity);
    	populateQueryNamedParameter(q, args);
    	List<T> results = q.getResultList();
    	return results;    	
    }

	//@Override
	public <T> List<T> findByNativeQuery(String query, Class<T> entityClass) {
		return findByNativeQuery(query, entityClass, (List<QueryParameter>)null);
	}   
	
	//@Override
	public <T> List<T> findByNativeQuery(String query, Class<T> entityClass, QueryParameter... parms) {
		Query q = getEntityManager().createNativeQuery(query, entityClass);
		populateQueryNamedParameter(q, parms);
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>)q.getResultList();
		return result;
	}

    @Override
    public <T> List<T> findByNativeQuery(String query, Class<T> entityClass, List<QueryParameter> args) {
    	return findByNativeQuery(query, args, entityClass, null);
    }
    
	@Override
	public <T> List<T> findByNativeQuery(String sql, Class<T> entityClass, final int... rowStartIdxAndCount) {
		return findByNativeQuery(sql, null, entityClass, rowStartIdxAndCount);
	}     
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findByNativeQuery(String sql, List<QueryParameter> parms, Class<T> entityClass, final int... rowStartIdxAndCount) {
		Query q = getEntityManager().createNativeQuery(sql, entityClass);
		populateQueryNamedParameter(q, parms);
		
		List<T> results = null;
		
		if(rowStartIdxAndCount == null || rowStartIdxAndCount.length == 0) {
			results = q.getResultList();
		} else {
			results = (List<T>) findUsingRowIndex(q, rowStartIdxAndCount);
		}
		return results;
	} 

    @Override
	public List<?> findAnyBySQL(String sql) {
    	Query q = getEntityManager().createNativeQuery(sql);
    	return q.getResultList();
	}

    @Override
    public Object findSingleResult(String query, final QueryParameter... queryParameters) {
    	Query q = getEntityManager().createQuery(query);
    	populateQueryNamedParameter(q, queryParameters);    	
    	return q.getSingleResult();
    }
    	
    @Override
    public Object findSingleResult(String query) {
    	Query q = getEntityManager().createQuery(query);   	
    	return q.getSingleResult();
    }
    
    @Override
    public Object findSingleResultByNamedQuery(String queryName) {
		Query q = getEntityManager().createNamedQuery(queryName);
		return q.getSingleResult();
	}
    
    @Override
    public Object findSingleResultByNamedQuery(String queryName, QueryParameter... args) {
    	Query q = getEntityManager().createNamedQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	return q.getSingleResult();
    }  
    
    
    protected void populateQueryNamedParameter(Query q, QueryParameter... queryParameters) {
    	if(queryParameters == null || queryParameters.length == 0) {
    		return;
    	}
    	
    	for (QueryParameter queryParameter : queryParameters) {
				if (queryParameter.isTemporalType()) {
					if (queryParameter.getValue() instanceof Date) {
						if(queryParameter.hasParameterName()) {
							q.setParameter(queryParameter.getParameterName(), (Date) queryParameter.getValue(), queryParameter.getType());
						} else {
							q.setParameter(queryParameter.getPostion(), (Date) queryParameter.getValue(), queryParameter.getType());
						}
					} else if (queryParameter.getValue() instanceof Calendar) {
						if(queryParameter.hasParameterName()) {
							q.setParameter(queryParameter.getParameterName(), (Calendar) queryParameter.getValue(), queryParameter.getType());
						} else {
							q.setParameter(queryParameter.getPostion(), (Calendar) queryParameter.getValue(), queryParameter.getType());
						}						
					} else {
						if(queryParameter.hasParameterName()) {
							q.setParameter(queryParameter.getParameterName(), queryParameter.getValue());
						} else {
							q.setParameter(queryParameter.getPostion(), queryParameter.getValue());
						}
					}				
				} else {
					if(queryParameter.hasParameterName()) {
						q.setParameter(queryParameter.getParameterName(), queryParameter.getValue());
					} else {
						q.setParameter(queryParameter.getPostion(), queryParameter.getValue());
					}
				}
		}    	
    }
    
    protected void populateQueryNamedParameter(Query q, List<QueryParameter> queryParameters) {
    	if(queryParameters == null || queryParameters.isEmpty()) {
    		return;
    	}
    	populateQueryNamedParameter(q, queryParameters.toArray(new QueryParameter[queryParameters.size()]));
    }
    
    @SuppressWarnings("unchecked")
    protected <T> List<T> findUsingRowIndex(TypedQuery<T> query, final int... rowStartIdxAndCount) {
    	return (List<T>)findUsingRowIndex((Query)query, rowStartIdxAndCount);
	}    
    
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
        
}
