/**
 * @copyright 2006 IEX, A Tekelec Company
 * @author tlark
 * @version "%I%, %G%"
 */
package com.iex.tv.domain.support;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * TODO Describe the purpose of this class/interface here
 * 
 * @author tlark
 * @since Apr 7, 2006
 */
@com.iex.Ident("$Id: FieldCriteria.java 73399 2010-07-21 13:23:23Z jkidd $")
public class FieldCriteria
{
    private Key key;
    private SortCriteria.Order order;
    private String filter;

    /**
     * Create a filter criteria based on the specified key.
     * 
     * @param criterias
     * @param key
     * @param filter
     * @return a filter criteria based on the specified key.
     */
    public static final void addFilter(Map<FieldCriteria.Key, FieldCriteria> criterias, FieldCriteria.Key key,
            String filter)
    {
        if ((criterias != null) && (key != null))
        {
            FieldCriteria match = criterias.get(key);
            if (match != null)
            {
                match.setFilter(filter);
            }
            else
            // New criteria key
            {
                criterias.put(key, new FieldCriteria(key, null, filter));
            }
        }
    }

    /**
     * Create a filter criteria based on the specified field.
     * 
     * @param criterias
     * @param field
     * @param filter
     * @return a filter criteria based on the specified field.
     */
    public static final void addFilter(Map<FieldCriteria.Key, FieldCriteria> criterias, String field, String filter)
    {
        addFilter(criterias, null, field, filter);
    }

    /**
     * Create a filter criteria based on the specified association-field pair.
     * 
     * @param criterias
     * @param association
     * @param field
     * @param filter
     * @return a filter criteria based on the specified association-field pair.
     */
    public static final void addFilter(Map<FieldCriteria.Key, FieldCriteria> criterias, String association,
            String field, String filter)
    {
        addFilter(criterias, new Key(association, field), filter);
    }

    /**
     * Create a sort criteria based on the specified key.
     * 
     * @param criterias
     * @param key
     * @param order
     * @return a sort criteria based on the specified key.
     */
    public static final void addSort(Map<FieldCriteria.Key, FieldCriteria> criterias, Key key, SortCriteria.Order order)
    {
        if ((criterias != null) && (key != null))
        {
            FieldCriteria match = criterias.get(key);
            if (match != null)
            {
                match.setOrder(order);
            }
            else
            // New criteria key
            {
                criterias.put(key, new FieldCriteria(key, order, null));
            }
        }
    }

    /**
     * Create a sort criteria based on the specified field.
     * 
     * @param criterias
     * @param field
     * @param order
     * @return a sort criteria based on the specified field.
     */
    public static final void addSort(Map<FieldCriteria.Key, FieldCriteria> criterias, String field,
            SortCriteria.Order order)
    {
        addSort(criterias, null, field, order);
    }

    /**
     * Create a sort criteria based on the specified association-field pair.
     * 
     * @param criterias
     * @param association
     * @param field
     * @param order
     * @return a sort criteria based on the specified association-field pair.
     */
    public static final void addSort(Map<FieldCriteria.Key, FieldCriteria> criterias, String association, String field,
            SortCriteria.Order order)
    {
        addSort(criterias, new Key(association, field), order);
    }

    /**
     * Build field criterias from the given sort options and filters.
     * 
     * @param sortCriterias
     * @param filterCriterias
     * @return a Set of 0 or more FieldCriteria from the given sort options and filters.
     */
    public static final <SKS extends Enum & IKeySupplier, FKS extends Enum & IKeySupplier> Map<Key, FieldCriteria> buildFieldCriteria(Set<SortCriteria<SKS>> sortCriterias,
            Set<FilterCriteria<FKS>> filterCriterias)
    {
        Map<Key, FieldCriteria> criterias = new LinkedHashMap<Key, FieldCriteria>();

        // Any sort options?
        if (!Utils.isEmpty(sortCriterias))
        {
            for (SortCriteria<SKS> sortCriteria : sortCriterias)
            {
                if (sortCriteria.isOrdered())
                {
                    for (Key key : sortCriteria.getKeySupplier().getFieldKeys())
                    {
                        addSort(criterias, key, sortCriteria.getOrder());
                    }
                }
            }
        }

        // Any filter options?
        if (!Utils.isEmpty(filterCriterias))
        {
            for (FilterCriteria<FKS> filterCriteria : filterCriterias)
            {
                if (filterCriteria.isFiltered())
                {
                    for (Key key : filterCriteria.getKeySupplier().getFieldKeys())
                    {
                        addFilter(criterias, key, filterCriteria.getMatchString());
                    }
                }
            }
        }

        return criterias;
    }

    /**
     * Create a default criteria (no ordering/filtering) for the specified field
     * 
     * @param field
     */
    public FieldCriteria(String field)
    {
        init(new Key(field), SortCriteria.Order.NONE, null);
    }

