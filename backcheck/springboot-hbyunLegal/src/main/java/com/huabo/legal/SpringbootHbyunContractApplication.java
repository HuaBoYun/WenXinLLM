package com.huabo.legal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
	    com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class,
		DataSourceAutoConfiguration.class})
@EnableEurekaClient
@ComponentScan(basePackages = {"com.huabo.legal","com.hbfk.util"})
@EnableScheduling
public class SpringbootHbyunContractApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbyunContractApplication.class, args);
		System.out.println("法务管理微服务启动成功！");
	}

}
