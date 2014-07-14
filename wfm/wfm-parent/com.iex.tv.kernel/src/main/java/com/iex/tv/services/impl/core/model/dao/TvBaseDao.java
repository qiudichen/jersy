/**
 * @copyright 2010 IEX, A NICE Company
 * @author cgulledge
 * @version "$Id: TvBaseDao.java 73399 2010-07-21 13:23:23Z jkidd $"
 */
package com.iex.tv.services.impl.core.model.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.iex.tv.core.framework.TvLogger;

public abstract class TvBaseDao
{
    private SessionFactory sessionFactory;

    private SqlMapClientTemplate sqlMapClientTemplate;
    
    private String idProperty;
    
    private boolean cacheQueries;
	
    private int FetchSize;
	
    private String queryCacheRegion;
	
    private int maxResults;
	
    private int timeOutInSecond;
		
    protected abstract Class<?> getPersistentClass();

    protected abstract TvLogger getTvLogger();
    
    public TvBaseDao() {
    	
    }

    protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
	
	protected Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	protected Session openSession() {
		return this.sessionFactory.openSession();
	}
	
	protected SqlMapClient getSqlMapClient() {
		return this.sqlMapClientTemplate.getSqlMapClient();
	}
	
	protected List<?> findByExample(String entityName, Object exampleEntity, final int... rowStartIdxAndCount) {

		Session session = this.getCurrentSession();
		Criteria executableCriteria = (entityName != null ?
						session.createCriteria(entityName) : session.createCriteria(exampleEntity.getClass()));

		executableCriteria.add(Example.create(exampleEntity));
		return find(executableCriteria, rowStartIdxAndCount);	
	}
	
	protected List<?> find(final DetachedCriteria mainCriteria, Collection<Criterion> criterions, final int... rowStartIdxAndCount) {
        if (criterions != null && !criterions.isEmpty())
        {
            for (Criterion criterion : criterions)
            {
                mainCriteria.add(criterion);
            }
        }

        // filter out duplicates from results
        mainCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
		
		Session session = this.getCurrentSession();
		Criteria executableCriteria = mainCriteria.getExecutableCriteria(session);
		return find(executableCriteria, rowStartIdxAndCount);		
	}	
	
	protected List<?> find(Criteria criteria, final int... rowStartIdxAndCount) {
		prepareCriteria(criteria);
		int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
		if (rowStartIdx > 0) {
			criteria.setFirstResult(rowStartIdx);
		}
		
		if (rowStartIdxAndCount.length > 1) {
			int rowCount = Math.max(0, rowStartIdxAndCount[1]);
			if (rowCount > 0) {
				criteria.setMaxResults(rowCount);
			}
		}
		return criteria.list();
	}
	
	protected void prepareCriteria(Criteria criteria) {
		if (isCacheQueries()) {
			criteria.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				criteria.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			criteria.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			criteria.setMaxResults(getMaxResults());
		}
		
		if(getTimeOutInSecond() > 0) {
			criteria.setTimeout(getTimeOutInSecond());			
		}
	}

    protected String getIdProperty() { 
    	if(this.idProperty == null) {
    		idProperty= getSessionFactory().getClassMetadata(this.getPersistentClass()) 
    	                                              .getIdentifierPropertyName(); 
    	}
    	return idProperty; 
	}
    
	public boolean isCacheQueries() {
		return cacheQueries;
	}

	public void setCacheQueries(boolean cacheQueries) {
		this.cacheQueries = cacheQueries;
	}

	protected int getFetchSize() {
		return FetchSize;
	}

	public void setFetchSize(int fetchSize) {
		FetchSize = fetchSize;
	}

	protected String getQueryCacheRegion() {
		return queryCacheRegion;
	}

	public void setQueryCacheRegion(String queryCacheRegion) {
		this.queryCacheRegion = queryCacheRegion;
	}

	protected int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	protected int getTimeOutInSecond() {
		return timeOutInSecond;
	}

	protected void setTimeOutInSecond(int timeOutInSecond) {
		this.timeOutInSecond = timeOutInSecond;
	}	
}

