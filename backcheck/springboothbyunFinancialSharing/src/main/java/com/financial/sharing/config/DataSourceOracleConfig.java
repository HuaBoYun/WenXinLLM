package com.financial.sharing.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.hbfk.sdk.log.interceptor.SqlLoggingInterceptor;
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

import javax.sql.DataSource;

@Configuration
public class DataSourceOracleConfig {

	@Bean(name = "oracleDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource oracleDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		// 设置达梦数据库驱动
		dataSource.setDriverClassName("dm.jdbc.driver.DmDriver");
		return dataSource;
	}

	
	@Bean(name = "oracleSqlSessionFactory")
	@Primary
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier(value = "oracleDataSource") DataSource dataSource,
			MybatisProperties mybatisProperties) throws Exception {
		// 使用 MybatisSqlSessionFactoryBean 以支持 MyBatis-Plus
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();

		// 创建分页插件
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setDialectType("oracle");
		paginationInterceptor.setOverflow(false); // 设置请求的页面大于最大页后操作，false继续请求，true调回到首页
		paginationInterceptor.setLimit(500); // 设置最大单页限制数量

		// 添加拦截器：SQL日志拦截器和分页插件
		bean.setPlugins(new Interceptor[] {new SqlLoggingInterceptor(), paginationInterceptor});
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		// 扫描所有路径下的 Mapper XML 文件
		Resource[] mapperOracleResources = resolver.getResources("classpath:mapper/oracle/*.xml");
		Resource[] oracleResources = resolver.getResources("classpath:oracle/*.xml");
		Resource[] mapperResources = resolver.getResources("classpath:mapper/*.xml");
		Resource[] mapperSubResources = resolver.getResources("classpath:mapper/**/*.xml");
		// 合并所有数组
		int totalLength = mapperOracleResources.length + oracleResources.length + mapperResources.length + mapperSubResources.length;
		Resource[] resources = new Resource[totalLength];
		int currentIndex = 0;
		System.arraycopy(mapperOracleResources, 0, resources, currentIndex, mapperOracleResources.length);
		currentIndex += mapperOracleResources.length;
		System.arraycopy(oracleResources, 0, resources, currentIndex, oracleResources.length);
		currentIndex += oracleResources.length;
		System.arraycopy(mapperResources, 0, resources, currentIndex, mapperResources.length);
		currentIndex += mapperResources.length;
		System.arraycopy(mapperSubResources, 0, resources, currentIndex, mapperSubResources.length);
		bean.setConfigLocation(mybatisConfigXml);
		bean.setTypeAliasesPackage("com.financial.sharing.oracle.entity");
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
