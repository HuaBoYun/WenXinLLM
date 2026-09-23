package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 票据管理模块 - 建表初始化器
 * 在应用启动时自动创建票据管理所需的12张表
 *
 * @author 华博云开发团队
 * @since 2024-12-30
 */
//@Component  # 已禁用，避免数据库依赖导致启动失败
public class BillTableInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BillTableInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String[] REQUIRED_TABLES = {
            "TBL_BILL_REGISTRATION",      // 票据登记表
            "TBL_BILL_ENDORSEMENT",        // 票据背书表
            "TBL_BILL_DISCOUNT",           // 票据贴现表
            "TBL_BILL_MATURITY",           // 票据到期表
            "TBL_BILL_POOL",               // 票据池表
            "TBL_COMMERCIAL_BILL",        // 商业汇票表
            "TBL_ELECTRONIC_BILL",        // 电子票据表
            "TBL_BILL_RISK_ASSESSMENT",    // 票据风险评估表
            "TCL_BILL_STATISTICS",         // 票据统计表
            "TBL_BANK_ACCEPTANCE",         // 银行承兑汇票表
            "TCL_LETTERS_OF_CREDIT",       // 信用证表
            "TCL_GUARANTEES"               // 保函表
    };

    @Override
    public void run(String... args) throws Exception {
        log.info("========================================");
        log.info("票据管理模块 - 检查并创建数据表");
        log.info("========================================");

        try {
            // 1. 检查表是否存在
            int existingCount = checkExistingTables();
            log.info("已存在 {} 张票据管理表", existingCount);

            if (existingCount == REQUIRED_TABLES.length) {
                log.info("所有票据管理表已存在,无需创建");
                return;
            }

            if (existingCount > 0) {
                log.warn("检测到部分表已存在 ({}/{})", existingCount, REQUIRED_TABLES.length);
            }

            // 2. 读取建表脚本
            log.info("正在读取建表脚本...");
            String script = readBillTableScript();
            if (script == null || script.isEmpty()) {
                log.error("建表脚本为空或未找到");
                return;
            }
            log.info("建表脚本读取成功 (长度: {} 字符)", script.length());

            // 3. 执行建表脚本
            log.info("正在执行建表脚本...");
            executeScript(script);

            // 4. 验证创建结果
            int finalCount = checkExistingTables();
            log.info("========================================");
            log.info("✓ 建表完成! 成功创建 {} 张票据管理表", finalCount);
            log.info("========================================");

        } catch (Exception e) {
            log.error("建表失败: {}", e.getMessage(), e);
            log.info("========================================");
            log.info("✗ 建表失败,但不影响应用启动");
            log.info("========================================");
        }
    }

    /**
     * 检查已存在的表数量
     */
    private int checkExistingTables() {
        try {
            String sql = "SELECT COUNT(*) as cnt FROM USER_TABLES " +
                         "WHERE TABLE_NAME LIKE 'TBL_BILL%' OR TABLE_NAME LIKE 'TCL_BILL%'";

            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("检查表失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 读取建表脚本
     */
    private String readBillTableScript() {
        try {
            ClassPathResource resource = new ClassPathResource("sql/bill_management_tables_dm8.sql");

            if (!resource.exists()) {
                log.error("建表脚本文件不存在: sql/bill_management_tables_dm8.sql");
                return null;
            }

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            return content.toString();
        } catch (Exception e) {
            log.error("读取建表脚本失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 执行建表脚本
     */
    private void executeScript(String script) {
        List<String> statements = parseStatements(script);

        int successCount = 0;
        int skipCount = 0;
        int errorCount = 0;

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {

            for (String sql : statements) {
                sql = sql.trim();

                // 跳过空语句和注释
                if (sql.isEmpty() || sql.startsWith("--")) {
                    continue;
                }

                try {
                    stmt.execute(sql);
                    successCount++;

                    if (successCount % 20 == 0) {
                        log.info("已执行 {} 条SQL语句...", successCount);
                    }
                } catch (Exception e) {
                    String errorMsg = e.getMessage();
                    if (errorMsg != null && (errorMsg.contains("已存在") ||
                                             errorMsg.contains("already exists") ||
                                             errorMsg.contains("name is already used"))) {
                        skipCount++;
                        log.debug("跳过已存在的对象: {}", sql.substring(0, Math.min(50, sql.length())));
                    } else {
                        errorCount++;
                        log.error("SQL执行失败: {}", sql.substring(0, Math.min(100, sql.length())));
                        log.error("  错误: {}", errorMsg);
                    }
                }
            }

            log.info("执行完成 - 成功: {}, 跳过: {}, 失败: {}", successCount, skipCount, errorCount);

        } catch (Exception e) {
            log.error("执行脚本失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 解析SQL语句
     */
    private List<String> parseStatements(String script) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        String[] lines = script.split("\n");
        for (String line : lines) {
            // 跳过注释行
            if (line.trim().startsWith("--")) {
                continue;
            }

            current.append(line).append("\n");

            // 如果行以分号结尾,表示一条语句结束
            if (line.trim().endsWith(";")) {
                statements.add(current.toString());
                current = new StringBuilder();
            }
        }

        // 添加最后一条语句(如果没有以分号结尾)
        if (current.length() > 0) {
            statements.add(current.toString());
        }

        return statements;
    }
}
