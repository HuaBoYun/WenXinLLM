package com.management.accountant.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据库配置类
 * 支持达梦数据库、MySQL数据库、Oracle数据库
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Configuration
public class DatabaseConfig {

    @Value("${spring.database.type:mysql}")
    private String databaseType;

    /**
     * 达梦数据库数据源配置
     */
    @Bean("dmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dm")
    public DataSource dmDataSource() {
        return new DruidDataSource();
    }

    /**
     * MySQL数据库数据源配置
     */
    @Bean("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        return new DruidDataSource();
    }

    /**
     * Oracle数据库数据源配置
     */
    @Bean("oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() {
        return new DruidDataSource();
    }

    /**
     * 主数据源配置
     * 根据配置文件中的database.type选择对应的数据源
     */
    @Bean
    @Primary
    public DataSource primaryDataSource() {
        switch (databaseType.toLowerCase()) {
            case "dm":
                return dmDataSource();
            case "oracle":
                return oracleDataSource();
            case "mysql":
            default:
                return mysqlDataSource();
        }
    }

    /**
     * 获取当前数据库类型
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * 判断是否为达梦数据库
     */
    public boolean isDmDatabase() {
        return "dm".equalsIgnoreCase(databaseType);
    }

    /**
     * 判断是否为MySQL数据库
     */
    public boolean isMysqlDatabase() {
        return "mysql".equalsIgnoreCase(databaseType);
    }

    /**
     * 判断是否为Oracle数据库
     */
    public boolean isOracleDatabase() {
        return "oracle".equalsIgnoreCase(databaseType);
    }
}
