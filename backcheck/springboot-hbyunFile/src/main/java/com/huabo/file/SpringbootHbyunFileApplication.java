package com.huabo.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.huabo.file","com.hbfk.util"})
public class SpringbootHbyunFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHbyunFileApplication.class, args);
        System.out.println("文件管理微服务启动成功！");
    }

}
