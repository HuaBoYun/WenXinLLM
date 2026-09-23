package com.huabo.fxgl.util;

import java.io.IOException;
import java.util.Properties;

public class SysConfig {
	
	private static Properties prop = null;
	
	static {
		if(prop == null) {
			prop = new Properties();
			try {
				prop.load(SysConfig.class.getClassLoader().getResourceAsStream("setting/jdbc.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String get(String key) {
		return prop.getProperty(key);
	}
	
	public static void put(String key,String val){
		prop.put(key, val);
	}
	
	public static Integer getInt(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}
	
	public static Boolean getBool(String key) {
		return Boolean.parseBoolean(prop.getProperty(key));
	}
	
}
