package com.huabo.system.utils;

import java.security.MessageDigest;

public class MD5Encrypt {

    public MD5Encrypt() {
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
}
