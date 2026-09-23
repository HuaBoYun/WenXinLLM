package com.huabo.audit.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author GJ.C
 * @CLASS_NAME: SpringContextConfig
 * @PACKAGE_NAME: com.huabo.audit.util
 * @date 2025/4/8 19:17.
 * @version: V1.0
 * @description:
 */
@Configuration
public class SpringContextConfig {
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}