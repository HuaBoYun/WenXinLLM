package com.huabo.file.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileDecryptor {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    
    private final byte[] key;
    private final byte[] iv;

    public FileDecryptor(String key, String iv) {
        // 确保密钥长度符合AES要求（16, 24, 32字节）
        this.key = ensureKeyLength(key);
        this.iv = iv.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
    
    /**
     * 确保密钥长度为有效的AES密钥长度
     */
    private byte[] ensureKeyLength(String key) {
        byte[] keyBytes = key.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int length = keyBytes.length;
        
        // AES密钥长度必须是16, 24, 或32字节
        if (length == 16 || length == 24 || length == 32) {
            return keyBytes;
        }
        
        // 如果长度不符合要求，进行填充或截断
        if (length < 16) {
            // 填充到16字节
            byte[] paddedKey = new byte[16];
            System.arraycopy(keyBytes, 0, paddedKey, 0, length);
            return paddedKey;
        } else if (length < 24) {
            // 填充到24字节
            byte[] paddedKey = new byte[24];
            System.arraycopy(keyBytes, 0, paddedKey, 0, length);
            return paddedKey;
        } else if (length < 32) {
            // 填充到32字节
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, length);
            return paddedKey;
        } else {
            // 截断到32字节
            byte[] truncatedKey = new byte[32];
            System.arraycopy(keyBytes, 0, truncatedKey, 0, 32);
            return truncatedKey;
        }
    }
    
    /**
     * 处理MultipartFile上传的加密文件
     */
    public InputStream decryptMultipartFile(MultipartFile encryptedFile) throws Exception {
        // 获取文件的Base64内容
        String base64Content = new String(encryptedFile.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
        return decryptBase64ToInputStream(base64Content);
    }
    
    /**
     * 解密Base64字符串为InputStream
     */
    public InputStream decryptBase64ToInputStream(String base64EncryptedData) throws Exception {
        try {
            // 解码Base64字符串
            byte[] encryptedData = Base64.getDecoder().decode(base64EncryptedData.trim());
            
            // 解密数据
            byte[] decryptedData = decrypt(encryptedData);
            
            return new ByteArrayInputStream(decryptedData);
            
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base64解码失败，请检查数据格式", e);
        } catch (Exception e) {
            throw new Exception("解密失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 核心解密方法
     */
    private byte[] decrypt(byte[] encryptedData) throws Exception {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            
            return cipher.doFinal(encryptedData);
            
        } catch (javax.crypto.IllegalBlockSizeException e) {
            throw new javax.crypto.IllegalBlockSizeException(
                "解密数据块大小不正确。加密数据长度: " + encryptedData.length + 
                "，期望是16的倍数。可能的原因：数据损坏、错误的密钥或加密模式不匹配。");
        } catch (javax.crypto.BadPaddingException e) {
            throw new javax.crypto.BadPaddingException(
                "解密填充错误。可能的原因：错误的密钥、IV或数据被篡改。");
        }
    }
    
    /**
     * 验证加密数据格式
     */
    public boolean isValidEncryptedData(String base64Data) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64Data.trim());
            return decoded.length % 16 == 0; // AES CBC模式要求数据长度是16的倍数
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取解密后的文件字节数组
     */
    public byte[] decryptToByteArray(String base64EncryptedData) throws Exception {
        try (InputStream is = decryptBase64ToInputStream(base64EncryptedData);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            
            return baos.toByteArray();
        }
    }



    //开始进行文件加密
    /**
     * 核心入口：处理MultipartFile原始文件，加密后返回InputStream（直接用于生成文件）
     * @param originalFile 原始待加密文件
     * @return 加密后的InputStream（Base64编码的AES加密流）
     * @throws Exception 加密异常
     */
    public InputStream encryptMultipartFile(MultipartFile originalFile) throws Exception {
        // 空文件校验
        if (originalFile == null || originalFile.isEmpty()) {
            throw new IllegalArgumentException("待加密文件不能为空");
        }
        // 获取原始文件字节数组
        byte[] originalData = originalFile.getBytes();
        // 加密并返回InputStream
        return encryptToInputStream(originalData);
    }
    /**
     * 加密原始字节数组，返回加密后的InputStream（核心转换方法）
     * @param originalData 原始待加密字节数组
     * @return 加密后的InputStream（Base64编码格式）
     * @throws Exception 加密异常
     */
    public InputStream encryptToInputStream(byte[] originalData) throws Exception {
        if (originalData == null || originalData.length == 0) {
            throw new IllegalArgumentException("待加密数据不能为空");
        }
        try {
            // 1. AES加密原始数据
            byte[] encryptedData = encrypt(originalData);
            // 2. Base64编码（与解密端的Base64解码对称）
            byte[] base64EncryptedData = Base64.getEncoder().encode(encryptedData);
            // 3. 封装为InputStream（UTF-8编码，与解密端一致）
            return new ByteArrayInputStream(base64EncryptedData);
        } catch (javax.crypto.IllegalBlockSizeException e) {
            throw new Exception("加密数据块异常：原始数据长度=" + originalData.length + "，请检查密钥/IV是否正确", e);
        } catch (javax.crypto.BadPaddingException e) {
            throw new Exception("加密填充错误：密钥/IV长度不符合要求（AES密钥16/24/32字节，IV固定16字节）", e);
        } catch (Exception e) {
            throw new Exception("加密失败：" + e.getMessage(), e);
        }
    }

    /**
     * 核心加密方法：AES CBC模式加密（与解密端完全对称）
     */
    private byte[] encrypt(byte[] originalData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // PKCS5Padding自动将数据补全为16字节倍数，无需手动处理
        return cipher.doFinal(originalData);
    }
}