package com.iex.tv.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.domain.BaseEntity;

@Transactional( propagation = Propagation.SUPPORTS )
public abstract class GenericDaoImpl<E extends BaseEntity, PK extends Serializable> extends BaseGenericDaoImpl implements GenericDao <E, PK>{
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
		return findByPk(entityClass, id);
    }
	
    @Override
    @Transactional( propagation = Propagation.MANDATORY )
    public void remove(PK pk) {
    	remove(entityClass, pk);
    }    

    @Override
    public List<E> find(String query, final int... rowStartIdxAndCount) {
    	return super.find(query, this.entityClass, rowStartIdxAndCount);
    }
    
    @Override
    public List<E> find(String query, final QueryParameter... queryParameters) {
    	return find(query, this.entityClass, queryParameters);
    }
        
    @Override
    public List<E> find(String query, final List<QueryParameter> queryParameters, final int... rowStartIdxAndCount) {
    	return  find(query, queryParameters, this.entityClass, rowStartIdxAndCount);
    }

    @Override
    public List<E> findAll(final int... rowStartIdxAndCount) {
    	return find(getFindAllSQL(), rowStartIdxAndCount);
    }
            
    @Override
    public List<E> findByNamedQuery(String queryName) {
		return findByNamedQuery(queryName, this.entityClass) ;
	}

    @Override
    public List<E> findByNamedQuery(String queryName, final int... rowStartIdxAndCount) {
		return findByNamedQuery(queryName, this.entityClass, rowStartIdxAndCount);
	}

    @Override
    public List<E> findByNamedQuery(String queryName, List<QueryParameter> args, final int... rowStartIdxAndCount) {
    	return findByNamedQuery(queryName, this.entityClass, args, rowStartIdxAndCount);
    }
    
    @Override
    public List<E> findByNamedQuery(String queryName, QueryParameter... args) {
    	return findByNamedQuery(queryName, this.entityClass, args);
    }

    @Override
    public List<E> findByNamedQuery(String queryName, List<QueryParameter> args) {
    	return findByNamedQuery(queryName, this.entityClass, args);
    }

    @Override
    public List<E> findByNativeQuery(String query) {
    	return findByNativeQuery(query, this.entityClass) ;
    }
    
    @Override
    public List<E> findByNativeQuery(String query, QueryParameter... parms) {
    	return findByNativeQuery(query, entityClass, parms);
    }
    
    @Override
    public List<E> findByNativeQuery(String query, List<QueryParameter> args) {
    	return findByNativeQuery(query, this.entityClass, args);
    }
    
	@Override
	public List<E> findByNativeQuery(String sql, final int... rowStartIdxAndCount) {
		return findByNativeQuery(sql, entityClass, rowStartIdxAndCount);
	}    

	@Override
	public List<E> findByNativeQuery(String sql, List<QueryParameter> parms, final int... rowStartIdxAndCount) {
		return findByNativeQuery(sql, parms, entityClass, rowStartIdxAndCount);
	}    
	
    private String findAllSQL;
    private String getFindAllSQL() {
    	if(findAllSQL == null) {
    		findAllSQL = getFindAllSQL(this.entityClass);
    	}
    	return findAllSQL;
    }
}
