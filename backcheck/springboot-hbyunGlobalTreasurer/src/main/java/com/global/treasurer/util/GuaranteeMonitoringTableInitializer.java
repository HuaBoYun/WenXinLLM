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
 * 担保监控表初始化器
 * 用于创建TBL_GUARANTEE_MONITORING表
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Component
public class GuaranteeMonitoringTableInitializer {

    private static final Logger log = LoggerFactory.getLogger(GuaranteeMonitoringTableInitializer.class);

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
        log.info("开始初始化担保监控表");
        log.info("========================================");

        try {
            createTable();
            createIndexes();
            insertInitData();
            verifyTable();

            log.info("========================================");
            log.info("担保监控表初始化完成!");
            log.info("========================================");
        } catch (Exception e) {
            log.error("担保监控表初始化失败", e);
        }
    }

    /**
     * 创建表
     */
    private void createTable() {
        String sql = "CREATE TABLE TBL_GUARANTEE_MONITORING (" +
                "ALERT_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "ALERT_NO VARCHAR(50) NOT NULL," +
                "ALERT_TYPE VARCHAR(50) NOT NULL," +
                "ALERT_LEVEL VARCHAR(20) NOT NULL," +
                "ALERT_MESSAGE VARCHAR(1000)," +
                "RELATED_GUARANTEE_ID BIGINT," +
                "ALERT_STATUS VARCHAR(20) DEFAULT 'PENDING'," +
                "ALERT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "HANDLER_ID BIGINT," +
                "HANDLER_NAME VARCHAR(100)," +
                "HANDLE_DATE TIMESTAMP," +
                "HANDLE_OPINION VARCHAR(1000)," +
                "COMPANY_ID BIGINT," +
                "COMPANY_NAME VARCHAR(200)," +
                "DELETE_FLAG INTEGER DEFAULT 0," +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "REMARK VARCHAR(1000)" +
                ")";

        executeUpdate(sql, "创建TBL_GUARANTEE_MONITORING表");
    }

    /**
     * 创建索引
     */
    private void createIndexes() {
        List<String> indexSqls = new ArrayList<>();

        // 唯一索引
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_ALERT_NO ON TBL_GUARANTEE_MONITORING(ALERT_NO)");

        // 普通索引
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_ALERT_TYPE ON TBL_GUARANTEE_MONITORING(ALERT_TYPE)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_ALERT_LEVEL ON TBL_GUARANTEE_MONITORING(ALERT_LEVEL)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_ALERT_STATUS ON TBL_GUARANTEE_MONITORING(ALERT_STATUS)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_ALERT_DATE ON TBL_GUARANTEE_MONITORING(ALERT_DATE)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_RELATED_GUARANTEE ON TBL_GUARANTEE_MONITORING(RELATED_GUARANTEE_ID)");

        for (String sql : indexSqls) {
            executeUpdate(sql, "创建索引");
        }
    }

    /**
     * 插入初始化数据
     */
    private void insertInitData() {
        // 先检查是否已有数据
        String checkSql = "SELECT COUNT(*) FROM TBL_GUARANTEE_MONITORING WHERE DELETE_FLAG = 0";
        Integer count = executeQuery(checkSql);

        if (count != null && count > 0) {
            log.info("表中已存在{}条数据，跳过初始化数据插入", count);
            return;
        }

        // 插入测试数据
        List<String> insertSqls = new ArrayList<>();
        insertSqls.add("INSERT INTO TBL_GUARANTEE_MONITORING (ALERT_NO, ALERT_TYPE, ALERT_LEVEL, ALERT_MESSAGE, RELATED_GUARANTEE_ID, ALERT_STATUS, ALERT_DATE, COMPANY_NAME, REMARK) VALUES " +
                "('ALERT2026001', 'EXPIRY_WARNING', 'HIGH', '担保合同GC2026001将于30天后到期，请及时处理', 1, 'PENDING', '2026-01-10', '华博集团有限公司', '合同到期预警')");
        insertSqls.add("INSERT INTO TBL_GUARANTEE_MONITORING (ALERT_NO, ALERT_TYPE, ALERT_LEVEL, ALERT_MESSAGE, RELATED_GUARANTEE_ID, ALERT_STATUS, ALERT_DATE, COMPANY_NAME, REMARK) VALUES " +
                "('ALERT2026002', 'RISK_INCREASE', 'MEDIUM', '担保物CL2026003评估价值下降超过10%，请关注', 2, 'PENDING', '2026-01-12', '子公司B', '资产价值下降预警')");
        insertSqls.add("INSERT INTO TBL_GUARANTEE_MONITORING (ALERT_NO, ALERT_TYPE, ALERT_LEVEL, ALERT_MESSAGE, RELATED_GUARANTEE_ID, ALERT_STATUS, ALERT_DATE, COMPANY_NAME, REMARK) VALUES " +
                "('ALERT2026003', 'COMPLIANCE_WARNING', 'LOW', '担保合同GC2026003年检即将到期，请提前准备', 3, 'HANDLED', '2026-01-08', '华博集团有限公司', '合规提醒')");

        for (String sql : insertSqls) {
            executeUpdate(sql, "插入初始化数据");
        }
    }

    /**
     * 验证表是否创建成功
     */
    private void verifyTable() {
        String sql = "SELECT COUNT(*) FROM TBL_GUARANTEE_MONITORING WHERE DELETE_FLAG = 0";
        Integer count = executeQuery(sql);

        if (count != null) {
            log.info("✅ 表验证成功！当前表中共有{}条数据", count);
        } else {
            log.warn("⚠️ 无法获取表中的数据条数");
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

    /**
     * 执行查询操作
     */
    private Integer executeQuery(String sql) {
        Connection connection = null;
        java.sql.ResultSet resultSet = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            log.error("查询失败: {}", e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                log.error("关闭数据库连接失败", e);
            }
        }

        return null;
    }
}
