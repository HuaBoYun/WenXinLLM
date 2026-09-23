package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库初始化工具类
 * 用于执行融资管理模块的数据库脚本
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Component
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    /**
     * 应用启动后自动执行初始化(可选)
     * 默认注释掉,需要手动调用initializeFinancingManagementTables()
     */
    // @PostConstruct
    public void autoInitialize() {
        log.info("数据库自动初始化已禁用,请手动调用initializeFinancingManagementTables()方法");
    }

    /**
     * 初始化融资管理模块数据库表
     * 执行顺序: 建表脚本 -> 测试数据脚本
     */
    public void initializeFinancingManagementTables() {
        log.info("========================================");
        log.info("开始初始化融资管理模块数据库表");
        log.info("========================================");

        try {
            // 第一步: 创建表
            boolean createSuccess = executeScriptFromClasspath(
                "db/oracle/financing_management_create_tables.sql",
                "建表脚本"
            );

            if (!createSuccess) {
                log.error("建表脚本执行失败,终止初始化流程");
                return;
            }

            // 等待1秒,确保表创建完成
            Thread.sleep(1000);

            // 第二步: 插入测试数据
            boolean initDataSuccess = executeScriptFromClasspath(
                "db/oracle/financing_management_init_data.sql",
                "测试数据脚本"
            );

            if (!initDataSuccess) {
                log.error("测试数据脚本执行失败");
                return;
            }

            log.info("========================================");
            log.info("融资管理模块数据库初始化完成!");
            log.info("========================================");

            // 验证初始化结果
            verifyInitialization();

        } catch (Exception e) {
            log.error("数据库初始化失败", e);
        }
    }

    /**
     * 从classpath路径执行SQL脚本
     *
     * @param scriptPath SQL脚本路径(相对于classpath)
     * @param scriptName 脚本名称(用于日志输出)
     * @return 执行是否成功
     */
    private boolean executeScriptFromClasspath(String scriptPath, String scriptName) {
        log.info("开始执行{}: {}", scriptName, scriptPath);

        Connection connection = null;
        Statement statement = null;
        BufferedReader reader = null;

        try {
            // 1. 建立数据库连接
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();
            connection.setAutoCommit(false);

            // 2. 读取SQL脚本
            reader = new BufferedReader(
                new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(scriptPath),
                    StandardCharsets.UTF_8
                )
            );

            // 3. 解析并执行SQL语句
            String line;
            StringBuilder sqlBuilder = new StringBuilder();
            int lineNumber = 0;
            int successCount = 0;
            int errorCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // 跳过空行和注释
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }

                // 累积SQL语句
                sqlBuilder.append(line).append("\n");

                // 遇到分号表示一条SQL语句结束
                if (line.endsWith(";")) {
                    String sql = sqlBuilder.toString();

                    // 移除末尾的分号
                    sql = sql.substring(0, sql.length() - 1).trim();

                    // 跳过纯SQL脚本控制语句
                    if (!sql.isEmpty() && !sql.startsWith("REM") && !sql.startsWith("PROMPT")) {
                        try {
                            log.debug("执行SQL [行{}]: {}", lineNumber, sql.substring(0, Math.min(50, sql.length())));

                            // 判断是否是PL/SQL块(包含DECLARE或BEGIN)
                            if (sql.toUpperCase().contains("DECLARE") || sql.toUpperCase().contains("BEGIN")) {
                                // PL/SQL块不需要分号结尾
                                statement.execute(sql);
                            } else {
                                // 普通SQL语句
                                statement.execute(sql);
                            }

                            successCount++;
                        } catch (Exception e) {
                            errorCount++;
                            log.warn("SQL执行失败 [行{}]: {}", lineNumber, e.getMessage());
                            log.debug("失败的SQL: {}", sql);

                            // 某些错误可以忽略(如表已存在)
                            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                                log.info("表已存在,跳过创建");
                            } else {
                                // 其他错误也继续执行,但记录警告
                                log.warn("继续执行下一条SQL...");
                            }
                        }
                    }

                    // 重置SQL构建器
                    sqlBuilder = new StringBuilder();
                }
            }

            // 提交事务
            connection.commit();

            log.info("{}执行完成 - 成功: {}, 失败: {}", scriptName, successCount, errorCount);
            return errorCount == 0 || successCount > 0;

        } catch (Exception e) {
            log.error("{}执行异常: {}", scriptName, e.getMessage(), e);

            // 回滚事务
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception rollbackEx) {
                log.error("回滚事务失败", rollbackEx);
            }

            return false;

        } finally {
            // 关闭资源
            try {
                if (reader != null) reader.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                log.error("关闭资源失败", e);
            }
        }
    }

    /**
     * 验证数据库初始化结果
     */
    private void verifyInitialization() {
        log.info("开始验证数据库初始化结果...");

        Connection connection = null;
        java.sql.Statement statement = null;
        java.sql.ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();

            // 验证表是否存在
            String[] tableNames = {
                "TBL_FINANCIAL_INSTITUTION",
                "TBL_GUARANTEE_APPLICATION",
                "TBL_GUARANTEE_CONTRACT",
                "TBL_COLLATERAL",
                "TBL_CREDIT_APPLICATION",
                "TBL_CREDIT_CONTRACT",
                "TBL_CREDIT_LIMIT",
                "TBL_BANK_LOAN",
                "TBL_BILL_DISCOUNT",
                "TBL_BILL_ACCEPTANCE",
                "TBL_FINANCIAL_LEASE"
            };

            int tableCount = 0;
            int totalRecords = 0;

            for (String tableName : tableNames) {
                try {
                    resultSet = statement.executeQuery(
                        "SELECT COUNT(*) FROM " + tableName
                    );

                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        tableCount++;
                        totalRecords += count;
                        log.info("表 {} - 记录数: {}", tableName, count);
                    }
                } catch (Exception e) {
                    log.warn("表 {} 验证失败: {}", tableName, e.getMessage());
                } finally {
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                }
            }

            log.info("验证完成 - 成功创建表: {}/11, 总记录数: {}", tableCount, totalRecords);

            if (tableCount == 11) {
                log.info("✅ 所有表创建成功!");
            } else {
                log.warn("⚠️ 部分表创建失败,请检查日志");
            }

        } catch (Exception e) {
            log.error("验证过程发生异常", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * 删除融资管理模块的所有表(危险操作,仅用于测试环境)
     */
    public void dropFinancingManagementTables() {
        log.warn("========================================");
        log.warn("警告: 即将删除融资管理模块的所有表!");
        log.warn("========================================");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();
            connection.setAutoCommit(false);

            // 按照依赖关系逆序删除表
            String[] dropOrder = {
                "DROP TABLE TBL_FINANCIAL_LEASE CASCADE CONSTRAINTS",
                "DROP TABLE TBL_BILL_ACCEPTANCE CASCADE CONSTRAINTS",
                "DROP TABLE TBL_BILL_DISCOUNT CASCADE CONSTRAINTS",
                "DROP TABLE TBL_BANK_LOAN CASCADE CONSTRAINTS",
                "DROP TABLE TBL_CREDIT_LIMIT CASCADE CONSTRAINTS",
                "DROP TABLE TBL_CREDIT_CONTRACT CASCADE CONSTRAINTS",
                "DROP TABLE TBL_CREDIT_APPLICATION CASCADE CONSTRAINTS",
                "DROP TABLE TBL_COLLATERAL CASCADE CONSTRAINTS",
                "DROP TABLE TBL_GUARANTEE_CONTRACT CASCADE CONSTRAINTS",
                "DROP TABLE TBL_GUARANTEE_APPLICATION CASCADE CONSTRAINTS",
                "DROP TABLE TBL_FINANCIAL_INSTITUTION CASCADE CONSTRAINTS"
            };

            for (String dropSql : dropOrder) {
                try {
                    statement.execute(dropSql);
                    log.info("删除表成功: {}", dropSql);
                } catch (Exception e) {
                    log.warn("删除表失败: {} - {}", dropSql, e.getMessage());
                }
            }

            connection.commit();
            log.info("所有表删除完成");

        } catch (Exception e) {
            log.error("删除表失败", e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception rollbackEx) {
                log.error("回滚事务失败", rollbackEx);
            }
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * 测试方法 - 可通过Main方法直接执行
     */
    public static void main(String[] args) {
        log.info("融资管理模块数据库初始化工具");
        log.info("========================================");

        DatabaseInitializer initializer = new DatabaseInitializer();

        // 设置数据库连接信息(从application.yml读取)
        // 如果直接运行Main方法,需要手动设置这些值
        initializer.dbUrl = "jdbc:dm://192.0.2.200:5236/REDACTED";
        initializer.dbUsername = "REDACTED";
        initializer.dbPassword = "REDACTED";

        // 执行初始化
        initializer.initializeFinancingManagementTables();

        log.info("========================================");
        log.info("执行完成!");
    }
}
