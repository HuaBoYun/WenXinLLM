package com.huabo.audit;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import com.hbfk.sdk.billing.annotation.EnableBilling;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableBilling
@ComponentScan(basePackages = {"com.huabo.audit", "com.hbfk.util", "com.hbfk.sdk.billing"})
@MapperScan(basePackages = {"com.huabo.audit.oracle.mapper"})
public class SpringbootHbyunOiAuditApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootHbyunOiAuditApplication.class, args);
        System.out.println("央企审计微服务启动成功！！4");
    }

}
