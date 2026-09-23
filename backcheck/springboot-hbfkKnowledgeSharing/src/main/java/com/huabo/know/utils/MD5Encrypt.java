package com.huabo.know.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encrypt {

    public MD5Encrypt() {
    }

    public static String encrypByMd5(String context) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(context.getBytes());
            byte[] encryContext = md.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < encryContext.length; ++offset) {
                int i = encryContext[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
            return context;
        }
    }
}
