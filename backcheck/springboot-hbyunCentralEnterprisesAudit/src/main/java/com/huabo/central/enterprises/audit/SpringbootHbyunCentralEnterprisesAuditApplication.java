package com.huabo.central.enterprises.audit;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class})
@EnableEurekaClient
@ComponentScan(basePackages = {"com.huabo.central.enterprises.audit","com.hbfk.util"})
@EnableScheduling
@EnableAsync
@EnableOperationLog(module = "hbyunCentralEnterprisesAuditModule")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
public class SpringbootHbyunCentralEnterprisesAuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbyunCentralEnterprisesAuditApplication.class, args);
		System.out.println("央企内审综合管理微服务启动成功！");
	}

}
