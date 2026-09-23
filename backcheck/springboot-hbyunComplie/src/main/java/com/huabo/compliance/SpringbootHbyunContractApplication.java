package com.huabo.compliance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		//org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableScheduling
@ComponentScan(basePackages = {"com.huabo.compliance","com.hbfk.util"})
@MapperScan(basePackages = {"com.huabo.compliance.oracle.mapper"})
public class SpringbootHbyunContractApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbyunContractApplication.class, args);
		System.out.println("合规管理微服务启动成功！5");
	}
}
