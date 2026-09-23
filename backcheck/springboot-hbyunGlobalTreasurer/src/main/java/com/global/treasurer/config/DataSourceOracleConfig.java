package com.global.treasurer.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
//import com.hbfk.sdk.log.interceptor.SqlLoggingInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
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
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"com.global.treasurer.mapper"}, sqlSessionTemplateRef = "oracleSqlSessionTemplate")
public class DataSourceOracleConfig {
	@Bean(name = "oracleDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource oracleDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		return dataSource;
	}

	@Bean(name = "oracleSqlSessionFactory")
	@Primary
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier(value = "oracleDataSource") DataSource dataSource,
			MybatisProperties mybatisProperties) throws Exception {
		// 使用MyBatis-Plus的SqlSessionFactoryBean以支持BaseMapper
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		Interceptor interceptor = new PageInterceptor();
		Properties properties = new Properties();
		//数据库
		//        properties.setProperty("helperDialect", "oracle");
		//        //是否分页合理化
		//        properties.setProperty("reasonable", "false");
		//        properties.setProperty("supportMethodsArguments","true");
		//        interceptor.setProperties(properties);
		//        bean.setPlugins(new Interceptor[] {interceptor});
		//bean.setPlugins(new Interceptor[] {new SqlLoggingInterceptor()});
		bean.setPlugins(new Interceptor[] {});  // 暂时禁用SQL日志拦截器
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		Resource[] allResources = new Resource[0];
		try {
			Resource[] generalResources = resolver.getResources("classpath:mapper/*.xml");
			List<Resource> allResourcesList = new ArrayList<>();
			if (generalResources != null && generalResources.length > 0) {
				allResourcesList.addAll(Arrays.asList(generalResources));
			}
			allResources = allResourcesList.toArray(new Resource[0]);
		} catch (Exception e) {
			// Ignore mapper files if directories don't exist
			allResources = new Resource[0];
		}
		bean.setConfigLocation(mybatisConfigXml);
		bean.setTypeAliasesPackage("com.global.treasurer.entity,com.global.treasurer.entity.TcSecurityParameter,com.global.treasurer.vo");
		bean.setMapperLocations(allResources);
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
