package com.huabo.finance.config;

import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

public class SystemStaticConfig {

	
	public static String method;
	
	public static String dbtype;
	
	static {
		try {
			//Properties properties = loadPropertyFile("application-dev.yml");
			
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			yaml.setResources(new ClassPathResource("application.yml"));
			Properties properties = yaml.getObject();
			
			method = properties.getProperty("finance.method");
			dbtype = properties.getProperty("finance.dbtype");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
