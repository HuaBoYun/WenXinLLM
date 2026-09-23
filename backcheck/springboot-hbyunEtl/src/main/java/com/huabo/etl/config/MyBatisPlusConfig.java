package com.huabo.etl.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName : MyBatisPlusConfig
 * @Description : MyBatisPlus配置
 * @Author : zhibo.cao
 * @Date: 2022-07-13
 * <p>
 * 自动分页: PaginationInnerInterceptor
 * 多租户: TenantLineInnerInterceptor
 * 动态表名: DynamicTableNameInnerInterceptor
 * 乐观锁: OptimisticLockerInnerInterceptor
 * sql 性能规范: IllegalSQLInnerInterceptor
 * 防止全表更新与删除: BlockAttackInnerInterceptor
 */
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
@MapperScan("com.huabo.etl.**.mapper")
public class MyBatisPlusConfig {

    private static final Long UNLIMITED = -1L;

    /**
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor()); //分页插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor()); //乐观锁插件
        return interceptor;
    }

    /**
     * 分页插件，自动识别数据库类型
     *
     * @return PaginationInnerInterceptor
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(UNLIMITED);
        // 分页合理化
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁插件
     *
     * @return OptimisticLockerInnerInterceptor
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }


}
