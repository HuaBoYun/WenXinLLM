package com.hbfk.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 网关跨域配置类
 * <p>配置全局CORS策略，允许所有来源、方法和请求头的跨域访问</p>
 *
 * @author hbyun
 */
@Configuration
public class CorsConfig {

    /**
     * 创建跨域过滤器
     * <p>配置允许所有来源(AllowedOriginPattern)、所有HTTP方法、所有请求头的CORS策略</p>
     *
     * @return CorsWebFilter 跨域过滤器实例
     */
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

}
