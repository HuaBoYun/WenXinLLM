package com.hbfk.sdk.log.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @Author: 61
 */
@Configuration
public class FeignConfig {
    public FeignConfig() {
        System.out.println("FeignConfig initialized!");
    }
    @Bean
    public ObjectMapper feignObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;  // 可以选择 BASIC, HEADERS, FULL
    }
}
