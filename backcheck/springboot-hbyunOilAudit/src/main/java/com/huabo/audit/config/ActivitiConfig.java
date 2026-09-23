package com.huabo.audit.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Activiti 配置 —— 强制指定 databaseType=oracle
 * 达梦数据库 product name 为 "DM DBMS"，Activiti 无法自动识别，
 * 需手动设置为 oracle（达梦语法兼容 Oracle）。
 * 只覆盖 SpringProcessEngineConfiguration，让 Activiti 自动配置用此 Bean 构建 processEngine。
 */
@Configuration
@AutoConfigureBefore(AbstractProcessEngineAutoConfiguration.class)
public class ActivitiConfig {

    @Bean
    @Primary
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            @Qualifier("oracleDataSource") DataSource dataSource,
            @Qualifier("oracleDataSourceTransactionManager") DataSourceTransactionManager transactionManager) {

        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setTransactionManager(transactionManager);
        // 达梦兼容 Oracle 语法，强制指定，跳过自动检测
        config.setDatabaseType("oracle");
        config.setDatabaseSchemaUpdate("false");
        config.setAsyncExecutorActivate(false);
        return config;
    }
}
