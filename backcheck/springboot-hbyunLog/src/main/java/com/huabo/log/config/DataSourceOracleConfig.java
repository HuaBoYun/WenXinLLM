package com.huabo.log.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * oracle数据库配置
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.use", havingValue = "oracle")
public class DataSourceOracleConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() {
        return new DruidDataSource();
    }

}
