package com.huabo.fxgl.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 * 解决分页查询total为0的问题
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * MyBatis-Plus拦截器配置
     * 
     * @return MybatisPlusInterceptor
     */
   /* @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 🔥 添加分页插件 - 这是解决total为0问题的关键
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        
        // 🔥 添加乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        
        return interceptor;
    }*/

    /**
     * 分页插件配置
     * 支持达梦数据库分页
     * 
     * @return PaginationInnerInterceptor
     */
    /*public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        
        // 🔥 设置数据库类型为达梦数据库
        // 达梦数据库使用Oracle兼容模式
        paginationInnerInterceptor.setDbType(DbType.DM);
        
        // 🔥 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(1000L);
        
        // 🔥 分页合理化：当页码超过总页数时，自动跳转到第一页
        paginationInnerInterceptor.setOverflow(true);
        
        // 🔥 当 count 为 0 时，是否进行 count 查询
        // 设置为 true 确保总是执行 count 查询
        paginationInnerInterceptor.setOptimizeJoin(true);
        
        return paginationInnerInterceptor;
    }*/

    /**
     * 乐观锁插件配置
     * 
     * @return OptimisticLockerInnerInterceptor
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }
}
