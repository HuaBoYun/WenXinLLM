package com.global.treasurer.util;

import com.alibaba.druid.pool.DruidDataSource;
import dm.jdbc.driver.DmDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * 达梦数据库连接诊断工具
 * 用于测试和诊断数据库连接问题
 */
public class DatabaseConnectionDiagnostics {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionDiagnostics.class);

    // 数据库配置
    private static final String DB_HOST = "192.0.2.200";
    private static final int DB_PORT = 5236;
    private static final String DB_NAME = "REDACTED";
    private static final String DB_USER = "REDACTED";
    private static final String DB_PASSWORD = "REDACTED";

    /**
     * 测试不同的JDBC URL格式
     */
    public static void testConnectionUrls() {
        log.info("========================================");
        log.info("开始测试达梦数据库连接URL格式");
        log.info("========================================");

        // 测试1: 标准URL格式
        testUrl("标准格式", "jdbc:dm://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME);

        // 测试2: 带字符集参数
        testUrl("带字符集", "jdbc:dm://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
                "?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");

        // 测试3: 简化URL格式
        testUrl("简化格式", "jdbc:dm://" + DB_HOST + ":" + DB_PORT);

        // 测试4: 带连接参数
        testUrl("带连接参数", "jdbc:dm://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
                "?zeroDateTimeBehavior=convertToNull" +
                "&useUnicode=true" +
                "&characterEncoding=utf-8" +
                "&serverTimezone=Asia/Shanghai" +
                "&connectTimeout=5000" +
                "&socketTimeout=30000");

        log.info("========================================");
        log.info("URL格式测试完成");
        log.info("========================================");
    }

    /**
     * 测试单个URL
     */
    private static void testUrl(String testName, String url) {
        log.info("");
        log.info("测试: {}", testName);
        log.info("URL: {}", url);

        Connection conn = null;
        long startTime = System.currentTimeMillis();

        try {
            // 加载驱动
            Class.forName("dm.jdbc.driver.DmDriver");
            log.info("✓ 驱动加载成功");

            // 尝试连接
            Properties props = new Properties();
            props.setProperty("user", DB_USER);
            props.setProperty("password", DB_PASSWORD);
            props.setProperty("connectTimeout", "5000");

            conn = DriverManager.getConnection(url, props);
            long connectTime = System.currentTimeMillis() - startTime;

            log.info("✓ 连接成功! 耗时: {}ms", connectTime);

            // 获取数据库信息
            DatabaseMetaData metaData = conn.getMetaData();
            log.info("  数据库产品: {}", metaData.getDatabaseProductName());
            log.info("  数据库版本: {}", metaData.getDatabaseProductVersion());
            log.info("  驱动名称: {}", metaData.getDriverName());
            log.info("  驱动版本: {}", metaData.getDriverVersion());
            log.info("  JDBC版本: {}", metaData.getJDBCMajorVersion() + "." + metaData.getJDBCMinorVersion());

            // 测试简单查询
            Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL");
            if (rs.next()) {
                log.info("✓ 查询测试成功: SELECT 1 FROM DUAL = {}", rs.getInt(1));
            }
            stmt.close();

            log.info("★★★ {} 测试通过 ★★★", testName);

        } catch (ClassNotFoundException e) {
            log.error("✗ 驱动加载失败: {}", e.getMessage());
        } catch (Exception e) {
            long failTime = System.currentTimeMillis() - startTime;
            log.error("✗ 连接失败 (耗时{}ms): {}", failTime, e.getMessage());
            log.error("  错误类型: {}", e.getClass().getName());

            // 打印详细错误堆栈
            if (e.getCause() != null) {
                log.error("  原因: {}", e.getCause().getMessage());
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    log.info("✓ 连接已关闭");
                } catch (Exception e) {
                    log.error("✗ 关闭连接失败: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 测试Druid连接池配置
     */
    public static void testDruidConfiguration() {
        log.info("");
        log.info("========================================");
        log.info("测试Druid连接池配置");
        log.info("========================================");

        DruidDataSource dataSource = new DruidDataSource();

        // 基本配置
        dataSource.setUrl("jdbc:dm://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
                "?zeroDateTimeBehavior=convertToNull" +
                "&useUnicode=true" +
                "&characterEncoding=utf-8" +
                "&serverTimezone=Asia/Shanghai");
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setDriverClassName("dm.jdbc.driver.DmDriver");

        // 连接池配置
        dataSource.setInitialSize(1);  // 减少初始连接数
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(5);    // 减少最大连接数用于测试
        dataSource.setMaxWait(30000);  // 增加等待时间到30秒

        // 测试配置
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(true);  // 借出时测试
        dataSource.setTestOnReturn(false);

        // 连接超时配置
        Properties connectProps = new Properties();
        connectProps.setProperty("connectTimeout", "5000");
        connectProps.setProperty("socketTimeout", "30000");
        dataSource.setConnectProperties(connectProps);

        log.info("Druid配置:");
        log.info("  URL: {}", dataSource.getUrl());
        log.info("  InitialSize: {}", dataSource.getInitialSize());
        log.info("  MinIdle: {}", dataSource.getMinIdle());
        log.info("  MaxActive: {}", dataSource.getMaxActive());
        log.info("  MaxWait: {}ms", dataSource.getMaxWait());
        log.info("  TestOnBorrow: {}", dataSource.isTestOnBorrow());
        log.info("");

        try {
            log.info("正在初始化连接池...");
            long startTime = System.currentTimeMillis();

            // 初始化连接池
            dataSource.init();

            long initTime = System.currentTimeMillis() - startTime;
            log.info("✓ 连接池初始化成功! 耗时: {}ms", initTime);
            log.info("  活跃连接数: {}", dataSource.getActiveCount());
            log.info("  空闲连接数: {}", dataSource.getPoolingCount());

            // 获取一个连接测试
            log.info("");
            log.info("正在获取连接...");
            startTime = System.currentTimeMillis();
            Connection conn = dataSource.getConnection();
            long getTime = System.currentTimeMillis() - startTime;
            log.info("✓ 获取连接成功! 耗时: {}ms", getTime);

            // 测试查询
            Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT SYSDATE FROM DUAL");
            if (rs.next()) {
                log.info("✓ 查询成功: 当前数据库时间 = {}", rs.getString(1));
            }
            stmt.close();

            conn.close();

            log.info("");
            log.info("★★★ Druid连接池测试通过 ★★★");

        } catch (Exception e) {
            log.error("✗ Druid连接池测试失败");
            log.error("  错误: {}", e.getMessage());
            log.error("  类型: {}", e.getClass().getName());

            if (e.getCause() != null) {
                log.error("  原因: {}", e.getCause().getMessage());
            }

            // 打印完整堆栈用于诊断
            log.error("详细错误信息:", e);
        } finally {
            try {
                dataSource.close();
                log.info("✓ 连接池已关闭");
            } catch (Exception e) {
                log.error("✗ 关闭连接池失败: {}", e.getMessage());
            }
        }

        log.info("========================================");
    }

    /**
     * 主测试方法
     */
    public static void main(String[] args) {
        log.info("开始达梦数据库连接诊断...");

        // 测试不同的URL格式
        testConnectionUrls();

        // 测试Druid配置
        testDruidConfiguration();

        log.info("");
        log.info("所有测试完成!");
    }
}
