package com.huabo.know;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.huabo.know.mapper") //添加mapper扫描
@ComponentScan(basePackages = {"com.huabo.know","com.hbfk.util"})
public class SpringbootHbfkKnowledgeSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHbfkKnowledgeSharingApplication.class, args);
		System.out.println("知识共享微服务启动成功！！");
	}

	/**
	 * 不扫描Manifest文件
	 * @return
	 */
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
			}
		};
	}

}
