package com.financial.sharing.config;

import com.github.pagehelper.PageInterceptor;
import com.hbfk.sdk.log.interceptor.SqlLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * MyBatis配置类
 * 使用PageHelper进行分页
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * PageHelper分页插件
     * 支持Oracle数据库
     */
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "oracle");
        properties.setProperty("reasonable", "true"); // 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页
        properties.setProperty("supportMethodsArguments", "false"); // 支持通过Mapper接口参数来传递分页参数
        properties.setProperty("params", "count=countSql"); // 为了支持startPage(Object params)方法
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    /**
     * SQL日志拦截器
     */
    @Bean
    public SqlLoggingInterceptor sqlLoggingInterceptor() {
        return new SqlLoggingInterceptor();
    }
}