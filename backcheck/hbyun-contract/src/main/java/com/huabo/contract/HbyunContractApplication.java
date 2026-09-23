package com.huabo.contract;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import com.hbfk.sdk.billing.annotation.EnableBilling;

/**
 * springboot主入口程序
 *
 * @author lee
 */
@SpringBootApplication
@EnableEurekaClient
@EnableBilling
@MapperScan({"com.huabo.contract.mapper", "com.hbfk.sdk.billing.mapper"})
@ComponentScan(basePackages = {"com.huabo.contract", "com.hbfk.util", "com.hbfk.sdk.billing"})
public class HbyunContractApplication {
    public static void main(String[] args) {
        SpringApplication.run(HbyunContractApplication.class, args);
    }
}