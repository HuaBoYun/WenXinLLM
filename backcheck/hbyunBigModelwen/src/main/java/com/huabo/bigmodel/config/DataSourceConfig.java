package com.huabo.bigmodel.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 数据源配置
 * 支持达梦数据库和MySQL数据库
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    /**
     * 达梦数据库数据源(主数据源)
     */
    @Primary
    @Bean(name = "dmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dm")
    public DataSource dmDataSource() {
        log.info("初始化达梦数据库数据源");
        return new DruidDataSource();
    }

    /**
     * MySQL数据库数据源(备用数据源)
     */
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        log.info("初始化MySQL数据库数据源");
        return new DruidDataSource();
    }

    /**
     * 达梦数据库 JdbcTemplate
     */
    @Primary
    @Bean(name = "dmJdbcTemplate")
    public JdbcTemplate dmJdbcTemplate() {
        return new JdbcTemplate(dmDataSource());
    }

    /**
     * MySQL数据库 JdbcTemplate
     */
    @Bean(name = "mysqlJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate() {
        return new JdbcTemplate(mysqlDataSource());
    }
}

