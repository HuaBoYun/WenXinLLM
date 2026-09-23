package com.huabo.es.config.properties;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * druid 配置属性
 *
 * @author zhibo.cao
 */
@Configuration
public class DruidProperties {

    private static int initialSize;


    private static int minIdle;


    private static int maxActive;


    private static int maxWait;


    private static int timeBetweenEvictionRunsMillis;


    private static int minEvictableIdleTimeMillis;


    private static int maxEvictableIdleTimeMillis;


    private static String validationQuery;


    private static boolean testWhileIdle;


    private static boolean testOnBorrow;


    private static boolean testOnReturn;


    @Value("${spring.datasource.druid.initialSize}")
    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    @Value("${spring.datasource.druid.minIdle}")
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    @Value("${spring.datasource.druid.maxActive}")
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    @Value("${spring.datasource.druid.maxWait}")
    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    public void setMaxEvictableIdleTimeMillis(int maxEvictableIdleTimeMillis) {
        this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
    }

    @Value("${spring.datasource.druid.validationQuery}")
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    @Value("${spring.datasource.druid.testWhileIdle}")
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    @Value("${spring.datasource.druid.testOnBorrow}")
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    @Value("${spring.datasource.druid.testOnReturn}")
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public DruidDataSource dataSource(DruidDataSource datasource) {
        /** 配置初始化大小、最小、最大 */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /** 配置获取连接等待超时的时间 */
        datasource.setMaxWait(maxWait);

        /** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /** 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        /**
         * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
         */
        datasource.setValidationQuery(validationQuery);
        /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        datasource.setTestWhileIdle(testWhileIdle);
        /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnBorrow(testOnBorrow);
        /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnReturn(testOnReturn);
        return datasource;
    }
}
