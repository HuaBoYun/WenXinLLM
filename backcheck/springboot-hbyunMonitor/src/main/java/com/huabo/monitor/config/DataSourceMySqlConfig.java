package com.huabo.monitor.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;

@Configuration
@MapperScan(basePackages = "com.huabo.monitor.mysql.mapper" , sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
public class DataSourceMySqlConfig {

	@Bean(name = "mysqlDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.mysql")
	public DataSource oracleDataSource() {
		return new DruidDataSource();
	}
	
	@Bean(name = "mysqlSqlSessionFactory")
	public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier(value = "mysqlDataSource") DataSource dataSource,MybatisProperties mybatisProperties) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        //数据库
        properties.setProperty("helperDialect", "mysql");
        //是否分页合理化
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments","true");
        interceptor.setProperties(properties);
        bean.setPlugins(new Interceptor[] {interceptor});
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		bean.setTypeAliasesPackage("com.huabo.monitor.mysql.entity");
		bean.setConfigLocation(mybatisConfigXml);
		bean.setDataSource(dataSource);
		//bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/huabo/gkb/mysqlmapper/xml/*.xml"));
		return bean.getObject();
	}
	
	@Bean(name = "mysqlDataSourceTransactionManager")
	public DataSourceTransactionManager mysqlDataSourceTransactionManager(@Qualifier(value = "mysqlDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean(name = "mysqlSqlSessionTemplate")
	public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier(value = "mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
