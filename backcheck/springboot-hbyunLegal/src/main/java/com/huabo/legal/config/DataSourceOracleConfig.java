package com.huabo.legal.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
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
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"com.huabo.legal.oracle.mapper",
		"com.huabo.legal.test.mybatis.mapper"}, sqlSessionTemplateRef = "oracleSqlSessionTemplate")
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
			MybatisProperties mybatisProperties) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		Interceptor interceptor = new PageInterceptor();
		Properties properties = new Properties();
		//数据库
		//        properties.setProperty("helperDialect", "oracle");
		//        //是否分页合理化
		//        properties.setProperty("reasonable", "false");
		//        properties.setProperty("supportMethodsArguments","true");
		//        interceptor.setProperties(properties);
		//        bean.setPlugins(new Interceptor[] {interceptor});
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		bean.setConfigLocation(mybatisConfigXml);
		bean.setTypeAliasesPackage("com.huabo.legal.oracle.entity");
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:oracle/mapper/*.xml"));
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
