package com.iex.tv.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.domain.BaseEntity;

@Transactional( propagation = Propagation.SUPPORTS )
public abstract class GenericDaoImpl<E extends BaseEntity, PK extends Serializable> implements GenericDao <E, PK>{
	protected Class<E> entityClass;
	
	protected abstract EntityManager getEntityManager();
	
	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
	}
    
    public GenericDaoImpl(final Class<E> entityClass) {
		this.entityClass = entityClass;
	}

    /**
     * find entity by pk.
     *
     * @param pk PK object of the entity
     * 
     * @return the found entity instance or null if the entity does not exist.
     * 
     * @throws IllegalStateException - if this EntityManager has been closed.
     * @throws IllegalArgumentException - pk is not a valid type for that entity's primary key
     */  
	@Override
    public E findByPk(PK id) { 
    	return getEntityManager().find(entityClass, id); 
    }

	@Override
	public void flush() {
		getEntityManager().flush();
	}
	
	@Override
	public void refresh(E entity) {
		getEntityManager().refresh(entity);
	}
	
	public void lock(E entity, LockModeType lockMode) {
		getEntityManager().lock(entity, lockMode);
	}
	
	public void clear() {
		getEntityManager().clear();
	}

	public void close() {
		getEntityManager().close();
	}
	
	public boolean contains(E entity) {
		return getEntityManager().contains(entity);
	}
	
	public void detach(E entity) {
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
    public void persist(E entity) {
		getEntityManager().persist(entity);
    }
    
    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public void remove(E entity) { 
		getEntityManager().remove(entity); 
    }

    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public void remove(PK pk) {
    	getEntityManager().remove(getEntityManager().getReference(entityClass, pk));
    }    
    
    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public E merge(E entity) {
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
    public List<E> find(String query, final int... rowStartIdxAndCount) {
    	Query q = getEntityManager().createQuery(query);
    	
    	@SuppressWarnings("unchecked")
    	List<E> result = (List<E>) findUsingRowIndex(q, rowStartIdxAndCount);
    	return result;
    }

    @Override
    public List<E> find(String query, final QueryParameter... queryParameters) {
    	Query q = getEntityManager().createQuery(query);
    	populateQueryNamedParameter(q, queryParameters);
    	
    	@SuppressWarnings("unchecked")
    	List<E> result = (List<E>) findUsingRowIndex(q);
    	return result;
    }
    
    @Override
    public List<E> find(String query, final List<QueryParameter> queryParameters, final int... rowStartIdxAndCount) {
    	Query q = getEntityManager().createQuery(query);
    	populateQueryNamedParameter(q, queryParameters);
    	
    	@SuppressWarnings("unchecked")
    	List<E> result = (List<E>) findUsingRowIndex(q, rowStartIdxAndCount);
    	return result;
    }
    
    @Override
    public List<E> findAll(final int... rowStartIdxAndCount) {
    	Query q = getEntityManager().createQuery(getFindAllSQL());
    	
    	@SuppressWarnings("unchecked")
    	List<E> results = (List<E>) findUsingRowIndex(q, rowStartIdxAndCount);
    	return results;
    }
    
    @Override
    public List<E> findAll(boolean cached, final int... rowStartIdxAndCount) {
    	Query q = getEntityManager().createQuery(getFindAllSQL());
    	
    	//JPA 1.0
    	//q.setHint("org.hibernate.cacheable", cached);
    	
    	//JPA 2.0
    	if(cached) {
    		q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE);
    	}
    	@SuppressWarnings("unchecked")
    	List<E> results = (List<E>) findUsingRowIndex(q, rowStartIdxAndCount);
    	return results;
    }
    
    @Override
    public List<E> findByNamedQuery(String queryName) {
		Query q = getEntityManager().createNamedQuery(queryName);
		@SuppressWarnings("unchecked")
		List<E> results = q.getResultList();
		return results;
	}
    
    @Override
    public List<E> findByNamedQuery(String queryName, QueryParameter... args) {
    	Query q = getEntityManager().createNamedQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	@SuppressWarnings("unchecked")
    	List<E> results = q.getResultList();
    	return results;
    }
    
    @Override
    public List<E> findByNamedQuery(String queryName, List<QueryParameter> args) {
    	Query q = getEntityManager().createNamedQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	@SuppressWarnings("unchecked")
    	List<E> results = q.getResultList();
    	return results;
    }
    
    @Override
    public List<E> findByNativeQuery(String queryName) {
    	Query q = getEntityManager().createNativeQuery(queryName);
    	@SuppressWarnings("unchecked")
    	List<E> results = q.getResultList();
    	return results;
    }
    
    @Override
    public List<E> findByNativeQuery(String queryName, QueryParameter... args) {
    	Query q = getEntityManager().createNativeQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	@SuppressWarnings("unchecked")
    	List<E> results = q.getResultList();
    	return results;
    }
    
    @Override
    public List<E> findByNativeQuery(String queryName, List<QueryParameter> args) {
    	Query q = getEntityManager().createNativeQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	@SuppressWarnings("unchecked")
    	List<E> results = q.getResultList();
    	return results;
    }
    
    @Override
	public List<?> findAnyBySQL(String sql) {
    	Query q = getEntityManager().createNativeQuery(sql);
    	return q.getResultList();
	}

	@Override
	public List<E> findEntitiesBySQL(String sql, final int... rowStartIdxAndCount) {
		return findEntitiesBySQL(sql, null, entityClass, rowStartIdxAndCount);
	}    

	@Override
	public List<E> findEntitiesBySQL(String sql, List<QueryParameter> parms, final int... rowStartIdxAndCount) {
		return findEntitiesBySQL(sql, parms, entityClass, rowStartIdxAndCount);
	}    

	@Override
	public <T extends BaseEntity> List<T> findEntitiesBySQL(String sql, Class<T> entityClass, final int... rowStartIdxAndCount) {
		return findEntitiesBySQL(sql, null, entityClass, rowStartIdxAndCount);
	}    

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> findEntitiesBySQL(String sql, List<QueryParameter> parms, Class<T> entityClass, final int... rowStartIdxAndCount) {
		Query q = getEntityManager().createNativeQuery(sql, entityClass);
		populateQueryNamedParameter(q, parms);
		List<T> result = (List<T>) findUsingRowIndex(q, rowStartIdxAndCount);
		return result;
	} 

    private void populateQueryNamedParameter(Query q, QueryParameter... queryParameters) {
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
    
    private void populateQueryNamedParameter(Query q, List<QueryParameter> queryParameters) {
    	if(queryParameters == null || queryParameters.isEmpty()) {
    		return;
    	}
    	populateQueryNamedParameter(q, queryParameters.toArray(new QueryParameter[queryParameters.size()]));
    }
    
    private List<?> findUsingRowIndex(Query query, final int... rowStartIdxAndCount) {
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
    
    private String findAllSQL;
    private String getFindAllSQL() {
    	if(findAllSQL == null) {
    		findAllSQL = (new StringBuilder("select o from ")).append(this.entityClass.getSimpleName()).append(" o").toString();
    	}
    	return findAllSQL;
    }      
}
