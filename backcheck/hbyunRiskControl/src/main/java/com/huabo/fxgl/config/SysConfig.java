package com.huabo.fxgl.config;

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
}
