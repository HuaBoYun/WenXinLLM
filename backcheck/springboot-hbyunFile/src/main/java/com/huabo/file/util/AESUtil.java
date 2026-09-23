package com.huabo.file.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

public class AESUtil {

    // 密钥和算法
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static final String SECRET_KEY = "1234567890123456"; // 16字节密钥
    private static final String INIT_VECTOR = "a1b2c3d4e5f6g7h8"; // 16字节初始化向量


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密字节数组并返回 Base64 编码的字符串
     *
     * @param data 要加密的字节数组
     * @return Base64 编码的加密字符串
     * @throws Exception 加密过程中可能出现的异常
     */
    public static String encrypt(byte[] data, String secretKey) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encryptedBytes = cipher.doFinal(data);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密 Base64 编码的字符串并返回字节数组
     *
     * @param base64EncryptedData Base64 编码的加密字符串
     * @return 解密后的字节数组
     * @throws Exception 解密过程中可能出现的异常
     */
    public static byte[] decrypt(String base64EncryptedData, String secretKey) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(base64EncryptedData);
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, "BC");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,iv);
        return cipher.doFinal(encryptedBytes);
    }

}

