package com.hbfk.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5util {
	
	public static String encrypByMd5(String plainText) {
		return stringToMD5(plainText);
	}
	
	public static String md5WithEncoding(String text, String encoding) throws Exception {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(text.getBytes(encoding));
	    byte[] digest = md.digest();
	    StringBuilder sb = new StringBuilder();
	    for (byte b : digest) {
	        sb.append(String.format("%02x", b & 0xff));
	    }
	    return sb.toString().substring(8, 24);
	}
	
	public static String stringToMD5(String plainText) {
        try {
            byte[] secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
            StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
            for (int i = 0; i < 32 - md5code.length(); i++) {
                md5code.insert(0, "0");
            }
            return md5code.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
