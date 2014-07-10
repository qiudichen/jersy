/**
 * @copyright 2006 IEX, A NICE Company
 * @author tlark
 * @version "$Id:TvDaoHibernate.java 12498 2007-02-11 01:48:11Z gulledge $"
 */
package com.iex.tv.services.impl.core.model.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.iex.tv.core.framework.TvLogger;
import com.iex.tv.core.utils.GenericFactory;
import com.iex.tv.core.utils.Utils;
import com.iex.tv.domain.support.TvPersistable;
import com.iex.tv.services.impl.core.model.dao.ITvDao;
import com.iex.tv.services.impl.core.model.dao.TvDaoException;

public abstract class TvDaoHibernate<T, OID extends Serializable> extends TvUpdaterDaoHibernate<T, OID> implements
        ITvDao<T, OID>
{
    /**
     * @param persistentClassParm
     * @param loggerParm
     */
    public TvDaoHibernate(Class<T> persistentClassParm, TvLogger loggerParm)
    {
        super(persistentClassParm, loggerParm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#insert(T)
     */
    @Override
    public T insert(T objParm) throws TvDaoException
    {
        // getLogger().debug("Inserting ", objParm);

        try
        {
            getHibernateTemplate().save(objParm);
            getLogger().debug("Inserted ", objParm);
        }
        catch (Exception except)
        {
            getLogger().error(except, "insert, obj=", objParm);
            throw new TvDaoException("insert, obj=" + objParm, except);
        }

        return objParm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#persist(T)
     */
    @Override
    public T persist(T objParm) throws TvDaoException
    {
        // getLogger().debug("Persisting ", objParm);

        try
        {
            getHibernateTemplate().saveOrUpdate(objParm);
            getLogger().debug("Persisted ", objParm);
        }
        catch (Exception except)
        {
            getLogger().error(except, "persist, obj=", objParm);
            throw new TvDaoException("persist, obj=" + objParm, except);
        }

        return objParm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#delete(T)
     */
    @Override
    public void delete(T objParm) throws TvDaoException
    {
        // getLogger().debug("Deleting ", objParm);

        try
        {
            getHibernateTemplate().delete(objParm);
            getLogger().debug("Deleted ", objParm);
        }
        catch (Exception except)
        {
            getLogger().error(except, "delete, obj=", objParm);
            throw new TvDaoException("delete, obj=" + objParm, except);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iex.tv.services.impl.core.model.dao.ITvDao#delete(java.io.Serializable)
     */
    @Override
    public void deleteByOid(OID oidParm) throws TvDaoException
    {
        // getLogger().debug("Deleting ", oidParm);

        T objectToDelete = findByOid(oidParm);
        if (objectToDelete != null)
        {
            try
            {
                getHibernateTemplate().delete(objectToDelete);
                getLogger().debug("Deleted ", oidParm);
            }
            catch (Exception except)
            {
                getLogger().error(except, "delete, obj=", oidParm);
                throw new TvDaoException("delete, obj=" + oidParm, except);
            }
        }
        else
        {
            getLogger().debug("No object found with oid: ", oidParm);
            throw new TvDaoException("delete, oid=" + oidParm);
        }
    }

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvDao#insertAll(java.util.Collection)
     */
    @Override
    public Collection<T> insertAll(Collection<T> colParm) throws TvDaoException
    {
        List<T> inserted = new ArrayList<T>(colParm.size());
        for (T obj : colParm)
        {
            inserted.add(insert(obj));
        }
        return inserted;
    }

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvDao#persistAll(java.util.Collection)
     */
    @Override
    public Collection<T> persistAll(Collection<T> colParm) throws TvDaoException
    {
        List<T> persisted = new ArrayList<T>(colParm.size());
        for (T obj : colParm)
        {
            persisted.add(persist(obj));
        }
        return persisted;
    }    

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvDao#deleteAll(java.util.Collection)
     */
    @Override
    public void deleteAll(Collection<T> colParm) throws TvDaoException
    {
        for (T obj : colParm)
        {
            delete(obj);
        }
    }

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvDao#deleteAllByOid(java.util.Set)
     */
    @Override
    public void deleteAllByOid(Collection<OID> oidsParm) throws TvDaoException
    {
        Collection<T> objectsToDelete = findByOids(oidsParm);
        deleteAll(objectsToDelete);
    }

    /**
     * Save's the specified persistable based on it's internal ChangeAction (ADD, UPDATE, or DELETE)
     * 
     * @param persistable
     * @return The saved persistable or null if the persistable was deleted.
     * @throws TvDaoException If a parameter validation error occurs or a db-related exception is thrown
     */
    public <P extends TvPersistable> P saveTvPersistable(P persistable) throws TvDaoException
    {
        return HibernateUtils.saveTvPersistable(persistable, getHibernateTemplate(), getLogger());
    }
    
    /**
     * default for getThemInChunks()
     */
    private static final int MAX_ENTRIES_IN_COLLECTION_RESTRICTION = 500;
    
    /**
     * 
     * For use with getThemInChunks, return the first part of the DetachedCriteria for the query
     *
     * @author rcarvalho
     * @since Jul 30, 2013
     */
    public interface InitCriteria
    {
        public DetachedCriteria init();
    }
    
    /**
     * 
     * For use with getThemInChunks, adds the tail end of the criteria for the query
     *
     * @author rcarvalho
     * @since Jul 30, 2013
     */
    public interface AddCriteria
    {
        public void add(DetachedCriteria criteria);
    }

    /**
     * 
     * Break up a query with too many items in a list into a union of multiple queries.
     *
     * @param chunkSize if null, defaults to MAX_ENTRIES_IN_COLLECTION_RESTRICTION
     * @param initCriteria see InitCriteria
     * @param columnSelector says it all...
     * @param valueCollection collection of oids/ints/whatever to select
     * @param addCriteria see AddCriteria
     * @return
     * @throws TvDaoException
     */
    @SuppressWarnings("unchecked")
    public <T, T2> Collection<T> getThemInChunks(
        Integer chunkSize,
        InitCriteria initCriteria,
        String columnSelector, Collection<T2> valueCollection,
        AddCriteria addCriteria) throws TvDaoException
    {
        if (chunkSize == null)
        {
            chunkSize = MAX_ENTRIES_IN_COLLECTION_RESTRICTION;
        }
        getLogger().debug("*** getThemInChunks ", chunkSize, " ", valueCollection.size());
        Collection<T> results = GenericFactory.newArrayList();
        {
            final int sizeOfFullAgentOidsList = valueCollection.size();
            final List<T2> agentOidList = Utils.collectionToList(valueCollection);
            int fromIndex = 0;
            int toIndex = Math.min(fromIndex + chunkSize, sizeOfFullAgentOidsList);
            getLogger().debug("Number of oids is " + sizeOfFullAgentOidsList);
            while (fromIndex < toIndex)
            {
                DetachedCriteria criteria = initCriteria.init();
                getLogger().debug("Invoking query for index " + fromIndex + " to " + toIndex);
                Collection<T2> args = agentOidList.subList(fromIndex, toIndex);

                HibernateUtils.addCollectionRestriction(criteria, columnSelector, args);
                
                if (addCriteria != null) 
                {
                    addCriteria.add(criteria);
                }

                Collection<T> tmpResults;
                tmpResults = HibernateUtils.findByCriteria(criteria, getHibernateTemplate());
                if (!Utils.isEmpty(tmpResults))
                {
                    results.addAll(tmpResults);
                }
                // Update from and to index appropriately
                fromIndex = toIndex;
                toIndex = Math.min(fromIndex + chunkSize, sizeOfFullAgentOidsList);

            }
        }
        getLogger().debug("*** leaving getThemInChunks.");
        return results;
    }
    
    
}
