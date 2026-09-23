package com.huabo.system.utils;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**单匙密码体制:DES
 * @author wangfeng
 *
 */
public class DesUtil {
	private static final int default_length = 56;
	
	private static final String default_char = "utf-8";
	
	private static final boolean isIBM = System.getProperty("java.vendor").contains("IBM")?true:false;
	
	private static String CipherProviderName = "SunJCE";
	
	/**创建秘钥
	 * @param password 密码
	 * @return 返回秘钥
	 * @throws InvalidKeyException 
	 * @throws InvalidKeySpecException 
	 */
	public static Key generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        Key key = null;
        SecureRandom secureRandom = null;
        KeyGenerator keyGen = null;//创建指定密钥算法的密钥生成器
        if(isIBM){
        	//防止WAS下IBM JDK加解密出错
        	sun.security.provider.Sun sun = new sun.security.provider.Sun();
            Security.addProvider(sun);
        	secureRandom = SecureRandom.getInstance("SHA1PRNG",sun);
            try {
                com.sun.crypto.provider.SunJCE sunJCE = new com.sun.crypto.provider.SunJCE();
                Security.addProvider(sunJCE);
                keyGen = KeyGenerator.getInstance("DES",  sunJCE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
        	secureRandom = SecureRandom.getInstance("SHA1PRNG");
        	keyGen = KeyGenerator.getInstance("DES");
        }
        secureRandom.setSeed(password.getBytes(default_char));  //防止linux下 随机生成key             
        keyGen.init(default_length, secureRandom);//指定密钥的强度，初始化密钥对生成器
        key = keyGen.generateKey();
        return key;
	}

	
	/**加密字符串，默认采用UTF-8编码
	 * @param source 需要加密的内容
	 * @param password 密码
	 * @return String
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws InvalidKeySpecException 
	 */
	public static String encode(String source,String password) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException{
		String str = null;
		byte[] b = encode(source.getBytes(default_char),password);
		str = parseByte2Hex(b);
		return str;
	}
	
	/**根据秘钥做DES加密
	 * @param source 需要加密的内容
	 * @param password 密码
	 * @return byte[]
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws InvalidKeyException 
	 * @throws InvalidKeySpecException 
	 */
	public static byte[] encode(byte[] source,String password)throws NoSuchAlgorithmException,UnsupportedEncodingException, InvalidKeyException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		byte[] b = null;
		Key key = generateKey(password);
		b = encode(source,key);
		return b;
	}	

	/**DES加密
	 * @param key  秘钥
	 * @param source 需要加密的信息
	 * @return 加密后的信息
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] encode(byte[] source,Key key) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] target = null;
		Cipher cipher = null;
		if(isIBM){
			cipher = Cipher.getInstance("DES",CipherProviderName);
		}else{
			cipher = Cipher.getInstance("DES");
		}
		cipher.init(Cipher.ENCRYPT_MODE, key);
		target = cipher.doFinal(source);
		return target;
	}
	
	/**加密字符串，默认采用UTF-8编码
	 * @param source 需要加密的内容
	 * @param password 密码
	 * @return String
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws InvalidKeySpecException 
	 */
	public static String decode(String source,String password) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, NoSuchPaddingException, InvalidKeySpecException{
		String str = null;
		byte[] b = parseHex2Byte(source);
		b = decode(b,password);
		str = new String(b,default_char);
		return str;
	}
	
	/**DES解密信息
	 * @param password 密码
	 * @param source	需要解密的信息
	 * @return 解密后的信息
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidKeyException 
	 * @throws InvalidKeySpecException 
	 */
	public static byte[] decode(byte[] source,String password) throws NoSuchAlgorithmException,UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException{
		byte[] b = null;
		Key key = generateKey(password);
		b = decode(source,key);
		return b;
	}

	/**DES解密信息
	 * @param key  秘钥
	 * @param source	需要解密的信息
	 * @return 解密后的信息
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static byte[] decode(byte[] source,Key key) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
		byte[] dissect = null;
		Cipher cipher = null;
		if(isIBM){
			cipher = Cipher.getInstance("DES",CipherProviderName);
		}else{
			cipher = Cipher.getInstance("DES");
		}
		cipher.init(Cipher.DECRYPT_MODE, key);// 使用私钥解密
		dissect = cipher.doFinal(source);
		return dissect;
	}
	
	 /** 
     * 将二进制转换成16进制 
     * @method parseByte2HexStr 
     * @param buf 
     * @return 
     * @throws  
     * @since v1.0 
     */  
    public static String parseByte2Hex(byte buf[]){  
        StringBuffer sb = new StringBuffer();  
        for(int i = 0; i < buf.length; i++){  
            String hex = Integer.toHexString(buf[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            sb.append(hex.toUpperCase());  
        }  
        return sb.toString();  
    }  
      
    /** 
     * 将16进制转换为二进制 
     * @method parseHexStr2Byte 
     * @param hexStr 
     * @return 
     * @throws  
     * @since v1.0 
     */  
    public static byte[] parseHex2Byte(String hexStr){  
        if(hexStr.length() < 1)  
            return null;  
        byte[] result = new byte[hexStr.length()/2];  
        for (int i = 0;i< hexStr.length()/2; i++) {  
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
            result[i] = (byte) (high * 16 + low);  
        }  
        return result;  
    }
    
    public static void main(String args[])throws Exception{

    	String str= "测试加密";
    	str = encode(str,"123123");
    	System.out.println(str);
    	str = decode(str,"123123");
    	System.out.println(str);

    	String str1 = "CBCE9E21CC7653FC0EF207DE825EF268";
    	str1 = decode(str1,"123123");
    	System.out.println(str1);
    }
}
