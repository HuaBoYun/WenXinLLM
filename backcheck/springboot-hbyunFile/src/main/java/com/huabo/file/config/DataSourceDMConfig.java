package com.huabo.file.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 达梦数据库配置
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.use", havingValue = "dm")
public class DataSourceDMConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dm")
    public DataSource DmDataSource() {
        return new DruidDataSource();
    }

}
