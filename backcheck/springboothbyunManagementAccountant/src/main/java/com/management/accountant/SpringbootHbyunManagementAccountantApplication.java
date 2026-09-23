package com.management.accountant;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import com.hbfk.sdk.billing.annotation.EnableBilling;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class})
@EnableEurekaClient
@MapperScan({"com.management.accountant.oracle.mapper", "com.hbfk.sdk.billing.mapper"})
@ComponentScan(
	basePackages = {"com.management.accountant", "com.hbfk.util", "com.hbfk"},
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.REGEX,
			pattern = "com\\.management\\.accountant\\.oracle\\.(controller|service|mapper|entity)\\.(control|preparation|system)\\..*"
		)
	}
)
@EnableScheduling
@EnableAsync
@EnableOperationLog(module = "hbyunGlobalTreasurerModule")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
@EnableBilling
public class SpringbootHbyunManagementAccountantApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbyunManagementAccountantApplication.class, args);
		System.out.println("管理会计微服务启动成功！");
	}

}
