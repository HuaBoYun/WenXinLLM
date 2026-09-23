package com.huabo.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * elt微服务主程序入口
 *
 * @author zhibo.cao
 * @createTime 2022/11/29
 */
@SpringBootApplication
@EnableEurekaClient
@EnableAsync
public class EtlApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtlApplication.class, args);
    }


}
