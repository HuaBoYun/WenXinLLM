package com.huabo.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * springboot主入口程序
 *
 * @author lee
 */
@SpringBootApplication(exclude = {
	    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
	    com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class
	})
@EnableEurekaClient
@MapperScan("com.huabo.finance.mapper") //添加mapper扫描
@ComponentScan(basePackages = {"com.huabo.finance","com.hbfk.util"})
public class HbyunFinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HbyunFinanceApplication.class, args);
        System.out.println("财务数仓微服务启动成功！");
    }


}
