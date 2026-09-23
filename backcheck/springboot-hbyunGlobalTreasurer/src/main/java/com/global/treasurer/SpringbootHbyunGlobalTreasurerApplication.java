package com.global.treasurer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class})
@EnableEurekaClient
@ComponentScan(basePackages = {"com.global.treasurer", "com.hbfk.util.redis", "com.hbfk.util"},
	excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.hbfk\\.util\\.mibiao\\..*"))
@MapperScan({"com.global.treasurer.oracle.mapper", "com.global.treasurer.financialProductDefinition.mapper", "com.global.treasurer.mapper", "com.global.treasurer.mapper.derivatives"})  // 扫描所有mapper包
@EnableScheduling
@EnableAsync
public class SpringbootHbyunGlobalTreasurerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbyunGlobalTreasurerApplication.class, args);
		System.out.println("==============================================");
		System.out.println("全球司库微服务启动成功！");
		System.out.println("【重要】已启用 multipart/form-data 支持");
		System.out.println("【重要】FlexibleRequestBody 解析器已注册");
		System.out.println("【重要】FundTransfer Controller 已更新");
		System.out.println("==============================================");
	}

}
