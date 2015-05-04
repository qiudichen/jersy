package com.iex.cloud.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
public abstract class BaseHibernateDaoImpl implements BaseDao {	
	protected abstract SessionFactory getSessionFactory();
	
	public boolean isJta() {
		return jta;
	}

	public void setJta(boolean jta) {
		this.jta = jta;
	}
	
	boolean jta = true;
	
    public BaseHibernateDaoImpl() {

	}
	
    protected Session getSession() {
    	if(this.isJta()) {
    		return getSessionFactory().getCurrentSession();
    	} else {
    		return getSessionFactory().openSession();
    	}
    }
    
	@Override
    public <T, Key extends Serializable> T findByPk(Class<T> entityClass, Key key) { 
    	return (T)getSession().get(entityClass, key); 
    }

	@Override
	public void flush() {
		getSession().flush();
	}
	
	@Override
	public <T> void refresh(T entity) {
		getSession().refresh(entity);
	}
	
	@Override
	public <T> void lock(T entity, LockOptions lockMode) {
		getSession().buildLockRequest(lockMode).lock(entity);
	}
	
	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public void close() {
		getSession().close();
	}
	
	@Override
	public <T> boolean contains(T entity) {
		return getSession().contains(entity);
	}
	
	@Override
	public <T> void detach(T entity) {
		getSession().evict(entity);
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
		getSession().persist(entity);
    }

    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public <T, Key extends Serializable> void remove(Class<T> entityClass, Key pk) {
    	Session em = getSession();
		T t = (T)em.load(entityClass, pk);
    	if(t != null) {
    		em.delete(t);
    	}
    }    
    
	@Override
    @Transactional( propagation = Propagation.MANDATORY )
    public <T> T merge(T entity) {
        return (T)getSession().merge(entity);
    } 

    @Override
    @Transactional( propagation = Propagation.MANDATORY )    
    public int update(String query, final List<QueryParameter> args) {
    	Query q = getSession().createQuery(query);
    	populateQueryNamedParameter(q, args);
    	int ret = q.executeUpdate();
    	return ret;
    }

    @Override
    @Transactional( propagation = Propagation.MANDATORY )    
    public int update(String query, QueryParameter... args) {
    	Query q = getSession().createQuery(query);
    	populateQueryNamedParameter(q, args);
    	int ret = q.executeUpdate();
    	return ret;
    }

    @Override
    @Transactional( propagation = Propagation.MANDATORY )    
    public int updateByNativeQuery(String queryName, QueryParameter... queryParameter) {
    	Query q = getSession().createSQLQuery(queryName);
    	populateQueryNamedParameter(q, queryParameter);
    	int ret = q.executeUpdate();
    	return ret;
    }
    
    @Override
    public <T> List<T> find(String query, Class<T> classEntity, final int... rowStartIdxAndCount) {
    	Query q = getSession().createQuery(query);
    	List<T> result = (List<T>)findUsingRowIndex(q, rowStartIdxAndCount);
    	return result;
    }
    
    @Override
    public <T> List<T> find(String query, Class<T> classEntity, final QueryParameter... queryParameters) {
    	Query q = getSession().createQuery(query);
    	populateQueryNamedParameter(q, queryParameters);
    	List<T> result = (List<T>)findUsingRowIndex(q);
    	return result;    	
    }
    
