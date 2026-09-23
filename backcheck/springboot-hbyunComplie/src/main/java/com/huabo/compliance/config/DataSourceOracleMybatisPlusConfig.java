//package com.huabo.legal.config;
//
//import javax.sql.DataSource;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
//import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import com.fasterxml.jackson.annotation.JacksonInject.Value;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Configuration
//@MapperScan(basePackages = {"com.huabo.legal.exam.**.mapper","com.huabo.legal.startup.bbs.mapper"}, sqlSessionTemplateRef = "oracleMybatisPlusSqlSessionTemplate")
//public class DataSourceOracleMybatisPlusConfig {
//
//	@Bean(name = "oracleMybatisPlusDataSource")
//	//@ConfigurationProperties(prefix = "spring.datasource.oracle")
//	@ConfigurationProperties(prefix = "spring.datasource.oracle")
//	public DataSource oracleDataSource() {
//		return new DruidDataSource();
//	}
//
//	@Bean(name = "oracleMybatisPlusSqlSessionFactory")
//	public SqlSessionFactory oracleMybatisSqlSessionFactory(@Qualifier(value = "oracleMybatisPlusDataSource") DataSource dataSource,
//			MybatisProperties mybatisProperties) throws Exception {
//		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
//		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
//		bean.setDataSource(dataSource);
//		bean.setTypeAliasesPackage("com.huabo.legal.exam.**.entity");
//		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:oracle/mapper/exam/*.xml"));
//		bean.setConfigLocation(mybatisConfigXml);
//		bean.setTransactionFactory(new SpringManagedTransactionFactory());
//		return bean.getObject();
//	}
//
//	@Bean(name = "oracleMybatisPlusDataSourceTransactionManager")
//	public DataSourceTransactionManager oracleMybatisPlusDataSourceTransactionManager(
//			@Qualifier(value = "oracleMybatisPlusDataSource") DataSource dataSource) {
//		return new DataSourceTransactionManager(dataSource);
//	}
//
//	@Bean(name = "oracleMybatisPlusSqlSessionTemplate")
//	public SqlSessionTemplate oracleMybatisPlusSqlSessionTemplate(
//			@Qualifier(value = "oracleMybatisPlusSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
//}
