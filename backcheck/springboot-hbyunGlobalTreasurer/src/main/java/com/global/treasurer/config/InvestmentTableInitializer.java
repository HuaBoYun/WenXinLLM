package com.global.treasurer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * 投资管理模块 - 数据库表自动初始化器
 * 在服务启动时检查并自动创建投资管理所需的5张表
 *
 * @author 华博云开发团队
 * @since 2024-12-31
 */
//@Component  # 已禁用，避免数据库依赖导致启动失败
@Order(100) // 确保在其他初始化器之后执行
public class InvestmentTableInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(InvestmentTableInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 需要创建的表名列表
     */
    private static final String[] REQUIRED_TABLES = {
            "TBL_INVESTMENT_PRODUCT",      // 投资产品表
            "TBL_INVESTMENT_PLAN",         // 投资计划表
            "TBL_INVESTMENT_MONITORING",   // 投资监控表
            "TBL_BANK_WEALTH_INVESTMENT",  // 银行理财投资表
            "TBL_BOND_INVESTMENT"          // 债券投资表
    };

    @Override
    public void run(String... args) throws Exception {
        log.info("========== 开始检查投资管理模块数据库表结构 ==========");
        
        try {
            for (String tableName : REQUIRED_TABLES) {
                if (!tableExists(tableName)) {
                    log.info("表 {} 不存在，开始创建...", tableName);
                    createTable(tableName);
                    log.info("表 {} 创建成功", tableName);
                } else {
                    log.info("表 {} 已存在，跳过创建", tableName);
                }
            }
            log.info("========== 投资管理模块数据库表检查完成 ==========");
        } catch (Exception e) {
            log.error("投资管理模块数据库表初始化失败", e);
            // 不抛出异常，避免影响应用启动
        }
    }

    /**
     * 检查表是否存在
     */
    private boolean tableExists(String tableName) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            // 达梦数据库表名大写
            ResultSet rs = metaData.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"});
            return rs.next();
        } catch (Exception e) {
            log.warn("检查表 {} 是否存在时出错: {}", tableName, e.getMessage());
            return false;
        }
    }

    /**
     * 根据表名创建对应的表
     */
    private void createTable(String tableName) {
        switch (tableName) {
            case "TBL_INVESTMENT_PRODUCT":
                createInvestmentProductTable();
                break;
            case "TBL_INVESTMENT_PLAN":
                createInvestmentPlanTable();
                break;
            case "TBL_INVESTMENT_MONITORING":
                createInvestmentMonitoringTable();
                break;
            case "TBL_BANK_WEALTH_INVESTMENT":
                createBankWealthInvestmentTable();
                break;
            case "TBL_BOND_INVESTMENT":
                createBondInvestmentTable();
                break;
            default:
                log.warn("未知的表名: {}", tableName);
        }
    }

    /**
     * 创建投资产品表
     */
    private void createInvestmentProductTable() {
        String sql = "CREATE TABLE TBL_INVESTMENT_PRODUCT (" +
                "PRODUCT_ID BIGINT NOT NULL, " +
                "PRODUCT_CODE VARCHAR(50) NOT NULL, " +
                "PRODUCT_NAME VARCHAR(200) NOT NULL, " +
                "PRODUCT_TYPE VARCHAR(50) NOT NULL, " +
                "ISSUER VARCHAR(200), " +
                "RISK_LEVEL VARCHAR(20) NOT NULL, " +
                "EXPECTED_RETURN_RATE DECIMAL(10,4), " +
                "MIN_INVESTMENT_AMOUNT DECIMAL(18,2), " +
                "MAX_INVESTMENT_AMOUNT DECIMAL(18,2), " +
                "INVESTMENT_TERM INT, " +
                "NET_VALUE DECIMAL(10,4) DEFAULT 1.0000, " +
                "LAUNCH_DATE DATE, " +
                "MATURITY_DATE DATE, " +
                "SUBSCRIPTION_START_DATE DATE, " +
                "SUBSCRIPTION_END_DATE DATE, " +
                "REDEMPTION_ALLOWED INT DEFAULT 1, " +
                "PRODUCT_STATUS VARCHAR(20) DEFAULT 'ACTIVE', " +
                "PRODUCT_DESCRIPTION VARCHAR(2000), " +
                "SUSPEND_REASON VARCHAR(500), " +
                "CURRENCY_CODE VARCHAR(10) DEFAULT 'CNY', " +
                "COMPANY_ID BIGINT, " +
                "COMPANY_NAME VARCHAR(200), " +
                "DELETE_FLAG INT DEFAULT 0, " +
                "CREATED_BY BIGINT, " +
                "CREATED_BY_NAME VARCHAR(100), " +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATED_BY BIGINT, " +
                "UPDATED_BY_NAME VARCHAR(100), " +
                "UPDATED_TIME TIMESTAMP, " +
                "REMARK VARCHAR(500), " +
                "PRIMARY KEY (PRODUCT_ID)" +
                ")";
        jdbcTemplate.execute(sql);
        
        // 创建索引
        executeIgnoreError("CREATE UNIQUE INDEX IDX_PRODUCT_CODE ON TBL_INVESTMENT_PRODUCT(PRODUCT_CODE)");
        executeIgnoreError("CREATE INDEX IDX_PRODUCT_TYPE ON TBL_INVESTMENT_PRODUCT(PRODUCT_TYPE)");
        executeIgnoreError("CREATE INDEX IDX_PRODUCT_STATUS ON TBL_INVESTMENT_PRODUCT(PRODUCT_STATUS)");
        executeIgnoreError("CREATE INDEX IDX_PRODUCT_COMPANY ON TBL_INVESTMENT_PRODUCT(COMPANY_ID)");
    }

    /**
     * 创建投资计划表
     */
    private void createInvestmentPlanTable() {
        String sql = "CREATE TABLE TBL_INVESTMENT_PLAN (" +
                "PLAN_ID BIGINT NOT NULL, " +
                "PLAN_NO VARCHAR(50) NOT NULL, " +
                "PLAN_NAME VARCHAR(200) NOT NULL, " +
                "INVESTMENT_TYPE VARCHAR(50) NOT NULL, " +
                "RISK_LEVEL VARCHAR(20) NOT NULL, " +
                "PLAN_AMOUNT DECIMAL(18,2) NOT NULL, " +
                "INVESTED_AMOUNT DECIMAL(18,2) DEFAULT 0, " +
                "EXPECTED_RETURN_RATE DECIMAL(10,4), " +
                "ACTUAL_RETURN_RATE DECIMAL(10,4), " +
                "PLAN_START_DATE DATE NOT NULL, " +
                "PLAN_END_DATE DATE NOT NULL, " +
                "PLAN_STATUS VARCHAR(20) DEFAULT 'DRAFT', " +
                "PLAN_DESCRIPTION VARCHAR(2000), " +
                "APPROVAL_COMMENTS VARCHAR(500), " +
                "APPROVED_BY BIGINT, " +
                "APPROVED_BY_NAME VARCHAR(100), " +
                "APPROVAL_TIME TIMESTAMP, " +
                "REJECTION_REASON VARCHAR(500), " +
                "COMPLETION_NOTES VARCHAR(500), " +
                "CANCEL_REASON VARCHAR(500), " +
                "CURRENCY_CODE VARCHAR(10) DEFAULT 'CNY', " +
                "COMPANY_ID BIGINT, " +
                "COMPANY_NAME VARCHAR(200), " +
                "DELETE_FLAG INT DEFAULT 0, " +
                "CREATED_BY BIGINT, " +
                "CREATED_BY_NAME VARCHAR(100), " +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATED_BY BIGINT, " +
                "UPDATED_BY_NAME VARCHAR(100), " +
                "UPDATED_TIME TIMESTAMP, " +
                "REMARK VARCHAR(500), " +
                "PRIMARY KEY (PLAN_ID)" +
                ")";
        jdbcTemplate.execute(sql);

        executeIgnoreError("CREATE UNIQUE INDEX IDX_PLAN_NO ON TBL_INVESTMENT_PLAN(PLAN_NO)");
        executeIgnoreError("CREATE INDEX IDX_PLAN_STATUS ON TBL_INVESTMENT_PLAN(PLAN_STATUS)");
        executeIgnoreError("CREATE INDEX IDX_PLAN_TYPE ON TBL_INVESTMENT_PLAN(INVESTMENT_TYPE)");
        executeIgnoreError("CREATE INDEX IDX_PLAN_COMPANY ON TBL_INVESTMENT_PLAN(COMPANY_ID)");
    }

    /**
     * 创建投资监控表
     */
    private void createInvestmentMonitoringTable() {
        String sql = "CREATE TABLE TBL_INVESTMENT_MONITORING (" +
                "MONITORING_ID BIGINT NOT NULL, " +
                "INVESTMENT_ID BIGINT NOT NULL, " +
                "INVESTMENT_TYPE VARCHAR(50), " +
                "MONITORING_DATE DATE NOT NULL, " +
                "MONITORING_TYPE VARCHAR(20) NOT NULL, " +
                "PREVIOUS_VALUE DECIMAL(18,2), " +
                "CURRENT_VALUE DECIMAL(18,2), " +
                "VALUE_CHANGE DECIMAL(18,2), " +
                "VALUE_CHANGE_RATE DECIMAL(10,4), " +
                "PERFORMANCE_SCORE INT, " +
                "RISK_SCORE INT, " +
                "ALERT_LEVEL VARCHAR(20), " +
                "ALERT_MESSAGE VARCHAR(500), " +
                "ALERT_TYPE VARCHAR(50), " +
                "ALERT_STATUS VARCHAR(20) DEFAULT 'PENDING', " +
                "MONITORING_STATUS VARCHAR(20) DEFAULT 'ACTIVE', " +
                "MONITORING_NOTES VARCHAR(1000), " +
                "COMPANY_ID BIGINT, " +
                "COMPANY_NAME VARCHAR(200), " +
                "DELETE_FLAG INT DEFAULT 0, " +
                "CREATED_BY BIGINT, " +
                "CREATED_BY_NAME VARCHAR(100), " +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATED_BY BIGINT, " +
                "UPDATED_BY_NAME VARCHAR(100), " +
                "UPDATED_TIME TIMESTAMP, " +
                "REMARK VARCHAR(500), " +
                "PRIMARY KEY (MONITORING_ID)" +
                ")";
        jdbcTemplate.execute(sql);

        executeIgnoreError("CREATE INDEX IDX_MONITORING_INVESTMENT ON TBL_INVESTMENT_MONITORING(INVESTMENT_ID)");
        executeIgnoreError("CREATE INDEX IDX_MONITORING_DATE ON TBL_INVESTMENT_MONITORING(MONITORING_DATE)");
        executeIgnoreError("CREATE INDEX IDX_MONITORING_ALERT ON TBL_INVESTMENT_MONITORING(ALERT_LEVEL, ALERT_STATUS)");
    }

    /**
     * 创建银行理财投资表
     */
    private void createBankWealthInvestmentTable() {
        String sql = "CREATE TABLE TBL_BANK_WEALTH_INVESTMENT (" +
                "INVESTMENT_ID BIGINT NOT NULL, " +
                "INVESTMENT_NO VARCHAR(50) NOT NULL, " +
                "PRODUCT_ID BIGINT, " +
                "PRODUCT_CODE VARCHAR(50), " +
                "PRODUCT_NAME VARCHAR(200) NOT NULL, " +
                "BANK_NAME VARCHAR(200) NOT NULL, " +
                "INVESTMENT_AMOUNT DECIMAL(18,2) NOT NULL, " +
                "EXPECTED_RETURN_RATE DECIMAL(10,4), " +
                "ACTUAL_RETURN_RATE DECIMAL(10,4), " +
                "EXPECTED_RETURN DECIMAL(18,2), " +
                "ACTUAL_RETURN DECIMAL(18,2), " +
                "INVESTMENT_TERM INT NOT NULL, " +
                "VALUE_DATE DATE NOT NULL, " +
                "MATURITY_DATE DATE NOT NULL, " +
                "REDEMPTION_DATE DATE, " +
                "RISK_LEVEL VARCHAR(20) NOT NULL, " +
                "INVESTMENT_STATUS VARCHAR(20) DEFAULT 'PENDING', " +
                "CURRENCY_CODE VARCHAR(10) DEFAULT 'CNY', " +
                "PLAN_ID BIGINT, " +
                "PLAN_NO VARCHAR(50), " +
                "COMPANY_ID BIGINT, " +
                "COMPANY_NAME VARCHAR(200), " +
                "DELETE_FLAG INT DEFAULT 0, " +
                "CREATED_BY BIGINT, " +
                "CREATED_BY_NAME VARCHAR(100), " +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATED_BY BIGINT, " +
                "UPDATED_BY_NAME VARCHAR(100), " +
                "UPDATED_TIME TIMESTAMP, " +
                "REMARK VARCHAR(500), " +
                "PRIMARY KEY (INVESTMENT_ID)" +
                ")";
        jdbcTemplate.execute(sql);

        executeIgnoreError("CREATE UNIQUE INDEX IDX_BANK_INVESTMENT_NO ON TBL_BANK_WEALTH_INVESTMENT(INVESTMENT_NO)");
        executeIgnoreError("CREATE INDEX IDX_BANK_INVESTMENT_STATUS ON TBL_BANK_WEALTH_INVESTMENT(INVESTMENT_STATUS)");
        executeIgnoreError("CREATE INDEX IDX_BANK_INVESTMENT_MATURITY ON TBL_BANK_WEALTH_INVESTMENT(MATURITY_DATE)");
        executeIgnoreError("CREATE INDEX IDX_BANK_INVESTMENT_COMPANY ON TBL_BANK_WEALTH_INVESTMENT(COMPANY_ID)");
    }

    /**
     * 创建债券投资表
     */
    private void createBondInvestmentTable() {
        String sql = "CREATE TABLE TBL_BOND_INVESTMENT (" +
                "INVESTMENT_ID BIGINT NOT NULL, " +
                "INVESTMENT_NO VARCHAR(50) NOT NULL, " +
                "BOND_CODE VARCHAR(50) NOT NULL, " +
                "BOND_NAME VARCHAR(200) NOT NULL, " +
                "BOND_TYPE VARCHAR(50) NOT NULL, " +
                "ISSUER VARCHAR(200) NOT NULL, " +
                "CREDIT_RATING VARCHAR(20), " +
                "FACE_VALUE DECIMAL(18,2) NOT NULL, " +
                "PURCHASE_PRICE DECIMAL(18,4) NOT NULL, " +
                "PURCHASE_QUANTITY INT NOT NULL, " +
                "INVESTMENT_AMOUNT DECIMAL(18,2) NOT NULL, " +
                "COUPON_RATE DECIMAL(10,4) NOT NULL, " +
                "YIELD_TO_MATURITY DECIMAL(10,4), " +
                "COUPON_FREQUENCY VARCHAR(20), " +
                "PURCHASE_DATE DATE NOT NULL, " +
                "MATURITY_DATE DATE NOT NULL, " +
                "NEXT_COUPON_DATE DATE, " +
                "ACCUMULATED_INTEREST DECIMAL(18,2) DEFAULT 0, " +
                "CURRENT_MARKET_VALUE DECIMAL(18,2), " +
                "UNREALIZED_PNL DECIMAL(18,2) DEFAULT 0, " +
                "RISK_LEVEL VARCHAR(20) NOT NULL, " +
                "INVESTMENT_STATUS VARCHAR(20) DEFAULT 'HOLDING', " +
                "CURRENCY_CODE VARCHAR(10) DEFAULT 'CNY', " +
                "PLAN_ID BIGINT, " +
                "PLAN_NO VARCHAR(50), " +
                "COMPANY_ID BIGINT, " +
                "COMPANY_NAME VARCHAR(200), " +
                "DELETE_FLAG INT DEFAULT 0, " +
                "CREATED_BY BIGINT, " +
                "CREATED_BY_NAME VARCHAR(100), " +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATED_BY BIGINT, " +
                "UPDATED_BY_NAME VARCHAR(100), " +
                "UPDATED_TIME TIMESTAMP, " +
                "REMARK VARCHAR(500), " +
                "PRIMARY KEY (INVESTMENT_ID)" +
                ")";
        jdbcTemplate.execute(sql);

        executeIgnoreError("CREATE UNIQUE INDEX IDX_BOND_INVESTMENT_NO ON TBL_BOND_INVESTMENT(INVESTMENT_NO)");
        executeIgnoreError("CREATE INDEX IDX_BOND_CODE ON TBL_BOND_INVESTMENT(BOND_CODE)");
        executeIgnoreError("CREATE INDEX IDX_BOND_TYPE ON TBL_BOND_INVESTMENT(BOND_TYPE)");
        executeIgnoreError("CREATE INDEX IDX_BOND_INVESTMENT_STATUS ON TBL_BOND_INVESTMENT(INVESTMENT_STATUS)");
        executeIgnoreError("CREATE INDEX IDX_BOND_MATURITY ON TBL_BOND_INVESTMENT(MATURITY_DATE)");
        executeIgnoreError("CREATE INDEX IDX_BOND_COMPANY ON TBL_BOND_INVESTMENT(COMPANY_ID)");
    }

    /**
     * 执行SQL，忽略错误（用于创建索引等可能已存在的对象）
     */
    private void executeIgnoreError(String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.warn("执行SQL时出错(已忽略): {}", e.getMessage());
        }
    }
}

