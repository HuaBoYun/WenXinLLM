package com.hbfk.sdk.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* @Description: 配置类
* @Author: 61
*/
@ConfigurationProperties(prefix = "hbfk.sdk.log.thread")
@Data
public class LogThreadProperties {

    /**
     * 核心线程池大小
     */
    private int CorePoolSize = 10;
    /**
     *  最大线程
     */
    private int MaxPoolSize = 20;
    /**
     * 活跃时间
     */
    private int KeepAliveSeconds = 20;
    /**
     * 池队列容量
     */
    private int QueueCapacity = 60;
    /**
     * 线程名称前缀
     */
    private String ThreadNamePrefix = "log-thread-";

}
