package com.huabo.contract.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 数据库兼容性MyBatis配置
 */
@Configuration
public class DatabaseCompatibilityMybatisConfig {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Autowired
    private DatabaseCompatibilityInterceptor interceptor;

    /**
     * 注册数据库兼容性拦截器到所有SqlSessionFactory
     */
    @PostConstruct
    public void addInterceptor() {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }
}
