package com.huabo.fxgl.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
public class RestTemplateConfig {

    @Resource
    private CjbdiConfig cjbdiConfig;

    /**
     * 通用 RestTemplate，使用默认超时（connectTimeout=10s, readTimeout=30s）
     * 供其他模块使用，不影响 CJBDI 专用配置
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(cjbdiConfig.getConnectTimeout());
        factory.setReadTimeout(cjbdiConfig.getReadTimeout());
        return new RestTemplate(factory);
    }

    /**
     * CJBDI 专用 RestTemplate
     * connectTimeout=15s，readTimeout=150s
     * 涉诉/失信/限高等外部接口响应较慢，需要更长的读取超时
     */
    @Bean
    @Qualifier("cjbdiRestTemplate")
    public RestTemplate cjbdiRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(150000);
        return new RestTemplate(factory);
    }
}