    @Override
    public <T> List<T> find(String query, final List<QueryParameter> queryParameters, Class<T> classEntity, final int... rowStartIdxAndCount) {
    	Query q = getSession().createQuery(query);
    	populateQueryNamedParameter(q, queryParameters);
    	List<T> result = (List<T>)findUsingRowIndex(q, rowStartIdxAndCount);
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
		Query q = getSession().getNamedQuery(queryName);
		List<T> results = (List<T>)q.list();
		return results;    	
    }
    
    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, final int... rowStartIdxAndCount) {
		Query q = getSession().getNamedQuery(queryName);
		List<T> results = (List<T>)findUsingRowIndex(q, rowStartIdxAndCount);
		return results;
    }
    
    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, List<QueryParameter> args, final int... rowStartIdxAndCount) {
    	Query q = getSession().getNamedQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	List<T> results = (List<T>)findUsingRowIndex(q, rowStartIdxAndCount);
    	return results;    	
    }

    @Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, final QueryParameter... args) {
    	Query q = getSession().getNamedQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	List<T> results =  (List<T>)q.list();
    	return results;    	
    }

	@Override
    public <T> List<T> findByNamedQuery(String queryName, Class<T> classEntity, List<QueryParameter> args) {
    	Query q = getSession().getNamedQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	List<T> results = (List<T>)q.list();
    	return results;    	
    }

	@Override
	public List<?> findByNativeQuery(String query) {
		return findByNativeQuery(query, (List<QueryParameter>)null);
	}   
	
	@Override
	public List<?> findByNativeQuery(String query, QueryParameter... parms) {
		Query q = getSession().createSQLQuery(query);
		populateQueryNamedParameter(q, parms);
		List<?> result = q.list();
		return result;
	}

    @Override
    public List<?> findByNativeQuery(String query, List<QueryParameter> args) {
    	return findByNativeQuery(query, args, null);
    }
    
	@Override
	public List<?> findByNativeQuery(String sql, final int... rowStartIdxAndCount) {
		return findByNativeQuery(sql, null, rowStartIdxAndCount);
	}     
	
	@Override
	public List<?> findByNativeQuery(String sql, List<QueryParameter> parms, final int... rowStartIdxAndCount) {
		Query q = getSession().createSQLQuery(sql);
		populateQueryNamedParameter(q, parms);
		
		List<?> results = null;
		
		if(rowStartIdxAndCount == null || rowStartIdxAndCount.length == 0) {
			results = q.list();
		} else {
			results = findUsingRowIndex(q, rowStartIdxAndCount);
		}
		return results;
	} 

    @Override
    public Object findSingleResult(String query, final QueryParameter... queryParameters) {
    	Query q = getSession().createQuery(query);
    	populateQueryNamedParameter(q, queryParameters);    	
    	return q.uniqueResult();
    }
    	
    @Override
    public Object findSingleResult(String query) {
    	Query q = getSession().createQuery(query);   	
    	return q.uniqueResult();
    }
    
    @Override
    public Object findSingleResultByNamedQuery(String queryName) {
		Query q = getSession().getNamedQuery(queryName);
		return q.uniqueResult();
	}
    
    @Override
    public Object findSingleResultByNamedQuery(String queryName, QueryParameter... args) {
    	Query q = getSession().getNamedQuery(queryName);
    	populateQueryNamedParameter(q, args);
    	return q.uniqueResult();
    }  
    
    
    protected void populateQueryNamedParameter(Query q, QueryParameter... queryParameters) {
    	if(queryParameters == null || queryParameters.length == 0) {
    		return;
    	}
    	
    	for (QueryParameter queryParameter : queryParameters) {
				if (queryParameter.isTemporalType()) {
					if (queryParameter.getValue() instanceof Date) {
						if(queryParameter.hasParameterName()) {
							switch(queryParameter.getType()) {
								case DATE:
									q.setDate(queryParameter.getParameterName(), (Date) queryParameter.getValue());
									break;
								case TIME:
									q.setTime(queryParameter.getParameterName(), (Date) queryParameter.getValue());
									break;								
								case TIMESTAMP:
									q.setTimestamp(queryParameter.getParameterName(), (Date) queryParameter.getValue());
									break;								
							}
						} else {
							switch(queryParameter.getType()) {
							case DATE:
								q.setDate(queryParameter.getPostion(), (Date) queryParameter.getValue());
								break;
							case TIME:
								q.setTime(queryParameter.getPostion(), (Date) queryParameter.getValue());
								break;								
							case TIMESTAMP:
								q.setTimestamp(queryParameter.getPostion(), (Date) queryParameter.getValue());
								break;								
							}							
						}
					} else if (queryParameter.getValue() instanceof Calendar) {
						Date value = ((Calendar) queryParameter.getValue()).getTime();
						
						if(queryParameter.hasParameterName()) {
							switch(queryParameter.getType()) {
								case DATE:
									q.setDate(queryParameter.getParameterName(), value);
									break;
								case TIME:
									q.setTime(queryParameter.getParameterName(), value);
									break;								
								case TIMESTAMP:
									q.setTimestamp(queryParameter.getParameterName(), value);
									break;								
							}
						} else {
							switch(queryParameter.getType()) {
								case DATE:
									q.setDate(queryParameter.getPostion(), value);
									break;
								case TIME:
									q.setTime(queryParameter.getPostion(), value);
									break;								
								case TIMESTAMP:
									q.setTimestamp(queryParameter.getPostion(), value);
									break;								
							}							
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
    	return query.list();
	}  
    
    protected String getFindAllSQL(Class<?> entityClass) {
    	return (new StringBuilder("SELECT o FROM ")).append(entityClass.getSimpleName()).append(" o").toString();
    }
            
}
