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
@Order(40)
public class DerivativesValuationTableInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DerivativesValuationTableInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            createValuationTable();
            insertDefaultValuationData();
            createValuationHistoryTable();
            insertDefaultHistoryData();
        } catch (Exception e) {
            log.error("估值表初始化失败", e);
        }
    }

    private void createValuationTable() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_DERIVATIVES_VALUATION'",
                Integer.class);
            if (count != null && count > 0) {
                log.info("TBL_DERIVATIVES_VALUATION 表已存在，跳过创建");
                return;
            }
            jdbcTemplate.execute(
                "CREATE TABLE TBL_DERIVATIVES_VALUATION (" +
                "VALUATION_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "CONTRACT_CODE VARCHAR(50) NOT NULL," +
                "PRODUCT_TYPE VARCHAR(20)," +
                "UNDERLYING_ASSET VARCHAR(100)," +
                "NOTIONAL_AMOUNT DECIMAL(20,2)," +
                "MARKET_VALUE DECIMAL(20,2)," +
                "PRESENT_VALUE DECIMAL(20,2)," +
                "UNREALIZED_PNL DECIMAL(20,2)," +
                "VALUATION_DATE VARCHAR(20)," +
                "VALUATION_METHOD VARCHAR(50)," +
                "CURRENCY VARCHAR(10) DEFAULT 'CNY'," +
                "STATUS VARCHAR(20) DEFAULT 'ACTIVE'," +
                "ORG_ID BIGINT," +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "DEL_FLAG CHAR(1) DEFAULT '0'" +
                ")"
            );
            log.info("TBL_DERIVATIVES_VALUATION 表创建成功");
        } catch (Exception e) {
            log.error("创建 TBL_DERIVATIVES_VALUATION 表失败: {}", e.getMessage());
        }
    }

    private void insertDefaultValuationData() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_DERIVATIVES_VALUATION WHERE DEL_FLAG = '0'", Integer.class);
            if (count != null && count > 0) return;
            String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION (CONTRACT_CODE,PRODUCT_TYPE,UNDERLYING_ASSET,NOTIONAL_AMOUNT,MARKET_VALUE,PRESENT_VALUE,UNREALIZED_PNL,VALUATION_DATE,VALUATION_METHOD,CURRENCY) VALUES ('FW2026001','FORWARD','USD/CNY',1000000,9850000,9820000,-30000,'" + today + "','市场法','CNY')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION (CONTRACT_CODE,PRODUCT_TYPE,UNDERLYING_ASSET,NOTIONAL_AMOUNT,MARKET_VALUE,PRESENT_VALUE,UNREALIZED_PNL,VALUATION_DATE,VALUATION_METHOD,CURRENCY) VALUES ('OP2026001','OPTION','EUR/USD',500000,25000,24800,4800,'" + today + "','Black-Scholes','USD')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION (CONTRACT_CODE,PRODUCT_TYPE,UNDERLYING_ASSET,NOTIONAL_AMOUNT,MARKET_VALUE,PRESENT_VALUE,UNREALIZED_PNL,VALUATION_DATE,VALUATION_METHOD,CURRENCY) VALUES ('SW2026001','SWAP','LIBOR/CNY',2000000,1980000,1975000,-25000,'" + today + "','收益法','CNY')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION (CONTRACT_CODE,PRODUCT_TYPE,UNDERLYING_ASSET,NOTIONAL_AMOUNT,MARKET_VALUE,PRESENT_VALUE,UNREALIZED_PNL,VALUATION_DATE,VALUATION_METHOD,CURRENCY) VALUES ('FT2026001','FUTURES','沪深300',800000,820000,818000,18000,'" + today + "','市场法','CNY')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION (CONTRACT_CODE,PRODUCT_TYPE,UNDERLYING_ASSET,NOTIONAL_AMOUNT,MARKET_VALUE,PRESENT_VALUE,UNREALIZED_PNL,VALUATION_DATE,VALUATION_METHOD,CURRENCY) VALUES ('FW2026002','FORWARD','GBP/CNY',600000,5910000,5900000,-10000,'" + today + "','市场法','CNY')");
            log.info("TBL_DERIVATIVES_VALUATION 默认数据插入完成");
        } catch (Exception e) {
            log.error("插入默认估值数据失败: {}", e.getMessage());
        }
    }

    private void createValuationHistoryTable() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_DERIVATIVES_VALUATION_HISTORY'",
                Integer.class);
            if (count != null && count > 0) {
                log.info("TBL_DERIVATIVES_VALUATION_HISTORY 表已存在，跳过创建");
                return;
            }
            jdbcTemplate.execute(
                "CREATE TABLE TBL_DERIVATIVES_VALUATION_HISTORY (" +
                "HISTORY_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "VALUATION_DATE VARCHAR(20)," +
                "PRODUCT_TYPE VARCHAR(20)," +
                "TOTAL_VALUE DECIMAL(20,2)," +
                "DAILY_CHANGE DECIMAL(20,2)," +
                "DAILY_CHANGE_RATE DECIMAL(10,4)," +
                "REMARK VARCHAR(200)," +
                "ORG_ID BIGINT," +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "DEL_FLAG CHAR(1) DEFAULT '0'" +
                ")"
            );
            log.info("TBL_DERIVATIVES_VALUATION_HISTORY 表创建成功");
        } catch (Exception e) {
            log.error("创建 TBL_DERIVATIVES_VALUATION_HISTORY 表失败: {}", e.getMessage());
        }
    }

    private void insertDefaultHistoryData() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_DERIVATIVES_VALUATION_HISTORY WHERE DEL_FLAG = '0'", Integer.class);
            if (count != null && count > 0) return;
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION_HISTORY (VALUATION_DATE,PRODUCT_TYPE,TOTAL_VALUE,DAILY_CHANGE,DAILY_CHANGE_RATE,REMARK) VALUES ('2026-03-23','全部',28637800,-42200,-0.0015,'正常估值')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION_HISTORY (VALUATION_DATE,PRODUCT_TYPE,TOTAL_VALUE,DAILY_CHANGE,DAILY_CHANGE_RATE,REMARK) VALUES ('2026-03-22','全部',28680000,35000,0.0012,'正常估值')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION_HISTORY (VALUATION_DATE,PRODUCT_TYPE,TOTAL_VALUE,DAILY_CHANGE,DAILY_CHANGE_RATE,REMARK) VALUES ('2026-03-21','全部',28645000,-18500,-0.0006,'市场波动')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION_HISTORY (VALUATION_DATE,PRODUCT_TYPE,TOTAL_VALUE,DAILY_CHANGE,DAILY_CHANGE_RATE,REMARK) VALUES ('2026-03-20','全部',28663500,52000,0.0018,'正常估值')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_VALUATION_HISTORY (VALUATION_DATE,PRODUCT_TYPE,TOTAL_VALUE,DAILY_CHANGE,DAILY_CHANGE_RATE,REMARK) VALUES ('2026-03-19','全部',28611500,-9800,-0.0003,'正常估值')");
            log.info("TBL_DERIVATIVES_VALUATION_HISTORY 默认数据插入完成");
        } catch (Exception e) {
            log.error("插入默认估值历史数据失败: {}", e.getMessage());
        }
    }
}

