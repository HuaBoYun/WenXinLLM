package com.global.treasurer.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类
 *
 * @author system
 * @since 2024-01-01
 */
@Configuration
public class MyBatisPlusConfig {
    /**
     * MyBatis-Plus分页插件
     * 支持达梦数据库分页查询
     *
     * 注意：此配置与PageHelper可能存在冲突
     * 如果分页total为0，请禁用PageHelper或使用PageHelper分页
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        System.out.println("========== 开始加载MyBatis-Plus分页插件（达梦数据库）=========");

        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(500);
        paginationInterceptor.setOverflow(false);

        System.out.println("========== MyBatis-Plus分页插件（达梦数据库）已加载完成 ==========");
        return paginationInterceptor;
    }
}
