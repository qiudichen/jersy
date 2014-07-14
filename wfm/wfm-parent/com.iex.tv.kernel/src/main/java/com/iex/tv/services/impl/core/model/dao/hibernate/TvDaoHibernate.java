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
import com.iex.tv.services.impl.core.model.dao.ITvDao;
import com.iex.tv.services.impl.core.model.dao.TvDaoException;

public class TvDaoHibernate<T, OID extends Serializable> extends TvUpdaterDaoHibernate<T, OID> implements
        ITvDao<T, OID>
{
	protected transient static TvLogger logger = null;
	
	public TvDaoHibernate()
	{
		super();
	}

	/**
     * @param persistentClassParm
     * @param loggerParm
     */
    public TvDaoHibernate(Class<T> persistentClassParm)
    {
        super(persistentClassParm);
    }

	@Override
	protected TvLogger getTvLogger() {
        if (logger == null)
        {
        	synchronized(this) {
        		if (logger == null) {
        			logger = new TvLogger(getClass());
        		}
        	}
        }
        return logger;
	}
	
    /*
     * (non-Javadoc)
     * 
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#insert(T)
     */
    @Override
    public T insert(T objParm) throws TvDaoException
    {
        try
        {
        	OID oid = (OID)this.getCurrentSession().save(objParm);
        	return objParm;
        }
        catch (Exception except)
        {
            getTvLogger().error(except, "insert, obj=", objParm);
            throw new TvDaoException("insert, obj=" + objParm, except);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#persist(T)
     */
    @Override
    public T persist(T objParm) throws TvDaoException
    {
        try
        {
        	getCurrentSession().saveOrUpdate(objParm);
        }
        catch (Exception except)
        {
            getTvLogger().error(except, "persist, obj=", objParm);
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
        try
        {
        	getCurrentSession().delete(objParm);
        }
        catch (Exception except)
        {
            getTvLogger().error(except, "delete, obj=", objParm);
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
        T objectToDelete = findByOid(oidParm);
        if (objectToDelete != null)
        {
            try
            {
            	getCurrentSession().delete(objectToDelete);
            }
            catch (Exception except)
            {
                getTvLogger().error(except, "delete, obj=", oidParm);
                throw new TvDaoException("delete, obj=" + oidParm, except);
            }
        }
        else
        {
            getTvLogger().debug("No object found with oid: ", oidParm);
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
        getTvLogger().debug("*** getThemInChunks ", chunkSize, " ", valueCollection.size());
        Collection<T> results = new ArrayList<T>();
        {
            final int sizeOfFullAgentOidsList = valueCollection.size();
            final List<T2> agentOidList = collectionToList(valueCollection);
            int fromIndex = 0;
            int toIndex = Math.min(fromIndex + chunkSize, sizeOfFullAgentOidsList);
            getTvLogger().debug("Number of oids is " + sizeOfFullAgentOidsList);
            while (fromIndex < toIndex)
            {
                DetachedCriteria criteria = initCriteria.init();
                getTvLogger().debug("Invoking query for index " + fromIndex + " to " + toIndex);
                Collection<T2> args = agentOidList.subList(fromIndex, toIndex);

                addCollectionRestriction(criteria, columnSelector, args);
                
                if (addCriteria != null) 
                {
                    addCriteria.add(criteria);
                }

                List<T> tmpResults = (List<T>)this.findByCriteria(criteria, null);
                
                if (tmpResults != null && !tmpResults.isEmpty())
                {
                    results.addAll(tmpResults);
                }
                // Update from and to index appropriately
                fromIndex = toIndex;
                toIndex = Math.min(fromIndex + chunkSize, sizeOfFullAgentOidsList);

            }
        }
        getTvLogger().debug("*** leaving getThemInChunks.");
        return results;
    }
}
