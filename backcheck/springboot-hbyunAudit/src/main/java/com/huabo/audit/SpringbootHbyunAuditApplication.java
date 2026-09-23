package com.huabo.audit;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import com.hbfk.sdk.billing.annotation.EnableBilling;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class,
		DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableScheduling
@EnableOperationLog(module = "智能审计")
@EnableBilling
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
@ComponentScan(basePackages = {"com.huabo.audit", "com.hbfk.util", "com.hbfk.entity", "com.hbfk.sdk.billing"})
@MapperScan(basePackages = {"com.huabo.audit.oracle.mapper", "com.hbfk.sdk.billing.mapper"})
public class SpringbootHbyunAuditApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootHbyunAuditApplication.class, args);
        System.out.println("智能审计微服务启动成功！！");
    }
}

