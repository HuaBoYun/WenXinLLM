package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 授信管理相关表初始化器
 * 用于创建授信合同、授信额度、授信评估、授信监控表
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Component
public class CreditManagementTablesInitializer {

    private static final Logger log = LoggerFactory.getLogger(CreditManagementTablesInitializer.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    /**
     * 应用启动后自动执行初始化
     */
    @PostConstruct
    public void initialize() {
        log.info("========================================");
        log.info("开始初始化授信管理相关表");
        log.info("========================================");

        try {
            // 检查并创建授信合同表
            createCreditContractTable();

            // 检查并创建授信额度表
            createCreditLimitTable();

            // 检查并创建授信评估表
            createCreditAssessmentTable();

            // 检查并创建授信监控表
            createCreditMonitoringTable();

            log.info("========================================");
            log.info("授信管理相关表初始化完成!");
            log.info("========================================");
        } catch (Exception e) {
            log.error("授信管理表初始化失败", e);
        }
    }

    /**
     * 创建授信合同表
     */
    private void createCreditContractTable() {
        String sql = "CREATE TABLE TBL_CREDIT_CONTRACT (" +
                "CONTRACT_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "CONTRACT_NO VARCHAR(50)," +
                "CREDIT_APPLICATION_ID BIGINT," +
                "BANK_CODE VARCHAR(50)," +
                "BANK_NAME VARCHAR(200)," +
                "CREDIT_LIMIT DECIMAL(20,2)," +
                "CURRENCY_CODE VARCHAR(10) DEFAULT 'CNY'," +
                "CREDIT_PERIOD INTEGER," +
                "START_DATE TIMESTAMP," +
                "END_DATE TIMESTAMP," +
                "INTEREST_RATE DECIMAL(10,4)," +
                "GUARANTEE_METHOD VARCHAR(50)," +
                "CONTRACT_STATUS VARCHAR(20) DEFAULT 'DRAFT'," +
                "SIGNING_DATE TIMESTAMP," +
                "EFFECTIVE_DATE TIMESTAMP," +
                "TERMINATION_DATE TIMESTAMP," +
                "USED_AMOUNT DECIMAL(20,2) DEFAULT 0," +
                "AVAILABLE_AMOUNT DECIMAL(20,2)," +
                "CONTRACT_FILE VARCHAR(500)," +
                "SIGNED_BY BIGINT," +
                "SIGNED_BY_NAME VARCHAR(100)," +
                "SIGNED_AT TIMESTAMP," +
                "COMPANY_ID BIGINT," +
                "COMPANY_NAME VARCHAR(200)," +
                "DELETE_FLAG INTEGER DEFAULT 0," +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "REMARK VARCHAR(1000)" +
                ")";

        executeUpdate(sql, "创建TBL_CREDIT_CONTRACT表");

        // 创建索引
        List<String> indexSqls = new ArrayList<>();
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_CREDIT_CONTRACT_NO ON TBL_CREDIT_CONTRACT(CONTRACT_NO)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_CONTRACT_APPLICATION ON TBL_CREDIT_CONTRACT(CREDIT_APPLICATION_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_CONTRACT_COMPANY ON TBL_CREDIT_CONTRACT(COMPANY_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_CONTRACT_STATUS ON TBL_CREDIT_CONTRACT(CONTRACT_STATUS)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_CONTRACT_BANK ON TBL_CREDIT_CONTRACT(BANK_CODE)");

        for (String indexSql : indexSqls) {
            executeUpdate(indexSql, "创建索引");
        }
    }

    /**
     * 创建授信额度表
     */
    private void createCreditLimitTable() {
        String sql = "CREATE TABLE TBL_CREDIT_LIMIT (" +
                "LIMIT_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "LIMIT_NO VARCHAR(50)," +
                "CONTRACT_ID BIGINT," +
                "COMPANY_ID BIGINT," +
                "COMPANY_NAME VARCHAR(200)," +
                "LIMIT_TYPE VARCHAR(50)," +
                "TOTAL_LIMIT DECIMAL(20,2)," +
                "USED_LIMIT DECIMAL(20,2) DEFAULT 0," +
                "FROZEN_LIMIT DECIMAL(20,2) DEFAULT 0," +
                "AVAILABLE_LIMIT DECIMAL(20,2)," +
                "CURRENCY_CODE VARCHAR(10) DEFAULT 'CNY'," +
                "START_DATE TIMESTAMP," +
                "END_DATE TIMESTAMP," +
                "LIMIT_STATUS VARCHAR(20) DEFAULT 'NORMAL'," +
                "INTEREST_RATE DECIMAL(10,4)," +
                "GUARANTEE_METHOD VARCHAR(50)," +
                "PURPOSE VARCHAR(500)," +
                "DELETE_FLAG INTEGER DEFAULT 0," +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "REMARK VARCHAR(1000)" +
                ")";

        executeUpdate(sql, "创建TBL_CREDIT_LIMIT表");

        // 创建索引
        List<String> indexSqls = new ArrayList<>();
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_CREDIT_LIMIT_NO ON TBL_CREDIT_LIMIT(LIMIT_NO)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_LIMIT_CONTRACT ON TBL_CREDIT_LIMIT(CONTRACT_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_LIMIT_COMPANY ON TBL_CREDIT_LIMIT(COMPANY_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_LIMIT_STATUS ON TBL_CREDIT_LIMIT(LIMIT_STATUS)");

        for (String indexSql : indexSqls) {
            executeUpdate(indexSql, "创建索引");
        }
    }

    /**
     * 创建授信评估表
     */
    private void createCreditAssessmentTable() {
        String sql = "CREATE TABLE TBL_CREDIT_ASSESSMENT (" +
                "ASSESSMENT_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "ASSESSMENT_NO VARCHAR(50)," +
                "COMPANY_ID BIGINT," +
                "COMPANY_NAME VARCHAR(200)," +
                "ASSESSMENT_TYPE VARCHAR(50)," +
                "ASSESSMENT_DATE TIMESTAMP," +
                "ASSESSOR_ID BIGINT," +
                "ASSESSOR_NAME VARCHAR(100)," +
                "RISK_LEVEL VARCHAR(20)," +
                "CREDIT_RATING VARCHAR(50)," +
                "FINANCIAL_SCORE DECIMAL(5,2)," +
                "OPERATIONAL_SCORE DECIMAL(5,2)," +
                "MANAGEMENT_SCORE DECIMAL(5,2)," +
                "TOTAL_SCORE DECIMAL(5,2)," +
                "ASSESSMENT_RESULT VARCHAR(500)," +
                "CREDIT_LIMIT DECIMAL(20,2)," +
                "ASSESSMENT_STATUS VARCHAR(20) DEFAULT 'DRAFT'," +
                "APPROVAL_DATE TIMESTAMP," +
                "APPROVER_ID BIGINT," +
                "APPROVER_NAME VARCHAR(100)," +
                "COMMENTS VARCHAR(1000)," +
                "VALID_UNTIL TIMESTAMP," +
                "NEXT_ASSESSMENT_DATE TIMESTAMP," +
                "DELETE_FLAG INTEGER DEFAULT 0," +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "REMARK VARCHAR(1000)" +
                ")";

        executeUpdate(sql, "创建TBL_CREDIT_ASSESSMENT表");

        // 创建索引
        List<String> indexSqls = new ArrayList<>();
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_CREDIT_ASSESSMENT_NO ON TBL_CREDIT_ASSESSMENT(ASSESSMENT_NO)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_ASSESSMENT_COMPANY ON TBL_CREDIT_ASSESSMENT(COMPANY_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_ASSESSMENT_TYPE ON TBL_CREDIT_ASSESSMENT(ASSESSMENT_TYPE)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_ASSESSMENT_STATUS ON TBL_CREDIT_ASSESSMENT(ASSESSMENT_STATUS)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_ASSESSMENT_DATE ON TBL_CREDIT_ASSESSMENT(ASSESSMENT_DATE)");

        for (String indexSql : indexSqls) {
            executeUpdate(indexSql, "创建索引");
        }
    }

    /**
     * 创建授信监控表
     */
    private void createCreditMonitoringTable() {
        String sql = "CREATE TABLE TBL_CREDIT_MONITORING (" +
                "ALERT_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "ALERT_NO VARCHAR(50)," +
                "ALERT_TYPE VARCHAR(50)," +
                "ALERT_LEVEL VARCHAR(20)," +
                "ALERT_MESSAGE VARCHAR(1000)," +
                "RELATED_CREDIT_ID BIGINT," +
                "ALERT_STATUS VARCHAR(20) DEFAULT 'PENDING'," +
                "ALERT_DATE TIMESTAMP," +
                "HANDLER_ID BIGINT," +
                "HANDLER_NAME VARCHAR(100)," +
                "HANDLE_DATE TIMESTAMP," +
                "HANDLE_COMMENTS VARCHAR(1000)," +
                "COMPANY_ID BIGINT," +
                "COMPANY_NAME VARCHAR(200)," +
                "DELETE_FLAG INTEGER DEFAULT 0," +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "REMARK VARCHAR(1000)" +
                ")";

        executeUpdate(sql, "创建TBL_CREDIT_MONITORING表");

        // 创建索引
        List<String> indexSqls = new ArrayList<>();
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_CREDIT_MONITORING_NO ON TBL_CREDIT_MONITORING(ALERT_NO)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_MONITORING_CREDIT ON TBL_CREDIT_MONITORING(RELATED_CREDIT_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_MONITORING_COMPANY ON TBL_CREDIT_MONITORING(COMPANY_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_MONITORING_STATUS ON TBL_CREDIT_MONITORING(ALERT_STATUS)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_MONITORING_LEVEL ON TBL_CREDIT_MONITORING(ALERT_LEVEL)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_CREDIT_MONITORING_DATE ON TBL_CREDIT_MONITORING(ALERT_DATE)");

        for (String indexSql : indexSqls) {
            executeUpdate(indexSql, "创建索引");
        }
    }

    /**
     * 执行更新操作
     */
    private void executeUpdate(String sql, String operation) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            log.info("✅ {} - 成功", operation);
        } catch (Exception e) {
            log.error("❌ {} - 失败: {}", operation, e.getMessage());
            // 表已存在不算错误
            if (!e.getMessage().contains("对象已存在") && !e.getMessage().contains("already exists")) {
                log.error("SQL: {}", sql);
            }
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                log.error("关闭数据库连接失败", e);
            }
        }
    }
}
