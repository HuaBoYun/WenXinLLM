package com.huabo.fxgl;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import com.hbfk.sdk.billing.annotation.EnableBilling;

/**
 * 风险管控服务主程序入口
 * @author LiYe
 * @date 2022/07/30
 */
@MapperScan(basePackages = {"com.huabo.fxgl.mapper", "com.hbfk.sdk.billing.mapper"})
@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@EnableBilling
@EnableOperationLog(module = "风险管控服务")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log.feign")
@ComponentScan(basePackages = {"com.huabo.fxgl", "com.hbfk.util", "com.hbfk.sdk.billing"})
//@EntityScan("com.huabo.fxgl.entity")
public class RiskControlApplication
{
    public static void main(String[] args )
    {
        SpringApplication.run(RiskControlApplication.class, args);
        System.out.println("风险管控微服务启动成功！");
    }

}