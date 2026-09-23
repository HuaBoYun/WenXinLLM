package com.hbfk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * 华博云API网关服务启动类
 * <p>基于Spring Cloud Gateway实现统一路由转发、请求过滤、跨域处理等功能</p>
 * <p>注册到Eureka服务注册中心，作为所有微服务的统一入口</p>
 *
 * @author hbyun
 */
@SpringBootApplication
@EnableEurekaClient
public class SpringbootHbfkGatewayServiceApplication {

	/**
	 * 网关服务启动入口
	 *
	 * @param args 启动参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbfkGatewayServiceApplication.class, args);
		System.out.println("GeWay微服务启动成功！");
	}

//	@Bean
//    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//        return builder.routes().build();
//    }
}
