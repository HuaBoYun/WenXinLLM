package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 第三方账户管理表初始化工具
 * 自动检查并添加缺失的列,不影响其他表
 *
 * @author 华博云开发团队
 * @since 2025-12-26
 */
//@Component  // 已禁用，避免因数据源配置问题导致启动失败
public class ThirdPartyAccountTableInitializer {
    private static final Logger log = LoggerFactory.getLogger(ThirdPartyAccountTableInitializer.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 应用启动后自动执行
     */
    @PostConstruct
    public void init() {
        try {
            checkAndAddColumns();
        } catch (Exception e) {
            log.error("第三方账户管理表初始化失败", e);
        }
    }

    /**
     * 检查并添加缺失的列
     */
    private void checkAndAddColumns() {
        String tableName = "TBL_THIRD_PARTY_ACCOUNT";

        // 检查表是否存在
        Integer tableCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = ?",
            Integer.class,
            tableName
        );

        if (tableCount == null || tableCount == 0) {
            log.info("表 {} 不存在，跳过初始化", tableName);
            return;
        }

        log.info("开始检查表 {} 的列...", tableName);

        // 检查并添加 API_URL 列
        checkAndAddColumn(tableName, "API_URL", "VARCHAR(500)", "API接口地址");

        // 检查并添加 SYNC_FREQUENCY 列
        checkAndAddColumn(tableName, "SYNC_FREQUENCY", "VARCHAR(20)", "同步频率");

        // 检查并添加 ACCOUNT_SECRET 列
        checkAndAddColumn(tableName, "ACCOUNT_SECRET", "VARCHAR(500)", "账户密钥");

        log.info("表 {} 列检查完成", tableName);
    }

    /**
     * 检查并添加单个列
     */
    private void checkAndAddColumn(String tableName, String columnName, String columnType, String comment) {
        try {
            // 检查列是否存在
            Integer columnCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_COL_COMMENTS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class,
                tableName,
                columnName
            );

            if (columnCount != null && columnCount > 0) {
                log.info("列 {}.{} 已存在，跳过", tableName, columnName);
                return;
            }

            // 列不存在，添加列
            String sql = String.format("ALTER TABLE %s ADD %s %s", tableName, columnName, columnType);
            log.info("执行SQL: {}", sql);
            jdbcTemplate.execute(sql);

            // 添加注释
            String commentSql = String.format("COMMENT ON COLUMN %s.%s IS '%s'", tableName, columnName, comment);
            log.info("执行SQL: {}", commentSql);
            jdbcTemplate.execute(commentSql);

            log.info("成功添加列: {}.{}", tableName, columnName);

        } catch (Exception e) {
            // 列可能已存在或其他原因，忽略错误
            if (e.getMessage() != null && e.getMessage().contains("已存在")) {
                log.info("列 {}.{} 已存在", tableName, columnName);
            } else {
                log.warn("添加列 {}.{} 时出现异常: {}", tableName, columnName, e.getMessage());
            }
        }
    }
}
