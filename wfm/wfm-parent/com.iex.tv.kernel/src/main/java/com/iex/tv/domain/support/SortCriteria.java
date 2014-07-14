/**
 * @copyright 2006 IEX, A Tekelec Company
 * @author tlark
 * @version "%I%, %G%"
 */
package com.iex.tv.domain.support;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO Describe the purpose of this class/interface here
 * 
 * @param <KS>
 * @author tlark
 * @since Apr 10, 2006
 */
@com.iex.Ident("$Id: SortCriteria.java 73399 2010-07-21 13:23:23Z jkidd $") //$NON-NLS-1$
public class SortCriteria<KS extends Enum & FieldCriteria.IKeySupplier> implements Serializable
{
    /**
     * TODO Describe the purpose of this method here
     *
     * @param <KS>
     * @param keySupplier
     * @return
     */
    public static final <KS extends Enum & FieldCriteria.IKeySupplier> SortCriteria<KS> ascend(KS keySupplier)
    {
        return new SortCriteria<KS>(keySupplier, true);
    }

    /**
     * TODO Describe the purpose of this method here
     *
     * @param <KS>
     * @param keySupplier
     * @return
     */
    public static final <KS extends Enum & FieldCriteria.IKeySupplier> SortCriteria<KS> descend(KS keySupplier)
    {
        return new SortCriteria<KS>(keySupplier, false);
    }

    /**
     * TODO Describe the purpose of this class/interface here
     * 
     * @author tlark
     * @since Apr 7, 2006
     */
    @com.iex.Ident("$Id: SortCriteria.java 73399 2010-07-21 13:23:23Z jkidd $") //$NON-NLS-1$
    public enum Order
    {
        NONE, ASCEND, DESCEND;

        /**
         * Returns either ASCEND or DESCEND, based on ascending's value.
         * 
         * @param ascending
         * @return either ASCEND or DESCEND, based on ascending's value.
         */
        public static final Order valueOf(boolean ascending)
        {
            return (ascending ? ASCEND : DESCEND);
        }

        public boolean isAscending()
        {
            return equals(ASCEND);
        }

        public boolean isOrdered()
        {
            return !equals(NONE);
        }
    }

    private KS keySupplier;
    private Order order;

    /**
     * Creates a sort option.
     * 
     * @param keySupplierParm Cannot be null
     * @throws IllegalArgumentException if keySupplierParm is null
     */
    public SortCriteria(KS keySupplierParm)
    {
        this(keySupplierParm, Order.NONE);
    }

    /**
     * Creates a sort option where the order is based on the ascending parm.
     * 
     * @param keySupplierParm Cannot be null
     * @param ascendingParm
     * @throws IllegalArgumentException if keySupplierParm is null
     */
    public SortCriteria(KS keySupplierParm, boolean ascendingParm)
    {
        this(keySupplierParm, (ascendingParm ? Order.ASCEND : Order.DESCEND));
    }

    /**
     * Creates a sort option with the given order. If the order is null, NONE will be used.
     * 
     * @param keySupplierParm Cannot be null
     * @param orderParm If null, NONE will be used.
     * @throws IllegalArgumentException if keySupplierParm is null
     */
    public SortCriteria(KS keySupplierParm, Order orderParm)
    {
        if (keySupplierParm != null)
        {
            keySupplier = keySupplierParm;
            setOrder(orderParm);
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
     * @return Returns the order (will not be null)
     */
    public Order getOrder()
    {
        return order;
    }

    /**
     * Sets the order. If null, NONE will be used.
     * 
     * @param orderParm If null, NONE will be used.
     */
    public void setOrder(Order orderParm)
    {
        order = (orderParm != null ? orderParm : Order.NONE);
    }

    public boolean isAscending()
    {
        return getOrder().isAscending();
    }

    public boolean isOrdered()
    {
        return getOrder().isOrdered();
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

        Boolean defaultEquals = Utils.defaultEquals(this, obj, SortCriteria.class);
        if (defaultEquals == null)
        {
            SortCriteria other = (SortCriteria) obj;

            // Compare order
            if (!Utils.equals(this.getOrder(), other.getOrder()))
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
        return (getKeySupplier() == null ? 0 : getKeySupplier().hashCode());
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
        attributes.put("order", getOrder());
        attributes.put("keySupplier", getKeySupplier());

        return Utils.toStringHelper(this, attributes).toString();
    }
}
