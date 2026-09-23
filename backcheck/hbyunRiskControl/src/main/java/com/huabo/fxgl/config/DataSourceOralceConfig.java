package com.huabo.fxgl.config;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.hbfk.sdk.log.interceptor.SqlLoggingInterceptor;

@Configuration
@MapperScan(basePackages = "com.huabo.fxgl.mapper", sqlSessionTemplateRef = "oracleSqlSessionTemplate")
public class DataSourceOralceConfig {

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig conf = new GlobalConfig();
        List<IKeyGenerator> keyGenerators = new LinkedList<>();
        keyGenerators.add(keyGenerator());
        conf.setDbConfig(new GlobalConfig.DbConfig().setKeyGenerators(keyGenerators));
        return conf;
    }

    @Bean
    public IKeyGenerator keyGenerator() {
//        System.out.println("-----------------IKeyGenerator----------------");
        return new OracleKeyGenerator();
    }

    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    @Primary
    public DataSource oracleDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "oracleSqlSessionFactory")
    @Primary
    public SqlSessionFactory oracleSqlSessionFactory(@Qualifier(value = "oracleDataSource") DataSource dataSource) throws Exception {
        /**
         * 修订分页bug
         */
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        /*MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.ORACLE));
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "oracle");
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "true");
        interceptor.setProperties(properties);
        bean.setPlugins(interceptor, new SqlLoggingInterceptor());*/
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
        bean.setConfigLocation(mybatisConfigXml);
//		如果想使用xml请在引处添加mapper.xml,暂时没有想到更好的办法
//		Resource mappers = resolver.getResource("classpath:com/huabo/fxgl/mapper/xml/ZuzhijigouMapper1.xml");
//		bean.setMapperLocations(mappers);
        bean.setTypeAliasesPackage("com.huabo.fxgl.entity");
        bean.setTypeEnumsPackage("com.huabo.fxgl.entity.enums");
        bean.setVfs(SpringBootVFS.class);
        bean.setGlobalConfig(globalConfig());
//		bean.setMapperLocations(mappers);
		bean.setPlugins(new SqlLoggingInterceptor());
        return bean.getObject();
    }

    @Bean(name = "oracleDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager oracleDataSourceTransactionManager(@Qualifier(value = "oracleDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "oracleSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier(value = "oracleSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