    /**
     * Create a default criteria (no ordering/filtering) for the specified association-field pair.
     * 
     * @param association
     * @param field
     */
    public FieldCriteria(String association, String field)
    {
        init(new Key(association, field), SortCriteria.Order.NONE, null);
    }

    /**
     * Create a default criteria (no ordering/filtering) for the specified key
     * 
     * @param key
     */
    public FieldCriteria(Key key)
    {
        init(key, SortCriteria.Order.NONE, null);
    }

    public FieldCriteria(Key key, SortCriteria.Order order, String filter)
    {
        init(key, order, filter);
    }

    private void init(Key keyParm, SortCriteria.Order orderParm, String filterParm)
    {
        key = keyParm;

        setOrder(orderParm);
        setFilter(filterParm);
    }

    public Key getKey()
    {
        return key;
    }

    /**
     * @return Returns the filter.
     */
    public String getFilter()
    {
        return filter;
    }

    /**
     * @param filter The filter to set.
     */
    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    /**
     * TODO Describe the purpose of this method here
     * 
     * @return
     */
    public boolean isFiltered()
    {
        return ((getFilter() != null) && (getFilter().length() > 0));
    }

    /**
     * @return Returns the sort order.
     */
    public SortCriteria.Order getOrder()
    {
        return order;
    }

    /**
     * @param order The sort order to set. If null, then NONE will be used.
     */
    public void setOrder(SortCriteria.Order order)
    {
        this.order = (order != null ? order : SortCriteria.Order.NONE);
    }

    public boolean isOrdered()
    {
        return !getOrder().equals(SortCriteria.Order.NONE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        boolean equals = false;

        Boolean defaultEquals = Utils.defaultEquals(this, obj, FieldCriteria.class);
        if (defaultEquals == null)
        {
            FieldCriteria other = (FieldCriteria) obj;

            // Compare keys
            if (!Utils.equals(this.getKey(), other.getKey()))
            {
                equals = false;
            }
            // Compare order
            else if (!Utils.equals(this.getOrder(), other.getOrder()))
            {
                equals = false;
            }
            // Compare filter
            else if (!Utils.equals(this.getFilter(), other.getFilter()))
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
        return (getKey() != null ? getKey().hashCode() : 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        Map<String, Object> attributes = new LinkedHashMap<String, Object>(3);
        attributes.put("key", getKey());
        attributes.put("order", getOrder());
        attributes.put("filter", getFilter());

        return Utils.toStringHelper(this, attributes).toString();
    }

    /**
     * TODO Describe the purpose of this class/interface here
     * 
     * @author tlark
     * @since Jul 7, 2006
     */
    @com.iex.Ident("$Id: FieldCriteria.java 73399 2010-07-21 13:23:23Z jkidd $") //$NON-NLS-1$
    public static interface IKeySupplier
    {
        /**
         * TODO Describe the purpose of this method here
         * 
         * @return
         */
        public Set<Key> getFieldKeys();
    }

    /**
     * TODO Describe the purpose of this class/interface here
     * 
     * @author tlark
     * @since Apr 7, 2006
     */
    @com.iex.Ident("$Id: FieldCriteria.java 73399 2010-07-21 13:23:23Z jkidd $") //$NON-NLS-1$
    public static class Key implements Serializable
    {
        private String association;
        private String field;

        /**
         * Create an unassociated field key.
         * 
         * @param fieldParm
         */
        public Key(String fieldParm)
        {
            this(null, fieldParm);
        }

        /**
         * Create an associated field key.
         * 
         * @param associationParm
         * @param fieldParm
         */
        public Key(String associationParm, String fieldParm)
        {
            association = associationParm;
            field = fieldParm;
        }

        public String getAssociation()
        {
            return association;
        }

        public String getField()
        {
            return field;
        }

        public boolean isAssociatedField()
        {
            return !Utils.isBlank(getAssociation());
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            boolean equals = false;

            Boolean defaultEquals = Utils.defaultEquals(this, obj, Key.class);
            if (defaultEquals == null)
            {
                Key other = (Key) obj;

                // Compare associations
                if (!Utils.equals(this.getAssociation(), other.getAssociation()))
                {
                    equals = false;
                }
                // Compare fields
                else if (!Utils.equals(this.getField(), other.getField()))
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
            int result = 17;

            result = 37 * result + (getAssociation() == null ? 0 : getAssociation().hashCode());
            result = 37 * result + (getField() == null ? 0 : getField().hashCode());

            return result;
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
            if (isAssociatedField())
            {
                attributes.put("association", getAssociation());
            }
            attributes.put("field", getField());

            return Utils.toStringHelper(null, attributes).toString();
        }
    }
}
