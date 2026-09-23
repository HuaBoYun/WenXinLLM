package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 风险监控表初始化工具
 * 自动检查并创建 TBL_RISK_MONITORING 表
 *
 * @author 华博云开发团队
 * @since 2025-03-24
 */
@Component
public class RiskMonitoringTableInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RiskMonitoringTableInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            if (tableExists()) {
                log.info("TBL_RISK_MONITORING 表已存在，跳过初始化");
                return;
            }
            log.info("开始创建 TBL_RISK_MONITORING 表...");
            createTable();
            log.info("TBL_RISK_MONITORING 表创建成功");
        } catch (Exception e) {
            log.warn("TBL_RISK_MONITORING 表初始化失败（可忽略）: {}", e.getMessage());
        }
    }

    private boolean tableExists() {
        try {
            // 兼容 MySQL 和 DM 数据库
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TBL_RISK_MONITORING WHERE 1=0", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createTable() {
        // MySQL 建表语句
        String sql = "CREATE TABLE IF NOT EXISTS TBL_RISK_MONITORING (" +
                "RECORD_ID BIGINT NOT NULL COMMENT '记录ID'," +
                "MONITORING_NO VARCHAR(50) COMMENT '监控编号'," +
                "RISK_TYPE_ID BIGINT COMMENT '风险类型ID'," +
                "RISK_TYPE_NAME VARCHAR(100) COMMENT '风险类型名称'," +
                "RISK_STATUS VARCHAR(20) DEFAULT 'NORMAL' COMMENT '风险状态'," +
                "CURRENT_RISK_VALUE DECIMAL(18,4) COMMENT '当前风险值'," +
                "THRESHOLD_VALUE DECIMAL(18,4) COMMENT '阈值'," +
                "WARNING_VALUE DECIMAL(18,4) COMMENT '预警值'," +
                "CRITICAL_VALUE DECIMAL(18,4) COMMENT '临界值'," +
                "ALERT_TRIGGERED TINYINT(1) DEFAULT 0 COMMENT '是否触发警报'," +
                "ALERT_MESSAGE VARCHAR(500) COMMENT '警报消息'," +
                "MONITORING_DATE DATE COMMENT '监控日期'," +
                "NEXT_MONITORING_DATE DATE COMMENT '下次监控日期'," +
                "ACTION_TAKEN TEXT COMMENT '处理措施'," +
                "HANDLED_BY BIGINT COMMENT '处理人ID'," +
                "HANDLED_AT DATETIME COMMENT '处理时间'," +
                "REMARK VARCHAR(500) COMMENT '备注'," +
                "ORG_ID BIGINT COMMENT '组织ID'," +
                "CREATE_TIME DATETIME COMMENT '创建时间'," +
                "UPDATE_TIME DATETIME COMMENT '更新时间'," +
                "CREATE_BY VARCHAR(50) COMMENT '创建人'," +
                "UPDATE_BY VARCHAR(50) COMMENT '更新人'," +
                "PRIMARY KEY (RECORD_ID)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险监控表'";
        jdbcTemplate.execute(sql);
    }
}

