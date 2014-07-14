package com.iex.tv.domain.support;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Utils {
    public static final boolean equals(Object obj1, Object obj2)
    {
        return ((obj1 == obj2) || ((obj1 != null) && obj1.equals(obj2)));
    }
    
    public static final Boolean defaultEquals(Object obj1, Object obj2, Class<?> base)
    {
        Boolean equals = null;

        if (obj1 == obj2)
        {
            equals = Boolean.TRUE;
        }
        else
        {
            if (obj1 != null && obj2 != null && obj1.getClass() == obj2.getClass())
            {
                equals = null;
            }
            else if ((obj1 == null) || (obj2 == null) || !base.isInstance(obj1) || !base.isInstance(obj2))
            {
                equals = Boolean.FALSE;
            }
        }

        return equals;
    }
    
    public static final StringBuilder buildDelimitedString(Collection<?> objects, String delimiter, StringBuilder buf)
    {
        if (buf == null)
        {
            buf = new StringBuilder();
        }

        if ((objects != null) && (delimiter != null))
        {
            for (Object obj : objects)
            {
                // Make sure we get "readable" array entries
                if ((obj != null) && obj.getClass().isArray())
                {
                    buildDelimitedString((Object[]) obj, ",", buf);
                }
                else
                {
                    buf.append(obj);
                }
                buf.append(delimiter);
            }

            // Remove trailing delimiter
            if (buf.length() > 0)
            {
                buf.setLength(buf.length() - delimiter.length());
            }
        }

        return buf;
    }    
    
    public static final StringBuilder buildDelimitedString(Object[] objects, String delimiter, StringBuilder buf)
    {
        if (buf == null)
        {
            buf = new StringBuilder();
        }

        if (!isEmpty(objects) && (delimiter != null))
        {
            for (Object obj : objects)
            {
                // Make sure we get "readable" array entries
                if ((obj != null) && obj.getClass().isArray())
                {
                    buf.append('[');
                    buildDelimitedString((Object[]) obj, ",", buf);
                    buf.append(']');
                }
                else
                {
                    buf.append(obj);
                }
                buf.append(delimiter);
            }

            // Remove trailing delimiter
            if (buf.length() > 0)
            {
                buf.setLength(buf.length() - delimiter.length());
            }
        }

        return buf;
    }
    
    public static final StringBuilder buildDelimitedString(Map<String, ?> map, String delimiter, StringBuilder buf)
    {
        if (buf == null)
        {
            buf = new StringBuilder();
        }

        if (!Utils.isEmpty(map) && (delimiter != null))
        {
            for (Map.Entry<String, ?> entry : map.entrySet())
            {
                buf.append(entry.getKey());

                Object value = entry.getValue();
                if (value != null)
                {
                    buf.append('=');

                    // Make sure we get "readable" array entries
                    if (value.getClass().isArray())
                    {
                        buildDelimitedString((Object[]) value, ",", buf);
                    }
                    else
                    {
                        buf.append(value);
                    }
                }

                buf.append(delimiter);
            }

            // Remove trailing delimiter
            if (buf.length() > 0)
            {
                buf.setLength(buf.length() - delimiter.length());
            }
        }

        return buf;
    }

    
    public static final boolean isEmpty(Object[] array)
    {
        return ((array == null) || (array.length == 0));
    }  
    
    public static final boolean isEmpty(Map<?, ?> map)
    {
        return ((map == null) || (map.isEmpty()));
    }
    
    public static final boolean isEmpty(Collection<?> collection)
    {
        return ((collection == null) || (collection.isEmpty()));
    }
    
    public static final StringBuilder toStringHelper(Object obj, Map<String, ?> attributes)
    {
        StringBuilder buf = new StringBuilder();

        if (obj != null)
        {
            // Discard the package portion of the class name.
            buf.append(obj.getClass().getName());
            buf.delete(0, buf.lastIndexOf(".") + 1);
        }

        if (!isEmpty(attributes))
        {
            buf.append('[');
            buildDelimitedString(attributes, ", ", buf);
            buf.append(']');
        }

        return buf;
    }
    
    public static final boolean isBlank(String string)
    {
        return StringUtils.isBlank(string);
    }    
}
