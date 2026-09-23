package com.financial.sharing;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class
	})
@EnableEurekaClient
@ComponentScan(basePackages = {"com.financial.sharing", "com.financial.sharing.config", "com.hbfk.util"})
@MapperScan({
    "com.financial.sharing.mapper",
    "com.financial.sharing.business.mapper",
    "com.financial.sharing.budgetControl.mapper",
    "com.financial.sharing.budgetPlanning.mapper",
    "com.financial.sharing.consolidationReport.mapper",
    "com.financial.sharing.dataCollection.mapper",
    "com.financial.sharing.enterpriseReport.mapper",
    "com.financial.sharing.groupControl.mapper",
    "com.financial.sharing.mysql.mapper",
    "com.financial.sharing.oracle.mapper"
})
@EnableScheduling
@EnableAsync
@EnableOperationLog(module = "hbyunFinancialSharingModule")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
public class SpringbootHbyunFinancialSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbyunFinancialSharingApplication.class, args);
		System.out.println("财务共享微服务启动成功！");
	}

}
