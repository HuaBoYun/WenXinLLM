package com.huabo.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
@EnableEurekaClient
@ComponentScan(basePackages = "com.huabo.monitor")
public class SpringbootHbyunMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHbyunMonitorApplication.class, args);
    }

}
