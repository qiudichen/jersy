package com.iex.tv.dao.core;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import com.iex.tv.domain.BaseEntity;

public interface CommonDao {
	
	public String RETRIEVEMODE_KEY = "javax.persistence.cache.retrieveMode";
	public String STOREMODE_KEY = "javax.persistence.cache.storeMode";
	
	/**
	 * Make an instance managed and persistent.
	 * 
	 * @param entity
	 *            entity instance
	 * @param flush
	 *            if flush method is called
	 * @throws EntityExistsException
	 *             if the entity already exists. (If the entity already exists,
	 *             the <code>EntityExistsException</code> may be thrown when the
	 *             persist operation is invoked, or the
	 *             <code>EntityExistsException</code> or another
	 *             <code>PersistenceException</code> may be thrown at flush or
	 *             commit time.)
	 * @throws IllegalArgumentException
	 *             if the instance is not an entity
	 * @throws TransactionRequiredException
	 *             if invoked on a container-managed entity manager of type
	 *             <code>PersistenceContextType.TRANSACTION</code> and there is
	 *             no transaction
	 */
	public <T extends BaseEntity> void persist(T entity, boolean flush);

	/**
	 * Make an instance managed and persistent. flush method won't be called
	 * 
	 * @param entity
	 *            entity instance
	 * @throws EntityExistsException
	 *             if the entity already exists. (If the entity already exists,
	 *             the <code>EntityExistsException</code> may be thrown when the
	 *             persist operation is invoked, or the
	 *             <code>EntityExistsException</code> or another
	 *             <code>PersistenceException</code> may be thrown at flush or
	 *             commit time.)
	 * @throws IllegalArgumentException
	 *             if the instance is not an entity
	 * @throws TransactionRequiredException
	 *             if invoked on a container-managed entity manager of type
	 *             <code>PersistenceContextType.TRANSACTION</code> and there is
	 *             no transaction
	 */
	public <T extends BaseEntity> void persist(T entity);

	/**
	 * Synchronize the persistence context to the underlying database.
	 * 
	 * @throws TransactionRequiredException
	 *             if there is no transaction
	 * @throws PersistenceException
	 *             if the flush fails
	 */
	public void flush();

	/**
	 * Check if the instance is a managed entity instance belonging to the
	 * current persistence context.
	 * 
	 * @param entity
	 *            entity instance
	 * @return boolean indicating if entity is in persistence context
	 * @throws IllegalArgumentException
	 *             if not an entity
	 */
	public <T extends BaseEntity> boolean contains(T entity);

	/**
	 * Remove the given entity from the persistence context, causing a managed
	 * entity to become detached. Unflushed changes made to the entity if any
	 * (including removal of the entity), will not be synchronized to the
	 * database. Entities which previously referenced the detached entity will
	 * continue to reference it.
	 * 
	 * @param entity
	 *            entity instance
	 * @throws IllegalArgumentException
	 *             if the instance is not an entity
	 * @since Java Persistence 2.0
	 */
	public <T extends BaseEntity> void detach(T entity);

	/**
	 * Remove the entity instance.
	 * 
	 * @param entity
	 *            entity instance
	 * @throws IllegalArgumentException
	 *             if the instance is not an entity or is a detached entity
	 * @throws TransactionRequiredException
	 *             if invoked on a container-managed entity manager of type
	 *             <code>PersistenceContextType.TRANSACTION</code> and there is
	 *             no transaction
	 */
	public <T extends BaseEntity> void remove(T entity);

	/**
	 * Remove the entity instance with Primary PrimaryKey.
	 * 
	 * @param entity
	 *            entity instance
	 * @throws IllegalArgumentException
	 *             if the instance is not an entity or is a detached entity
	 * @throws TransactionRequiredException
	 *             if invoked on a container-managed entity manager of type
	 *             <code>PersistenceContextType.TRANSACTION</code> and there is
	 *             no transaction
	 */
	public <T extends BaseEntity, PrimaryKey extends Serializable> void remove(
			Class<T> entityClass, PrimaryKey pk);

	/**
	 * Merge the state of the given entity into the current persistence context.
	 * 
	 * @param entity
	 *            entity instance
	 * @return the managed instance that the state was merged to
	 * @throws IllegalArgumentException
	 *             if instance is not an entity or is a removed entity
	 * @throws TransactionRequiredException
	 *             if invoked on a container-managed entity manager of type
	 *             <code>PersistenceContextType.TRANSACTION</code> and there is
	 *             no transaction
	 */
	public <T extends BaseEntity> T merge(T entity);

