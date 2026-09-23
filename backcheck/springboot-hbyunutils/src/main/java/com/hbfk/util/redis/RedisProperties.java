package com.hbfk.util.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {

	private String host;
    private int port;
    private String password;
}
