/**
 * @copyright 2007 IEX, A NICE Company
 * @author kchennupati
 * @version "$Id: TvReaderDaoHibernate.java 80212 2011-05-23 21:54:55Z ikaliko $"
 */
package com.iex.tv.services.impl.core.model.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;

import com.iex.tv.core.framework.TvLogger;
import com.iex.tv.core.utils.Utils;
import com.iex.tv.domain.support.FieldCriteria;
import com.iex.tv.domain.support.SortCriteria;
import com.iex.tv.services.impl.core.model.dao.ITvReaderDao;
import com.iex.tv.services.impl.core.model.dao.TvBaseDao;
import com.iex.tv.services.impl.core.model.dao.TvDaoException;
import com.iex.tv.services.impl.core.model.utils.NameValue;

public abstract class TvReaderDaoHibernate<T, OID extends Serializable> extends TvBaseDao implements
        ITvReaderDao<T, OID>
{
    private Class<T> persistentClass;

    private transient TvLogger logger;

    /**
     *
     */
    public TvReaderDaoHibernate()
    {
    	try {
    		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    		this.persistentClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];   
    	} catch(Exception e) {
    		getLogger().warn("Unable to determine Entity Class");
    	}
    }

    /**
     * @param persistentClassParm
     * @param loggerParm
     */
    public TvReaderDaoHibernate(Class<T> persistentClassParm)
    {
    	this(persistentClassParm, null);
    }
    
    /**
     * @param persistentClassParm
     * @param loggerParm
     */
    public TvReaderDaoHibernate(Class<T> persistentClassParm, TvLogger loggerParm)
    {
        persistentClass = persistentClassParm;
        logger = loggerParm;
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#findById(OID)
     */
    @Override
    public T findByOid(OID oidParm) throws TvDaoException
    {
        if (oidParm == null)
        {
            getLogger().warn("Invalid parm(s): oid=", oidParm);
            return null;
        }
        try
        {
            @SuppressWarnings("unchecked")
            T result = (T) this.getCurrentSession().get(getPersistentClass(), oidParm);
            return result;
        }
        catch (Exception except)
        {
            getLogger().error(except, "exc TvReaderDaoHb:findByOid(", oidParm, ")");
            throw new TvDaoException("find, oid=" + oidParm, except);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvReaderDao#findByIds(java.util.Collection)
     */
    @Override
    public Collection<T> findByOids(Collection<OID> oidsParm) throws TvDaoException
    {

        if (Utils.isEmpty(oidsParm))
        {
        	getLogger().warn("Ignoring findByOids since no oids supplied, Entity Class[", this.getPersistentClass().getName(), "}");
        	return Collections.emptyList();
        }
         
        DetachedCriteria criteria = getMainCriteria();
        HibernateUtils.addCollectionRestriction(criteria, getIdProperty(), oidsParm);
        
        Collection<T> results = findByCriteria(criteria, null);
        return results;
    }

    @Override
    public T refresh(T objParm) throws TvDaoException
    {
        try
        {
            this.getCurrentSession().refresh(objParm);
        }
        catch (Exception except)
        {
            getLogger().error(except, "refresh, obj=", objParm);
            throw new TvDaoException("refresh, obj=" + objParm, except);
        }
        return objParm;
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#findAll()
     */
    @Override
    public Collection<T> findAll() throws TvDaoException
    {
        return findByCriteria(getMainCriteria(), null);
    }

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvReaderDao#findByPropertyValues(com.iex.tv.services.impl.core.model.utils.NameValue[])
     */
    @Override
    public Collection<T> findByPropertyValues(NameValue... nameValuesParm) throws TvDaoException
    {
        DetachedCriteria criteria = getMainCriteria();
        HibernateUtils.addPropertyValueRestrictions(criteria, nameValuesParm);
        return findByCriteria(criteria, null);
    }

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvReaderDao#findOidsByPropertyValues(com.iex.tv.services.impl.core.model.utils.NameValue[])
     */
    @Override
    public Set<OID> findOidsByPropertyValues(NameValue... nameValuesParm) throws TvDaoException
    {
        DetachedCriteria criteria = getMainCriteria();
        HibernateUtils.addPropertyValueRestrictions(criteria, nameValuesParm);
        criteria.setProjection(Projections.projectionList().add(Projections.property("oid")));
        @SuppressWarnings("unchecked")
		List<OID> oids = (List<OID>)find(criteria, null);
        return new HashSet<OID>(oids);
    }

    /* (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvReaderDao#existWithPropertyValues(com.iex.tv.services.impl.core.model.utils.NameValue[])
     */
    @Override
    public boolean existWithPropertyValues(NameValue... nameValuesParm) throws TvDaoException
    {
        Set<OID> oids = findOidsByPropertyValues(nameValuesParm);
        return !oids.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#findByExample(T, java.lang.String...)
     */
    @Override
    public Collection<T> findByExample(T exampleInstanceParm, String... excludePropertiesParm) throws TvDaoException
    {
        List<T> results = null;

        if (exampleInstanceParm != null)
        {
            getLogger().debug("findByExample=", exampleInstanceParm, ", exclude=",
                    Arrays.toString(excludePropertiesParm));

            // If no field names to exclude, use HibernateTemplate's built-in method
            if (Utils.isEmpty(excludePropertiesParm))
            {
                @SuppressWarnings("unchecked")
                List<T> list = getHibernateTemplate().findByExample(exampleInstanceParm);
                results = list;
            }
            else
            // One or more field(s) to exclude
            {
                Example example = Example.create(exampleInstanceParm);
                for (String excludeFieldName : excludePropertiesParm)
                {
                    example.excludeProperty(excludeFieldName);
                }

                Collection<Criterion> criterions = new ArrayList<Criterion>(1);
                criterions.add(example);
                results = findByCriteria(getMainCriteria(), criterions);
            }
            getLogger().info("Found ", results.size(), " result(s), example=", exampleInstanceParm, ", exclude=",
                    Arrays.toString(excludePropertiesParm));
        }
        else
        // Invalid input parm(s)
        {
            getLogger().warn("TvReaderDaoHb:findByExample: Invalid exampleParm=", exampleInstanceParm);
        }

        return results;
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.services.api.core.model.dao.ITvDao#findUniqueByExample(java.lang.Object, java.lang.String[])
     */
    @Override
    public T findUniqueByExample(T exampleInstanceParm, String... excludePropertiesParm) throws TvDaoException
    {
        T result = null;

        Collection<T> results = findByExample(exampleInstanceParm, excludePropertiesParm);
        if (!Utils.isEmpty(results))
        {
            if (results.size() == 1)
            {
                result = results.iterator().next();
            }
            else
            {
                IncorrectResultSizeDataAccessException irsdae = new IncorrectResultSizeDataAccessException(1, results
                        .size());
                getLogger().warn(irsdae, " TvReaderDaoHb:findByUniqueExample ", exampleInstanceParm, ", exclude=",
                        Arrays.toString(excludePropertiesParm));
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.iex.tv.services.impl.core.model.dao.ITvReaderDao#addFieldOptions(org.hibernate.criterion.DetachedCriteria,
     * java.util.Map)
     */
    @Override
    public DetachedCriteria addFieldOptions(DetachedCriteria mainCriteria,
            Map<FieldCriteria.Key, FieldCriteria> fieldOptions) throws TvDaoException
    {
        // Create one if not supplied
        if (mainCriteria == null)
        {
            mainCriteria = getMainCriteria();
        }

        if (!Utils.isEmpty(fieldOptions))
        {
            try
            {
                // Each field criteria that refers to an association needs its own alias/criteria and disjunction filter
                Map<String, DetachedCriteria> associatedCriterias = new LinkedHashMap<String, DetachedCriteria>();
                Map<String, Disjunction> associatedFilters = new LinkedHashMap<String, Disjunction>();

                for (FieldCriteria fc : fieldOptions.values())
                {
                    DetachedCriteria targetCriteria = null;

                    // Is this field criteria for the main class or an associated class?
                    FieldCriteria.Key key = fc.getKey();
                    if (key.isAssociatedField())
                    {
                        DetachedCriteria associatedCriteria = associatedCriterias.get(key.getAssociation());
                        if (associatedCriteria == null)
                        {
                            associatedCriteria = mainCriteria.createCriteria(key.getAssociation());
                            associatedCriterias.put(key.getAssociation(), associatedCriteria);
                        }

                        targetCriteria = associatedCriteria;
                    }
                    else
                    // Not an associated field criteria
                    {
                        targetCriteria = mainCriteria;
                    }

                    // Ordered?
                    SortCriteria.Order order = fc.getOrder();
                    if (order.isOrdered())
                    {
                        if (order.isAscending())
                        {
                            targetCriteria.addOrder(Order.asc(key.getField()));
                        }
                        else
                        {
                            targetCriteria.addOrder(Order.desc(key.getField()));
                        }
                    }

                    // Filtered?
                    if (fc.isFiltered())
                    {
                        Disjunction filter = associatedFilters.get(key.getAssociation());
                        if (filter == null)
                        {
                            filter = Restrictions.disjunction();
                            associatedFilters.put(key.getAssociation(), filter);
                            targetCriteria.add(filter);
                        }

                        filter.add(Restrictions.ilike(key.getField(), fc.getFilter(), MatchMode.ANYWHERE));
                    }
                }
            }
            catch (Exception except)
            {
                getLogger().error(except, "exc TvReaderDaoHb:addFieldOptions Criteria=", mainCriteria, ", options=",
                        fieldOptions);
                throw new TvDaoException("Criteria=" + mainCriteria + ", options=" + fieldOptions, except);
            }
        }

        return mainCriteria;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.iex.tv.services.impl.core.model.dao.ITvReaderDao#findByCriteria(org.hibernate.criterion.DetachedCriteria,
     * java.util.Collection, int, int)
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<T> findByCriteria(DetachedCriteria mainCriteria, Collection<Criterion> criterions, int... rowStartIdxAndCount) throws TvDaoException
    {
        return (List<T>)find(mainCriteria, criterions, rowStartIdxAndCount);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.iex.tv.services.impl.core.model.dao.ITvReaderDao#findUniqueResult(org.hibernate.criterion.DetachedCriteria,
     * java.util.Collection)
     */
    @Override
    public T findUniqueResult(DetachedCriteria mainCriteria, Collection<Criterion> criterions) throws TvDaoException
    {
        try
        {
            @SuppressWarnings("unchecked")
            T unique = (T) DataAccessUtils.requiredUniqueResult(findByCriteria(mainCriteria, criterions));
            return unique;
        }

        catch (EmptyResultDataAccessException except)
        {
            // No error here, just that no object matches the specified criteria
        	return null;
        }
        catch (IncorrectResultSizeDataAccessException except)
        {
            // expecting a unique result, yet more than 1 result was found
            getLogger().error("findUniqueResult() found more than 1 result, criteria=", mainCriteria);
            throw new TvDaoException("findUniqueResult() found more than 1 result, criteria=" + mainCriteria, except);
        }
        catch (Exception except)
        {
            getLogger().error(except, "exc TvReaderDaoHb:findUniqueResult criteria=", mainCriteria);
            throw new TvDaoException("Unique result criteria=" + mainCriteria, except);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.iex.tv.services.impl.core.model.dao.ITvReaderDao#getPersistentClass()
     */
    @Override
    public final Class<T> getPersistentClass()
    {
        return persistentClass;
    }

    /**
     * @param persistentClassParm The persistentClass to set.
     */
    public final void setPersistentClass(Class<T> persistentClassParm)
    {
        persistentClass = persistentClassParm;
    }

    /**
     * Returns the base criteria.
     * 
     * @return the base criteria (will not be null)
     */
    protected DetachedCriteria getMainCriteria()
    {
        return DetachedCriteria.forClass(getPersistentClass());
    }

    /**
     * Returns the base criteria.
     * 
     * @return the base criteria (will not be null)
     */
    protected DetachedCriteria getMainCriteriaWithAlias(String aliasParm)
    {
        return DetachedCriteria.forClass(getPersistentClass(), aliasParm);
    }

    /**
     * Returns the logger
     * 
     * @return logger
     */
    protected final TvLogger getLogger()
    {
        if (logger == null)
        {
            logger = new TvLogger(getClass());
        }

        return logger;
    }

    /**
     * @param loggerParm The logger to set.
     */
    protected final void setLogger(TvLogger loggerParm)
    {
        logger = loggerParm;
    }
}
