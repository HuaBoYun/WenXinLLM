package com.huabo.cybermonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * springboot主入口程序
 * @author kangjx
 * @createTime 2022/7/11
 */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {"com.huabo.cybermonitor", "com.hbfk.util", "com.hbfk.sdk.billing"})
@MapperScan({"com.huabo.cybermonitor.mapper", "com.hbfk.sdk.billing.mapper"})
public class CyberMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyberMonitorApplication.class , args);
        System.out.println("国资穿透微服务启动成功！");
    }

}
