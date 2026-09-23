package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 融资基础参数表初始化器
 * 用于创建TBL_FINANCING_BASIC_PARAMS表
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Component
public class FinancingBasicParamsTableInitializer {

    private static final Logger log = LoggerFactory.getLogger(FinancingBasicParamsTableInitializer.class);

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
        log.info("开始初始化融资基础参数表");
        log.info("========================================");

        try {
            createTable();
            createIndexes();
            insertInitData();
            verifyTable();

            log.info("========================================");
            log.info("融资基础参数表初始化完成!");
            log.info("========================================");
        } catch (Exception e) {
            log.error("融资基础参数表初始化失败", e);
        }
    }

    /**
     * 创建表
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS TBL_FINANCING_BASIC_PARAMS (" +
                "PARAM_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "PARAM_CODE VARCHAR(50) NOT NULL," +
                "PARAM_NAME VARCHAR(200) NOT NULL," +
                "PARAM_TYPE VARCHAR(50) NOT NULL," +
                "PARAM_VALUE VARCHAR(500)," +
                "PARAM_DESC VARCHAR(1000)," +
                "STATUS VARCHAR(20) DEFAULT 'ENABLE'," +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "CREATE_USER BIGINT," +
                "UPDATE_USER BIGINT," +
                "ORG_ID BIGINT NOT NULL," +
                "REMARK VARCHAR(1000)" +
                ")";

        executeUpdate(sql, "创建TBL_FINANCING_BASIC_PARAMS表");
    }

    /**
     * 创建索引
     */
    private void createIndexes() {
        List<String> indexSqls = new ArrayList<>();

        // 唯一索引
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_FINANCING_PARAMS_CODE_ORG ON TBL_FINANCING_BASIC_PARAMS(PARAM_CODE, ORG_ID)");

        // 普通索引
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_FINANCING_PARAMS_TYPE ON TBL_FINANCING_BASIC_PARAMS(PARAM_TYPE)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_FINANCING_PARAMS_STATUS ON TBL_FINANCING_BASIC_PARAMS(STATUS)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_FINANCING_PARAMS_ORG ON TBL_FINANCING_BASIC_PARAMS(ORG_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_FINANCING_PARAMS_CREATE_TIME ON TBL_FINANCING_BASIC_PARAMS(CREATE_TIME)");

        for (String sql : indexSqls) {
            executeUpdate(sql, "创建索引");
        }
    }

    /**
     * 插入初始化数据
     */
    private void insertInitData() {
        // 先检查是否已有数据
        String checkSql = "SELECT COUNT(*) FROM TBL_FINANCING_BASIC_PARAMS";
        Integer count = executeQuery(checkSql);

        if (count != null && count > 0) {
            log.info("表中已存在{}条数据，跳过初始化数据插入", count);
            return;
        }

        // 插入测试数据
        List<String> insertSqls = new ArrayList<>();
        insertSqls.add("INSERT INTO TBL_FINANCING_BASIC_PARAMS (PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, PARAM_DESC, STATUS, CREATE_USER, ORG_ID, REMARK) VALUES " +
                "('INTEREST_RATE_BASE', '基准利率', 'RATE', '4.35', '人民银行基准利率', 'ENABLE', 1, 1, '人民银行公布的基准利率')");
        insertSqls.add("INSERT INTO TBL_FINANCING_BASIC_PARAMS (PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, PARAM_DESC, STATUS, CREATE_USER, ORG_ID, REMARK) VALUES " +
                "('MAX_LOAN_RATIO', '最大贷款比例', 'RULE', '0.7', '企业最大贷款比例', 'ENABLE', 1, 1, '企业资产最大可贷款比例')");
        insertSqls.add("INSERT INTO TBL_FINANCING_BASIC_PARAMS (PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, PARAM_DESC, STATUS, CREATE_USER, ORG_ID, REMARK) VALUES " +
                "('MIN_CREDIT_SCORE', '最低授信评分', 'RULE', '600', '企业授信最低评分要求', 'ENABLE', 1, 1, '企业授信申请最低信用评分')");
        insertSqls.add("INSERT INTO TBL_FINANCING_BASIC_PARAMS (PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, PARAM_DESC, STATUS, CREATE_USER, ORG_ID, REMARK) VALUES " +
                "('MAX_GUARANTEE_PERIOD', '最长担保期限', 'PERIOD', '120', '担保合同最长期限（月）', 'ENABLE', 1, 1, '担保合同最长有效期限')");
        insertSqls.add("INSERT INTO TBL_FINANCING_BASIC_PARAMS (PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, PARAM_DESC, STATUS, CREATE_USER, ORG_ID, REMARK) VALUES " +
                "('DEFAULT_INTEREST_RATE', '默认利率', 'RATE', '5.5', '默认贷款利率', 'ENABLE', 1, 1, '企业贷款默认利率')");

        for (String sql : insertSqls) {
            executeUpdate(sql, "插入初始化数据");
        }
    }

    /**
     * 验证表是否创建成功
     */
    private void verifyTable() {
        String sql = "SELECT COUNT(*) FROM TBL_FINANCING_BASIC_PARAMS";
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
            connection = java.sql.DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
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
            connection = java.sql.DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
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
