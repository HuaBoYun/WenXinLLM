package com.financial.sharing.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 数据库初始化器
 * 在应用启动时自动创建必要的表
 */
@Slf4j
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 显式加载达梦数据库驱动并注册到 DriverManager
            Class<?> driverClass = Class.forName("dm.jdbc.driver.DmDriver");
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(driver);
            log.info("达梦数据库驱动加载并注册成功");

            // 检查数据源是否为空
            if (dataSource == null) {
                log.warn("数据源未配置，跳过数据库初始化");
                return;
            }

            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {

                log.info("开始初始化数据库表...");

                // 创建收入批量结账表
                createRevenueBatchSettlementTable(stmt);

                log.info("数据库表初始化完成！");
            }
        } catch (ClassNotFoundException e) {
            log.error("数据库驱动类未找到：{}", e.getMessage());
            log.error("请确保达梦数据库JDBC驱动(DmJdbcDriver18.jar)已正确配置");
            log.error("驱动类名：dm.jdbc.driver.DmDriver");
        } catch (Exception e) {
            log.warn("数据库初始化失败，这在开发环境中是正常的", e);
        }
    }

    /**
     * 创建收入批量结账表 (达梦数据库语法)
     */
    private void createRevenueBatchSettlementTable(Statement stmt) {
        try {
            // 检查表是否存在
            String checkTableSql = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'T_REVENUE_BATCH_SETTLEMENT'";
            java.sql.ResultSet rs = stmt.executeQuery(checkTableSql);
            if (rs.next() && rs.getInt(1) > 0) {
                log.info("T_REVENUE_BATCH_SETTLEMENT 表已存在");
                rs.close();
                // 检查是否有数据，如果没有则插入测试数据
                checkAndInsertTestData(stmt);
                return;
            }
            rs.close();

            // 创建收入批量结账表
            String createTableSql = "CREATE TABLE T_REVENUE_BATCH_SETTLEMENT (" +
                    "SETTLEMENT_ID BIGINT NOT NULL, " +
                    "ORG_CODE VARCHAR(50), " +
                    "ORG_NAME VARCHAR(200), " +
                    "PERIOD_SCHEME VARCHAR(50), " +
                    "ACCOUNTING_PERIOD VARCHAR(20), " +
                    "SETTLEMENT_STATUS INT DEFAULT 0, " +
                    "SETTLEMENT_USER_ID BIGINT, " +
                    "SETTLEMENT_USER_NAME VARCHAR(100), " +
                    "SETTLEMENT_TIME DATETIME, " +
                    "CANCEL_USER_ID BIGINT, " +
                    "CANCEL_USER_NAME VARCHAR(100), " +
                    "CANCEL_TIME DATETIME, " +
                    "BOOK_ID BIGINT, " +
                    "TENANT_ID BIGINT DEFAULT 1, " +
                    "VERSION INT DEFAULT 1, " +
                    "IS_DELETED INT DEFAULT 0, " +
                    "CREATE_TIME DATETIME DEFAULT SYSDATE, " +
                    "UPDATE_TIME DATETIME DEFAULT SYSDATE, " +
                    "CONSTRAINT PK_REVENUE_BATCH_SETTLEMENT PRIMARY KEY (SETTLEMENT_ID))";
            stmt.execute(createTableSql);
            log.info("T_REVENUE_BATCH_SETTLEMENT 表创建成功");

            // 添加表注释
            stmt.execute("COMMENT ON TABLE T_REVENUE_BATCH_SETTLEMENT IS '收入批量结账表'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.SETTLEMENT_ID IS '结账ID'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.ORG_CODE IS '组织编码'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.ORG_NAME IS '组织名称'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.PERIOD_SCHEME IS '会计期间方案'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.ACCOUNTING_PERIOD IS '会计期间'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.SETTLEMENT_STATUS IS '结账状态(0未结账1已结账)'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.SETTLEMENT_USER_ID IS '结账人ID'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.SETTLEMENT_USER_NAME IS '结账人姓名'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.SETTLEMENT_TIME IS '结账时间'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.CANCEL_USER_ID IS '取消结账人ID'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.CANCEL_USER_NAME IS '取消结账人姓名'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.CANCEL_TIME IS '取消结账时间'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.BOOK_ID IS '账簿ID'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.TENANT_ID IS '租户ID'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.VERSION IS '版本号'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.IS_DELETED IS '删除标识(0否1是)'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.CREATE_TIME IS '创建时间'");
            stmt.execute("COMMENT ON COLUMN T_REVENUE_BATCH_SETTLEMENT.UPDATE_TIME IS '更新时间'");

            // 插入测试数据
            insertBatchSettlementTestData(stmt);

        } catch (Exception e) {
            log.warn("创建收入批量结账表失败: {}", e.getMessage());
        }
    }

    /**
     * 检查表中是否有数据，如果没有则插入测试数据
     */
    private void checkAndInsertTestData(Statement stmt) {
        try {
            String countSql = "SELECT COUNT(*) FROM T_REVENUE_BATCH_SETTLEMENT";
            java.sql.ResultSet rs = stmt.executeQuery(countSql);
            if (rs.next() && rs.getInt(1) == 0) {
                log.info("T_REVENUE_BATCH_SETTLEMENT 表为空，插入测试数据...");
                insertBatchSettlementTestData(stmt);
            } else {
                log.info("T_REVENUE_BATCH_SETTLEMENT 表已有 {} 条数据", rs.getInt(1));
            }
            rs.close();
        } catch (Exception e) {
            log.warn("检查测试数据失败: {}", e.getMessage());
        }
    }

    /**
     * 插入收入批量结账测试数据
     */
    private void insertBatchSettlementTestData(Statement stmt) {
        try {
            String insertSql1 = "INSERT INTO T_REVENUE_BATCH_SETTLEMENT " +
                    "(SETTLEMENT_ID, ORG_CODE, ORG_NAME, PERIOD_SCHEME, ACCOUNTING_PERIOD, " +
                    "SETTLEMENT_STATUS, BOOK_ID, TENANT_ID, VERSION, IS_DELETED, CREATE_TIME, UPDATE_TIME) " +
                    "VALUES (1001, 'ORG001', '华博云科技有限公司', '标准会计期间', '2025-01', " +
                    "0, 1, 1, 1, 0, SYSDATE, SYSDATE)";
            stmt.execute(insertSql1);

            String insertSql2 = "INSERT INTO T_REVENUE_BATCH_SETTLEMENT " +
                    "(SETTLEMENT_ID, ORG_CODE, ORG_NAME, PERIOD_SCHEME, ACCOUNTING_PERIOD, " +
                    "SETTLEMENT_STATUS, SETTLEMENT_USER_ID, SETTLEMENT_USER_NAME, SETTLEMENT_TIME, " +
                    "BOOK_ID, TENANT_ID, VERSION, IS_DELETED, CREATE_TIME, UPDATE_TIME) " +
                    "VALUES (1002, 'ORG001', '华博云科技有限公司', '标准会计期间', '2025-02', " +
                    "1, 1001, '张三', SYSDATE, 1, 1, 1, 0, SYSDATE, SYSDATE)";
            stmt.execute(insertSql2);

            String insertSql3 = "INSERT INTO T_REVENUE_BATCH_SETTLEMENT " +
                    "(SETTLEMENT_ID, ORG_CODE, ORG_NAME, PERIOD_SCHEME, ACCOUNTING_PERIOD, " +
                    "SETTLEMENT_STATUS, BOOK_ID, TENANT_ID, VERSION, IS_DELETED, CREATE_TIME, UPDATE_TIME) " +
                    "VALUES (1003, 'ORG002', '子公司A', '标准会计期间', '2025-01', " +
                    "0, 1, 1, 1, 0, SYSDATE, SYSDATE)";
            stmt.execute(insertSql3);

            log.info("收入批量结账测试数据插入成功");
        } catch (Exception e) {
            log.warn("插入收入批量结账测试数据失败: {}", e.getMessage());
        }
    }
}

