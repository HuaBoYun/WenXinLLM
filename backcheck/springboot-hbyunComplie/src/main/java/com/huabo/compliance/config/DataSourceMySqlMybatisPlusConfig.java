//package com.huabo.legal.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
//@Slf4j
//@Configuration
//@MapperScan(basePackages = {"com.huabo.legal.exam.**.mapper"}, sqlSessionTemplateRef = "mysqlMybatisPlusSqlSessionTemplate")
//public class DataSourceMySqlMybatisPlusConfig {
//
//	@Bean(name = "mysqlMybatisPlusDataSource")
//	//@ConfigurationProperties(prefix = "spring.datasource.oracle")
//	@ConfigurationProperties(prefix = "spring.datasource.mysql")
//	public DataSource oracleDataSource() {
//		return new DruidDataSource();
//	}
//
//	@Bean(name = "mysqlMybatisPlusSqlSessionFactory")
//	public SqlSessionFactory mysqlMybatisSqlSessionFactory(@Qualifier(value = "mysqlMybatisPlusDataSource") DataSource dataSource,
//			MybatisProperties mybatisProperties) throws Exception {
//		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
//		bean.setDataSource(dataSource);
//		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mysql/exam/*.xml"));
//		return bean.getObject();
//	}
//
//	@Bean(name = "mysqlMybatisPlusDataSourceTransactionManager")
//	public DataSourceTransactionManager mysqlMybatisPlusDataSourceTransactionManager(
//			@Qualifier(value = "mysqlMybatisPlusDataSource") DataSource dataSource) {
//		return new DataSourceTransactionManager(dataSource);
//	}
//
//	@Bean(name = "mysqlMybatisPlusSqlSessionTemplate")
//	public SqlSessionTemplate mysqlMybatisPlusSqlSessionTemplate(
//			@Qualifier(value = "mysqlMybatisPlusSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
//}
