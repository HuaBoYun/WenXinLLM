package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 合作伙伴风险评估表初始化工具
 * 
 * @author AI Assistant
 * @date 2026-03-05
 */
@Component
public class PartnerRiskAssessmentTableInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PartnerRiskAssessmentTableInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            // 检查表是否存在
            if (tableExists()) {
                log.info("TC_PARTNER_RISK_ASSESSMENT表已存在,跳过初始化");
                return;
            }

            log.info("开始创建TC_PARTNER_RISK_ASSESSMENT表...");
            createTable();
            createIndexes();
            log.info("TC_PARTNER_RISK_ASSESSMENT表创建成功");

        } catch (Exception e) {
            log.error("TC_PARTNER_RISK_ASSESSMENT表初始化失败", e);
        }
    }

    private boolean tableExists() {
        try {
            String sql = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TC_PARTNER_RISK_ASSESSMENT'";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE TC_PARTNER_RISK_ASSESSMENT (" +
                "ID VARCHAR2(32) NOT NULL, " +
                "PARTNER_ID VARCHAR2(32) NOT NULL, " +
                "ASSESSMENT_TYPE VARCHAR2(20), " +
                "RISK_LEVEL VARCHAR2(10), " +
                "RISK_SCORE NUMBER(5,2), " +
                "RISK_STATUS VARCHAR2(20), " +
                "LAST_ASSESSMENT_DATE DATE, " +
                "NEXT_ASSESSMENT_DATE DATE, " +
                "ASSESSMENT_DESCRIPTION VARCHAR2(1000), " +
                "CONTROL_MEASURES VARCHAR2(1000), " +
                "REMARKS VARCHAR2(500), " +
                "IS_ENABLED NUMBER(1) DEFAULT 1, " +
                "CREATE_TIME DATE DEFAULT SYSDATE, " +
                "UPDATE_TIME DATE DEFAULT SYSDATE, " +
                "CREATE_USER VARCHAR2(32), " +
                "UPDATE_USER VARCHAR2(32), " +
                "VERSION_NO NUMBER(10) DEFAULT 0, " +
                "CONSTRAINT PK_TC_PARTNER_RISK_ASSESSMENT PRIMARY KEY (ID)" +
                ")";
        jdbcTemplate.execute(sql);
    }

    private void createIndexes() {
        try {
            jdbcTemplate.execute("CREATE INDEX IDX_PARTNER_RISK_PARTNER_ID ON TC_PARTNER_RISK_ASSESSMENT(PARTNER_ID)");
            jdbcTemplate.execute("CREATE INDEX IDX_PARTNER_RISK_LEVEL ON TC_PARTNER_RISK_ASSESSMENT(RISK_LEVEL)");
            jdbcTemplate.execute("CREATE INDEX IDX_PARTNER_RISK_STATUS ON TC_PARTNER_RISK_ASSESSMENT(RISK_STATUS)");
            jdbcTemplate.execute("CREATE INDEX IDX_PARTNER_RISK_TYPE ON TC_PARTNER_RISK_ASSESSMENT(ASSESSMENT_TYPE)");
        } catch (Exception e) {
            log.warn("创建索引时出现警告: " + e.getMessage());
        }
    }
}

