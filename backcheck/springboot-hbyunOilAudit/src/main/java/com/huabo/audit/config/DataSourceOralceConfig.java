package com.huabo.audit.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
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

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

@Configuration
@MapperScan(basePackages = "com.huabo.audit.oracle.mapper" , sqlSessionTemplateRef = "oracleSqlSessionTemplate")
public class DataSourceOralceConfig {

	@Bean(name = "oracleDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.oracle")
	@Primary
	public DataSource oracleDataSource() {
		return new DruidDataSource();
	}
	
	@Bean(name = "oracleSqlSessionFactory")
	@Primary
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier(value = "oracleDataSource") DataSource dataSource,MybatisProperties mybatisProperties) throws Exception {
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
//		Interceptor interceptor = new PageInterceptor();
//		Properties properties = new Properties();
//		//数据库
//		properties.setProperty("helperDialect", "oracle");
//		//是否分页合理化
//		properties.setProperty("reasonable", "false");
//		properties.setProperty("supportMethodsArguments","true");
//		interceptor.setProperties(properties);
//		bean.setPlugins(new Interceptor[] {interceptor});
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		bean.setConfigLocation(mybatisConfigXml);
		bean.setTypeAliasesPackage("com.huabo.audit.oracle.entity");
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "oracleDataSourceTransactionManager")
	@Primary
	public DataSourceTransactionManager oracleDataSourceTransactionManager(@Qualifier(value = "oracleDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean(name = "oracleSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier(value = "oracleSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
