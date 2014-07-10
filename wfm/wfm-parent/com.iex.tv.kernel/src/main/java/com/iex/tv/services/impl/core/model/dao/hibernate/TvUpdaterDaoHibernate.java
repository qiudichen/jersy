/**
 * @copyright 2007 IEX, A NICE Company
 * @author cgulledge
 * @version "$Id: TvUpdaterDaoHibernate.java 73399 2010-07-21 13:23:23Z jkidd $"
 */
package com.iex.tv.services.impl.core.model.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.iex.tv.core.framework.TvLogger;
import com.iex.tv.services.impl.core.model.dao.ITvUpdaterDao;
import com.iex.tv.services.impl.core.model.dao.TvDaoException;

public abstract class TvUpdaterDaoHibernate<T, OID extends Serializable> extends TvReaderDaoHibernate<T, OID> implements
        ITvUpdaterDao<T, OID>
{
    /**
     * @param persistentClassParm
     * @param loggerParm
     */
    public TvUpdaterDaoHibernate(Class<T> persistentClassParm, TvLogger loggerParm)
    {
        super(persistentClassParm, loggerParm);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#update(T)
     */
    @Override
    public T update(T objParm) throws TvDaoException
    {
        try
        {
            getCurrentSession().update(objParm);
        }
        catch (Exception except)
        {
            getLogger().error(except, "update, obj=", objParm);
            throw new TvDaoException("update, obj=" + objParm, except);
        }

        return objParm;
    }
    
    @Override
    public T merge(T objParm) throws TvDaoException
    {
    	try
    	{
    		getCurrentSession().merge(objParm);
    	}
    	catch (Exception except)
    	{
    		getLogger().error(except, "update, obj=", objParm);
    		throw new TvDaoException("update, obj=" + objParm, except);
    	}
    	
    	return objParm;
    }
    
    @Override
    public T save(T objParm) throws TvDaoException
    {
    	try
    	{
    		getCurrentSession().save(objParm);
    	}
    	catch (Exception except)
    	{
    		getLogger().error(except, "update, obj=", objParm);
    		throw new TvDaoException("update, obj=" + objParm, except);
    	}
    	
    	return objParm;
    }

    @Override
    public T saveOrUpdate(T objParm) throws TvDaoException
    {
    	try
    	{
    		getCurrentSession().saveOrUpdate(objParm);
    	}
    	catch (Exception except)
    	{
    		getLogger().error(except, "update, obj=", objParm);
    		throw new TvDaoException("update, obj=" + objParm, except);
    	}
    	
    	return objParm;
    }
    

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvUpdaterDao#updateAll(java.util.Collection)
     */
    @Override
    public Collection<T> updateAll(Collection<T> colParm) throws TvDaoException
    {
        List<T> updated = new ArrayList<T>(colParm.size());
        for (T obj : colParm)
        {
            updated.add(update(obj));
        }
        return updated;
    }    

    /*
     * (non-Javadoc)
     *
     * @see com.iex.tv.services.impl.core.model.dao.ITvDao#flush()
     */
    @Override
    public void flush()
    {
        getCurrentSession().flush();
    }

}
