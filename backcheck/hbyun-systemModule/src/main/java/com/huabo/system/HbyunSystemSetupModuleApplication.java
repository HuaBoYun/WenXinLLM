package com.huabo.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
// import com.hbfk.sdk.billing.annotation.EnableBilling; // 计费SDK暂时注释

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = GroovyTemplateAutoConfiguration.class) // 核心：排除Groovy自动配置
@EnableEurekaClient
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableOperationLog(module = "系统设置服务")
// @EnableBilling // 计费SDK暂时注释
@EnableFeignClients(basePackages = {"com.hbfk.sdk.log.feign"})
@ComponentScan(basePackages = {"com.huabo.system","com.huabo.landray","com.landray","com.hbfk.util","com.hbfk.entity"}) // 计费SDK包扫描暂时注释: ,"com.hbfk.sdk.billing"
@MapperScan({"com.huabo.system.mapper"}) // 计费SDK mapper扫描暂时注释: ,"com.hbfk.sdk.billing.mapper"
public class HbyunSystemSetupModuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(HbyunSystemSetupModuleApplication.class, args);
		System.out.println("系统设置微服务启动成功！");
	}
}
