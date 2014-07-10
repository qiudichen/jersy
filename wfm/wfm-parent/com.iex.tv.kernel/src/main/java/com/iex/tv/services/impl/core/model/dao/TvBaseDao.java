/**
 * @copyright 2010 IEX, A NICE Company
 * @author cgulledge
 * @version "$Id: TvBaseDao.java 73399 2010-07-21 13:23:23Z jkidd $"
 */
package com.iex.tv.services.impl.core.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;

public abstract class TvBaseDao
{
    /** the DAO helper object that does the (possibly) customer-dependent selection of DAO-required objects */
    private ITvDaoHelper daoHelper;

    /**
     * @return HibernateTemplate to use
     */
    protected HibernateTemplate getHibernateTemplate() {
        return daoHelper.getHibernateTemplate();
    }
    
    /**
     * @return SessionFactory to use
     */
    protected SessionFactory getSessionFactory() {
        return daoHelper.getSessionFactory();
    }
    
    /**
     * @param allowCreate true if a new session should be created if necessary
     * @return Session to use
     */
    protected Session getSession(boolean allowCreate) {
        return daoHelper.getSession(allowCreate);
    }
    
    /**
     * @return Session to use
     */
    protected Session getSession() {
        return daoHelper.getSession();
    }
    
    /**
     * @return SqlMapClientTemplate to use
     */
    public SqlMapClientTemplate getSqlMapClientTemplate() {
        return daoHelper.getSqlMapClientTemplate();
    }
    
    /**
     * @return SqlMapClient to use
     */
    public SqlMapClient getSqlMapClient() {
        return daoHelper.getSqlMapClient();
    }

    /**
     * @return the DAO helper being used (should probably be private)
     */
    public ITvDaoHelper getDaoHelper()
    {
        return daoHelper;
    }

    /**
     * @param daoHelperParm the DAO helper to use
     */
    public void setDaoHelper(ITvDaoHelper daoHelperParm)
    {
        daoHelper = daoHelperParm;
    }
}

