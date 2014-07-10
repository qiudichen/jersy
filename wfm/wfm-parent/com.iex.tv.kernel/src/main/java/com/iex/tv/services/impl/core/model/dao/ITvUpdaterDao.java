
/**
 * TODO Describe the purpose of this class/interface here
public interface ITvUpdaterDao
{

}
/**
 * @copyright 2007 IEX, A NICE Company
 * @author cgulledge
 * @version "$Id: ITvUpdaterDao.java 73399 2010-07-21 13:23:23Z jkidd $"
 */
package com.iex.tv.services.impl.core.model.dao;

import java.io.Serializable;
import java.util.Collection;

public interface ITvUpdaterDao<T, OID extends Serializable> extends ITvReaderDao<T, OID>
{
    /**
     * Updates the object. If the object does not exist, a TvDaoException will be thrown.
     * <p>
     * Use this method instead of persist() when you expect the object to exist.
     * <p>
     * 
     * @param obj The object to update where it's ID is non-null.
     * @return The updated object.
     * @throws TvDaoException if the obj's ID is null or it did not exist.
     */
    public T update(T obj) throws TvDaoException;
    
    public T merge(T objParm) throws TvDaoException;
    
    public T saveOrUpdate(T objParm) throws TvDaoException;
    
    public T save(T objParm) throws TvDaoException;
    /**
     * Updates all objects from the collection. If one of the objects does not exist, a TvDaoException will be thrown.
     * <p>
     * Use this method instead of persist() when you expect the objects to exist.
     * <p>
     * 
     * @param Collection of objects to update where there oids are non-null.
     * @return Collection of updated objects.
     * @throws TvDaoException if one of the obj's oids is null or it does not exist.
     */
    public Collection<T> updateAll(Collection<T> col) throws TvDaoException;

    /**
     * if using Hibernate, invokes a session flush
     */
    public void flush();
}
