package com.huabo.compliance;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


@SpringBootApplication
@EnableEurekaClient
@EnableOperationLog(module = "hbyunComplianceExtModule")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
@ComponentScan(basePackages = {"com.huabo.compliance","com.hbfk.util"})
public class SpringbootHbyunComplianceExtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbyunComplianceExtApplication.class, args);
		System.out.println("合规-扩展管理微服务启动成功！");
	}

}
