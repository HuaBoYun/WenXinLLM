package com.huabo.financialdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * springboot主入口程序
 *
 * @author lee
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.hbfk.util","com.huabo.financialdata"})
public class FinancialDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialDataApplication.class, args);
    }

}
