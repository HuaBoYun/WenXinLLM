package com.hbfk.util;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileNameEncoderUtil {
	/**
     * 根据浏览器编码文件名
     */
    public static String encodeFileName(String fileName, HttpServletRequest request) {
        try {
            String userAgent = request.getHeader("User-Agent");
            
            if (userAgent == null) {
                // 默认编码
                return URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            }
            
            String lowerCaseUserAgent = userAgent.toLowerCase();
            
            if (lowerCaseUserAgent.contains("msie") || lowerCaseUserAgent.contains("trident")) {
                // IE浏览器
                return URLEncoder.encode(fileName, "UTF-8");
            } else if (lowerCaseUserAgent.contains("firefox")) {
                // Firefox浏览器
                return "=?UTF-8?B?" + 
                       new String(Base64.getEncoder().encode(fileName.getBytes(StandardCharsets.UTF_8))) + 
                       "?=";
            } else {
                // Chrome、Safari、Edge等其他浏览器
                return URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            }
        } catch (Exception e) {
            // 编码失败，返回原始文件名（可能在某些浏览器无法正常显示）
            return fileName;
        }
    }
    
    /**
     * 通用文件名编码方法（简化版）
     */
    public static String encodeFileName(String fileName) {
        try {
            return URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            return fileName;
        }
    }
}
