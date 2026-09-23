package com.huabo.audit.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;

import com.hbfk.sdk.log.interceptor.SqlLoggingInterceptor;
import tk.mybatis.spring.annotation.MapperScan;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.huabo.audit.oracle.mapper" , sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceOralceConfig {

	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource oracleDataSource() {
		return new DruidDataSource();
	}
	
	@Bean(name = "sqlSessionFactory")
	@Primary
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier(value = "dataSource") DataSource dataSource,MybatisProperties mybatisProperties) throws Exception {
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		/*MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.DM));
		Properties properties = new Properties();
		// properties.setProperty("helperDialect", "dm");
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "true");

        interceptor.setProperties(properties);*/
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		bean.setConfigLocation(mybatisConfigXml);
		bean.setTypeAliasesPackage("com.huabo.audit.oracle.entity");
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		bean.setGlobalConfig(globalConfig());
		bean.setPlugins(new SqlLoggingInterceptor());
		return bean.getObject();
	}

	@Bean
	public GlobalConfig globalConfig() {
		GlobalConfig conf = new GlobalConfig();
		conf.setDbConfig(new GlobalConfig.DbConfig().setKeyGenerator(new OracleKeyGenerator()));
		return conf;
	}

	
	@Bean(name = "dataSourceTransactionManager")
	@Primary
	public DataSourceTransactionManager oracleDataSourceTransactionManager(@Qualifier(value = "dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean(name = "sqlSessionTemplate")
	@Primary
	public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier(value = "sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
