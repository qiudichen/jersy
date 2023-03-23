package com.nice.wfm.security.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class MessageDigestCredentialHandler {
	private static int SALT_SIZE = 16;
	private static int SALT_HASH_SIZE = 24;
	private static int PRE_SIZE = 12;
	private static int PRE_HASH_SIZE = 16;
	
	public static String getSHA512Hash(String password) throws NoSuchAlgorithmException {
		byte[] salt = getSalt(SALT_SIZE);
		byte[] pre = getSalt(PRE_SIZE);
		return getSHA512Hash(password, salt, pre);
	}
	
	private static String getSHA512Hash(String password, byte[] salt, byte[] pre) {
        String generatedPassword = getSHA512Hash(password, salt);
        return new String(Base64.getEncoder().encode(pre))
                + new String(Base64.getEncoder().encode(salt))
                + generatedPassword;
    }
	
	private static String getSHA512Hash(String password, byte[] salt) {
		
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return generatedPassword;
	}

    private static byte[] getSalt(int size) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[size];
        random.nextBytes(salt);
        return salt;
    }
    
    public static boolean comareTo(String planText, String hash) {
    	if(hash == null || hash.length() < PRE_HASH_SIZE + SALT_HASH_SIZE + 128) {
    		return false;
    	}
    	
    	String saltHash = hash.substring(PRE_HASH_SIZE, PRE_HASH_SIZE + SALT_HASH_SIZE);
    	String hashPW = hash.substring(PRE_HASH_SIZE + SALT_HASH_SIZE);
    	
    	byte[] salt = Base64.getDecoder().decode(saltHash.getBytes());
    	
    	String encode = getSHA512Hash(planText, salt);
    	return hashPW.equals(encode);
    }
    
	public static void main(String args[]) throws NoSuchAlgorithmException
    {  
		// same salt should be passed
		String planText = "iex.prms";
		
        String hash = getSHA512Hash("iex.prms");
        
        boolean flag = comareTo(planText, hash);
        System.out.println(flag);
    }

}
