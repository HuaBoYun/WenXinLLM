package com.hbfk.util;



import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import sun.security.provider.Sun;

import com.sun.crypto.provider.SunJCE;

public class DesUtil {
//    private static final int default_length = 56;
//    private static final String default_char = "utf-8";
    private static final boolean isIBM = System.getProperty("java.vendor").contains("IBM");

    private static String CipherProviderName = "SunJCE";

    public static Key generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        Key key = null;
        SecureRandom secureRandom = null;
        KeyGenerator keyGen = null;
        if (isIBM) {
            Sun sun = new Sun();
            Security.addProvider(sun);
            secureRandom = SecureRandom.getInstance("SHA1PRNG", sun);
            try {
                SunJCE sunJCE = new SunJCE();
                Security.addProvider(sunJCE);
                keyGen = KeyGenerator.getInstance("DES", sunJCE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            keyGen = KeyGenerator.getInstance("DES");
        }
        secureRandom.setSeed(password.getBytes("utf-8"));
        keyGen.init(56, secureRandom);
        key = keyGen.generateKey();
        return key;
    }

    public static String encode(String source, String password) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        String str = null;
        byte[] b = encode(source.getBytes("utf-8"), password);
        str = parseByte2Hex(b);
        return str;
    }

    public static byte[] encode(byte[] source, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        byte[] b = (byte[]) null;
        Key key = generateKey(password);
        b = encode(source, key);
        return b;
    }

    public static byte[] encode(byte[] source, Key key) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] target = (byte[]) null;
        Cipher cipher = null;
        if (isIBM)
            cipher = Cipher.getInstance("DES", CipherProviderName);
        else {
            cipher = Cipher.getInstance("DES");
        }
        cipher.init(1, key);
        target = cipher.doFinal(source);
        return target;
    }

    public static String decode(String source, String password) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, NoSuchPaddingException, InvalidKeySpecException {
        String str = null;
        byte[] b = parseHex2Byte(source);
        b = decode(b, password);
        str = new String(b, "utf-8");
        return str;
    }

    public static byte[] decode(byte[] source, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        byte[] b = (byte[]) null;
        Key key = generateKey(password);
        b = decode(source, key);
        return b;
    }

    public static byte[] decode(byte[] source, Key key) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
        byte[] dissect = (byte[]) null;
        Cipher cipher = null;
        if (isIBM)
            cipher = Cipher.getInstance("DES", CipherProviderName);
        else {
            cipher = Cipher.getInstance("DES");
        }
        cipher.init(2, key);
        dissect = cipher.doFinal(source);
        return dissect;
    }

    public static String parseByte2Hex(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHex2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; ++i) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encode("seeyon1;"+System.currentTimeMillis(), "seeyon123456"));
//        String str = "zhangs;" + System.currentTimeMillis();
//        str = encode(str, "seeyon123456");
//        System.out.println(str);
//        str = decode(str, "seeyon123456");
//        System.out.println(str);
    }
}