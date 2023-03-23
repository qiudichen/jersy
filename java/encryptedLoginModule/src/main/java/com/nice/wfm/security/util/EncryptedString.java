/**
 * @copyright 2006 IEX, A NICE Company
 * @author gulledge
 * @version "$Id: EncryptedString.java 171497 2019-06-24 23:02:19Z cgulledge $"
 */

package com.nice.wfm.security.util;

import java.io.Serializable;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptedString implements Serializable
{
	private static final Logger logger = LoggerFactory.getLogger(EncryptedString.class);
	
    /** Length of the salt prefix to use for encrypted strings. */
    public static final int SALT_LENGTH = 4;

    private static final Random SALT_RANDOMIZER = new Random(System.currentTimeMillis());

    // TV4-7258 (Convergys nodes/apache "clogged" issue)
    // Use singletons instead of creating every time.
    // Use distinct singletons based on whether we're encrypting or decrypting
    private static final Cipher DECRYPT_CIPHER = initCipher(null, Cipher.DECRYPT_MODE);
    private static final Cipher ENCRYPT_CIPHER = initCipher(null, Cipher.ENCRYPT_MODE);

    /**
     * The plain text string. <b>Note: this string must never be saved in the database, or written to a log, report or
     * screen!</b>
     */
    private transient String plaintext;

    /** The encrypted string. */
    private String cryptext;

    /**
     * Simple construction allowed only by this class (and maybe Hibernate). All others should use one of the create
     * methods.
     */
    private EncryptedString()
    {
    }

    /**
     * Create a new encryptedString given the plaintext.
     * 
     * @param plaintextParm Unencrypted string.
     * @return new EncryptedString
     */
    public static EncryptedString createFromPlaintext(String plaintextParm)
    {
        EncryptedString newEncryptedString = new EncryptedString();
        newEncryptedString.setPlaintext(plaintextParm);
        return newEncryptedString;
    }

    /**
     * Create a new encryptedString given the cryptext.
     * 
     * @param cryptextParm encrypted string.
     * @return new EncryptedString
     */
    public static EncryptedString createFromCryptext(String cryptextParm)
    {
        EncryptedString newEncryptedString = new EncryptedString();
        newEncryptedString.setCryptext(cryptextParm);
        return newEncryptedString;
    }

    /**
     * @return Returns the cryptext.
     */
    public String getCryptext()
    {
        if (cryptext == null)
        {
            cryptext = encryptString(plaintext);
        }

        return cryptext;
    }

    /** "salt" generation method, allows equal value plaintexts to have different cryptexts */
    private byte[] makeSalt()
    {
        byte[] salt = new byte[SALT_LENGTH];

        // TV4-7258 (Convergys nodes/apache "clogged" issue)
        // Use one simple Random instead of SecureRandom for creating salt.
        SALT_RANDOMIZER.nextBytes(salt);

        return salt;
    }

    /**
     * Encipher/Decipher an array of bytes using the TvSymmetricKey.
     * 
     * @param bytes array of bytes to encipher or decipher.
     * @param mode cipher mode (Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE)
     * @return array of enciphered/deciphered bytes
     */
    private byte[] cipher(byte[] bytes, int mode)
    {
        if ((DECRYPT_CIPHER == null) || (ENCRYPT_CIPHER == null))
        {
            logger.error("Invalid state; DECRYPT_CIPHER=", DECRYPT_CIPHER, ", ENCRYPT_CIPHER=", ENCRYPT_CIPHER);
            return new byte[0];
        }

        byte[] cipherBytes = null;

        Cipher cipher = (mode == Cipher.DECRYPT_MODE) ? DECRYPT_CIPHER : ENCRYPT_CIPHER;

        // TV4-7258 (Convergys nodes/apache "clogged" issue) Per Sun bug
        // (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6383195), Cipher is NOT thread safe,
        // so synchronize on it.
        synchronized (cipher)
        {
            try
            {
                // TV4-7258 (Convergys nodes/apache "clogged" issue)
                // Moved Cipher.init() call to static block since, according to Cipher.doFinal javadoc:
                // "Upon finishing, this method resets this cipher object to the state it was in when previously
                // initialized via a call to init. That is, the object is reset and available to encrypt or decrypt
                // (depending on the operation mode that was specified in the call to init) more data."
                cipherBytes = cipher.doFinal(bytes);
            }
            catch (Exception except)
            {
                logger.error(cryptext, "cipher failed; bytes.length=" + "(bytes != null ? bytes.length : 0)" + ", mode=" + mode);
                initCipher(cipher, mode);
            }
        }

        return cipherBytes == null ? new byte[0] : cipherBytes;
    }

    /**
     * Encrypt a string and return the resulting string encoded in Base64.
     * 
     * @param string
     * @return
     */
    private String encryptString(String string)
    {
        if (string == null)
        {
            return null;
        }

        byte salt[] = makeSalt();

        byte[] plaintextBytes = string.getBytes();
        byte[] cryptBytes = new byte[SALT_LENGTH + plaintextBytes.length];
        for (int i = 0; i < SALT_LENGTH; ++i)
        {
            cryptBytes[i] = salt[i];
        }
        for (int i = 0; i < plaintextBytes.length; ++i)
        {
            cryptBytes[SALT_LENGTH + i] = plaintextBytes[i];
        }

        byte[] cipherBytes = cipher(cryptBytes, Cipher.ENCRYPT_MODE);

        return new String(Base64.encodeBase64(cipherBytes));
    }

    /**
     * Decode a base 64 string to bytes and decrypt the result.
     * 
     * @param string
     * @return
     */
    private String decryptString(String string)
    {
        if (string == null)
        {
            return null;
        }

        byte[] cryptBytes = string.getBytes();
        byte[] cipherBytes = Base64.decodeBase64(cryptBytes);
        byte[] plainBytes = cipher(cipherBytes, Cipher.DECRYPT_MODE);

        if (plainBytes.length >= SALT_LENGTH)
        {
            return new String(plainBytes, SALT_LENGTH, plainBytes.length - SALT_LENGTH);
        }
        else
        {
            return "";
        }
    }

    /**
     * Set the cryptext (and plaintext from decrypted cryptext)
     * 
     * @param cryptextParm The cryptext to set.
     */
    private void setCryptext(String cryptextParm)
    {
        cryptext = cryptextParm;
    }

    /**
     * @return Returns the plaintext.
     */
    public String getPlaintext()
    {
        if (plaintext == null)
        {
            plaintext = decryptString(cryptext);
        }

        return plaintext;
    }

    /**
     * Set the plaintext (and cryptext from the encrypted plaintext)
     * 
     * @param plaintextParm The plaintext to set.
     */
    private void setPlaintext(String plaintextParm)
    {
        plaintext = plaintextParm;

        // Since plaintext does not get serialized, we must create cryptext now (non-lazily)
        cryptext = encryptString(plaintextParm);
    }

    @Override
    public String toString()
    {
    	return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Create/initialize a Cipher object
     * 
     * @param cipher
     * @param opmode
     * @return if cipher is not null, it will be returned; otherwise a new Cipher will be returned.
     */
    private static final Cipher initCipher(Cipher cipher, int opmode)
    {
        Key key = TvSymmetricKey.getInstance().getKey();
        String xform = key.getAlgorithm() + "/CBC/PKCS5Padding";

        // Setup randomizer
        SecureRandom randomizer;
        try
        {
            randomizer = SecureRandom.getInstance("SHA1PRNG", "SUN");
        }
        catch (Exception e)
        {
            logger.warn("[opmode=" + opmode + "]:Using system default for SecureRandom which may be much slower!", e);
            randomizer = new SecureRandom();
        }

        randomizer.setSeed(System.currentTimeMillis());

        // Setup cipher
        try
        {
            AlgorithmParameters parms = AlgorithmParameters.getInstance(key.getAlgorithm());
            parms.init(new IvParameterSpec("iex.prms".getBytes()));

            if (cipher == null)
            {
                cipher = Cipher.getInstance(xform);
            }

            // TV4-7258 (Convergys nodes/apache "clogged" issue)
            // When no SecureRandom object is passed in, the init method uses an instance created via new
            // SecureRandom().
            // On Solaris 10, this defaults to "Provider:SunPKCS11-Solaris version 1.6, Algorithm:PKCS11" which is
            // approx 5 times slower than "Provider:SUN version 1.6, Algorithm:SHA1PRNG"
            cipher.init(opmode, key, parms, randomizer);
        }
        catch (Exception e)
        {
            logger.error("[opmode=" + opmode + "]:Cannot setup Cipher; xform=" + xform, e);
        }

        return cipher;
    }
}
