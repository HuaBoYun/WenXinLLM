package com.huabo.system.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {
	
/*	public static void main(String[] args)throws Exception{
		String s = "测试加密fdsfdsfdsfsdfsdafdfsdfsdf";
        System.out.println(SHA1(s));
        System.out.println(SHA256(s));
	}*/
	
	/**加密基础方法
	 * @param inStr 需要加密的字符串
	 * @param seed 种子码，可以没有
	 * @param algorithm  加密算法
	 * @return
	 */
	public static String SHA(String inStr,String seed,String algorithm) {
        MessageDigest md = null;
        String outStr = null;
        try {
            md = MessageDigest.getInstance(algorithm);     //选择SHA-1，也可以选择MD5,SHA-256
            if(seed!=null) {
            	md.update(seed.getBytes("utf-8"));
            }
            byte[] digest = md.digest(inStr.getBytes());       //返回的是byet[]，要转化为String存储比较方便
            outStr = byte2String(digest);
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return outStr;
	}
	
	public static String SHA256(String inStr) {
		return SHA(inStr,null,"SHA-256");
	}
	
	public static String SHA256(String inStr,String seed) {
		return SHA(inStr,seed,"SHA-256");
	}
	
    public static String SHA1(String inStr) {
    	return SHA(inStr,null,"SHA-1");
    }
    public static String SHA1(String inStr,String seed) {
    	return SHA(inStr,seed,"SHA-1");
    }
    
    
    private static String byte2String(byte[] digest) {
        String str = "";
        String tempStr = "";
        
        for (int i = 0; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            }
            else {
                str = str + tempStr;
            }
        }
        return str.toLowerCase();
    }
}

