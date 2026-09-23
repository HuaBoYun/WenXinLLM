package com.huabo.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.hbfk.sdk.log.annotation.EnableOperationLog;


@SpringBootApplication
@EnableEurekaClient
@EnableOperationLog(module = "SpringbootHbyunMonitor2")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
@ComponentScan(basePackages = {"com.huabo.monitor","com.hbfk.util"})
public class SpringbootHbyunMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootHbyunMonitorApplication.class, args);
        System.out.println("内控管理微服务启动成功！2");
    }
    
}
