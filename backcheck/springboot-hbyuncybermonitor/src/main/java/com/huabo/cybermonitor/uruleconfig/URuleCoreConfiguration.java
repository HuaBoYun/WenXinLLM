package com.huabo.cybermonitor.uruleconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * URule配置
 * 暂时禁用，因为 urule-core-context.xml 文件不存在
 */
//@Configuration
//@ImportResource({"classpath:urule-core-context.xml"})
public class URuleCoreConfiguration {
	
	@Bean
    public PropertySourcesPlaceholderConfigurer propertySourceLoader() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setOrder(1);
        return configurer;
    }
	
}
