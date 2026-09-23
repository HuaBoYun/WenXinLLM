package com.huabo.file.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * sqlServer数据库配置
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.use", havingValue = "sqlserver")
public class DataSourceSqlServerConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource sqlServerDataSource() {
        return new DruidDataSource();
    }

}
