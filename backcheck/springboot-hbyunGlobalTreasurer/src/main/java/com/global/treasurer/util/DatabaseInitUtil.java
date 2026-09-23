package com.global.treasurer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 数据库初始化工具类
 * 用于创建必要的数据库表和序列
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
//@Component  // 已禁用，避免数据库依赖导致启动失败
public class DatabaseInitUtil {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitUtil.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 应用启动后自动检查并创建表
     */
    @PostConstruct
    public void initDatabase() {
        try {
            // 初始化业务系统注册表
            initBusinessSystemRegistry();

            // 初始化数据映射配置表
            initDataMappingConfig();

            // 初始化印鉴组合配置表
            initSealCombination();

            // 初始化印鉴类型表
            initSealType();

            // 初始化印鉴使用记录表
            initSealUsageRecord();

            // 初始化司库参数配置表
            initTreasuryParameter();

            // 初始化第三方账户管理表
            initThirdPartyAccount();

        } catch (Exception e) {
            log.error("数据库初始化失败", e);
            // 不抛出异常,避免影响应用启动
        }
    }

    /**
     * 初始化业务系统注册表
     */
    private void initBusinessSystemRegistry() {
        // 检查表是否存在
        if (!checkTableExists("TBL_BUSINESS_SYSTEM_REGISTRY")) {
            log.info("表 TBL_BUSINESS_SYSTEM_REGISTRY 不存在,开始创建...");
            createBusinessSystemRegistryTable();
            log.info("表 TBL_BUSINESS_SYSTEM_REGISTRY 创建成功");
        } else {
            log.info("表 TBL_BUSINESS_SYSTEM_REGISTRY 已存在,跳过创建");
        }

        // 检查序列是否存在
        if (!checkSequenceExists("SEQ_BUSINESS_SYSTEM_REGISTRY")) {
            log.info("序列 SEQ_BUSINESS_SYSTEM_REGISTRY 不存在,开始创建...");
            createSequence();
            log.info("序列 SEQ_BUSINESS_SYSTEM_REGISTRY 创建成功");
        } else {
            log.info("序列 SEQ_BUSINESS_SYSTEM_REGISTRY 已存在,跳过创建");
        }

        // 检查并插入测试数据
        long count = queryTableCount();
        if (count == 0) {
            log.info("表 TBL_BUSINESS_SYSTEM_REGISTRY 为空,开始插入测试数据...");
            insertTestData();
            log.info("测试数据插入成功");
        } else {
            log.info("表 TBL_BUSINESS_SYSTEM_REGISTRY 已有 {} 条数据,跳过测试数据插入", count);
        }
    }

    /**
     * 初始化数据映射配置表
     */
    private void initDataMappingConfig() {
        // 检查表是否存在
        if (!checkTableExists("TBL_DATA_MAPPING_CONFIG")) {
            log.info("表 TBL_DATA_MAPPING_CONFIG 不存在,开始创建...");
            createDataMappingConfigTable();
            log.info("表 TBL_DATA_MAPPING_CONFIG 创建成功");
        } else {
            log.info("表 TBL_DATA_MAPPING_CONFIG 已存在,跳过创建");
            // 表已存在,检查并添加缺失的列(向后兼容)
            addMissingColumnsToDataMappingTable();
        }

        // 检查序列是否存在
        if (!checkSequenceExists("SEQ_DATA_MAPPING_CONFIG")) {
            log.info("序列 SEQ_DATA_MAPPING_CONFIG 不存在,开始创建...");
            createDataMappingSequence();
            log.info("序列 SEQ_DATA_MAPPING_CONFIG 创建成功");
        } else {
            log.info("序列 SEQ_DATA_MAPPING_CONFIG 已存在,跳过创建");
        }

        // 检查并插入测试数据
        long count = queryDataMappingTableCount();
        if (count == 0) {
            log.info("表 TBL_DATA_MAPPING_CONFIG 为空,开始插入测试数据...");
            insertDataMappingTestData();
            log.info("数据映射测试数据插入成功");
        } else {
            log.info("表 TBL_DATA_MAPPING_CONFIG 已有 {} 条数据,跳过测试数据插入", count);
        }
    }

