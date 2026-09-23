package com.hbfk.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关插件配置类
 * <p>根据配置属性条件化加载网关插件，包括SQL注入拦截和XSS注入拦截过滤器</p>
 * <p>仅当 spring.cloud.gateway.plugin.config.enable=true 时生效</p>
 *
 * @author hbyun
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "enable"}, havingValue = "true")
public class GatewayPluginConfig {
    /**
     * Gateway插件是否生效
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(GatewayPluginProperties.class)
    @ConfigurationProperties(GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX)
    public GatewayPluginProperties gatewayPluginProperties(){
        return new GatewayPluginProperties();
    }


    /**
     * sql注入拦截插件
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SqlInjectionFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "sqlInjection.enable" },havingValue = "true")
    public SqlInjectionFilter sqlInjectionFilter(@Autowired GatewayPluginProperties gatewayPluginProperties){
        SqlInjectionFilter sqlInjectionFilter = new SqlInjectionFilter(gatewayPluginProperties);
        log.debug("Load SQL Injection Filter Config Bean");
        return sqlInjectionFilter;
    }

    /**
     * xss注入拦截插件
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(XssInjectionFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "xssInjection.enable" },havingValue = "true")
    public XssInjectionFilter xssInjectionFilter(@Autowired GatewayPluginProperties gatewayPluginProperties){
        XssInjectionFilter xssInjectionFilter = new XssInjectionFilter(gatewayPluginProperties);
        log.debug("Load XSS Injection Filter Config Bean");
        return xssInjectionFilter;
    }
}
