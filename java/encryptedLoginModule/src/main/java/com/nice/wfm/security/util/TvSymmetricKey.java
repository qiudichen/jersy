/**
 * @copyright 2007 IEX, A NICE Company
 * @author cgulledge
 * @version "$Id: TvSymmetricKey.java 73396 2010-07-21 12:46:31Z jkidd $"
 */
package com.nice.wfm.security.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Key;

import org.apache.commons.codec.binary.Base64;

class TvSymmetricKey
{
    /** The singleton instance of this key pair */
    private static final TvSymmetricKey INSTANCE = new TvSymmetricKey();

    /** The java.security key pair object */
    private final Key key;

    /**
     * the secret key, serialized to a string. Warning: never change this value, or risk losing the ability to connect
     * to customer databases!
     */
    private final String keyString = new StringBuilder().append("rO0ABXNyAB9qYXZheC5jcnlwdG8uc3BlYy5TZWNyZXRLZXlTcG")
            .append("VjW0cLZuIwYU0CAAJMAAlhbGdvcml0aG10ABJMamF2YS9sYW5nL1N0cmluZztbAANrZXl0AAJbQnhwdAAGREVTZWRldXIA")
            .append("AltCrPMX+AYIVOACAAB4cAAAABhYGuZikdULq9xK1QGGBGRDihqbofd5uU8=").toString();

    /**
     * Construct the singleton TvSymmetricKey.
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private TvSymmetricKey()
    {
        Key newKey = null;
        try
        {
            newKey = (Key) deserializeFromString(keyString);
        }
        catch (Exception except)
        {
            // TODO Auto-generated catch block
            except.printStackTrace();
        }

        key = newKey;
    }

    /**
     * Get the key pair instance.
     * 
     * @return
     */
    public static TvSymmetricKey getInstance()
    {
        return INSTANCE;
    }

    /**
     * Get the key.
     * 
     * @return
     */
    public Key getKey()
    {
        return key;
    }
    
    public static Serializable deserializeFromString(String serialString) throws IOException, ClassNotFoundException
    {
        Serializable object = null;
        ByteArrayInputStream bis = null;
        try
        {
            bis = new ByteArrayInputStream(Base64.decodeBase64(serialString.getBytes()));
            ObjectInputStream ois = null;
            try
            {
                ois = new ObjectInputStream(bis);
                object = (Serializable) ois.readObject();
            }
            finally
            {
                if (ois != null)
                {
                    ois.close();
                }
            }
        }
        finally
        {
            if (bis != null)
            {
                bis.close();
            }
        }
        return object;
    }
}
