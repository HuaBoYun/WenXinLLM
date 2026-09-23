package com.global.treasurer.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 全球司库服务扫描配置
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
@Configuration
@ComponentScan(
    basePackages = {"com.global.treasurer", "com.hbfk.util", "com.hbfk.sdk.billing"},
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.hbfk\\.util\\.mibiao\\..*")
)
@MapperScan({"com.global.treasurer.mapper", "com.hbfk.sdk.billing.mapper"})
public class TreasurerScanConfig {
}