package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 资金归集模块数据库表初始化器
 * 启动时自动检查并创建所需的数据库表
 */
@Component
@Order(20)
public class FundConcentrationTableInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(FundConcentrationTableInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化资金归集模块数据库表...");
        try (Connection conn = dataSource.getConnection()) {
            createTableIfNotExists("TBL_CONCENTRATION_STRATEGY", getStrategyTableDDL());
            createTableIfNotExists("TBL_CONCENTRATION_PLAN", getPlanTableDDL());
            createTableIfNotExists("TBL_CONCENTRATION_EXECUTION", getExecutionTableDDL());
            createTableIfNotExists("TBL_FUND_POOL", getFundPoolTableDDL());
            createTableIfNotExists("TBL_REPORT_STATISTICS", getReportStatisticsTableDDL());
            createTableIfNotExists("TBL_REGULATORY_ANALYSIS", getRegulatoryAnalysisTableDDL());
            createTableIfNotExists("TBL_EXCEPTION_HANDLING", getExceptionHandlingTableDDL());
            log.info("资金归集模块数据库表初始化完成");
        } catch (Exception e) {
            log.error("资金归集模块数据库表初始化失败", e);
        }
    }

    private void createTableIfNotExists(String tableName, String ddl) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = ?",
                Integer.class, tableName);
            if (count == null || count == 0) {
                jdbcTemplate.execute(ddl);
                log.info("表 {} 创建成功", tableName);
            } else {
                log.debug("表 {} 已存在，跳过创建", tableName);
            }
        } catch (Exception e) {
            log.error("创建表 {} 失败: {}", tableName, e.getMessage());
        }
    }

    private String getStrategyTableDDL() {
        return "CREATE TABLE TBL_CONCENTRATION_STRATEGY (" +
            "STRATEGY_ID BIGINT PRIMARY KEY," +
            "STRATEGY_NAME VARCHAR(200) NOT NULL," +
            "STRATEGY_TYPE VARCHAR(50)," +
            "FREQUENCY VARCHAR(50)," +
            "EXECUTION_TIME VARCHAR(20)," +
            "STRATEGY_STATUS VARCHAR(20) DEFAULT 'INACTIVE'," +
            "STRATEGY_DESCRIPTION VARCHAR(500)," +
            "TARGET_ACCOUNT_ID VARCHAR(100)," +
            "TARGET_ACCOUNT_NAME VARCHAR(200)," +
            "CONCENTRATION_CONDITION VARCHAR(500)," +
            "MIN_CONCENTRATION_AMOUNT DECIMAL(20,2)," +
            "RETAIN_BALANCE DECIMAL(20,2)," +
            "CONCENTRATION_RATIO DECIMAL(5,2)," +
            "DAILY_LIMIT DECIMAL(20,2)," +
            "SINGLE_LIMIT DECIMAL(20,2)," +
            "RISK_LEVEL VARCHAR(20)," +
            "CREATOR_ID VARCHAR(100)," +
            "CREATOR_NAME VARCHAR(100)," +
            "CREATE_TIME TIMESTAMP," +
            "UPDATE_TIME TIMESTAMP," +
            "DEL_FLAG CHAR(1) DEFAULT '0'" +
            ")";
    }

    private String getPlanTableDDL() {
        return "CREATE TABLE TBL_CONCENTRATION_PLAN (" +
            "PLAN_ID BIGINT PRIMARY KEY," +
            "PLAN_NAME VARCHAR(200) NOT NULL," +
            "STRATEGY_ID BIGINT," +
            "STRATEGY_NAME VARCHAR(200)," +
            "PLAN_AMOUNT DECIMAL(20,2)," +
            "ACTUAL_AMOUNT DECIMAL(20,2)," +
            "EXECUTION_PROGRESS INT DEFAULT 0," +
            "PLAN_STATUS VARCHAR(20) DEFAULT 'PENDING'," +
            "EXECUTION_TIME TIMESTAMP," +
            "CREATOR_ID VARCHAR(100)," +
            "CREATOR_NAME VARCHAR(100)," +
            "PLAN_DESCRIPTION VARCHAR(500)," +
            "CREATE_TIME TIMESTAMP," +
            "UPDATE_TIME TIMESTAMP," +
            "REMARK VARCHAR(500)," +
            "DEL_FLAG CHAR(1) DEFAULT '0'" +
            ")";
    }

    private String getExecutionTableDDL() {
        return "CREATE TABLE TBL_CONCENTRATION_EXECUTION (" +
            "EXECUTION_ID BIGINT PRIMARY KEY," +
            "EXECUTION_NO VARCHAR(100)," +
            "PLAN_ID BIGINT," +
            "STRATEGY_ID BIGINT," +
            "EXECUTION_STATUS VARCHAR(20)," +
            "EXECUTION_AMOUNT DECIMAL(20,2)," +
            "START_TIME TIMESTAMP," +
            "END_TIME TIMESTAMP," +
            "ERROR_MESSAGE VARCHAR(1000)," +
            "CREATE_TIME TIMESTAMP," +
            "UPDATE_TIME TIMESTAMP," +
            "DEL_FLAG CHAR(1) DEFAULT '0'" +
            ")";
    }

    private String getFundPoolTableDDL() {
        return "CREATE TABLE TBL_FUND_POOL (" +
            "POOL_ID BIGINT PRIMARY KEY," +
            "POOL_NAME VARCHAR(200) NOT NULL," +
            "POOL_TYPE VARCHAR(50)," +
            "POOL_STATUS VARCHAR(20) DEFAULT 'ACTIVE'," +
            "TOTAL_BALANCE DECIMAL(20,2)," +
            "AVAILABLE_BALANCE DECIMAL(20,2)," +
            "CURRENCY VARCHAR(10) DEFAULT 'CNY'," +
            "CREATOR_ID VARCHAR(100)," +
            "CREATOR_NAME VARCHAR(100)," +
            "CREATE_TIME TIMESTAMP," +
            "UPDATE_TIME TIMESTAMP," +
            "REMARK VARCHAR(500)," +
            "DEL_FLAG CHAR(1) DEFAULT '0'" +
            ")";
    }

    private String getReportStatisticsTableDDL() {
        return "CREATE TABLE TBL_REPORT_STATISTICS (" +
            "REPORT_ID VARCHAR(64) PRIMARY KEY," +
            "REPORT_NO VARCHAR(100)," +
            "REPORT_NAME VARCHAR(200) NOT NULL," +
            "REPORT_TYPE VARCHAR(50)," +
            "REPORT_PERIOD VARCHAR(20)," +
            "POOL_ID VARCHAR(64)," +
            "COMPANY_ID VARCHAR(64)," +
            "COMPANY_NAME VARCHAR(200)," +
            "START_DATE DATE," +
            "END_DATE DATE," +
            "TOTAL_COLLECTION DECIMAL(20,2)," +
            "TOTAL_ALLOCATION DECIMAL(20,2)," +
            "TOTAL_LOAN DECIMAL(20,2)," +
            "TOTAL_REPAYMENT DECIMAL(20,2)," +
            "TOTAL_INTEREST DECIMAL(20,2)," +
            "COLLECTION_COUNT INT," +
            "ALLOCATION_COUNT INT," +
            "LOAN_COUNT INT," +
            "SUCCESS_RATE DECIMAL(5,2)," +
            "AVERAGE_AMOUNT DECIMAL(20,2)," +
            "REPORT_STATUS VARCHAR(20) DEFAULT 'DRAFT'," +
            "GENERATE_TIME TIMESTAMP," +
            "GENERATE_BY VARCHAR(100)," +
            "REPORT_DATA CLOB," +
            "REMARK VARCHAR(500)," +
            "CREATE_BY VARCHAR(100)," +
            "CREATE_TIME TIMESTAMP," +
            "UPDATE_BY VARCHAR(100)," +
            "UPDATE_TIME TIMESTAMP," +
            "DEL_FLAG CHAR(1) DEFAULT '0'" +
            ")";
    }

    private String getRegulatoryAnalysisTableDDL() {
        return "CREATE TABLE TBL_REGULATORY_ANALYSIS (" +
            "ANALYSIS_ID VARCHAR(64) PRIMARY KEY," +
            "ANALYSIS_NO VARCHAR(100)," +
            "ANALYSIS_NAME VARCHAR(200) NOT NULL," +
            "ANALYSIS_TYPE VARCHAR(50)," +
            "REGULATOR VARCHAR(100)," +
            "ANALYSIS_STATUS VARCHAR(20) DEFAULT 'DRAFT'," +
            "COMPLIANCE_STATUS VARCHAR(20)," +
            "RISK_LEVEL VARCHAR(20)," +
            "ANALYSIS_DATE DATE," +
            "START_DATE DATE," +
            "END_DATE DATE," +
            "ANALYSIS_RESULT CLOB," +
            "RISK_DESCRIPTION VARCHAR(1000)," +
            "RECOMMENDATION VARCHAR(1000)," +
            "REVIEWER VARCHAR(100)," +
            "REVIEW_TIME TIMESTAMP," +
            "REVIEW_RESULT VARCHAR(20)," +
            "REVIEW_REMARK VARCHAR(500)," +
            "REMARK VARCHAR(500)," +
            "CREATE_BY VARCHAR(100)," +
            "CREATE_TIME TIMESTAMP," +
            "UPDATE_BY VARCHAR(100)," +
            "UPDATE_TIME TIMESTAMP," +
            "DEL_FLAG CHAR(1) DEFAULT '0'" +
            ")";
    }

    private String getExceptionHandlingTableDDL() {
        return "CREATE TABLE TBL_EXCEPTION_HANDLING (" +
            "EXCEPTION_ID VARCHAR(64) PRIMARY KEY," +
            "EXCEPTION_NO VARCHAR(100)," +
            "EXCEPTION_TITLE VARCHAR(200) NOT NULL," +
            "EXCEPTION_TYPE VARCHAR(50)," +
            "SEVERITY VARCHAR(20)," +
            "EXCEPTION_STATUS VARCHAR(20) DEFAULT 'PENDING'," +
            "DESCRIPTION VARCHAR(2000)," +
            "SOLUTION VARCHAR(2000)," +
            "ASSIGNEE VARCHAR(100)," +
            "OCCUR_TIME TIMESTAMP," +
            "EXPECTED_RESOLVE_TIME TIMESTAMP," +
            "ACTUAL_RESOLVE_TIME TIMESTAMP," +
            "HANDLE_METHOD VARCHAR(200)," +
            "HANDLE_RESULT VARCHAR(500)," +
            "CLOSE_REMARK VARCHAR(500)," +
            "CREATOR_ID VARCHAR(100)," +
            "CREATOR_NAME VARCHAR(100)," +
            "CREATE_TIME TIMESTAMP," +
            "UPDATE_TIME TIMESTAMP," +
            "DEL_FLAG CHAR(1) DEFAULT '0'" +
            ")";
    }
}

