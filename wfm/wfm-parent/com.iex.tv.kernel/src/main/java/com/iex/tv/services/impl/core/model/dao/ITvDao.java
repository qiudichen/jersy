/**
 * @copyright 2006 IEX, A NICE Company
 * @author tlark
 * @version "$Id: ITvDao.java 73399 2010-07-21 13:23:23Z jkidd $"
 */
package com.iex.tv.services.impl.core.model.dao;

import java.io.Serializable;
import java.util.Collection;

/**
 * Based on the GenericDAO from the Caveat Emptor project.
 * <p>
 * All CRUD (create, read, update, delete) basic data access operations are isolated in this interface and shared across
 * all DAO implementations.
 * 
 * @author tlark
 * @since Aug 24, 2006
 */
public interface ITvDao<T, OID extends Serializable> extends ITvUpdaterDao<T, OID>
{
    /**
     * Inserts the object. If the object already exists, a TvDaoException will be thrown.
     * <p>
     * Use this method instead of persist() when you expect the object to be non-existant.
     * <p>
     * 
     * @param obj The object to insert where it's ID is null.
     * @return The inserted object (with the object's ID populated).
     * @throws TvDaoException if the obj's ID is non-null or it already existed.
     */
    public T insert(T obj) throws TvDaoException;

    /**
     * Persists the object.
     * <p>
     * Use this method when it doesn't matter how the object gets persisted (i.e. inserted or updated).
     * <p>
     * 
     * @param obj The object to persist.
     * @return The persisted object
     * @throws TvDaoException
     */
    public T persist(T obj) throws TvDaoException;

    /**
     * Deletes the object. If the object does not exist, a TvDaoException will be thrown.
     * 
     * @param obj The object to delete
     * @throws TvDaoException if the obj's ID is null or it did not exist.
     */
    public void delete(T obj) throws TvDaoException;

    /**
     * Deletes the object. If the object does not exist, a TvDaoException will be thrown.
     * 
     * @param oidParm oid of the object to delete
     * @throws TvDaoException
     */
    public void deleteByOid(OID oidParm) throws TvDaoException;
    
    /**
     * Inserts collection of objects. If one of the objects already exists, a TvDaoException will be thrown.
     * <p>
     * Use this method instead of persist() when you expect the objects to be non-existant.
     * <p>
     * 
     * @param Collection of objects to insert where their oids are null.
     * @return A collection of inserted objects (with the object's oids populated).
     * @throws TvDaoException if one of the obj's oid is non-null or it already existed.
     */
    public Collection<T> insertAll(Collection<T> col) throws TvDaoException;

    /**
     * Persists collection of objects.
     * <p>
     * Use this method when it doesn't matter how the object gets persisted (i.e. inserted or updated).
     * <p>
     * 
     * @param Collection of objects to be persisted.
     * @return Collection of objects after they have been persisted.
     * @throws TvDaoException
     */
    public Collection<T> persistAll(Collection<T> col) throws TvDaoException;

    /**
     * Deletes all objects from the collection from the database. If one of objects does not exist, a TvDaoException will be thrown.
     * 
     * @param Collection of objects to be deleted
     * @throws TvDaoException if the one of obj's oids is null or it did not exist.
     */
    public void deleteAll(Collection<T> col) throws TvDaoException;

    /**
     * Deletes object with oids from the given collection.
     * 
     * @param Collection of oids
     * @throws TvDaoException
     */
    public void deleteAllByOid(Collection<OID> oidsParm) throws TvDaoException;
}
