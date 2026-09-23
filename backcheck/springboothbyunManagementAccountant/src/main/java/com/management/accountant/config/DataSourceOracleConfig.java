package com.management.accountant.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.github.pagehelper.PageInterceptor;
import com.hbfk.sdk.log.interceptor.SqlLoggingInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
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
import org.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(
    basePackages = {
        "com.management.accountant.oracle.mapper"
    },
    sqlSessionTemplateRef = "oracleSqlSessionTemplate"
)
public class DataSourceOracleConfig {

	@Bean(name = "oracleDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.oracle")
	@Primary
	public DataSource oracleDataSource() {
		return new DruidDataSource();
	}

	@Bean(name = "oracleSqlSessionFactory")
	@Primary
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier(value = "oracleDataSource") DataSource dataSource,
			MybatisProperties mybatisProperties,
			MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		bean.setPlugins(new Interceptor[] {mybatisPlusInterceptor, new SqlLoggingInterceptor()});
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		// 只扫描实际存在的Mapper XML路径
		String[] mapperLocations = {
			"classpath:mapper/*.xml",
			"classpath:mapper/budget/*.xml",
			"classpath:mapper/advanced/*.xml",
			"classpath:mapper/integration/*.xml",
			"classpath:oracle/mapper/*.xml"
		};
		java.util.List<Resource> resourceList = new java.util.ArrayList<>();
		for (String location : mapperLocations) {
			try {
				Resource[] locationResources = resolver.getResources(location);
				if (locationResources != null && locationResources.length > 0) {
					for (Resource r : locationResources) {
						resourceList.add(r);
					}
				}
			} catch (Exception e) {
				// 忽略不存在的路径
			}
		}
		Resource[] resources = resourceList.toArray(new Resource[0]);
		bean.setConfigLocation(mybatisConfigXml);
		// 只扫描实际存在的实体类包
		bean.setTypeAliasesPackage("com.management.accountant.oracle.entity,com.management.accountant.oracle.entity.budget,com.management.accountant.oracle.entity.advanced,com.management.accountant.oracle.entity.integration");
		bean.setMapperLocations(resources);
		return bean.getObject();
	}

	@Bean(name = "oracleDataSourceTransactionManager")
	@Primary
	public DataSourceTransactionManager oracleDataSourceTransactionManager(@Qualifier(value = "oracleDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "oracleSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier(value = "oracleSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