    /**
     * 检查表是否存在
     */
    private boolean checkTableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("检查表是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查序列是否存在
     */
    private boolean checkSequenceExists(String sequenceName) {
        try {
            String sql = "SELECT COUNT(*) FROM USER_SEQUENCES WHERE SEQUENCE_NAME = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, sequenceName);
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("检查序列是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查列是否存在
     */
    private boolean checkColumnExists(String tableName, String columnName) {
        try {
            String sql = "SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName);
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("检查列是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 删除表
     */
    private void dropTable(String tableName) {
        try {
            // 先尝试使用CASCADE约束删除表及其依赖对象
            String sql = "DROP TABLE " + tableName + " CASCADE CONSTRAINTS";
            jdbcTemplate.execute(sql);
            log.info("表 {} 已删除", tableName);
        } catch (Exception e) {
            log.warn("删除表失败: {}", e.getMessage());
        }
    }

    /**
     * 查询表记录数
     */
    private long queryTableCount() {
        try {
            String sql = "SELECT COUNT(*) FROM TBL_BUSINESS_SYSTEM_REGISTRY";
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("查询表记录数失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 创建业务系统注册表
     */
    private void createBusinessSystemRegistryTable() {
        String createTableSql =
            "CREATE TABLE TBL_BUSINESS_SYSTEM_REGISTRY (" +
            "    ID NUMBER(20) NOT NULL, " +
            "    SYSTEM_NAME VARCHAR2(200) NOT NULL, " +
            "    SYSTEM_CODE VARCHAR2(100) NOT NULL, " +
            "    SYSTEM_TYPE VARCHAR2(50), " +
            "    API_URL VARCHAR2(500), " +
            "    AUTH_TYPE VARCHAR2(50), " +
            "    AUTH_CONFIG CLOB, " +
            "    DESCRIPTION VARCHAR2(1000), " +
            "    SYSTEM_DESC VARCHAR2(1000), " +
            "    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "    CREATE_USER VARCHAR2(100), " +
            "    UPDATE_USER VARCHAR2(100), " +
            "    CONSTRAINT PK_BUSINESS_SYSTEM_REGISTRY PRIMARY KEY (ID)" +
            ")";

        jdbcTemplate.execute(createTableSql);

        // 添加表注释
        jdbcTemplate.execute("COMMENT ON TABLE TBL_BUSINESS_SYSTEM_REGISTRY IS '业务系统注册表,用于存储各业务系统的连接和配置信息'");

        // 添加列注释
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.ID IS '主键ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_NAME IS '系统名称'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_CODE IS '系统编码,唯一标识'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_TYPE IS '系统类型(如:财务系统、OA系统、ERP系统等)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.API_URL IS '接口地址'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.AUTH_TYPE IS '认证方式(如:TOKEN、OAUTH2、BASIC_AUTH等)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.AUTH_CONFIG IS '认证配置JSON字符串'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.DESCRIPTION IS '系统描述'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_DESC IS '系统描述(备用字段)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.CREATE_TIME IS '创建时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.UPDATE_TIME IS '更新时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.CREATE_USER IS '创建人'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.UPDATE_USER IS '更新人'");

        // 创建索引(达梦数据库不支持COMMENT ON INDEX,已移除)
        try {
            jdbcTemplate.execute("CREATE UNIQUE INDEX IDX_SYSTEM_CODE ON TBL_BUSINESS_SYSTEM_REGISTRY(SYSTEM_CODE)");
        } catch (Exception e) {
            log.warn("创建唯一索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SYSTEM_NAME ON TBL_BUSINESS_SYSTEM_REGISTRY(SYSTEM_NAME)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SYSTEM_TYPE ON TBL_BUSINESS_SYSTEM_REGISTRY(SYSTEM_TYPE)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_CREATE_TIME ON TBL_BUSINESS_SYSTEM_REGISTRY(CREATE_TIME)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }
    }

    /**
     * 创建序列
     */
    private void createSequence() {
        String createSequenceSql =
            "CREATE SEQUENCE SEQ_BUSINESS_SYSTEM_REGISTRY " +
            "START WITH 1 " +
            "INCREMENT BY 1 " +
            "NOMAXVALUE " +
            "NOMINVALUE " +
            "NOCACHE";

        jdbcTemplate.execute(createSequenceSql);

        // 达梦数据库不支持COMMENT ON SEQUENCE,跳过
        // jdbcTemplate.execute("COMMENT ON SEQUENCE SEQ_BUSINESS_SYSTEM_REGISTRY IS '业务系统注册表主键序列'");
    }

    /**
     * 插入测试数据
     */
    private void insertTestData() {
        String insertSql =
            "INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY " +
            "(ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG, DESCRIPTION, SYSTEM_DESC, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_BUSINESS_SYSTEM_REGISTRY.NEXTVAL, '财务共享系统', 'FINANCIAL_SHARING', 'FINANCIAL', 'http://localhost:8001/api', 'TOKEN', '{\"token\": \"test-token-123\"}', '财务共享服务中心系统', '财务共享服务中心系统', TIMESTAMP '2024-12-01 10:00:00', TIMESTAMP '2024-12-01 10:00:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        insertSql =
            "INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY " +
            "(ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG, DESCRIPTION, SYSTEM_DESC, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_BUSINESS_SYSTEM_REGISTRY.NEXTVAL, 'OA办公系统', 'OA_SYSTEM', 'OA', 'http://oa.example.com/api', 'OAUTH2', '{\"clientId\": \"client123\", \"clientSecret\": \"secret123\"}', '企业办公自动化系统', '企业办公自动化系统', TIMESTAMP '2024-12-02 14:30:00', TIMESTAMP '2024-12-02 14:30:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        insertSql =
            "INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY " +
            "(ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG, DESCRIPTION, SYSTEM_DESC, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_BUSINESS_SYSTEM_REGISTRY.NEXTVAL, 'ERP企业资源规划', 'ERP_SYSTEM', 'ERP', 'http://erp.example.com/api', 'BASIC_AUTH', '{\"username\": \"erp_user\", \"password\": \"encrypted_password\"}', '企业资源规划系统', '企业资源规划系统', TIMESTAMP '2024-12-03 09:15:00', TIMESTAMP '2024-12-03 09:15:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        insertSql =
            "INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY " +
            "(ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG, DESCRIPTION, SYSTEM_DESC, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_BUSINESS_SYSTEM_REGISTRY.NEXTVAL, '人力资源系统', 'HR_SYSTEM', 'HR', 'http://hr.example.com/api', 'TOKEN', '{\"token\": \"hr-token-456\"}', '人力资源管理系统', '人力资源管理系统', TIMESTAMP '2024-12-04 16:20:00', TIMESTAMP '2024-12-04 16:20:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        insertSql =
            "INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY " +
            "(ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG, DESCRIPTION, SYSTEM_DESC, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_BUSINESS_SYSTEM_REGISTRY.NEXTVAL, '客户关系管理', 'CRM_SYSTEM', 'CRM', 'http://crm.example.com/api', 'API_KEY', '{\"apiKey\": \"crm-key-789\", \"apiSecret\": \"crm-secret\"}', '客户关系管理系统', '客户关系管理系统', TIMESTAMP '2024-12-05 11:45:00', TIMESTAMP '2024-12-05 11:45:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        insertSql =
            "INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY " +
            "(ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG, DESCRIPTION, SYSTEM_DESC, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_BUSINESS_SYSTEM_REGISTRY.NEXTVAL, '供应链管理', 'SCM_SYSTEM', 'SCM', 'http://scm.example.com/api', 'TOKEN', '{\"token\": \"scm-token-101\"}', '供应链管理系统', '供应链管理系统', TIMESTAMP '2024-12-06 13:30:00', TIMESTAMP '2024-12-06 13:30:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);
    }

    /**
     * 查询数据映射表记录数
     */
    private long queryDataMappingTableCount() {
        try {
            String sql = "SELECT COUNT(*) FROM TBL_DATA_MAPPING_CONFIG";
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("查询数据映射表记录数失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 创建数据映射配置表
     */
    private void createDataMappingConfigTable() {
        String createTableSql =
            "CREATE TABLE TBL_DATA_MAPPING_CONFIG (" +
            "    ID NUMBER(20) NOT NULL, " +
            "    MAPPING_NAME VARCHAR2(200) NOT NULL, " +
            "    MAPPING_CODE VARCHAR2(100) NOT NULL, " +
            "    SOURCE_SYSTEM VARCHAR2(50) NOT NULL, " +
            "    TARGET_SYSTEM VARCHAR2(50) NOT NULL, " +
            "    SOURCE_FIELD VARCHAR2(500), " +
            "    TARGET_FIELD VARCHAR2(500), " +
            "    MAPPING_TYPE VARCHAR2(50) NOT NULL, " +
            "    MAPPING_RULE CLOB, " +
            "    DESCRIPTION VARCHAR2(1000), " +
            "    STATUS INTEGER DEFAULT 1, " +
            "    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "    CREATE_USER VARCHAR2(50), " +
            "    UPDATE_USER VARCHAR2(50), " +
            "    CONSTRAINT PK_DATA_MAPPING_CONFIG PRIMARY KEY (ID)" +
            ")";

        jdbcTemplate.execute(createTableSql);

        // 添加表注释
        jdbcTemplate.execute("COMMENT ON TABLE TBL_DATA_MAPPING_CONFIG IS '数据映射配置表,用于存储不同系统间的数据字段映射关系'");

        // 添加列注释
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.ID IS '主键ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.MAPPING_NAME IS '映射名称'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.MAPPING_CODE IS '映射编码,唯一标识'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.SOURCE_SYSTEM IS '源系统编码'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.TARGET_SYSTEM IS '目标系统编码'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.SOURCE_FIELD IS '源字段(JSON数组格式,多字段)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.TARGET_FIELD IS '目标字段(JSON数组格式,多字段)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.MAPPING_TYPE IS '映射类型(字段映射/数据转换/规则映射)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.MAPPING_RULE IS '映射规则(JSON格式的转换规则)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.DESCRIPTION IS '映射描述'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.STATUS IS '状态(1-启用,0-禁用)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.CREATE_TIME IS '创建时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.UPDATE_TIME IS '更新时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.CREATE_USER IS '创建人'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_DATA_MAPPING_CONFIG.UPDATE_USER IS '更新人'");

        // 创建索引
        try {
            jdbcTemplate.execute("CREATE UNIQUE INDEX IDX_MAPPING_CODE ON TBL_DATA_MAPPING_CONFIG(MAPPING_CODE)");
        } catch (Exception e) {
            log.warn("创建唯一索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SOURCE_SYSTEM ON TBL_DATA_MAPPING_CONFIG(SOURCE_SYSTEM)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_TARGET_SYSTEM ON TBL_DATA_MAPPING_CONFIG(TARGET_SYSTEM)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_STATUS ON TBL_DATA_MAPPING_CONFIG(STATUS)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_CREATE_TIME ON TBL_DATA_MAPPING_CONFIG(CREATE_TIME)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }
    }

    /**
     * 创建数据映射序列
     */
    private void createDataMappingSequence() {
        String createSequenceSql =
            "CREATE SEQUENCE SEQ_DATA_MAPPING_CONFIG " +
            "START WITH 1 " +
            "INCREMENT BY 1 " +
            "NOMAXVALUE " +
            "NOMINVALUE " +
            "NOCACHE";

        jdbcTemplate.execute(createSequenceSql);
    }

    /**
     * 插入数据映射测试数据
     */
    private void insertDataMappingTestData() {
        // 测试数据1: ERP到财务系统的字段映射
        String insertSql =
            "INSERT INTO TBL_DATA_MAPPING_CONFIG " +
            "(ID, MAPPING_NAME, MAPPING_CODE, SOURCE_SYSTEM, TARGET_SYSTEM, SOURCE_FIELD, TARGET_FIELD, MAPPING_TYPE, MAPPING_RULE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_DATA_MAPPING_CONFIG.NEXTVAL, 'ERP订单字段映射', 'ERP_ORDER_MAPPING', 'ERP_SYSTEM', 'FINANCIAL_SYSTEM', " +
            "'[\"order_id\", \"customer_name\", \"order_amount\", \"order_date\"]', " +
            "'[\"orderId\", \"customerName\", \"totalAmount\", \"transactionDate\"]', " +
            "'FIELD_MAPPING', " +
            "'{\"order_id\": \"orderId\", \"customer_name\": \"customerName\", \"order_amount\": \"totalAmount\", \"order_date\": \"transactionDate\"}', " +
            "'ERP系统订单字段到财务系统字段的映射关系', 1, TIMESTAMP '2024-12-20 10:00:00', TIMESTAMP '2024-12-20 10:00:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        // 测试数据2: 银行接口数据转换
        insertSql =
            "INSERT INTO TBL_DATA_MAPPING_CONFIG " +
            "(ID, MAPPING_NAME, MAPPING_CODE, SOURCE_SYSTEM, TARGET_SYSTEM, SOURCE_FIELD, TARGET_FIELD, MAPPING_TYPE, MAPPING_RULE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_DATA_MAPPING_CONFIG.NEXTVAL, '银行流水数据转换', 'BANK_TRANSACTION_TRANSFORM', 'BANK_SYSTEM', 'FINANCIAL_SYSTEM', " +
            "'[\"transaction_no\", \"debit_amount\", \"credit_amount\", \"balance\"]', " +
            "'[\"transNo\", \"debitAmt\", \"creditAmt\", \"accountBalance\"]', " +
            "'DATA_TRANSFORM', " +
            "'{\"transaction_no\": \"transNo\", \"debit_amount\": \"debitAmt\", \"credit_amount\": \"creditAmt\", \"balance\": \"accountBalance\", \"amountUnit\": \"convertToFen\"}', " +
            "'银行流水数据格式转换,金额单位转换为分', 1, TIMESTAMP '2024-12-21 14:30:00', TIMESTAMP '2024-12-21 14:30:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        // 测试数据3: 税务系统字段映射
        insertSql =
            "INSERT INTO TBL_DATA_MAPPING_CONFIG " +
            "(ID, MAPPING_NAME, MAPPING_CODE, SOURCE_SYSTEM, TARGET_SYSTEM, SOURCE_FIELD, TARGET_FIELD, MAPPING_TYPE, MAPPING_RULE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_DATA_MAPPING_CONFIG.NEXTVAL, '税务发票字段映射', 'TAX_INVOICE_MAPPING', 'TAX_SYSTEM', 'FINANCIAL_SYSTEM', " +
            "'[\"invoice_code\", \"invoice_no\", \"buyer_name\", \"seller_name\", \"total_tax\"]', " +
            "'[\"invoiceCode\", \"invoiceNumber\", \"buyerName\", \"sellerName\", \"taxAmount\"]', " +
            "'FIELD_MAPPING', " +
            "'{\"invoice_code\": \"invoiceCode\", \"invoice_no\": \"invoiceNumber\", \"buyer_name\": \"buyerName\", \"seller_name\": \"sellerName\", \"total_tax\": \"taxAmount\"}', " +
            "'税务系统发票字段到财务系统的映射', 1, TIMESTAMP '2024-12-22 09:15:00', TIMESTAMP '2024-12-22 09:15:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        // 测试数据4: CRM客户数据转换
        insertSql =
            "INSERT INTO TBL_DATA_MAPPING_CONFIG " +
            "(ID, MAPPING_NAME, MAPPING_CODE, SOURCE_SYSTEM, TARGET_SYSTEM, SOURCE_FIELD, TARGET_FIELD, MAPPING_TYPE, MAPPING_RULE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_DATA_MAPPING_CONFIG.NEXTVAL, 'CRM客户数据同步', 'CRM_CUSTOMER_SYNC', 'CRM_SYSTEM', 'FINANCIAL_SYSTEM', " +
            "'[\"customer_id\", \"customer_type\", \"credit_limit\", \"register_date\"]', " +
            "'[\"customerId\", \"clientType\", \"creditLine\", \"registrationDate\"]', " +
            "'DATA_TRANSFORM', " +
            "'{\"customer_id\": \"customerId\", \"customer_type\": \"convertClientType\", \"credit_limit\": \"creditLine\", \"register_date\": \"registrationDate\"}', " +
            "'CRM客户数据同步到财务系统,包含类型转换规则', 1, TIMESTAMP '2024-12-23 16:45:00', TIMESTAMP '2024-12-23 16:45:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        // 测试数据5: OA审批数据映射
        insertSql =
            "INSERT INTO TBL_DATA_MAPPING_CONFIG " +
            "(ID, MAPPING_NAME, MAPPING_CODE, SOURCE_SYSTEM, TARGET_SYSTEM, SOURCE_FIELD, TARGET_FIELD, MAPPING_TYPE, MAPPING_RULE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_DATA_MAPPING_CONFIG.NEXTVAL, 'OA审批单映射', 'OA_APPROVAL_MAPPING', 'OA_SYSTEM', 'FINANCIAL_SYSTEM', " +
            "'[\"approval_no\", \"applicant\", \"approval_type\", \"amount\", \"approval_status\"]', " +
            "'[\"approvalNo\", \"requester\", \"expenseType\", \"requestAmount\", \"workflowStatus\"]', " +
            "'FIELD_MAPPING', " +
            "'{\"approval_no\": \"approvalNo\", \"applicant\": \"requester\", \"approval_type\": \"expenseType\", \"amount\": \"requestAmount\", \"approval_status\": \"workflowStatus\"}', " +
            "'OA系统审批单据到财务报销单的映射', 1, TIMESTAMP '2024-12-24 11:20:00', TIMESTAMP '2024-12-24 11:20:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        // 测试数据6: 供应商系统数据转换(已禁用)
        insertSql =
            "INSERT INTO TBL_DATA_MAPPING_CONFIG " +
            "(ID, MAPPING_NAME, MAPPING_CODE, SOURCE_SYSTEM, TARGET_SYSTEM, SOURCE_FIELD, TARGET_FIELD, MAPPING_TYPE, MAPPING_RULE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_DATA_MAPPING_CONFIG.NEXTVAL, '供应商数据映射', 'SUPPLIER_DATA_MAPPING', 'SUPPLIER_SYSTEM', 'FINANCIAL_SYSTEM', " +
            "'[\"supplier_code\", \"supplier_name\", \"contact\", \"address\"]', " +
            "'[\"vendorCode\", \"vendorName\", \"contactPerson\", \"vendorAddress\"]', " +
            "'FIELD_MAPPING', " +
            "'{\"supplier_code\": \"vendorCode\", \"supplier_name\": \"vendorName\", \"contact\": \"contactPerson\", \"address\": \"vendorAddress\"}', " +
            "'供应商系统数据到财务系统的映射(测试用,暂时禁用)', 0, TIMESTAMP '2024-12-24 13:00:00', TIMESTAMP '2024-12-24 13:00:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);

        // 测试数据7: 复杂规则映射示例
        insertSql =
            "INSERT INTO TBL_DATA_MAPPING_CONFIG " +
            "(ID, MAPPING_NAME, MAPPING_CODE, SOURCE_SYSTEM, TARGET_SYSTEM, SOURCE_FIELD, TARGET_FIELD, MAPPING_TYPE, MAPPING_RULE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) " +
            "VALUES " +
            "(SEQ_DATA_MAPPING_CONFIG.NEXTVAL, '费用分摊规则映射', 'EXPENSE_ALLOCATION_RULE', 'OA_SYSTEM', 'FINANCIAL_SYSTEM', " +
            "'[\"expense_amount\", \"department\", \"project_code\", \"allocation_ratio\"]', " +
            "'[\"totalExpense\", \"costCenter\", \"projectRef\", \"distributionRatio\"]', " +
            "'RULE_MAPPING', " +
            "'{\"expense_amount\": \"totalExpense\", \"department\": \"costCenter\", \"project_code\": \"projectRef\", \"allocation_ratio\": \"distributionRatio\", \"validationRule\": \"sum(allocation_ratio) == 100\"}', " +
            "'费用分摊规则映射,包含校验规则', 1, TIMESTAMP '2024-12-24 14:30:00', TIMESTAMP '2024-12-24 14:30:00', 'admin', 'admin')";

        jdbcTemplate.execute(insertSql);
    }

    /**
     * 添加缺失的列(向后兼容旧的列名)
     * 这个方法用于处理已存在的表,添加旧的列名作为计算列
     */
    private void addMissingColumnsToDataMappingTable() {
        try {
            // 检查并添加 SOURCE_FIELDS 列(如果不存在)
            // 使用计算列的方式,映射到 SOURCE_FIELD
            String alterSql = "ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD SOURCE_FIELDS VARCHAR2(500) GENERATED ALWAYS AS (SOURCE_FIELD) VIRTUAL";
            try {
                jdbcTemplate.execute(alterSql);
                log.info("成功添加向后兼容列: SOURCE_FIELDS");
            } catch (Exception e) {
                // 列可能已存在,尝试使用普通列添加
                if (e.getMessage() != null && e.getMessage().contains("已存在")) {
                    log.info("列 SOURCE_FIELDS 已存在,跳过添加");
                } else {
                    log.warn("添加计算列 SOURCE_FIELDS 失败,尝试添加普通列: {}", e.getMessage());
                    // 尝试添加普通列并更新数据
                    String addColumnSql = "ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD SOURCE_FIELDS VARCHAR2(500)";
                    try {
                        jdbcTemplate.execute(addColumnSql);
                        jdbcTemplate.execute("UPDATE TBL_DATA_MAPPING_CONFIG SET SOURCE_FIELDS = SOURCE_FIELD WHERE SOURCE_FIELDS IS NULL");
                        log.info("成功添加普通列: SOURCE_FIELDS");
                    } catch (Exception ex) {
                        log.info("列 SOURCE_FIELDS 已存在或添加失败: {}", ex.getMessage());
                    }
                }
            }

            // 检查并添加 TARGET_FIELDS 列
            alterSql = "ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD TARGET_FIELDS VARCHAR2(500) GENERATED ALWAYS AS (TARGET_FIELD) VIRTUAL";
            try {
                jdbcTemplate.execute(alterSql);
                log.info("成功添加向后兼容列: TARGET_FIELDS");
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("已存在")) {
                    log.info("列 TARGET_FIELDS 已存在,跳过添加");
                } else {
                    log.warn("添加计算列 TARGET_FIELDS 失败,尝试添加普通列: {}", e.getMessage());
                    String addColumnSql = "ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD TARGET_FIELDS VARCHAR2(500)";
                    try {
                        jdbcTemplate.execute(addColumnSql);
                        jdbcTemplate.execute("UPDATE TBL_DATA_MAPPING_CONFIG SET TARGET_FIELDS = TARGET_FIELD WHERE TARGET_FIELDS IS NULL");
                        log.info("成功添加普通列: TARGET_FIELDS");
                    } catch (Exception ex) {
                        log.info("列 TARGET_FIELDS 已存在或添加失败: {}", ex.getMessage());
                    }
                }
            }

            // 检查并添加 MAPPING_RULES 列
            alterSql = "ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD MAPPING_RULES CLOB GENERATED ALWAYS AS (MAPPING_RULE) VIRTUAL";
            try {
                jdbcTemplate.execute(alterSql);
                log.info("成功添加向后兼容列: MAPPING_RULES");
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("已存在")) {
                    log.info("列 MAPPING_RULES 已存在,跳过添加");
                } else {
                    log.warn("添加计算列 MAPPING_RULES 失败,尝试添加普通列: {}", e.getMessage());
                    String addColumnSql = "ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD MAPPING_RULES CLOB";
                    try {
                        jdbcTemplate.execute(addColumnSql);
                        // 尝试从MAPPING_RULE复制数据,如果MAPPING_RULE列不存在会失败,但不需要报错
                        try {
                            jdbcTemplate.execute("UPDATE TBL_DATA_MAPPING_CONFIG SET MAPPING_RULES = MAPPING_RULE WHERE MAPPING_RULES IS NULL");
                        } catch (Exception updateEx) {
                            log.info("MAPPING_RULE列不存在,跳过数据复制: {}", updateEx.getMessage());
                        }
                        log.info("成功添加普通列: MAPPING_RULES");
                    } catch (Exception ex) {
                        log.info("列 MAPPING_RULES 已存在或添加失败: {}", ex.getMessage());
                    }
                }
            }

            // 检查并添加 MAPPING_CODE 列(如果不存在)
            alterSql = "ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD MAPPING_CODE VARCHAR2(100)";
            try {
                jdbcTemplate.execute(alterSql);
                // 如果添加成功,生成唯一编码
                jdbcTemplate.execute("UPDATE TBL_DATA_MAPPING_CONFIG SET MAPPING_CODE = 'MAPPING_' || ID WHERE MAPPING_CODE IS NULL");
                log.info("成功添加列: MAPPING_CODE");
            } catch (Exception e) {
                log.info("列 MAPPING_CODE 已存在或添加失败: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.warn("添加向后兼容列时出现异常(可忽略): {}", e.getMessage());
        }
    }

    /**
     * 初始化印鉴组合配置表
     */
    private void initSealCombination() {
        // 检查 TBL_SEAL_COMBINATION 表是否存在且完整
        boolean tableExists = checkTableExists("TBL_SEAL_COMBINATION");
        boolean tableHasRequiredColumns = checkTableHasRequiredColumns("TBL_SEAL_COMBINATION");

        if (tableExists && !tableHasRequiredColumns) {
            // 表存在但缺少列,删除旧表并重建
            log.warn("表 TBL_SEAL_COMBINATION 存在但缺少必要列,将删除并重建...");
            try {
                jdbcTemplate.execute("DROP TABLE TBL_SEAL_COMBINATION");
                log.info("旧表 TBL_SEAL_COMBINATION 已删除");
            } catch (Exception e) {
                log.error("删除旧表失败: {}", e.getMessage());
            }
        }

        // 创建新表
        if (!checkTableExists("TBL_SEAL_COMBINATION")) {
            log.info("开始创建 TBL_SEAL_COMBINATION 表...");
            createTblSealCombinationTable();
            log.info("表 TBL_SEAL_COMBINATION 创建成功");
        } else {
            log.info("表 TBL_SEAL_COMBINATION 已存在且结构完整,跳过创建");
        }

        // 检查序列是否存在
        if (!checkSequenceExists("SEQ_SEAL_COMBINATION_ID")) {
            log.info("序列 SEQ_SEAL_COMBINATION_ID 不存在,开始创建...");
            createSealCombinationSequence();
            log.info("序列 SEQ_SEAL_COMBINATION_ID 创建成功");
        } else {
            log.info("序列 SEQ_SEAL_COMBINATION_ID 已存在,跳过创建");
        }

        // 检查并插入测试数据到 TBL_SEAL_COMBINATION 表
        long count = queryTblSealCombinationCount();
        if (count == 0) {
            log.info("表 TBL_SEAL_COMBINATION 为空,开始插入测试数据...");
            insertTblSealCombinationTestData();
            log.info("测试数据插入成功");
        } else {
            log.info("表 TBL_SEAL_COMBINATION 已有 {} 条数据,跳过测试数据插入", count);
        }
    }

    /**
     * 检查表是否包含所有必需的列
     */
    private boolean checkTableHasRequiredColumns(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME IN ('SEAL_LIST', 'DESCRIPTION', 'COMBINATION_TYPE', 'BUSINESS_TYPE', 'AUTHORITY_LEVEL', 'MAX_AMOUNT_LIMIT', 'USAGE_COUNT', 'IS_ENABLED')";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName.toUpperCase());
            return count != null && count >= 8;
        } catch (Exception e) {
            log.debug("检查表列时出错: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查并添加 TC_SEAL_COMBINATION 表缺失的列
     */
    private void addMissingColumnsToTcSealCombination() {
        try {
            // 需要添加的列定义
            String[] columnsToAdd = {
                "COMBINATION_TYPE VARCHAR(50)",
                "BUSINESS_TYPE VARCHAR(50)",
                "AUTHORITY_LEVEL VARCHAR(50)",
                "MAX_AMOUNT_LIMIT DECIMAL(20,2)",
                "USAGE_COUNT INTEGER DEFAULT 0",
                "IS_ENABLED INTEGER DEFAULT 1",
                "SEAL_LIST CLOB",
                "DESCRIPTION CLOB"
            };

            // 列注释
            String[] columnComments = {
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.COMBINATION_TYPE IS '组合类型(SINGLE_SEAL/DUAL_SEAL/TRIPLE_SEAL/MULTIPLE_SEAL/SPECIAL_COMBINATION)'",
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.BUSINESS_TYPE IS '业务类型(FUND_TRANSFER/INVESTMENT_TRANSACTION/BILL_BUSINESS/CONTRACT_SIGNING/AUTHORIZATION_APPROVAL)'",
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.AUTHORITY_LEVEL IS '权限级别(LEVEL_1/LEVEL_2/LEVEL_3/SPECIAL)'",
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.MAX_AMOUNT_LIMIT IS '最大金额限制'",
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.USAGE_COUNT IS '使用次数统计'",
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.IS_ENABLED IS '是否启用(1启用,0禁用)'",
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.SEAL_LIST IS '印章列表JSON格式'",
                "COMMENT ON COLUMN TC_SEAL_COMBINATION.DESCRIPTION IS '组合描述'"
            };

            // 列名
            String[] columnNames = {
                "COMBINATION_TYPE", "BUSINESS_TYPE", "AUTHORITY_LEVEL",
                "MAX_AMOUNT_LIMIT", "USAGE_COUNT", "IS_ENABLED",
                "SEAL_LIST", "DESCRIPTION"
            };

            for (int i = 0; i < columnsToAdd.length; i++) {
                try {
                    // 尝试添加列(如果列已存在会报错,我们捕获并忽略)
                    String alterSql = "ALTER TABLE TC_SEAL_COMBINATION ADD " + columnsToAdd[i];
                    jdbcTemplate.execute(alterSql);
                    log.info("成功添加列: {}", columnNames[i]);

                    // 添加列注释
                    jdbcTemplate.execute(columnComments[i]);
                    log.info("成功添加列注释: {}", columnNames[i]);
                } catch (Exception e) {
                    // 列已存在或其他错误,继续处理下一列
                    log.debug("列 {} 可能已存在或添加失败: {}", columnNames[i], e.getMessage());
                }
            }

            // 尝试创建索引
            try {
                jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS IDX_COMBINATION_TYPE ON TC_SEAL_COMBINATION(COMBINATION_TYPE)");
                jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS IDX_BUSINESS_TYPE ON TC_SEAL_COMBINATION(BUSINESS_TYPE)");
                log.info("成功创建或更新索引");
            } catch (Exception e) {
                log.debug("创建索引时出现错误(可能已存在): {}", e.getMessage());
            }

        } catch (Exception e) {
            log.warn("检查并添加 TC_SEAL_COMBINATION 表列时出现错误: {}", e.getMessage());
        }
    }

    /**
     * 创建 TC_SEAL_COMBINATION 表(新版本,包含完整字段)
     */
    private void createTblSealCombinationTable() {
        String sql = "CREATE TABLE TBL_SEAL_COMBINATION (" +
                "ID BIGINT PRIMARY KEY, " +
                "COMBINATION_NAME VARCHAR(200) NOT NULL, " +
                "COMBINATION_CODE VARCHAR(100) NOT NULL UNIQUE, " +
                "COMBINATION_TYPE VARCHAR(50), " +
                "BUSINESS_TYPE VARCHAR(50), " +
                "AUTHORITY_LEVEL VARCHAR(50), " +
                "MAX_AMOUNT_LIMIT DECIMAL(20,2), " +
                "USAGE_COUNT INTEGER DEFAULT 0, " +
                "SEAL_LIST CLOB, " +
                "DESCRIPTION CLOB, " +
                "IS_ENABLED INTEGER DEFAULT 1, " +
                "STATUS INTEGER DEFAULT 1, " +
                "CREATE_BY VARCHAR(100), " +
                "CREATE_TIME TIMESTAMP, " +
                "UPDATE_BY VARCHAR(100), " +
                "UPDATE_TIME TIMESTAMP, " +
                "REMARK VARCHAR(500)" +
                ")";
        jdbcTemplate.execute(sql);

        // 添加表注释
        jdbcTemplate.execute("COMMENT ON TABLE TBL_SEAL_COMBINATION IS '印鉴组合配置表'");

        // 添加列注释
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.ID IS '主键ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.COMBINATION_NAME IS '组合名称'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.COMBINATION_CODE IS '组合编码(唯一)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.COMBINATION_TYPE IS '组合类型(SINGLE_SEAL/DUAL_SEAL/TRIPLE_SEAL/MULTIPLE_SEAL/SPECIAL)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.BUSINESS_TYPE IS '业务类型(FUND_TRANSFER/INVESTMENT_TRANSACTION/BILL_BUSINESS/CONTRACT_SIGNING/AUTHORIZATION_APPROVAL)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.AUTHORITY_LEVEL IS '权限级别(LEVEL_1/LEVEL_2/LEVEL_3/SPECIAL)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.MAX_AMOUNT_LIMIT IS '最大金额限制'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.USAGE_COUNT IS '使用次数统计'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.SEAL_LIST IS '印章列表JSON格式'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.DESCRIPTION IS '组合描述'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.IS_ENABLED IS '是否启用(1启用,0禁用)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.STATUS IS '状态(1正常,0删除)'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.CREATE_BY IS '创建人'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.CREATE_TIME IS '创建时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.UPDATE_BY IS '更新人'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.UPDATE_TIME IS '更新时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.REMARK IS '备注'");

        // 创建索引
        jdbcTemplate.execute("CREATE INDEX IDX_TBL_COMBINATION_CODE ON TBL_SEAL_COMBINATION(COMBINATION_CODE)");
        jdbcTemplate.execute("CREATE INDEX IDX_TBL_COMBINATION_TYPE ON TBL_SEAL_COMBINATION(COMBINATION_TYPE)");
        jdbcTemplate.execute("CREATE INDEX IDX_TBL_BUSINESS_TYPE ON TBL_SEAL_COMBINATION(BUSINESS_TYPE)");
        jdbcTemplate.execute("CREATE INDEX IDX_TBL_STATUS ON TBL_SEAL_COMBINATION(STATUS)");
        jdbcTemplate.execute("CREATE INDEX IDX_TBL_CREATE_TIME ON TBL_SEAL_COMBINATION(CREATE_TIME)");
    }

    /**
     * 查询 TBL_SEAL_COMBINATION 表记录数
     */
    private long queryTblSealCombinationCount() {
        String sql = "SELECT COUNT(*) FROM TBL_SEAL_COMBINATION";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 插入 TBL_SEAL_COMBINATION 测试数据
     */
    private void insertTblSealCombinationTestData() {
        String[] testData = {
            "(1, '法人章单一组合', 'SEAL_SINGLE_001', 'SINGLE_SEAL', 'FUND_TRANSFER', 'LEVEL_1', 500000.00, 15, '[{\"sealId\":\"1\",\"sealName\":\"法人章\"}]', '仅法人章的单一组合,用于小额资金划转', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '标准单一印鉴组合')",
            "(2, '法人章+财务章双组合', 'SEAL_DUAL_001', 'DUAL_SEAL', 'FUND_TRANSFER', 'LEVEL_2', 1000000.00, 28, '[{\"sealId\":\"1\",\"sealName\":\"法人章\"},{\"sealId\":\"2\",\"sealName\":\"财务章\"}]', '法人章和财务章的双章组合,用于中等金额资金划转', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '标准双印鉴组合')",
            "(3, '三章组合', 'SEAL_TRIPLE_001', 'TRIPLE_SEAL', 'INVESTMENT_TRANSACTION', 'LEVEL_3', 5000000.00, 8, '[{\"sealId\":\"1\",\"sealName\":\"法人章\"},{\"sealId\":\"2\",\"sealName\":\"财务章\"},{\"sealId\":\"3\",\"sealName\":\"合同章\"}]', '法人章、财务章和合同章的三章组合,用于投资交易', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '高级印鉴组合')",
            "(4, '票据业务专用组合', 'SEAL_BILL_001', 'MULTIPLE_SEAL', 'BILL_BUSINESS', 'LEVEL_2', 2000000.00, 12, '[{\"sealId\":\"2\",\"sealName\":\"财务章\"},{\"sealId\":\"4\",\"sealName\":\"发票章\"}]', '财务章和发票章的组合,专门用于票据业务', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '票据业务专用')",
            "(5, '合同签署特殊组合', 'SEAL_CONTRACT_001', 'SPECIAL_COMBINATION', 'CONTRACT_SIGNING', 'SPECIAL', 10000000.00, 5, '[{\"sealId\":\"1\",\"sealName\":\"法人章\"},{\"sealId\":\"3\",\"sealName\":\"合同章\"},{\"sealId\":\"5\",\"sealName\":\"公章\"}]', '包含法人章、合同章和公章的特殊组合,用于重要合同签署', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '特殊权限组合')",
            "(6, '授权审批组合', 'SEAL_AUTH_001', 'DUAL_SEAL', 'AUTHORIZATION_APPROVAL', 'LEVEL_2', 3000000.00, 18, '[{\"sealId\":\"1\",\"sealName\":\"法人章\"},{\"sealId\":\"5\",\"sealName\":\"公章\"}]', '法人章和公章组合,用于授权审批', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '授权审批专用')",
            "(7, '资金划转一级组合', 'SEAL_FUND_L1_001', 'SINGLE_SEAL', 'FUND_TRANSFER', 'LEVEL_1', 100000.00, 45, '[{\"sealId\":\"2\",\"sealName\":\"财务章\"}]', '仅财务章,用于小额资金划转', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '一级权限')",
            "(8, '投资交易高级组合', 'SEAL_INVEST_H_001', 'MULTIPLE_SEAL', 'INVESTMENT_TRANSACTION', 'SPECIAL', 10000000.00, 3, '[{\"sealId\":\"1\",\"sealName\":\"法人章\"},{\"sealId\":\"2\",\"sealName\":\"财务章\"},{\"sealId\":\"3\",\"sealName\":\"合同章\"},{\"sealId\":\"5\",\"sealName\":\"公章\"}]', '全套印章组合,用于大额投资交易', 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, '最高级别组合')"
        };

        for (String data : testData) {
            String sql = "INSERT INTO TBL_SEAL_COMBINATION (ID, COMBINATION_NAME, COMBINATION_CODE, COMBINATION_TYPE, BUSINESS_TYPE, AUTHORITY_LEVEL, MAX_AMOUNT_LIMIT, USAGE_COUNT, SEAL_LIST, DESCRIPTION, IS_ENABLED, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) VALUES " + data;
            jdbcTemplate.execute(sql);
        }
    }

    /**
     * 创建印鉴组合配置表
     */
    private void createSealCombinationTable() {
        String sql = "CREATE TABLE TBL_SEAL_COMBINATION (" +
                "ID BIGINT PRIMARY KEY, " +
                "COMBINATION_NAME VARCHAR(100) NOT NULL, " +
                "COMBINATION_CODE VARCHAR(50) NOT NULL, " +
                "SEAL_LIST VARCHAR(2000), " +
                "DESCRIPTION VARCHAR(500), " +
                "STATUS INTEGER DEFAULT 1, " +
                "CREATE_BY VARCHAR(50), " +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATE_BY VARCHAR(50), " +
                "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "REMARK VARCHAR(500), " +
                "CONSTRAINT UK_SEAL_COMBINATION_CODE UNIQUE (COMBINATION_CODE)" +
                ")";
        jdbcTemplate.execute(sql);

        // 添加表注释
        jdbcTemplate.execute("COMMENT ON TABLE TBL_SEAL_COMBINATION IS '印鉴组合配置表'");

        // 添加列注释
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.ID IS '主键ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.COMBINATION_NAME IS '组合名称'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.COMBINATION_CODE IS '组合编码'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.SEAL_LIST IS '印章列表JSON'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.DESCRIPTION IS '组合描述'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.STATUS IS '状态:1启用,0禁用'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.CREATE_BY IS '创建人'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.CREATE_TIME IS '创建时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.UPDATE_BY IS '更新人'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.UPDATE_TIME IS '更新时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_COMBINATION.REMARK IS '备注'");

        // 创建索引
        jdbcTemplate.execute("CREATE INDEX IDX_SEAL_COMBINATION_NAME ON TBL_SEAL_COMBINATION(COMBINATION_NAME)");
        jdbcTemplate.execute("CREATE INDEX IDX_SEAL_COMBINATION_STATUS ON TBL_SEAL_COMBINATION(STATUS)");
        jdbcTemplate.execute("CREATE INDEX IDX_SEAL_COMBINATION_CREATE_TIME ON TBL_SEAL_COMBINATION(CREATE_TIME)");
    }

    /**
     * 创建印鉴组合序列
     */
    private void createSealCombinationSequence() {
        String sql = "CREATE SEQUENCE SEQ_SEAL_COMBINATION_ID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE";
        jdbcTemplate.execute(sql);
    }

    /**
     * 查询印鉴组合表记录数
     */
    private long querySealCombinationCount() {
        String sql = "SELECT COUNT(*) FROM TBL_SEAL_COMBINATION";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 插入印鉴组合测试数据
     */
    private void insertSealCombinationTestData() {
        // 测试数据1: 财务章+法人章组合
        String sql1 = "INSERT INTO TBL_SEAL_COMBINATION (ID, COMBINATION_NAME, COMBINATION_CODE, SEAL_LIST, DESCRIPTION, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_SEAL_COMBINATION_ID.NEXTVAL, '财务章+法人章组合', 'FINANCE_LEGAL_001', '[\"财务专用章\", \"法人章\"]', '用于财务相关业务的双印章组合', 1, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '标准财务印章组合')";
        jdbcTemplate.execute(sql1);

        // 测试数据2: 公章+财务章组合
        String sql2 = "INSERT INTO TBL_SEAL_COMBINATION (ID, COMBINATION_NAME, COMBINATION_CODE, SEAL_LIST, DESCRIPTION, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_SEAL_COMBINATION_ID.NEXTVAL, '公章+财务章组合', 'OFFICIAL_FINANCE_001', '[\"公章\", \"财务专用章\"]', '用于重要合同签署的公章与财务章组合', 1, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '高级授权组合')";
        jdbcTemplate.execute(sql2);

        // 测试数据3: 合同专用章组合
        String sql3 = "INSERT INTO TBL_SEAL_COMBINATION (ID, COMBINATION_NAME, COMBINATION_CODE, SEAL_LIST, DESCRIPTION, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_SEAL_COMBINATION_ID.NEXTVAL, '合同专用章组合', 'CONTRACT_SEAL_001', '[\"合同专用章\", \"法人章\"]', '用于合同业务的标准印章组合', 1, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '合同业务专用')";
        jdbcTemplate.execute(sql3);

        // 测试数据4: 三印章组合
        String sql4 = "INSERT INTO TBL_SEAL_COMBINATION (ID, COMBINATION_NAME, COMBINATION_CODE, SEAL_LIST, DESCRIPTION, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_SEAL_COMBINATION_ID.NEXTVAL, '三印章组合', 'TRIPLE_SEAL_001', '[\"公章\", \"财务专用章\", \"法人章\"]', '高级业务三印章联合使用', 1, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '高级业务组合')";
        jdbcTemplate.execute(sql4);

        // 测试数据5: 发票专用章组合
        String sql5 = "INSERT INTO TBL_SEAL_COMBINATION (ID, COMBINATION_NAME, COMBINATION_CODE, SEAL_LIST, DESCRIPTION, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_SEAL_COMBINATION_ID.NEXTVAL, '发票专用章组合', 'INVOICE_SEAL_001', '[\"发票专用章\", \"财务专用章\"]', '用于发票相关业务', 1, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '发票业务专用')";
        jdbcTemplate.execute(sql5);

        // 测试数据6: 单印章-公章
        String sql6 = "INSERT INTO TBL_SEAL_COMBINATION (ID, COMBINATION_NAME, COMBINATION_CODE, SEAL_LIST, DESCRIPTION, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_SEAL_COMBINATION_ID.NEXTVAL, '单印章-公章', 'SINGLE_OFFICIAL_001', '[\"公章\"]', '单独使用公章', 1, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '单公章使用')";
        jdbcTemplate.execute(sql6);
    }

    /**
     * 初始化印鉴类型表
     */
    private void initSealType() {
        // 检查表是否存在,以及表结构是否正确
        if (checkTableExists("TC_SEAL_TYPE")) {
            // 检查必需的列是否存在
            if (!checkColumnExists("TC_SEAL_TYPE", "TYPE_ID")) {
                log.warn("表 TC_SEAL_TYPE 存在但缺少必需的列,将删除并重建");
                dropTable("TC_SEAL_TYPE");
                log.info("表 TC_SEAL_TYPE 不存在,开始创建...");
                createSealTypeTable();
                log.info("表 TC_SEAL_TYPE 创建成功");
            } else {
                log.info("表 TC_SEAL_TYPE 已存在且结构正确,跳过创建");
            }
        } else {
            log.info("表 TC_SEAL_TYPE 不存在,开始创建...");
            createSealTypeTable();
            log.info("表 TC_SEAL_TYPE 创建成功");
        }

        // 检查并插入测试数据
        long count = querySealTypeCount();
        if (count == 0) {
            log.info("表 TC_SEAL_TYPE 为空,开始插入测试数据...");
            insertSealTypeTestData();
            log.info("测试数据插入成功");
        } else {
            log.info("表 TC_SEAL_TYPE 已有 {} 条数据,跳过测试数据插入", count);
        }
    }

    /**
     * 创建印鉴类型表
     */
    private void createSealTypeTable() {
        String sql = "CREATE TABLE TC_SEAL_TYPE (" +
                "TYPE_ID VARCHAR(50) PRIMARY KEY, " +
                "TYPE_CODE VARCHAR(50) NOT NULL, " +
                "TYPE_NAME VARCHAR(100) NOT NULL, " +
                "IS_ACTIVE INTEGER DEFAULT 1, " +
                "REMARK VARCHAR(500), " +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "CONSTRAINT UK_SEAL_TYPE_CODE UNIQUE (TYPE_CODE)" +
                ")";
        jdbcTemplate.execute(sql);

        // 添加表注释
        jdbcTemplate.execute("COMMENT ON TABLE TC_SEAL_TYPE IS '印鉴类型表'");

        // 添加列注释
        jdbcTemplate.execute("COMMENT ON COLUMN TC_SEAL_TYPE.TYPE_ID IS '类型ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TC_SEAL_TYPE.TYPE_CODE IS '类型编码'");
        jdbcTemplate.execute("COMMENT ON COLUMN TC_SEAL_TYPE.TYPE_NAME IS '类型名称'");
        jdbcTemplate.execute("COMMENT ON COLUMN TC_SEAL_TYPE.IS_ACTIVE IS '是否启用:1启用,0禁用'");
        jdbcTemplate.execute("COMMENT ON COLUMN TC_SEAL_TYPE.REMARK IS '备注'");
        jdbcTemplate.execute("COMMENT ON COLUMN TC_SEAL_TYPE.CREATE_TIME IS '创建时间'");

        // 创建索引
        jdbcTemplate.execute("CREATE INDEX IDX_SEAL_TYPE_CODE ON TC_SEAL_TYPE(TYPE_CODE)");
        jdbcTemplate.execute("CREATE INDEX IDX_SEAL_TYPE_NAME ON TC_SEAL_TYPE(TYPE_NAME)");
        jdbcTemplate.execute("CREATE INDEX IDX_SEAL_TYPE_ACTIVE ON TC_SEAL_TYPE(IS_ACTIVE)");
    }

    /**
     * 查询印鉴类型表记录数
     */
    private long querySealTypeCount() {
        try {
            String sql = "SELECT COUNT(*) FROM TC_SEAL_TYPE";
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("查询印鉴类型表记录数失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 插入印鉴类型测试数据
     */
    private void insertSealTypeTestData() {
        // 测试数据1: 公章
        String sql1 = "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_CODE, TYPE_NAME, IS_ACTIVE, REMARK, CREATE_TIME) " +
                "VALUES ('SEQ001', 'OFFICIAL_SEAL', '公章', 1, '企业公章,用于重要文件和合同', CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql1);

        // 测试数据2: 法人章
        String sql2 = "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_CODE, TYPE_NAME, IS_ACTIVE, REMARK, CREATE_TIME) " +
                "VALUES ('SEQ002', 'LEGAL_SEAL', '法人章', 1, '法人代表人名章,用于授权文件', CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql2);

        // 测试数据3: 财务专用章
        String sql3 = "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_CODE, TYPE_NAME, IS_ACTIVE, REMARK, CREATE_TIME) " +
                "VALUES ('SEQ003', 'FINANCE_SEAL', '财务专用章', 1, '财务部门专用章,用于财务相关业务', CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql3);

        // 测试数据4: 合同专用章
        String sql4 = "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_CODE, TYPE_NAME, IS_ACTIVE, REMARK, CREATE_TIME) " +
                "VALUES ('SEQ004', 'CONTRACT_SEAL', '合同专用章', 1, '合同业务专用章', CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql4);

        // 测试数据5: 发票专用章
        String sql5 = "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_CODE, TYPE_NAME, IS_ACTIVE, REMARK, CREATE_TIME) " +
                "VALUES ('SEQ005', 'INVOICE_SEAL', '发票专用章', 1, '发票业务专用章', CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql5);

        // 测试数据6: 人事专用章
        String sql6 = "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_CODE, TYPE_NAME, IS_ACTIVE, REMARK, CREATE_TIME) " +
                "VALUES ('SEQ006', 'HR_SEAL', '人事专用章', 1, '人事部门专用章,用于人事相关文件', CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql6);
    }

    /**
     * 初始化印鉴使用记录表
     */
    private void initSealUsageRecord() {
        // 检查表是否存在,以及表结构是否正确
        if (checkTableExists("TBL_SEAL_USAGE_RECORD")) {
            // 检查必需的列是否存在
            if (!checkColumnExists("TBL_SEAL_USAGE_RECORD", "RECORD_NUMBER")) {
                log.warn("表 TBL_SEAL_USAGE_RECORD 存在但缺少必需的列,将删除并重建");
                dropTable("TBL_SEAL_USAGE_RECORD");
                log.info("表 TBL_SEAL_USAGE_RECORD 不存在,开始创建...");
                createSealUsageRecordTable();
                log.info("表 TBL_SEAL_USAGE_RECORD 创建成功");
            } else {
                log.info("表 TBL_SEAL_USAGE_RECORD 已存在且结构正确,跳过创建");
            }
        } else {
            log.info("表 TBL_SEAL_USAGE_RECORD 不存在,开始创建...");
            createSealUsageRecordTable();
            log.info("表 TBL_SEAL_USAGE_RECORD 创建成功");
        }

        // 检查序列是否存在
        if (!checkSequenceExists("SEQ_SEAL_USAGE_RECORD_ID")) {
            log.info("序列 SEQ_SEAL_USAGE_RECORD_ID 不存在,开始创建...");
            createSealUsageRecordSequence();
            log.info("序列 SEQ_SEAL_USAGE_RECORD_ID 创建成功");
        } else {
            log.info("序列 SEQ_SEAL_USAGE_RECORD_ID 已存在,跳过创建");
        }

        // 检查并插入测试数据
        long count = querySealUsageRecordCount();
        if (count == 0) {
            log.info("表 TBL_SEAL_USAGE_RECORD 为空,开始插入测试数据...");
            insertSealUsageRecordTestData();
            log.info("测试数据插入成功");
        } else {
            log.info("表 TBL_SEAL_USAGE_RECORD 已有 {} 条数据,跳过测试数据插入", count);
        }
    }

    /**
     * 创建印鉴使用记录表
     */
    private void createSealUsageRecordTable() {
        String sql = "CREATE TABLE TBL_SEAL_USAGE_RECORD (" +
                "ID BIGINT PRIMARY KEY, " +
                "RECORD_NUMBER VARCHAR(50) NOT NULL, " +
                "SEAL_ID BIGINT, " +
                "SEAL_CODE VARCHAR(50), " +
                "SEAL_NAME VARCHAR(100), " +
                "OPERATOR_ID VARCHAR(50), " +
                "OPERATOR_NAME VARCHAR(100), " +
                "BUSINESS_TYPE VARCHAR(50), " +
                "BUSINESS_NUMBER VARCHAR(100), " +
                "USAGE_STATUS VARCHAR(20) DEFAULT 'PENDING', " +
                "USAGE_TIME TIMESTAMP, " +
                "USAGE_DESCRIPTION VARCHAR(500), " +
                "APPROVER VARCHAR(50), " +
                "APPROVAL_TIME TIMESTAMP, " +
                "REMARK VARCHAR(500), " +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "CONSTRAINT UK_RECORD_NUMBER UNIQUE (RECORD_NUMBER)" +
                ")";
        jdbcTemplate.execute(sql);

        // 添加表注释
        jdbcTemplate.execute("COMMENT ON TABLE TBL_SEAL_USAGE_RECORD IS '印鉴使用记录表'");

        // 添加列注释
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.ID IS '主键ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.RECORD_NUMBER IS '记录编号'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.SEAL_ID IS '印鉴ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.SEAL_CODE IS '印鉴编码'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.SEAL_NAME IS '印鉴名称'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.OPERATOR_ID IS '使用人员ID'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.OPERATOR_NAME IS '使用人员姓名'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.BUSINESS_TYPE IS '业务类型'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.BUSINESS_NUMBER IS '业务单号'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.USAGE_STATUS IS '使用状态:PENDING-待审批,APPROVED-已批准,REJECTED-已拒绝,USED-已使用'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.USAGE_TIME IS '使用时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.USAGE_DESCRIPTION IS '使用说明'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.APPROVER IS '审批人'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.APPROVAL_TIME IS '审批时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.REMARK IS '备注'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.CREATE_TIME IS '创建时间'");
        jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_USAGE_RECORD.UPDATE_TIME IS '更新时间'");

        // 创建索引
        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SEAL_USAGE_RECORD_NUMBER ON TBL_SEAL_USAGE_RECORD(RECORD_NUMBER)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SEAL_USAGE_SEAL_ID ON TBL_SEAL_USAGE_RECORD(SEAL_ID)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SEAL_USAGE_OPERATOR ON TBL_SEAL_USAGE_RECORD(OPERATOR_ID)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SEAL_USAGE_STATUS ON TBL_SEAL_USAGE_RECORD(USAGE_STATUS)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SEAL_USAGE_TIME ON TBL_SEAL_USAGE_RECORD(USAGE_TIME)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_SEAL_USAGE_CREATE_TIME ON TBL_SEAL_USAGE_RECORD(CREATE_TIME)");
        } catch (Exception e) {
            log.warn("创建索引失败(可能已存在): {}", e.getMessage());
        }
    }

    /**
     * 创建印鉴使用记录序列
     */
    private void createSealUsageRecordSequence() {
        String sql = "CREATE SEQUENCE SEQ_SEAL_USAGE_RECORD_ID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE";
        jdbcTemplate.execute(sql);
    }

    /**
     * 查询印鉴使用记录表记录数
     */
    private long querySealUsageRecordCount() {
        try {
            String sql = "SELECT COUNT(*) FROM TBL_SEAL_USAGE_RECORD";
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("查询印鉴使用记录表记录数失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 插入印鉴使用记录测试数据
     */
    private void insertSealUsageRecordTestData() {
        // 测试数据1
        String sql1 = "INSERT INTO TBL_SEAL_USAGE_RECORD (ID, RECORD_NUMBER, SEAL_ID, SEAL_CODE, SEAL_NAME, OPERATOR_ID, OPERATOR_NAME, BUSINESS_TYPE, BUSINESS_NUMBER, USAGE_STATUS, USAGE_TIME, USAGE_DESCRIPTION, APPROVER, APPROVAL_TIME, REMARK, CREATE_TIME, UPDATE_TIME) " +
                "VALUES (SEQ_SEAL_USAGE_RECORD_ID.NEXTVAL, 'USR20251225001', 1, 'OFFICIAL_SEAL', '公章', 'STAFF001', '张三', '合同签署', 'CONTRACT20251225001', 'APPROVED', TO_TIMESTAMP('2025-12-25 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), '签署销售合同', 'APPROVER001', TO_TIMESTAMP('2025-12-25 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), '正常使用', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql1);

        // 测试数据2
        String sql2 = "INSERT INTO TBL_SEAL_USAGE_RECORD (ID, RECORD_NUMBER, SEAL_ID, SEAL_CODE, SEAL_NAME, OPERATOR_ID, OPERATOR_NAME, BUSINESS_TYPE, BUSINESS_NUMBER, USAGE_STATUS, USAGE_TIME, USAGE_DESCRIPTION, APPROVER, APPROVAL_TIME, REMARK, CREATE_TIME, UPDATE_TIME) " +
                "VALUES (SEQ_SEAL_USAGE_RECORD_ID.NEXTVAL, 'USR20251225002', 2, 'FINANCE_SEAL', '财务专用章', 'STAFF002', '李四', '财务报销', 'EXPENSE20251225001', 'PENDING', NULL, '费用报销申请', NULL, NULL, '待审批', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql2);

        // 测试数据3
        String sql3 = "INSERT INTO TBL_SEAL_USAGE_RECORD (ID, RECORD_NUMBER, SEAL_ID, SEAL_CODE, SEAL_NAME, OPERATOR_ID, OPERATOR_NAME, BUSINESS_TYPE, BUSINESS_NUMBER, USAGE_STATUS, USAGE_TIME, USAGE_DESCRIPTION, APPROVER, APPROVAL_TIME, REMARK, CREATE_TIME, UPDATE_TIME) " +
                "VALUES (SEQ_SEAL_USAGE_RECORD_ID.NEXTVAL, 'USR20251225003', 1, 'OFFICIAL_SEAL', '公章', 'STAFF003', '王五', '证明文件', 'CERT20251225001', 'USED', TO_TIMESTAMP('2025-12-24 15:20:00', 'YYYY-MM-DD HH24:MI:SS'), '开具公司证明', 'APPROVER001', TO_TIMESTAMP('2025-12-24 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '已完成', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql3);

        // 测试数据4
        String sql4 = "INSERT INTO TBL_SEAL_USAGE_RECORD (ID, RECORD_NUMBER, SEAL_ID, SEAL_CODE, SEAL_NAME, OPERATOR_ID, OPERATOR_NAME, BUSINESS_TYPE, BUSINESS_NUMBER, USAGE_STATUS, USAGE_TIME, USAGE_DESCRIPTION, APPROVER, APPROVAL_TIME, REMARK, CREATE_TIME, UPDATE_TIME) " +
                "VALUES (SEQ_SEAL_USAGE_RECORD_ID.NEXTVAL, 'USR20251225004', 3, 'CONTRACT_SEAL', '合同专用章', 'STAFF001', '张三', '合同签署', 'CONTRACT20251225002', 'APPROVED', TO_TIMESTAMP('2025-12-25 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '签署采购合同', 'APPROVER002', TO_TIMESTAMP('2025-12-25 13:30:00', 'YYYY-MM-DD HH24:MI:SS'), '正常使用', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql4);

        // 测试数据5
        String sql5 = "INSERT INTO TBL_SEAL_USAGE_RECORD (ID, RECORD_NUMBER, SEAL_ID, SEAL_CODE, SEAL_NAME, OPERATOR_ID, OPERATOR_NAME, BUSINESS_TYPE, BUSINESS_NUMBER, USAGE_STATUS, USAGE_TIME, USAGE_DESCRIPTION, APPROVER, APPROVAL_TIME, REMARK, CREATE_TIME, UPDATE_TIME) " +
                "VALUES (SEQ_SEAL_USAGE_RECORD_ID.NEXTVAL, 'USR20251225005', 4, 'INVOICE_SEAL', '发票专用章', 'STAFF004', '赵六', '发票开具', 'INVOICE20251225001', 'USED', TO_TIMESTAMP('2025-12-23 16:45:00', 'YYYY-MM-DD HH24:MI:SS'), '开具增值税发票', 'APPROVER001', TO_TIMESTAMP('2025-12-23 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), '已完成', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        jdbcTemplate.execute(sql5);
    }

    /**
     * 初始化司库参数配置表
     */
    private void initTreasuryParameter() {
        // 检查表是否存在
        if (!checkTableExists("TBL_TREASURY_PARAMETER")) {
            log.info("表 TBL_TREASURY_PARAMETER 不存在,开始创建...");
            createTreasuryParameterTable();
            log.info("表 TBL_TREASURY_PARAMETER 创建成功");
        } else {
            log.info("表 TBL_TREASURY_PARAMETER 已存在,跳过创建");
        }

        // 检查序列是否存在
        if (!checkSequenceExists("SEQ_TREASURY_PARAMETER_ID")) {
            log.info("序列 SEQ_TREASURY_PARAMETER_ID 不存在,开始创建...");
            createTreasuryParameterSequence();
            log.info("序列 SEQ_TREASURY_PARAMETER_ID 创建成功");
        } else {
            log.info("序列 SEQ_TREASURY_PARAMETER_ID 已存在,跳过创建");
        }

        // 检查并插入测试数据
        long count = queryTreasuryParameterCount();
        if (count == 0) {
            log.info("表 TBL_TREASURY_PARAMETER 为空,开始插入测试数据...");
            insertTreasuryParameterTestData();
            log.info("测试数据插入成功");
        } else {
            log.info("表 TBL_TREASURY_PARAMETER 已有 {} 条数据,跳过测试数据插入", count);
        }
    }

    /**
     * 创建司库参数配置表
     */
    private void createTreasuryParameterTable() {
        // 先尝试删除可能存在的孤立约束(达梦数据库可能有孤立约束)
        try {
            // 尝试直接删除约束(如果约束存在但没有关联表)
            jdbcTemplate.execute("ALTER TABLE TBL_TREASURY_PARAMETER DROP CONSTRAINT UK_PARAM_CODE");
            log.info("已删除约束UK_PARAM_CODE");
        } catch (Exception e) {
            log.debug("删除约束UK_PARAM_CODE失败(可能不存在): {}", e.getMessage());
        }

        // 尝试删除表(如果存在)
        try {
            jdbcTemplate.execute("DROP TABLE TBL_TREASURY_PARAMETER CASCADE CONSTRAINTS");
            log.info("已删除旧的TBL_TREASURY_PARAMETER表");
        } catch (Exception e) {
            log.debug("删除旧表失败(可能不存在): {}", e.getMessage());
        }

        // 创建新表(不使用CONSTRAINT子句,避免孤立约束问题)
        String sql = "CREATE TABLE TBL_TREASURY_PARAMETER (" +
                "ID BIGINT PRIMARY KEY, " +
                "PARAM_CODE VARCHAR(100) NOT NULL, " +
                "PARAM_NAME VARCHAR(200) NOT NULL, " +
                "PARAM_TYPE VARCHAR(50) NOT NULL, " +
                "PARAM_VALUE VARCHAR(1000), " +
                "DESCRIPTION VARCHAR(500), " +
                "IS_ENABLED NUMBER(1) DEFAULT 1, " +
                "CREATE_BY VARCHAR(50), " +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATE_BY VARCHAR(50), " +
                "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "REMARK VARCHAR(500)" +
                ")";
        jdbcTemplate.execute(sql);

        // 创建唯一索引代替CONSTRAINT
        try {
            jdbcTemplate.execute("CREATE UNIQUE INDEX UK_PARAM_CODE ON TBL_TREASURY_PARAMETER(PARAM_CODE)");
            log.info("已创建唯一索引UK_PARAM_CODE");
        } catch (Exception e) {
            log.debug("唯一索引UK_PARAM_CODE已存在或创建失败: {}", e.getMessage());
        }

        // 添加普通索引
        try {
            jdbcTemplate.execute("CREATE INDEX IDX_PARAM_NAME ON TBL_TREASURY_PARAMETER(PARAM_NAME)");
        } catch (Exception e) {
            log.debug("索引IDX_PARAM_NAME已存在或创建失败: {}", e.getMessage());
        }
        try {
            jdbcTemplate.execute("CREATE INDEX IDX_PARAM_TYPE ON TBL_TREASURY_PARAMETER(PARAM_TYPE)");
        } catch (Exception e) {
            log.debug("索引IDX_PARAM_TYPE已存在或创建失败: {}", e.getMessage());
        }
        try {
            jdbcTemplate.execute("CREATE INDEX IDX_IS_ENABLED ON TBL_TREASURY_PARAMETER(IS_ENABLED)");
        } catch (Exception e) {
            log.debug("索引IDX_IS_ENABLED已存在或创建失败: {}", e.getMessage());
        }
        try {
            jdbcTemplate.execute("CREATE INDEX IDX_CREATE_TIME ON TBL_TREASURY_PARAMETER(CREATE_TIME)");
        } catch (Exception e) {
            log.debug("索引IDX_CREATE_TIME已存在或创建失败: {}", e.getMessage());
        }

        log.info("司库参数配置表及索引创建成功");
    }

    /**
     * 创建司库参数配置序列
     */
    private void createTreasuryParameterSequence() {
        String sql = "CREATE SEQUENCE SEQ_TREASURY_PARAMETER_ID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE";
        jdbcTemplate.execute(sql);
    }

    /**
     * 查询司库参数配置表数据量
     */
    private long queryTreasuryParameterCount() {
        try {
            String sql = "SELECT COUNT(*) FROM TBL_TREASURY_PARAMETER";
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("查询司库参数配置表数据量失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 插入司库参数配置测试数据
     */
    private void insertTreasuryParameterTestData() {
        // 测试数据1
        String sql1 = "INSERT INTO TBL_TREASURY_PARAMETER (ID, PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_TREASURY_PARAMETER_ID.NEXTVAL, 'MAX_LOGIN_RETRY_TIMES', '最大登录重试次数', 'SECURITY', '5', '用户登录失败最大重试次数，超过次数将锁定账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '系统安全参数')";
        jdbcTemplate.execute(sql1);

        // 测试数据2
        String sql2 = "INSERT INTO TBL_TREASURY_PARAMETER (ID, PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_TREASURY_PARAMETER_ID.NEXTVAL, 'SESSION_TIMEOUT_MINUTES', '会话超时时间', 'SYSTEM', '30', '用户会话超时时间，单位：分钟', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '系统参数')";
        jdbcTemplate.execute(sql2);

        // 测试数据3
        String sql3 = "INSERT INTO TBL_TREASURY_PARAMETER (ID, PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_TREASURY_PARAMETER_ID.NEXTVAL, 'DEFAULT_CURRENCY_CODE', '默认币种', 'BUSINESS', 'CNY', '系统默认使用的币种代码', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '业务参数')";
        jdbcTemplate.execute(sql3);

        // 测试数据4
        String sql4 = "INSERT INTO TBL_TREASURY_PARAMETER (ID, PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_TREASURY_PARAMETER_ID.NEXTVAL, 'API_REQUEST_TIMEOUT', 'API请求超时时间', 'INTERFACE', '30000', '外部API接口请求超时时间，单位：毫秒', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '接口参数')";
        jdbcTemplate.execute(sql4);

        // 测试数据5
        String sql5 = "INSERT INTO TBL_TREASURY_PARAMETER (ID, PARAM_CODE, PARAM_NAME, PARAM_TYPE, PARAM_VALUE, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_TREASURY_PARAMETER_ID.NEXTVAL, 'PASSWORD_MIN_LENGTH', '密码最小长度', 'SECURITY', '8', '用户密码最小长度要求', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '安全策略参数')";
        jdbcTemplate.execute(sql5);
    }

    /**
     * 初始化第三方账户管理表
     */
    private void initThirdPartyAccount() {
        try {
            // 检查表是否存在
            int count = queryThirdPartyAccountCount();

            if (count == 0) {
                // 表不存在或无数据,创建表和序列
                createThirdPartyAccountTable();
                createThirdPartyAccountSequence();

                // 插入测试数据
                insertThirdPartyAccountTestData();

                log.info("第三方账户管理表初始化完成");
            } else {
                log.info("第三方账户管理表已存在数据,跳过初始化");
            }
        } catch (Exception e) {
            log.error("第三方账户管理表初始化失败", e);
        }
    }

    /**
     * 查询第三方账户管理表数据量
     */
    private int queryThirdPartyAccountCount() {
        try {
            String sql = "SELECT COUNT(*) FROM TBL_THIRD_PARTY_ACCOUNT";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.debug("查询第三方账户管理表失败,可能表不存在: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 创建第三方账户管理表
     */
    private void createThirdPartyAccountTable() {
        // 先尝试删除可能存在的孤立约束(达梦数据库可能有孤立约束)
        try {
            // 尝试直接删除约束(如果约束存在但没有关联表)
            jdbcTemplate.execute("ALTER TABLE TBL_THIRD_PARTY_ACCOUNT DROP CONSTRAINT UK_ACCOUNT_CODE");
            log.info("已删除约束UK_ACCOUNT_CODE");
        } catch (Exception e) {
            log.debug("删除约束UK_ACCOUNT_CODE失败(可能不存在): {}", e.getMessage());
        }

        // 尝试删除表(如果存在)
        try {
            jdbcTemplate.execute("DROP TABLE TBL_THIRD_PARTY_ACCOUNT CASCADE CONSTRAINTS");
            log.info("已删除旧的TBL_THIRD_PARTY_ACCOUNT表");
        } catch (Exception e) {
            log.debug("删除旧表失败(可能不存在): {}", e.getMessage());
        }

        // 创建新表(不使用CONSTRAINT子句,避免孤立约束问题)
        String sql = "CREATE TABLE TBL_THIRD_PARTY_ACCOUNT (" +
                "ID BIGINT PRIMARY KEY, " +
                "ACCOUNT_CODE VARCHAR(50) NOT NULL, " +
                "ACCOUNT_NAME VARCHAR(200) NOT NULL, " +
                "THIRD_PARTY_SYSTEM VARCHAR(100) NOT NULL, " +
                "ACCOUNT_TYPE VARCHAR(50) NOT NULL, " +
                "ACCOUNT_IDENTIFIER VARCHAR(500), " +
                "AUTH_CONFIG CLOB, " +
                "CONNECTION_STATUS VARCHAR(50) DEFAULT 'PENDING', " +
                "LAST_SYNC_TIME TIMESTAMP, " +
                "DESCRIPTION VARCHAR(500), " +
                "IS_ENABLED NUMBER(1) DEFAULT 1, " +
                "CREATE_BY VARCHAR(50), " +
                "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UPDATE_BY VARCHAR(50), " +
                "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "REMARK VARCHAR(500)" +
                ")";
        jdbcTemplate.execute(sql);
        log.info("第三方账户管理表创建成功");

        // 创建唯一索引代替CONSTRAINT
        try {
            jdbcTemplate.execute("CREATE UNIQUE INDEX UK_ACCOUNT_CODE ON TBL_THIRD_PARTY_ACCOUNT(ACCOUNT_CODE)");
            log.info("已创建唯一索引UK_ACCOUNT_CODE");
        } catch (Exception e) {
            log.debug("唯一索引UK_ACCOUNT_CODE已存在或创建失败: {}", e.getMessage());
        }

        // 添加普通索引
        try {
            jdbcTemplate.execute("CREATE INDEX IDX_ACCOUNT_NAME ON TBL_THIRD_PARTY_ACCOUNT(ACCOUNT_NAME)");
        } catch (Exception e) {
            log.debug("索引IDX_ACCOUNT_NAME已存在或创建失败: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_THIRD_PARTY_SYSTEM ON TBL_THIRD_PARTY_ACCOUNT(THIRD_PARTY_SYSTEM)");
        } catch (Exception e) {
            log.debug("索引IDX_THIRD_PARTY_SYSTEM已存在或创建失败: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_ACCOUNT_TYPE ON TBL_THIRD_PARTY_ACCOUNT(ACCOUNT_TYPE)");
        } catch (Exception e) {
            log.debug("索引IDX_ACCOUNT_TYPE已存在或创建失败: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_CONNECTION_STATUS ON TBL_THIRD_PARTY_ACCOUNT(CONNECTION_STATUS)");
        } catch (Exception e) {
            log.debug("索引IDX_CONNECTION_STATUS已存在或创建失败: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("CREATE INDEX IDX_CREATE_TIME ON TBL_THIRD_PARTY_ACCOUNT(CREATE_TIME)");
        } catch (Exception e) {
            log.debug("索引IDX_CREATE_TIME已存在或创建失败: {}", e.getMessage());
        }

        // 添加表注释
        try {
            jdbcTemplate.execute("COMMENT ON TABLE TBL_THIRD_PARTY_ACCOUNT IS '第三方账户管理表'");
        } catch (Exception e) {
            log.debug("添加表注释失败: {}", e.getMessage());
        }

        // 添加字段注释
        try {
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.ID IS '主键ID'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.ACCOUNT_CODE IS '账户编码'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.ACCOUNT_NAME IS '账户名称'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.THIRD_PARTY_SYSTEM IS '第三方系统'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.ACCOUNT_TYPE IS '账户类型'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.ACCOUNT_IDENTIFIER IS '账户标识'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.AUTH_CONFIG IS '授权配置'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.CONNECTION_STATUS IS '连接状态'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.LAST_SYNC_TIME IS '最后同步时间'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.DESCRIPTION IS '描述'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.IS_ENABLED IS '是否启用'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.CREATE_BY IS '创建人'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.CREATE_TIME IS '创建时间'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.UPDATE_BY IS '更新人'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.UPDATE_TIME IS '更新时间'");
            jdbcTemplate.execute("COMMENT ON COLUMN TBL_THIRD_PARTY_ACCOUNT.REMARK IS '备注'");
        } catch (Exception e) {
            log.debug("添加字段注释失败: {}", e.getMessage());
        }

        log.info("第三方账户管理表及索引创建成功");
    }

    /**
     * 创建第三方账户管理表序列
     */
    private void createThirdPartyAccountSequence() {
        try {
            // 先尝试删除序列
            jdbcTemplate.execute("DROP SEQUENCE SEQ_THIRD_PARTY_ACCOUNT_ID");
            log.debug("已删除旧的SEQ_THIRD_PARTY_ACCOUNT_ID序列");
        } catch (Exception e) {
            log.debug("删除旧序列失败(可能不存在): {}", e.getMessage());
        }

        // 创建序列
        String sql = "CREATE SEQUENCE SEQ_THIRD_PARTY_ACCOUNT_ID START WITH 1 INCREMENT BY 1 NOCACHE";
        jdbcTemplate.execute(sql);
        log.info("序列 SEQ_THIRD_PARTY_ACCOUNT_ID 创建成功");
    }

    /**
     * 插入第三方账户管理表测试数据
     */
    private void insertThirdPartyAccountTestData() {
        // 测试数据1 - 工商银行对公账户
        String sql1 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'ICBC_CORP_001', '工商银行对公账户', 'ICBC', 'CHECKING', 'ICBC_ACC_1234567890', '{\"appKey\": \"test_key_001\", \"appSecret\": \"***\"}', 'SYNCHRONIZED', CURRENT_TIMESTAMP, '工商银行企业结算账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '主要结算账户')";
        jdbcTemplate.execute(sql1);

        // 测试数据2 - 建设银行储蓄账户
        String sql2 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'CCB_SAV_001', '建设银行储蓄账户', 'CCB', 'SAVINGS', 'CCB_ACC_9876543210', '{\"appKey\": \"test_key_002\", \"appSecret\": \"***\"}', 'SYNCHRONIZED', CURRENT_TIMESTAMP, '建设银行企业储蓄账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '备用资金账户')";
        jdbcTemplate.execute(sql2);

        // 测试数据3 - 农业银行信用账户
        String sql3 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'ABC_CREDIT_001', '农业银行信用账户', 'ABC', 'CREDIT', 'ABC_ACC_5555666677', '{\"appKey\": \"test_key_003\", \"appSecret\": \"***\"}', 'SYNCHRONIZED', CURRENT_TIMESTAMP, '农业银行授信账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '信贷业务账户')";
        jdbcTemplate.execute(sql3);

        // 测试数据4 - 中国银行对公账户
        String sql4 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'BOC_CORP_001', '中国银行对公账户', 'BOC', 'CHECKING', 'BOC_ACC_1111222233', '{\"appKey\": \"test_key_004\", \"appSecret\": \"***\"}', 'PENDING', NULL, '中国银行外汇结算账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '外汇业务专用')";
        jdbcTemplate.execute(sql4);

        // 测试数据5 - 工商银行储蓄账户
        String sql5 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'ICBC_SAV_002', '工商银行储蓄账户', 'ICBC', 'SAVINGS', 'ICBC_ACC_9999888877', '{\"appKey\": \"test_key_005\", \"appSecret\": \"***\"}', 'SYNCHRONIZED', CURRENT_TIMESTAMP, '工商银行定期存款账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '定期存款专用')";
        jdbcTemplate.execute(sql5);

        // 测试数据6 - 建设银行对公账户
        String sql6 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'CCB_CORP_002', '建设银行对公账户', 'CCB', 'CHECKING', 'CCB_ACC_6666777788', '{\"appKey\": \"test_key_006\", \"appSecret\": \"***\"}', 'FAILED', CURRENT_TIMESTAMP, '建设银行结算账户', 0, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '暂时停用')";
        jdbcTemplate.execute(sql6);

        // 测试数据7 - 农业银行对公账户
        String sql7 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'ABC_CORP_003', '农业银行对公账户', 'ABC', 'CHECKING', 'ABC_ACC_4444333322', '{\"appKey\": \"test_key_007\", \"appSecret\": \"***\"}', 'SYNCHRONIZED', CURRENT_TIMESTAMP, '农业银行基本账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '基本存款账户')";
        jdbcTemplate.execute(sql7);

        // 测试数据8 - 中国银行储蓄账户
        String sql8 = "INSERT INTO TBL_THIRD_PARTY_ACCOUNT (ID, ACCOUNT_CODE, ACCOUNT_NAME, THIRD_PARTY_SYSTEM, ACCOUNT_TYPE, ACCOUNT_IDENTIFIER, AUTH_CONFIG, CONNECTION_STATUS, LAST_SYNC_TIME, DESCRIPTION, IS_ENABLED, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK) " +
                "VALUES (SEQ_THIRD_PARTY_ACCOUNT_ID.NEXTVAL, 'BOC_SAV_004', '中国银行储蓄账户', 'BOC', 'SAVINGS', 'BOC_ACC_2222111100', '{\"appKey\": \"test_key_008\", \"appSecret\": \"***\"}', 'PENDING', NULL, '中国银行通知存款账户', 1, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, '通知存款账户')";
        jdbcTemplate.execute(sql8);

        log.info("第三方账户管理表测试数据插入成功");
    }
}
