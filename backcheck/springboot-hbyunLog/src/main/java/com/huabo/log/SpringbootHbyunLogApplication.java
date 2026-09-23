package com.huabo.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.hbfk.sdk.log.annotation.EnableOperationLog;

@SpringBootApplication
@EnableEurekaClient
@EnableOperationLog(module = "日志管理服务")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
@ComponentScan(basePackages = {"com.huabo.log","com.hbfk.util"})
public class SpringbootHbyunLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHbyunLogApplication.class, args);
        System.out.println("日志管理微服务启动成功！");
    }

}
