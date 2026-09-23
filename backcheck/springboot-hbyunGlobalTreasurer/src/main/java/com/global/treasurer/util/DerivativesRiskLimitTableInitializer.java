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

@Component
@Order(30)
public class DerivativesRiskLimitTableInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DerivativesRiskLimitTableInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            createTableIfNotExists();
            insertDefaultDataIfEmpty();
            createStressTestTableIfNotExists();
            insertDefaultStressTestDataIfEmpty();
        } catch (Exception e) {
            log.error("衍生品风险限额表初始化失败", e);
        }
    }

    private void createTableIfNotExists() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_DERIVATIVES_RISK_LIMIT'",
                Integer.class);
            if (count == null || count == 0) {
                jdbcTemplate.execute(
                    "CREATE TABLE TBL_DERIVATIVES_RISK_LIMIT (" +
                    "LIMIT_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                    "LIMIT_TYPE VARCHAR(100) NOT NULL," +
                    "LIMIT_AMOUNT DECIMAL(20,2)," +
                    "USED_AMOUNT DECIMAL(20,2) DEFAULT 0," +
                    "AVAILABLE_AMOUNT DECIMAL(20,2)," +
                    "USAGE_RATIO DECIMAL(10,4) DEFAULT 0," +
                    "CURRENCY VARCHAR(10) DEFAULT 'CNY'," +
                    "STATUS VARCHAR(20) DEFAULT 'NORMAL'," +
                    "ORG_ID BIGINT," +
                    "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "DEL_FLAG CHAR(1) DEFAULT '0'" +
                    ")"
                );
                log.info("TBL_DERIVATIVES_RISK_LIMIT 表创建成功");
            }
        } catch (Exception e) {
            log.error("创建 TBL_DERIVATIVES_RISK_LIMIT 表失败: {}", e.getMessage());
        }
    }

    private void insertDefaultDataIfEmpty() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_DERIVATIVES_RISK_LIMIT WHERE DEL_FLAG = '0'",
                Integer.class);
            if (count == null || count == 0) {
                String[][] defaults = {
                    {"总风险限额",    "50000000", "32500000", "17500000", "0.65", "CNY", "NORMAL"},
                    {"远期交易限额",  "20000000", "15000000",  "5000000", "0.75", "CNY", "WARNING"},
                    {"期货交易限额",  "15000000",  "8000000",  "7000000", "0.53", "CNY", "NORMAL"},
                    {"期权交易限额",  "10000000",  "6000000",  "4000000", "0.60", "CNY", "NORMAL"},
                    {"互换交易限额",   "5000000",  "3500000",  "1500000", "0.70", "CNY", "NORMAL"}
                };
                for (String[] row : defaults) {
                    jdbcTemplate.update(
                        "INSERT INTO TBL_DERIVATIVES_RISK_LIMIT " +
                        "(LIMIT_TYPE,LIMIT_AMOUNT,USED_AMOUNT,AVAILABLE_AMOUNT,USAGE_RATIO,CURRENCY,STATUS,DEL_FLAG) " +
                        "VALUES (?,?,?,?,?,?,?,'0')",
                        row[0], new java.math.BigDecimal(row[1]), new java.math.BigDecimal(row[2]),
                        new java.math.BigDecimal(row[3]), new java.math.BigDecimal(row[4]), row[5], row[6]
                    );
                }
                log.info("TBL_DERIVATIVES_RISK_LIMIT 默认数据插入完成");
            }
        } catch (Exception e) {
            log.error("插入默认限额数据失败: {}", e.getMessage());
        }
    }

    private void createStressTestTableIfNotExists() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_DERIVATIVES_STRESS_TEST'",
                Integer.class);
            if (count == null || count == 0) {
                jdbcTemplate.execute(
                    "CREATE TABLE TBL_DERIVATIVES_STRESS_TEST (" +
                    "TEST_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                    "SCENARIO_NAME VARCHAR(100) NOT NULL," +
                    "SCENARIO_DESC VARCHAR(500)," +
                    "PORTFOLIO_VALUE DECIMAL(20,2)," +
                    "STRESS_VALUE DECIMAL(20,2)," +
                    "LOSS DECIMAL(20,2)," +
                    "LOSS_RATIO DECIMAL(10,4)," +
                    "SEVERITY VARCHAR(20) DEFAULT 'MEDIUM'," +
                    "STATUS VARCHAR(20) DEFAULT 'NORMAL'," +
                    "ORG_ID BIGINT," +
                    "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "DEL_FLAG CHAR(1) DEFAULT '0'" +
                    ")"
                );
                log.info("TBL_DERIVATIVES_STRESS_TEST 表创建成功");
            }
        } catch (Exception e) {
            log.error("创建 TBL_DERIVATIVES_STRESS_TEST 表失败: {}", e.getMessage());
        }
    }

    private void insertDefaultStressTestDataIfEmpty() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_DERIVATIVES_STRESS_TEST WHERE DEL_FLAG = '0'",
                Integer.class);
            if (count == null || count == 0) {
                Object[][] defaults = {
                    {"汇率剧烈波动", "美元/人民币汇率波动超过10%",  32500000, 28500000, -4000000, -0.1231, "HIGH",     "TRIGGERED"},
                    {"利率大幅上升", "SHIBOR上升200BP",             32500000, 30800000, -1700000, -0.0523, "MEDIUM",   "NORMAL"},
                    {"股市崩盘",     "沪深300指数下跌20%",          32500000, 29200000, -3300000, -0.1015, "HIGH",     "WARNING"},
                    {"商品价格暴跌", "黄金和原油价格下跌15%",       32500000, 31200000, -1300000, -0.0400, "MEDIUM",   "NORMAL"},
                    {"流动性危机",   "市场流动性大幅下降,买卖价差扩大300%", 32500000, 27500000, -5000000, -0.1538, "CRITICAL", "ALERT"}
                };
                for (Object[] row : defaults) {
                    jdbcTemplate.update(
                        "INSERT INTO TBL_DERIVATIVES_STRESS_TEST " +
                        "(SCENARIO_NAME,SCENARIO_DESC,PORTFOLIO_VALUE,STRESS_VALUE,LOSS,LOSS_RATIO,SEVERITY,STATUS,DEL_FLAG) " +
                        "VALUES (?,?,?,?,?,?,?,?,'0')",
                        row[0], row[1],
                        new java.math.BigDecimal(row[2].toString()),
                        new java.math.BigDecimal(row[3].toString()),
                        new java.math.BigDecimal(row[4].toString()),
                        new java.math.BigDecimal(row[5].toString()),
                        row[6], row[7]
                    );
                }
                log.info("TBL_DERIVATIVES_STRESS_TEST 默认数据插入完成");
            }
        } catch (Exception e) {
            log.error("插入默认压力测试数据失败: {}", e.getMessage());
        }
    }
}

