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
 * 债券类别表初始化器
 * 用于创建TBL_BOND_CATEGORY表
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Component
public class BondCategoryTableInitializer {

    private static final Logger log = LoggerFactory.getLogger(BondCategoryTableInitializer.class);

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
        log.info("开始初始化债券类别表");
        log.info("========================================");

        try {
            createTable();
            createIndexes();
            insertInitData();
            verifyTable();

            log.info("========================================");
            log.info("债券类别表初始化完成!");
            log.info("========================================");
        } catch (Exception e) {
            log.error("债券类别表初始化失败", e);
        }
    }

    /**
     * 创建表
     */
    private void createTable() {
        String sql = "CREATE TABLE TBL_BOND_CATEGORY (" +
                "CATEGORY_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "CATEGORY_NAME VARCHAR(200) NOT NULL," +
                "CATEGORY_CODE VARCHAR(50) NOT NULL," +
                "PARENT_ID BIGINT," +
                "CATEGORY_LEVEL INTEGER," +
                "SORT_ORDER INTEGER DEFAULT 0," +
                "STATUS VARCHAR(20) DEFAULT 'ENABLE'," +
                "DELETE_FLAG INTEGER DEFAULT 0," +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "REMARK VARCHAR(1000)" +
                ")";

        executeUpdate(sql, "创建TBL_BOND_CATEGORY表");
    }

    /**
     * 创建索引
     */
    private void createIndexes() {
        List<String> indexSqls = new ArrayList<>();

        // 唯一索引
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_BOND_CATEGORY_CODE ON TBL_BOND_CATEGORY(CATEGORY_CODE)");

        // 普通索引
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_BOND_CATEGORY_PARENT ON TBL_BOND_CATEGORY(PARENT_ID)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_BOND_CATEGORY_LEVEL ON TBL_BOND_CATEGORY(CATEGORY_LEVEL)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_BOND_CATEGORY_STATUS ON TBL_BOND_CATEGORY(STATUS)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_BOND_CATEGORY_SORT ON TBL_BOND_CATEGORY(SORT_ORDER)");

        for (String sql : indexSqls) {
            executeUpdate(sql, "创建索引");
        }
    }

    /**
     * 插入初始化数据
     */
    private void insertInitData() {
        // 先检查是否已有数据
        String checkSql = "SELECT COUNT(*) FROM TBL_BOND_CATEGORY WHERE DELETE_FLAG = 0";
        Integer count = executeQuery(checkSql);

        if (count != null && count > 0) {
            log.info("表中已存在{}条数据，跳过初始化数据插入", count);
            return;
        }

        // 插入测试数据
        List<String> insertSqls = new ArrayList<>();
        insertSqls.add("INSERT INTO TBL_BOND_CATEGORY (CATEGORY_NAME, CATEGORY_CODE, PARENT_ID, CATEGORY_LEVEL, SORT_ORDER, STATUS, REMARK) VALUES " +
                "('国债', 'GOV_BOND', NULL, 1, 1, 'ENABLE', '政府发行的债券')");
        insertSqls.add("INSERT INTO TBL_BOND_CATEGORY (CATEGORY_NAME, CATEGORY_CODE, PARENT_ID, CATEGORY_LEVEL, SORT_ORDER, STATUS, REMARK) VALUES " +
                "('企业债', 'CORP_BOND', NULL, 1, 2, 'ENABLE', '企业发行的债券')");
        insertSqls.add("INSERT INTO TBL_BOND_CATEGORY (CATEGORY_NAME, CATEGORY_CODE, PARENT_ID, CATEGORY_LEVEL, SORT_ORDER, STATUS, REMARK) VALUES " +
                "('金融债', 'FIN_BOND', NULL, 1, 3, 'ENABLE', '金融机构发行的债券')");
        insertSqls.add("INSERT INTO TBL_BOND_CATEGORY (CATEGORY_NAME, CATEGORY_CODE, PARENT_ID, CATEGORY_LEVEL, SORT_ORDER, STATUS, REMARK) VALUES " +
                "('央行票据', 'CENTRAL_BANK_BILL', NULL, 1, 4, 'ENABLE', '中央银行发行的债券')");
        insertSqls.add("INSERT INTO TBL_BOND_CATEGORY (CATEGORY_NAME, CATEGORY_CODE, PARENT_ID, CATEGORY_LEVEL, SORT_ORDER, STATUS, REMARK) VALUES " +
                "('地方债', " +
                "('LOCAL_BOND', NULL, 1, 5, 'ENABLE', '地方政府发行的债券')");

        for (String sql : insertSqls) {
            executeUpdate(sql, "插入初始化数据");
        }
    }

    /**
     * 验证表是否创建成功
     */
    private void verifyTable() {
        String sql = "SELECT COUNT(*) FROM TBL_BOND_CATEGORY WHERE DELETE_FLAG = 0";
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
