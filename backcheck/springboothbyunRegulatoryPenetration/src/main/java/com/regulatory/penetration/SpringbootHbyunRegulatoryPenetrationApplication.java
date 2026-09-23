package com.regulatory.penetration;

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
@ComponentScan(basePackages = "com.regulatory.penetration")
@EnableScheduling
@EnableAsync
@EnableOperationLog(module = "hbyunGlobalTreasurerModule")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
public class SpringbootHbyunRegulatoryPenetrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.regulatory.penetration.SpringbootHbyunRegulatoryPenetrationApplication.class, args);
		System.out.println("穿透监管微服务启动成功！");
	}

}
