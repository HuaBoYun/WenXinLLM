package com.huabo.audit.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 计费专用达梦数据源配置
 * 计费 mapper 走达梦（与 systemModule 同库），业务 mapper 继续走 Oracle
 */
@Configuration
@MapperScan(basePackages = "com.hbfk.sdk.billing.mapper", sqlSessionTemplateRef = "billingSqlSessionTemplate")
public class BillingDataSourceConfig {

    @Bean(name = "billingDataSource")
    @ConfigurationProperties(prefix = "billing.datasource")
    public DataSource billingDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "billingSqlSessionFactory")
    public SqlSessionFactory billingSqlSessionFactory(@Qualifier("billingDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "billingSqlSessionTemplate")
    public SqlSessionTemplate billingSqlSessionTemplate(@Qualifier("billingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
