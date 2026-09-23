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
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Order(50)
public class DerivativesReportTableInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DerivativesReportTableInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            createReportTable();
            insertDefaultReportData();
            createTemplateTable();
            insertDefaultTemplateData();
        } catch (Exception e) {
            log.error("报表表初始化失败", e);
        }
    }

    private void createReportTable() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_DERIVATIVES_REPORT'", Integer.class);
            if (count != null && count > 0) { log.info("TBL_DERIVATIVES_REPORT 表已存在，跳过创建"); return; }
            jdbcTemplate.execute(
                "CREATE TABLE TBL_DERIVATIVES_REPORT (" +
                "REPORT_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "REPORT_NAME VARCHAR(200) NOT NULL," +
                "REPORT_TYPE VARCHAR(30)," +
                "REPORT_PERIOD VARCHAR(20)," +
                "START_DATE VARCHAR(20)," +
                "END_DATE VARCHAR(20)," +
                "OUTPUT_FORMAT VARCHAR(50)," +
                "GENERATED_BY VARCHAR(50)," +
                "GENERATE_TIME VARCHAR(30)," +
                "FILE_SIZE BIGINT DEFAULT 0," +
                "FILE_PATH VARCHAR(500)," +
                "STATUS VARCHAR(20) DEFAULT 'COMPLETED'," +
                "ORG_ID BIGINT," +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "DEL_FLAG CHAR(1) DEFAULT '0'" +
                ")"
            );
            log.info("TBL_DERIVATIVES_REPORT 表创建成功");
        } catch (Exception e) {
            log.error("创建 TBL_DERIVATIVES_REPORT 表失败: {}", e.getMessage());
        }
    }

    private void insertDefaultReportData() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_DERIVATIVES_REPORT WHERE DEL_FLAG = '0'", Integer.class);
            if (count != null && count > 0) return;
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_REPORT (REPORT_NAME,REPORT_TYPE,REPORT_PERIOD,START_DATE,END_DATE,OUTPUT_FORMAT,GENERATED_BY,GENERATE_TIME,FILE_SIZE,STATUS) VALUES ('" + today + "持仓报表','POSITION','MONTHLY','2026-03-01','" + today + "','CSV','系统管理员','" + now + "',2048576,'COMPLETED')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_REPORT (REPORT_NAME,REPORT_TYPE,REPORT_PERIOD,START_DATE,END_DATE,OUTPUT_FORMAT,GENERATED_BY,GENERATE_TIME,FILE_SIZE,STATUS) VALUES ('" + today + "损益报表','PNL','MONTHLY','2026-03-01','" + today + "','CSV','系统管理员','" + now + "',1536000,'COMPLETED')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_REPORT (REPORT_NAME,REPORT_TYPE,REPORT_PERIOD,START_DATE,END_DATE,OUTPUT_FORMAT,GENERATED_BY,GENERATE_TIME,FILE_SIZE,STATUS) VALUES ('" + today + "风险报表','RISK','MONTHLY','2026-03-01','" + today + "','CSV','系统管理员','" + now + "',1024000,'COMPLETED')");
            log.info("TBL_DERIVATIVES_REPORT 默认数据插入完成");
        } catch (Exception e) {
            log.error("插入默认报表数据失败: {}", e.getMessage());
        }
    }

    private void createTemplateTable() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_DERIVATIVES_REPORT_TEMPLATE'", Integer.class);
            if (count != null && count > 0) { log.info("TBL_DERIVATIVES_REPORT_TEMPLATE 表已存在，跳过创建"); return; }
            jdbcTemplate.execute(
                "CREATE TABLE TBL_DERIVATIVES_REPORT_TEMPLATE (" +
                "TEMPLATE_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "TEMPLATE_NAME VARCHAR(200) NOT NULL," +
                "TEMPLATE_TYPE VARCHAR(30)," +
                "DESCRIPTION VARCHAR(500)," +
                "CREATED_BY VARCHAR(50)," +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "DEL_FLAG CHAR(1) DEFAULT '0'" +
                ")"
            );
            log.info("TBL_DERIVATIVES_REPORT_TEMPLATE 表创建成功");
        } catch (Exception e) {
            log.error("创建 TBL_DERIVATIVES_REPORT_TEMPLATE 表失败: {}", e.getMessage());
        }
    }

    private void insertDefaultTemplateData() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TBL_DERIVATIVES_REPORT_TEMPLATE WHERE DEL_FLAG = '0'", Integer.class);
            if (count != null && count > 0) return;
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_REPORT_TEMPLATE (TEMPLATE_NAME,TEMPLATE_TYPE,DESCRIPTION,CREATED_BY) VALUES ('标准持仓报表模板','POSITION','包含所有持仓信息的标准模板','系统管理员')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_REPORT_TEMPLATE (TEMPLATE_NAME,TEMPLATE_TYPE,DESCRIPTION,CREATED_BY) VALUES ('详细损益报表模板','PNL','包含详细损益分析的报表模板','系统管理员')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_REPORT_TEMPLATE (TEMPLATE_NAME,TEMPLATE_TYPE,DESCRIPTION,CREATED_BY) VALUES ('风险分析报表模板','RISK','包含VaR、压力测试等风险指标','系统管理员')");
            jdbcTemplate.execute("INSERT INTO TBL_DERIVATIVES_REPORT_TEMPLATE (TEMPLATE_NAME,TEMPLATE_TYPE,DESCRIPTION,CREATED_BY) VALUES ('交易汇总报表模板','TRANSACTION','汇总所有衍生品交易数据','系统管理员')");
            log.info("TBL_DERIVATIVES_REPORT_TEMPLATE 默认数据插入完成");
        } catch (Exception e) {
            log.error("插入默认模板数据失败: {}", e.getMessage());
        }
    }
}

