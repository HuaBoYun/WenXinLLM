package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer //该注解表名应用为eureka服务，可以联合多个服务作为集群，对外体统服务注册以及发现功能
public class SpringcloudHbfkEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudHbfkEurekaApplication.class, args);
	}

}
