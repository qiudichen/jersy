/**
 * @copyright 2006 IEX, A Tekelec Company
 * @author tlark
 * @version "$Id: FilterCriteria.java 73399 2010-07-21 13:23:23Z jkidd $"
 */
package com.iex.tv.domain.support;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * FilterCriteria encapsulates a string filter and one or more fields that should be checked. When multiple fields are
 * used, a match will occur if ANY of the fields matches the filter.
 * 
 * @author tlark
 * @since Jul 6, 2006
 */
@com.iex.Ident("$Id: FilterCriteria.java 73399 2010-07-21 13:23:23Z jkidd $")
public class FilterCriteria<KS extends Enum & FieldCriteria.IKeySupplier> implements Serializable
{
    /**
     * TODO Describe the purpose of this method here
     * 
     * @param <KS>
     * @param keySupplier
     * @param matchString
     * @return
     */
    public static final <KS extends Enum & FieldCriteria.IKeySupplier> FilterCriteria<KS> match(KS keySupplier,
            String matchString)
    {
        return new FilterCriteria<KS>(keySupplier, matchString);
    }

    private String matchString;
    private KS keySupplier;

    /**
     * TODO Describe the purpose of this ctor here
     * 
     * @param keySupplierParm must be non-null
     * @throws IllegalArgumentException if keySupplierParm is null.
     */
    public FilterCriteria(KS keySupplierParm)
    {
        this(keySupplierParm, null);
    }

    /**
     * TODO Describe the purpose of this ctor here
     * 
     * @param keySupplierParm must be non-null
     * @param matchStringParm
     * @throws IllegalArgumentException if keySupplierParm is null.
     */
    public FilterCriteria(KS keySupplierParm, String matchStringParm)
    {
        if (keySupplierParm != null)
        {
            keySupplier = keySupplierParm;
            setMatchString(matchStringParm);
        }
        else
        {
            throw new IllegalArgumentException("keySupplierParm cannot be null");
        }
    }

    /**
     * @return Returns the keySupplier
     */
    public KS getKeySupplier()
    {
        return keySupplier;
    }

    /**
     * @return Returns the matchString.
     */
    public String getMatchString()
    {
        return matchString;
    }

    /**
     * @param matchStringParm The matchString to set.
     */
    public void setMatchString(String matchStringParm)
    {
        matchString = matchStringParm;
    }

    /**
     * Returns true if filtering will occur.
     * 
     * @return true if filtering will occur.
     */
    public boolean isFiltered()
    {
        return ((matchString != null) && (matchString.length() > 0));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object objParm)
    {
        boolean equals = false;

        Boolean defaultEquals = Utils.defaultEquals(this, objParm, FilterCriteria.class);
        if (defaultEquals == null)
        {
            FilterCriteria other = (FilterCriteria) objParm;

            // Compare matchStrings
            if (!Utils.equals(this.getMatchString(), other.getMatchString()))
            {
                equals = false;
            }
            // Compare fieldKeys
            else if (!Utils.equals(this.getKeySupplier(), other.getKeySupplier()))
            {
                equals = false;
            }
            else
            {
                equals = true;
            }
        }

        return (defaultEquals != null ? defaultEquals.booleanValue() : equals);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return (getMatchString() != null ? getMatchString().hashCode() : 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        Map<String, Object> attributes = new LinkedHashMap<String, Object>(2);
        attributes.put("match", getMatchString());
        attributes.put("keySupplier", getKeySupplier());

        return Utils.toStringHelper(this, attributes).toString();
    }
}
