package com.huabo.audit.service.impl;

import java.io.IOException;
import java.util.Properties;

public class SysConfig {

    private static Properties prop = null;

    public SysConfig() {
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

    public static void put(String key, String val) {
        prop.put(key, val);
    }

    public static Integer getInt(String key) {
        return Integer.parseInt(prop.getProperty(key));
    }

    public static Boolean getBool(String key) {
        return Boolean.parseBoolean(prop.getProperty(key));
    }

    static {
        if (prop == null) {
            prop = new Properties();

            try {
                prop.load(SysConfig.class.getClassLoader().getResourceAsStream("setting/jdbc.properties"));
            } catch (IOException var1) {
                var1.printStackTrace();
            }
        }

    }
}
