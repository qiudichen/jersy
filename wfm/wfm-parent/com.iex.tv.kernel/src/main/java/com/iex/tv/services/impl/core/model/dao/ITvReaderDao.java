/**
 * @copyright 2007 IEX, A NICE Company
 * @author kchennupati
 * @version "$Id: ITvReaderDao.java 80212 2011-05-23 21:54:55Z ikaliko $"
 */
package com.iex.tv.services.impl.core.model.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import com.iex.tv.domain.support.FieldCriteria;
//import com.iex.tv.domain.support.FieldCriteria;
import com.iex.tv.services.impl.core.model.utils.NameValue;

public interface ITvReaderDao<T, OID extends Serializable>
{
    /**
     * Retrieves a persistent object by id or null if not found.
     * 
     * @param oid
     * @return the persistent object matching the oid or null if not found.
     * @throws TvDaoException
     */
    public T findByOid(OID oid) throws TvDaoException;

    /**
     * Retrieves persistent objects by the specified ids. If none found, an empty collection is returned.
     * 
     * @param oids
     * @return Matching persistent objects or an empty collection if none found.
     * @throws TvDaoException
     */
    public Collection<T> findByOids(Collection<OID> oids) throws TvDaoException;

    /**
     * Refreshes (reloads from DB) a domain object.
     * 
     * @param objParm the domain object to refresh
     * @return the refreshed domain object
     * @throws TvDaoException
     */
    public T refresh(T objParm) throws TvDaoException;

    /**
     * Retrieves all persistent objects.
     * 
     * @return All persistent objects or an empty collection if none found.
     * @throws TvDaoException
     */
    public Collection<T> findAll() throws TvDaoException;
    
    /**
     * Retrieves persistent objects with matching property values. If no matching objects found, empty collection is returned.
     * 
     * @param Array of name-value pairs where name is the name of the property; value is the property value that has to be matched.
     * @return Matching persistent objects or an empty collection if none found.
     * @throws TvDaoException
     */
    public Collection<T> findByPropertyValues(NameValue... nameValuesParm) throws TvDaoException;
    
    /**
     * Retrieves oids of persistent objects with matching property values. If no matching objects found, empty set is returned.
     * 
     * @param Array of name-value pairs where name is the name of the property; value is the property value that has to be matched.
     * @return Oids of matching persistent objects or an empty set if none found.
     * @throws TvDaoException
     */
    public Set<OID> findOidsByPropertyValues(NameValue... nameValuesParm) throws TvDaoException;
    
    /**
     * Returns true if there exists at least one persistent object with matching property values.
     * 
     * @param Array of name-value pairs where name is the name of the property; value is the property value that has to be matched.
     * @return true if there exists at least one persistent object with matching property values.
     * @throws TvDaoException
     */
    public boolean existWithPropertyValues(NameValue... nameValuesParm) throws TvDaoException;

    /**
     * Retrieves all persistent objects that match the properties of the specified example object, excluding the listed
     * properties.
     * 
     * @param exampleInstance Example object where non-null properties are used to filter the results (i.e. WHERE
     *            clause)
     * @param excludeProperties List of 0 or more property names from the example class to exclude from the filtering.
     * @return Matching persistent objects or an empty collection if none found.
     * @throws TvDaoException
     */
    public Collection<T> findByExample(T exampleInstance, String... excludeProperties) throws TvDaoException;

    /**
     * Retrieves a persistent object that matches the properties of the specified example object, excluding the listed
     * properties.
     * 
     * @param exampleInstance Example object where non-null properties are used to filter the results (i.e. WHERE
     *            clause)
     * @param excludeProperty List of 0 or more property names from the example class to exclude from the filtering.
     * @return Matching persistent object or null if none or more than one found.
     * @throws TvDaoException
     */
    public T findUniqueByExample(T exampleInstance, String... excludeProperty) throws TvDaoException;

    /**
     * Add the specified field options to the base criteria.
     * 
     * @param mainCriteria if null, one will be created
     * @param fieldOptions
     * @return mainCriteria
     * @throws TvDaoException
     */
    public DetachedCriteria addFieldOptions(DetachedCriteria mainCriteria,
            Map<FieldCriteria.Key, FieldCriteria> fieldOptions) throws TvDaoException;

    /**
     * Returns results that match the specified criterions.
     * 
     * @param mainCriteria if null, one will be created
     * @param criterions
     * @param firstResult index of the first result object to be retrieved (numbered from 0)
     * @param maxResults maximum number of result objects to retrieve (or <=0 for no limit)
     * @return List of 0 or more results that match the specified criterions.
     */
    public List<T> findByCriteria(DetachedCriteria mainCriteria, Collection<Criterion> criterions, int...rowStartIdxAndCount) throws TvDaoException;

    /**
     * @param mainCriteria if null, one will be created
     * @param criterions
     * @return
     * @throws TvDaoException
     */
    public T findUniqueResult(DetachedCriteria mainCriteria, Collection<Criterion> criterions) throws TvDaoException;

    /**
     * @return Returns the persistentClass.
     */
    public Class<T> getPersistentClass();
}
