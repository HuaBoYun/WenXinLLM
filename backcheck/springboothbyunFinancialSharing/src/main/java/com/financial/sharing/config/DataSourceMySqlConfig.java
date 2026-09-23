package com.financial.sharing.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.hbfk.sdk.log.interceptor.SqlLoggingInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
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

import javax.sql.DataSource;

// @Configuration  // 已禁用：项目统一使用Oracle/达梦数据源，不再需要MySQL数据源配置
// @MapperScan(basePackages = {"com.financial.sharing.mysql.mapper"}, sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
public class DataSourceMySqlConfig {

	@Bean(name = "mysqlDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.mysql")
	public DataSource oracleDataSource() {
		return new DruidDataSource();
	}

	@Bean(name = "mysqlSqlSessionFactory")
	public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier(value = "mysqlDataSource") DataSource dataSource, MybatisProperties mybatisProperties)
			throws Exception {

		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		bean.setDataSource(dataSource);

		// 创建分页插件
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setDialectType("mysql");
		paginationInterceptor.setOverflow(false);
		paginationInterceptor.setLimit(500);

		// 添加拦截器：SQL日志拦截器和分页插件
		bean.setPlugins(new Interceptor[] {new SqlLoggingInterceptor(), paginationInterceptor});

		// 设置类型别名包
		bean.setTypeAliasesPackage("com.financial.sharing.mysql.entity");

		// 加载 MyBatis 配置文件
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		bean.setConfigLocation(mybatisConfigXml);

		// 加载XML mapper文件，支持MyBatis-Plus的BaseMapper和自定义SQL
		Resource[] mapperLocations = resolver.getResources("classpath*:mapper/mysql/*.xml");
		System.out.println("========= MySQL Mapper文件加载调试信息 =========");
		System.out.println("找到的Mapper文件数量: " + mapperLocations.length);
		for (Resource mapperLocation : mapperLocations) {
			System.out.println("Mapper文件: " + mapperLocation.getURL());
		}
		System.out.println("===============================================");
		bean.setMapperLocations(mapperLocations);

		return bean.getObject();
	}

	@Bean(name = "mysqlDataSourceTransactionManager")
	public DataSourceTransactionManager mysqlDataSourceTransactionManager(@Qualifier(value = "mysqlDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "mysqlSqlSessionTemplate")
	public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier(value = "mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
