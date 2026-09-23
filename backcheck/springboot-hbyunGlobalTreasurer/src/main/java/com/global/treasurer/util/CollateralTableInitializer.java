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
 * 担保物表初始化器
 * 用于创建TBL_COLLATERAL表
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Component
public class CollateralTableInitializer {

    private static final Logger log = LoggerFactory.getLogger(CollateralTableInitializer.class);

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
        log.info("开始初始化担保物表");
        log.info("========================================");

        try {
            createTable();
            createIndexes();
            insertInitData();
            verifyTable();

            log.info("========================================");
            log.info("担保物表初始化完成!");
            log.info("========================================");
        } catch (Exception e) {
            log.error("担保物表初始化失败", e);
        }
    }

    /**
     * 创建表
     */
    private void createTable() {
        String sql = "CREATE TABLE TBL_COLLATERAL (" +
                "COLLATERAL_ID BIGINT IDENTITY(1,1) PRIMARY KEY," +
                "COLLATERAL_NO VARCHAR(50) NOT NULL," +
                "COLLATERAL_TYPE VARCHAR(50) NOT NULL," +
                "COLLATERAL_NAME VARCHAR(200) NOT NULL," +
                "OWNER_COMPANY VARCHAR(200)," +
                "ASSESSED_VALUE DECIMAL(20,2)," +
                "CURRENCY_CODE VARCHAR(10) DEFAULT 'CNY'," +
                "LOCATION VARCHAR(500)," +
                "AREA DECIMAL(10,2)," +
                "PURCHASE_DATE TIMESTAMP," +
                "PURCHASE_PRICE DECIMAL(20,2)," +
                "DEPRECIATION_RATE DECIMAL(5,4)," +
                "CURRENT_VALUE DECIMAL(20,2)," +
                "VALUATION_DATE TIMESTAMP," +
                "COLLATERAL_STATUS VARCHAR(20) DEFAULT 'AVAILABLE'," +
                "MORTGAGE_STATUS VARCHAR(20) DEFAULT 'UNMORTGAGED'," +
                "PLEDGE_STATUS VARCHAR(20) DEFAULT 'UNPLEDGED'," +
                "REGISTRATION_NO VARCHAR(100)," +
                "REGISTRATION_DATE TIMESTAMP," +
                "CERTIFICATE_NO VARCHAR(100)," +
                "INSURANCE_STATUS VARCHAR(20) DEFAULT 'UNINSURED'," +
                "INSURANCE_COMPANY VARCHAR(200)," +
                "INSURANCE_POLICY_NO VARCHAR(100)," +
                "INSURANCE_AMOUNT DECIMAL(20,2)," +
                "INSURANCE_EXPIRY DATE," +
                "DESCRIPTION VARCHAR(1000)," +
                "REMARK VARCHAR(1000)," +
                "DELETE_FLAG INTEGER DEFAULT 0," +
                "CREATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UPDATED_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        executeUpdate(sql, "创建TBL_COLLATERAL表");
    }

    /**
     * 创建索引
     */
    private void createIndexes() {
        List<String> indexSqls = new ArrayList<>();

        // 唯一索引
        indexSqls.add("CREATE UNIQUE INDEX IF NOT EXISTS IDX_COLLATERAL_NO ON TBL_COLLATERAL(COLLATERAL_NO)");

        // 普通索引
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_COLLATERAL_TYPE ON TBL_COLLATERAL(COLLATERAL_TYPE)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_COLLATERAL_STATUS ON TBL_COLLATERAL(COLLATERAL_STATUS)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_COLLATERAL_OWNER ON TBL_COLLATERAL(OWNER_COMPANY)");
        indexSqls.add("CREATE INDEX IF NOT EXISTS IDX_COLLATERAL_VALUE ON TBL_COLLATERAL(CURRENT_VALUE)");

        for (String sql : indexSqls) {
            executeUpdate(sql, "创建索引");
        }
    }

    /**
     * 插入初始化数据
     */
    private void insertInitData() {
        // 先检查是否已有数据
        String checkSql = "SELECT COUNT(*) FROM TBL_COLLATERAL WHERE DELETE_FLAG = 0";
        Integer count = executeQuery(checkSql);

        if (count != null && count > 0) {
            log.info("表中已存在{}条数据，跳过初始化数据插入", count);
            return;
        }

        // 插入测试数据
        List<String> insertSqls = new ArrayList<>();
        insertSqls.add("INSERT INTO TBL_COLLATERAL (COLLATERAL_NO, COLLATERAL_TYPE, COLLATERAL_NAME, OWNER_COMPANY, ASSESSED_VALUE, CURRENCY_CODE, LOCATION, AREA, CURRENT_VALUE, VALUATION_DATE, COLLATERAL_STATUS, DESCRIPTION, REMARK) VALUES " +
                "('CL2026001', 'REAL_ESTATE', '华贸大厦A座', '华博集团有限公司', 5000000.00, 'CNY', '北京市朝阳区建国路88号', 1200.50, 5200000.00, '2026-01-01', 'AVAILABLE', '商业办公楼，位于核心商圈', '优质资产')");
        insertSqls.add("INSERT INTO TBL_COLLATERAL (COLLATERAL_NO, COLLATERAL_TYPE, COLLATERAL_NAME, OWNER_COMPANY, ASSESSED_VALUE, CURRENCY_CODE, LOCATION, AREA, CURRENT_VALUE, VALUATION_DATE, COLLATERAL_STATUS, DESCRIPTION, REMARK) VALUES " +
                "('CL2026002', 'EQUIPMENT', '数控加工中心XK-2000', '子公司A', 800000.00, 'CNY', '生产车间A区', 50.00, 750000.00, '2026-01-10', 'AVAILABLE', '高精度数控加工设备', '成色良好')");
        insertSqls.add("INSERT INTO TBL_COLLATERAL (COLLATERAL_NO, COLLATERAL_TYPE, COLLATERAL_NAME, OWNER_COMPANY, ASSESSED_VALUE, CURRENCY_CODE, LOCATION, CURRENT_VALUE, VALUATION_DATE, COLLATERAL_STATUS, DESCRIPTION, REMARK) VALUES " +
                "('CL2026003', 'LAND', '工业用地地块A', '子公司B', 2000000.00, 'CNY', '工业园区南区', 5000.00, 2100000.00, '2026-01-05', 'MORTGAGED', '工业用地，使用年限50年', '已用于银行抵押')");
        insertSqls.add("INSERT INTO TBL_COLLATERAL (COLLATERAL_NO, COLLATERAL_TYPE, COLLATERAL_NAME, OWNER_COMPANY, ASSESSED_VALUE, CURRENCY_CODE, CURRENT_VALUE, VALUATION_DATE, COLLATERAL_STATUS, DESCRIPTION, REMARK) VALUES " +
                "('CL2026004', 'INVENTORY', '原材料库存-钢材', '子公司C', 1500000.00, 'CNY', '仓库1号', 1450000.00, '2026-01-12', 'AVAILABLE', '各类钢材库存', '质量稳定')");

        for (String sql : insertSqls) {
            executeUpdate(sql, "插入初始化数据");
        }
    }

    /**
     * 验证表是否创建成功
     */
    private void verifyTable() {
        String sql = "SELECT COUNT(*) FROM TBL_COLLATERAL WHERE DELETE_FLAG = 0";
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
