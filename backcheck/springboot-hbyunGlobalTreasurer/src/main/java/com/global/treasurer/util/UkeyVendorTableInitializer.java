package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * UKey厂商管理表初始化工具
 * 自动检查并创建表和测试数据
 *
 * @author 华博云开发团队
 * @since 2025-12-26
 */
//@Component  # 已禁用，避免数据库依赖导致启动失败
public class UkeyVendorTableInitializer {
    private static final Logger log = LoggerFactory.getLogger(UkeyVendorTableInitializer.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 应用启动后自动执行
     */
    @PostConstruct
    public void init() {
        try {
            createSequence();
            checkAndCreateTable();
            checkAndInsertTestData();
        } catch (Exception e) {
            log.error("UKey厂商管理表初始化失败", e);
        }
    }

    /**
     * 创建序列
     */
    private void createSequence() {
        String sequenceName = "SEQ_UKEY_VENDOR";

        // 检查序列是否存在
        Integer sequenceCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM USER_SEQUENCES WHERE SEQUENCE_NAME = ?",
            Integer.class,
            sequenceName
        );

        if (sequenceCount != null && sequenceCount > 0) {
            log.info("序列 {} 已存在，跳过创建", sequenceName);
            return;
        }

        log.info("开始创建序列 {}...", sequenceName);

        // 创建序列
        String createSequenceSql = "CREATE SEQUENCE " + sequenceName +
            " START WITH 1" +
            " INCREMENT BY 1" +
            " NOMAXVALUE" +
            " NOMINVALUE" +
            " CACHE 20";

        log.info("执行SQL: {}", createSequenceSql);
        jdbcTemplate.execute(createSequenceSql);

        log.info("序列 {} 创建成功", sequenceName);
    }

    /**
     * 检查并创建表
     */
    private void checkAndCreateTable() {
        String tableName = "TBL_UKEY_VENDOR";

        // 检查表是否存在
        Integer tableCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = ?",
            Integer.class,
            tableName
        );

        if (tableCount != null && tableCount > 0) {
            log.info("表 {} 已存在，跳过创建", tableName);
            checkAndAddColumns(tableName);
            return;
        }

        log.info("开始创建表 {}...", tableName);

        // 创建表
        String createTableSql = "CREATE TABLE TBL_UKEY_VENDOR (" +
            "ID BIGINT PRIMARY KEY, " +
            "VENDOR_CODE VARCHAR(50) NOT NULL, " +
            "VENDOR_NAME VARCHAR(200) NOT NULL, " +
            "VENDOR_TYPE VARCHAR(50), " +
            "VENDOR_LEVEL VARCHAR(10), " +
            "CONTACT_PERSON VARCHAR(100), " +
            "CONTACT_PHONE VARCHAR(50), " +
            "CONTACT_EMAIL VARCHAR(100), " +
            "COOPERATION_STATUS VARCHAR(50), " +
            "EVALUATION_LEVEL VARCHAR(50), " +
            "CERTIFICATE_INFO CLOB, " +
            "CERTIFICATION_STATUS VARCHAR(50), " +
            "PRODUCT_MODELS VARCHAR(500), " +
            "CONTACT_INFO VARCHAR(500), " +
            "ADDRESS VARCHAR(500), " +
            "DESCRIPTION CLOB, " +
            "AUTH_METHODS VARCHAR(500), " +
            "REMARK VARCHAR(1000), " +
            "IS_ENABLED INTEGER DEFAULT 1, " +
            "CREATE_BY VARCHAR(100), " +
            "CREATE_TIME TIMESTAMP DEFAULT SYSDATE, " +
            "UPDATE_BY VARCHAR(100), " +
            "UPDATE_TIME TIMESTAMP DEFAULT SYSDATE" +
            ")";

        log.info("执行SQL: {}", createTableSql);
        jdbcTemplate.execute(createTableSql);

        // 添加字段注释
        addColumnComments(tableName);

        // 创建索引
        createIndexes(tableName);

