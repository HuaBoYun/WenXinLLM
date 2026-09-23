package com.financial.sharing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson配置类
 * 解决JavaScript Long精度丢失问题和LocalDateTime序列化问题
 * 将所有Long类型序列化为字符串，添加JavaTimeModule支持LocalDateTime
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule simpleModule = new SimpleModule();

        // 将Long类型序列化为字符串，解决JavaScript精度丢失问题
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 添加JavaTimeModule以支持LocalDateTime等Java 8时间类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        objectMapper.registerModule(simpleModule);
        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }
}