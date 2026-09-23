package com.hbfk.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class PropertyFileReader {
	private static Logger logger = LoggerFactory.getLogger(PropertyFileReader.class);
	
	public static String getItem(String key) {
		return getItem(key, "");
	}
	
	public static String getItem(String key, String defaultValue) {
		ResourceBundle rb = ResourceBundle.getBundle("fxgl");
		String value = "";
        
        try {
        	value = rb.getString(key);
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.error("error occured in read property file.");
        }
        
        if (StringUtils.isEmpty(value)) {
        	value = defaultValue;
        }
        
        return value.trim();
	}
	
	public static int getIntItem(String key) {
		return getIntItem(key, "0");
	}
	
	public static long getLongItem(String key) {
		return getLongItem(key, "0");
	}
	
	public static boolean getBooleanItem(String key) {
		return getBooleanItem(key, "true");
	}
	
	public static long getLongItem(String key, String defaultValue) {
		return Long.parseLong(getItem(key, defaultValue));
	}
	
	public static int getIntItem(String key, String defaultValue) {
		return Integer.parseInt(getItem(key, defaultValue));
	}
	
	public static boolean getBooleanItem(String key, String defaultValue) {
		return Boolean.parseBoolean(getItem(key, defaultValue));
	}
}