        log.info("表 {} 创建成功", tableName);
    }

    /**
     * 检查并添加缺失的列
     */
    private void checkAndAddColumns(String tableName) {
        log.info("开始检查表 {} 的列...", tableName);

        // 检查并添加 CERTIFICATION_STATUS 列
        checkAndAddColumn(tableName, "CERTIFICATION_STATUS", "VARCHAR(50)", "认证状态: CERTIFIED-已认证, PENDING-待认证, UNCERTIFIED-未认证");

        // 检查并添加 PRODUCT_MODELS 列
        checkAndAddColumn(tableName, "PRODUCT_MODELS", "VARCHAR(500)", "产品型号,多个型号用逗号分隔");

        // 检查并添加 CONTACT_INFO 列
        checkAndAddColumn(tableName, "CONTACT_INFO", "VARCHAR(500)", "联系方式(整合电话、邮箱等)");

        // 检查并添加 ADDRESS 列
        checkAndAddColumn(tableName, "ADDRESS", "VARCHAR(500)", "厂商地址");

        // 检查并添加 DESCRIPTION 列
        checkAndAddColumn(tableName, "DESCRIPTION", "CLOB", "厂商描述");

        log.info("表 {} 列检查完成", tableName);
    }

    /**
     * 检查并添加单个列
     */
    private void checkAndAddColumn(String tableName, String columnName, String columnType, String comment) {
        try {
            // 检查列是否存在
            Integer columnCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_COL_COMMENTS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class,
                tableName,
                columnName
            );

            if (columnCount != null && columnCount > 0) {
                log.info("列 {}.{} 已存在，跳过", tableName, columnName);
                return;
            }

            // 列不存在，添加列
            String sql = String.format("ALTER TABLE %s ADD %s %s", tableName, columnName, columnType);
            log.info("执行SQL: {}", sql);
            jdbcTemplate.execute(sql);

            // 添加注释
            String commentSql = String.format("COMMENT ON COLUMN %s.%s IS '%s'", tableName, columnName, comment);
            log.info("执行SQL: {}", commentSql);
            jdbcTemplate.execute(commentSql);

            log.info("成功添加列: {}.{}", tableName, columnName);

        } catch (Exception e) {
            // 列可能已存在或其他原因，忽略错误
            if (e.getMessage() != null && e.getMessage().contains("已存在")) {
                log.info("列 {}.{} 已存在", tableName, columnName);
            } else {
                log.warn("添加列 {}.{} 时出现异常: {}", tableName, columnName, e.getMessage());
            }
        }
    }

    /**
     * 添加字段注释
     */
    private void addColumnComments(String tableName) {
        String[] comments = {
            "COMMENT ON COLUMN " + tableName + ".ID IS '主键ID'",
            "COMMENT ON COLUMN " + tableName + ".VENDOR_CODE IS '厂商编码,唯一标识'",
            "COMMENT ON COLUMN " + tableName + ".VENDOR_NAME IS '厂商名称'",
            "COMMENT ON COLUMN " + tableName + ".VENDOR_TYPE IS '厂商类型'",
            "COMMENT ON COLUMN " + tableName + ".VENDOR_LEVEL IS '厂商等级'",
            "COMMENT ON COLUMN " + tableName + ".CONTACT_PERSON IS '联系人'",
            "COMMENT ON COLUMN " + tableName + ".CONTACT_PHONE IS '联系电话'",
            "COMMENT ON COLUMN " + tableName + ".CONTACT_EMAIL IS '联系邮箱'",
            "COMMENT ON COLUMN " + tableName + ".COOPERATION_STATUS IS '合作状态'",
            "COMMENT ON COLUMN " + tableName + ".EVALUATION_LEVEL IS '评价等级'",
            "COMMENT ON COLUMN " + tableName + ".CERTIFICATE_INFO IS '证书信息'",
            "COMMENT ON COLUMN " + tableName + ".CERTIFICATION_STATUS IS '认证状态'",
            "COMMENT ON COLUMN " + tableName + ".PRODUCT_MODELS IS '产品型号'",
            "COMMENT ON COLUMN " + tableName + ".CONTACT_INFO IS '联系方式'",
            "COMMENT ON COLUMN " + tableName + ".ADDRESS IS '厂商地址'",
            "COMMENT ON COLUMN " + tableName + ".DESCRIPTION IS '厂商描述'",
            "COMMENT ON COLUMN " + tableName + ".AUTH_METHODS IS '认证方式'",
            "COMMENT ON COLUMN " + tableName + ".REMARK IS '备注'",
            "COMMENT ON COLUMN " + tableName + ".IS_ENABLED IS '是否启用'",
            "COMMENT ON COLUMN " + tableName + ".CREATE_BY IS '创建人'",
            "COMMENT ON COLUMN " + tableName + ".CREATE_TIME IS '创建时间'",
            "COMMENT ON COLUMN " + tableName + ".UPDATE_BY IS '更新人'",
            "COMMENT ON COLUMN " + tableName + ".UPDATE_TIME IS '更新时间'",
            "COMMENT ON TABLE " + tableName + " IS 'UKey厂商管理表'"
        };

        for (String comment : comments) {
            try {
                log.info("执行SQL: {}", comment);
                jdbcTemplate.execute(comment);
            } catch (Exception e) {
                log.warn("添加注释失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 创建索引
     */
    private void createIndexes(String tableName) {
        String[] indexes = {
            "CREATE UNIQUE INDEX IDX_UKEY_VENDOR_CODE ON " + tableName + "(VENDOR_CODE)",
            "CREATE INDEX IDX_UKEY_VENDOR_TYPE ON " + tableName + "(VENDOR_TYPE)",
            "CREATE INDEX IDX_UKEY_VENDOR_COOP_STATUS ON " + tableName + "(COOPERATION_STATUS)",
            "CREATE INDEX IDX_UKEY_VENDOR_CERT_STATUS ON " + tableName + "(CERTIFICATION_STATUS)",
            "CREATE INDEX IDX_UKEY_VENDOR_ENABLED ON " + tableName + "(IS_ENABLED)",
            "CREATE INDEX IDX_UKEY_VENDOR_CREATE_TIME ON " + tableName + "(CREATE_TIME)"
        };

        for (String index : indexes) {
            try {
                log.info("执行SQL: {}", index);
                jdbcTemplate.execute(index);
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("已存在")) {
                    log.info("索引已存在，跳过");
                } else {
                    log.warn("创建索引失败: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 检查并插入测试数据
     */
    private void checkAndInsertTestData() {
        String tableName = "TBL_UKEY_VENDOR";

        // 检查是否已有数据
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM " + tableName,
            Integer.class
        );

        if (count != null && count > 0) {
            log.info("表 {} 已有 {} 条数据，跳过测试数据插入", tableName, count);
            return;
        }

        log.info("开始插入测试数据...");

        // 使用序列获取ID
        String sequenceSql = "SELECT SEQ_UKEY_VENDOR.NEXTVAL FROM DUAL";

        // 测试数据
        String[][] testData = {
            {"UKEY_VENDOR_001", "飞天诚信科技股份有限公司", "HARDWARE", "A", "ACTIVE", "CERTIFIED",
              "ePass3000, ePass3003Auto", "技术支持:400-888-8888; dev@example.com",
              "北京市海淀区中关村大街1号", "飞天诚信是国内领先的网络安全产品及解决方案提供商"},
            {"UKEY_VENDOR_002", "握奇数据股份有限公司", "HARDWARE", "A", "ACTIVE", "CERTIFIED",
              "WatchKEY, WatchDATA", "技术支持:010-88888888; dev@example.com",
              "北京市朝阳区望京科技园", "握奇数据是全球领先的数据安全解决方案提供商"},
            {"UKEY_VENDOR_003", "上海格尔软件股份有限公司", "SOFTWARE", "A", "ACTIVE", "CERTIFIED",
              "格尔认证终端", "技术支持:021-66666666; dev@example.com",
              "上海市浦东新区张江高科技园区", "格尔软件是中国密码行业的领军企业"},
            {"UKEY_VENDOR_004", "北京中星微电子有限公司", "HARDWARE", "B", "ACTIVE", "PENDING",
              "VCU系列", "技术支持:010-66666666; dev@example.com",
              "北京市海淀区中关村软件园", "中星微电子是国内领先的芯片设计公司"},
            {"UKEY_VENDOR_005", "北京世纪龙脉科技有限公司", "INTEGRATION", "B", "SUSPENDED", "UNCERTIFIED",
              "龙盾UKey", "技术支持:010-55555555; dev@example.com",
              "北京市海淀区上地信息路", "世纪龙脉专注于信息安全产品的研发和集成服务"}
        };

        for (String[] data : testData) {
            try {
                Long id = jdbcTemplate.queryForObject(sequenceSql, Long.class);

                String insertSql = "INSERT INTO " + tableName +
                    " (ID, VENDOR_CODE, VENDOR_NAME, VENDOR_TYPE, VENDOR_LEVEL, COOPERATION_STATUS, " +
                    "CERTIFICATION_STATUS, PRODUCT_MODELS, CONTACT_INFO, ADDRESS, DESCRIPTION, " +
                    "IS_ENABLED, CREATE_TIME, UPDATE_TIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, SYSDATE, SYSDATE)";

                jdbcTemplate.update(insertSql,
                    id, data[0], data[1], data[2], data[3], data[4], data[5],
                    data[6], data[7], data[8], data[9]
                );

                log.info("插入测试数据: {} - {}", data[0], data[1]);
            } catch (Exception e) {
                log.error("插入测试数据失败: {} - {}", data[0], data[1], e);
            }
        }

        log.info("测试数据插入完成");
    }
}