	public int update(String queryString, final List<QueryParameter> parameters);

	public int update(String queryString, QueryParameter... parameters);

	public int updateByNamedQuery(String namedQuery,
			final List<QueryParameter> parameters);

	public int updateByNamedQuery(String namedQuery,
			QueryParameter... parameters);

	public int updateByNativeQuery(String sqlString,
			final List<QueryParameter> parameters);

	public int updateByNativeQuery(String sqlString,
			QueryParameter... parameters);

	/**
	 * Get an instance, whose state may be lazily fetched. If the requested
	 * instance does not exist in the database, the
	 * <code>EntityNotFoundException</code> is thrown when the instance state is
	 * first accessed. (The persistence provider runtime is permitted to throw
	 * the <code>EntityNotFoundException</code> when <code>getReference</code>
	 * is called.) The application should not expect that the instance state
	 * will be available upon detachment, unless it was accessed by the
	 * application while the entity manager was open.
	 * 
	 * @param entityClass
	 *            entity class
	 * @param primaryKey
	 *            primary key
	 * @return the found entity instance
	 * @throws IllegalArgumentException
	 *             if the first argument does not denote an entity type or the
	 *             second argument is not a valid type for that entityÂ’s
	 *             primary key or is null
	 * @throws EntityNotFoundException
	 *             if the entity state cannot be accessed
	 */
	public <T extends BaseEntity, PrimaryKey extends Serializable> T getReference(
			Class<T> entityClass, PrimaryKey primaryKey);

	public <T extends BaseEntity, PrimaryKey extends Serializable> T findByPk(
			Class<T> entityClass, PrimaryKey key);

	/*
	 * set retrieveMode as BYPASS, em will ignore cache and retrieve from database directly
	 */
	public <T extends BaseEntity, PrimaryKey extends Serializable> T findByPk(
			Class<T> entityClass, PrimaryKey key, CacheRetrieveMode retrieveMode);
	
	public <T extends BaseEntity> List<T> findAll(Class<T> classEntity,
			final int... rowStartIdxAndCount);

	public <T extends BaseEntity> List<T> find(Class<T> classEntity,
			String queryString);

	public <T extends BaseEntity> List<T> find(Class<T> classEntity,
			String queryString, final int... rowStartIdxAndCount);

	public <T extends BaseEntity> List<T> find(Class<T> classEntity,
			String queryString, final QueryParameter... parameters);

	public <T extends BaseEntity> List<T> find(Class<T> classEntity,
			String queryString, final List<QueryParameter> parameters,
			int... rowStartIdxAndCount);

	public <T extends BaseEntity> List<T> findByNamedQuery(
			Class<T> classEntity, String namedQuery);

	public <T extends BaseEntity> List<T> findByNamedQuery(
			Class<T> classEntity, String namedQuery,
			final int... rowStartIdxAndCount);

	public <T extends BaseEntity> List<T> findByNamedQuery(
			Class<T> classEntity, String namedQuery,
			final QueryParameter... parameters);

	public <T extends BaseEntity> List<T> findByNamedQuery(
			Class<T> classEntity, String namedQuery,
			final List<QueryParameter> parameters, int... rowStartIdxAndCount);

	public <T extends BaseEntity> List<T> findByNativeQuery(
			Class<T> entityClass, String sqlString);

	public <T extends BaseEntity> List<T> findByNativeQuery(
			Class<T> entityClass, String sqlString,
			final int... rowStartIdxAndCount);

	public <T extends BaseEntity> List<T> findByNativeQuery(
			Class<T> entityClass, String sqlString,
			QueryParameter... parameters);

	public <T extends BaseEntity> List<T> findByNativeQuery(
			Class<T> entityClass, String sqlString,
			List<QueryParameter> parameters, final int... rowStartIdxAndCount);

	public Object findSingleResult(String queryString,
			final QueryParameter... parameters);

	public Object findSingleResultByNamedQuery(String nameQuery,
			QueryParameter... parameters);

	public Object findSingleResultByNativeQuery(String sqlString,
			QueryParameter... parameters);

	public List<?> findAnyBySQL(String sql);
	
	public void showStatistics();
}
