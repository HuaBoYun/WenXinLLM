package com.huabo.bigmodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 华博云大模型服务启动类
 * 提供OpenAI协议兼容的API接口
 *
 * @author HuaboCloud
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BigModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigModelApplication.class, args);
        System.out.println("========================================");
        System.out.println("  华博云大模型服务启动成功!");
        System.out.println("  OpenAI兼容API: /v1/chat/completions");
        System.out.println("  Swagger文档: /bigmodel/swagger-ui.html");
        System.out.println("========================================");
    }
}

