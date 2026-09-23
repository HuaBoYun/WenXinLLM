package com.global.treasurer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库表自动初始化器
 * 在服务启动时检查并自动创建不存在的表
 *
 * @author 华博云开发团队
 * @since 2024-12-24
 */
//@Component  # 已禁用，避免数据库依赖导致启动失败
public class DatabaseTableInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DatabaseTableInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始检查数据库表结构...");

        try (Connection connection = dataSource.getConnection()) {
            // 检查 TC_ETICKET_ACCOUNT_V2 表是否存在
            if (!isTableExists(connection, "TC_ETICKET_ACCOUNT_V2")) {
                log.warn("表 TC_ETICKET_ACCOUNT_V2 不存在,准备自动创建...");
                createETicketAccountV2Table(connection);
                log.info("表 TC_ETICKET_ACCOUNT_V2 创建成功!");
            } else {
                log.info("表 TC_ETICKET_ACCOUNT_V2 已存在,跳过创建");
            }

            // 检查 TC_SEAL_TYPE 表是否存在
            if (!isTableExists(connection, "TC_SEAL_TYPE")) {
                log.warn("表 TC_SEAL_TYPE 不存在,准备自动创建...");
                createSealTypeTable(connection);
                log.info("表 TC_SEAL_TYPE 创建成功!");
            } else {
                log.info("表 TC_SEAL_TYPE 已存在,跳过创建");
            }

            // 检查 TC_SEAL_ARCHIVE_V2 表是否存在
            if (!isTableExists(connection, "TC_SEAL_ARCHIVE_V2")) {
                log.warn("表 TC_SEAL_ARCHIVE_V2 不存在,准备自动创建...");
                createSealArchiveV2Table(connection);
                log.info("表 TC_SEAL_ARCHIVE_V2 创建成功!");
            } else {
                log.info("表 TC_SEAL_ARCHIVE_V2 已存在,跳过创建");
            }
        } catch (SQLException e) {
            log.error("检查数据库表结构失败", e);
        }
    }

    /**
     * 检查表是否存在
     */
    private boolean isTableExists(Connection connection, String tableName) {
        try {
            // 达梦数据库查询表是否存在的方式
            String sql = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = ?";
            Integer count = new JdbcTemplate(dataSource).queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("检查表 {} 是否存在时出错", tableName, e);
            return false;
        }
    }

    /**
     * 创建 TC_ETICKET_ACCOUNT_V2 表
     */
    private void createETicketAccountV2Table(Connection connection) {
        try {
            String createTableSQL = "CREATE TABLE TC_ETICKET_ACCOUNT_V2 (" +
                    "    ETICKET_ACCOUNT_ID DECIMAL(20,0) NOT NULL," +
                    "    ACCOUNT_NUMBER VARCHAR(50) NOT NULL," +
                    "    ACCOUNT_NAME VARCHAR(200) NOT NULL," +
                    "    ETICKET_SYSTEM VARCHAR(50) NOT NULL," +
                    "    ACCOUNT_TYPE VARCHAR(50) NOT NULL," +
                    "    BANK_NAME VARCHAR(200)," +
                    "    BRANCH_NAME VARCHAR(200)," +
                    "    BANK_CODE VARCHAR(50) NOT NULL," +
                    "    BANK_ACCOUNT_NUMBER VARCHAR(100) NOT NULL," +
                    "    ACCOUNT_BALANCE DECIMAL(18,2) DEFAULT 0.00," +
                    "    CREDIT_LIMIT DECIMAL(18,2) DEFAULT 0.00," +
                    "    OPEN_DATE DATE," +
                    "    ACCOUNT_STATUS VARCHAR(20) DEFAULT 'NORMAL'," +
                    "    IS_ACTIVE INTEGER DEFAULT 1," +
                    "    CONTACT_PERSON VARCHAR(100)," +
                    "    CONTACT_PHONE VARCHAR(50)," +
                    "    CONTACT_EMAIL VARCHAR(100)," +
                    "    REMARK VARCHAR(500)," +
                    "    DESCRIPTION VARCHAR(1000)," +
                    "    LAST_SYNC_TIME TIMESTAMP," +
                    "    SYNC_STATUS VARCHAR(20) DEFAULT 'PENDING'," +
                    "    ORG_ID DECIMAL(20,0)," +
                    "    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "    CREATE_USER VARCHAR(50)," +
                    "    UPDATE_USER VARCHAR(50)," +
                    "    CONSTRAINT PK_ETICKET_ACCOUNT_V2 PRIMARY KEY (ETICKET_ACCOUNT_ID)" +
                    ")";

            // 执行创建表语句
            new JdbcTemplate(dataSource).execute(createTableSQL);

            // 添加表注释
            String tableComment = "COMMENT ON TABLE TC_ETICKET_ACCOUNT_V2 IS '电票账户配置表 V2版本'";
            new JdbcTemplate(dataSource).execute(tableComment);

            // 添加列注释
            addColumnComments();

            // 创建索引
            createIndexes();

            // 插入测试数据
            insertTestData();

            log.info("TC_ETICKET_ACCOUNT_V2 表及其索引、测试数据创建完成");

        } catch (Exception e) {
            log.error("创建 TC_ETICKET_ACCOUNT_V2 表失败", e);
            throw new RuntimeException("创建 TC_ETICKET_ACCOUNT_V2 表失败", e);
        }
    }

    /**
     * 添加列注释
     */
    private void addColumnComments() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] columnComments = {
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ETICKET_ACCOUNT_ID IS '电票账户ID(主键)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ACCOUNT_NUMBER IS '账户编号'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ACCOUNT_NAME IS '账户名称'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ETICKET_SYSTEM IS '电票系统(ECDS/BECP/BANK_ETICKET)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ACCOUNT_TYPE IS '账户类型(ACCEPTANCE/DISCOUNT/REDISCOUNT/PLEDGE/CUSTODY/MARGIN)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.BANK_NAME IS '开户银行名称'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.BRANCH_NAME IS '分支机构名称'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.BANK_CODE IS '银行编码(ICBC/CCB/ABC/BOC/BOCOM/CMB等)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.BANK_ACCOUNT_NUMBER IS '银行账号'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ACCOUNT_BALANCE IS '账户余额'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.CREDIT_LIMIT IS '授信额度(万元)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.OPEN_DATE IS '开户日期'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ACCOUNT_STATUS IS '账户状态(NORMAL正常/FROZEN冻结/CLOSED关闭)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.IS_ACTIVE IS '是否启用(1启用/0禁用)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.CONTACT_PERSON IS '联系人姓名'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.CONTACT_PHONE IS '联系电话'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.CONTACT_EMAIL IS '联系邮箱'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.REMARK IS '备注信息'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.DESCRIPTION IS '描述信息'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.LAST_SYNC_TIME IS '最后同步时间'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.SYNC_STATUS IS '同步状态(SUCCESS成功/FAILED失败/PENDING待同步)'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.ORG_ID IS '组织ID'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.CREATE_TIME IS '创建时间'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.UPDATE_TIME IS '更新时间'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.CREATE_USER IS '创建人'",
                "COMMENT ON COLUMN TC_ETICKET_ACCOUNT_V2.UPDATE_USER IS '更新人'"
        };

        for (String comment : columnComments) {
            try {
                jdbcTemplate.execute(comment);
            } catch (Exception e) {
                log.warn("添加列注释失败: {}", comment, e);
            }
        }
    }

    /**
     * 创建索引
     */
    private void createIndexes() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] indexes = {
                "CREATE UNIQUE INDEX IDX_ETICKET_ACCOUNT_V2_NUMBER ON TC_ETICKET_ACCOUNT_V2(ACCOUNT_NUMBER)",
                "CREATE INDEX IDX_ETICKET_ACCOUNT_V2_NAME ON TC_ETICKET_ACCOUNT_V2(ACCOUNT_NAME)",
                "CREATE INDEX IDX_ETICKET_ACCOUNT_V2_SYSTEM ON TC_ETICKET_ACCOUNT_V2(ETICKET_SYSTEM)",
                "CREATE INDEX IDX_ETICKET_ACCOUNT_V2_TYPE ON TC_ETICKET_ACCOUNT_V2(ACCOUNT_TYPE)",
                "CREATE INDEX IDX_ETICKET_ACCOUNT_V2_BANK_CODE ON TC_ETICKET_ACCOUNT_V2(BANK_CODE)",
                "CREATE INDEX IDX_ETICKET_ACCOUNT_V2_STATUS ON TC_ETICKET_ACCOUNT_V2(ACCOUNT_STATUS)",
                "CREATE INDEX IDX_ETICKET_ACCOUNT_V2_ORG_ID ON TC_ETICKET_ACCOUNT_V2(ORG_ID)",
                "CREATE INDEX IDX_ETICKET_ACCOUNT_V2_CREATE_TIME ON TC_ETICKET_ACCOUNT_V2(CREATE_TIME)"
        };

        for (String index : indexes) {
            try {
                jdbcTemplate.execute(index);
            } catch (Exception e) {
                log.warn("创建索引失败: {}", index, e);
            }
        }
    }

    /**
     * 插入测试数据
     */
    private void insertTestData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] testData = {
                "INSERT INTO TC_ETICKET_ACCOUNT_V2 (ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_SYSTEM, ACCOUNT_TYPE, BANK_NAME, BRANCH_NAME, BANK_CODE, BANK_ACCOUNT_NUMBER, ACCOUNT_BALANCE, CREDIT_LIMIT, OPEN_DATE, ACCOUNT_STATUS, IS_ACTIVE, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL, REMARK, DESCRIPTION, SYNC_STATUS, ORG_ID, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES (1, 'ETK20240001', '工商银行电票账户-测试01', 'ECDS', 'ACCEPTANCE', '中国工商银行', '北京分行营业部', 'ICBC', '6222020200001234567', 500000.00, 1000.00, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'NORMAL', 1, '张三', '13800138000', 'zhangsan@example.com', '测试账户', '用于测试的电票账户配置', 'SUCCESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin')",
                "INSERT INTO TC_ETICKET_ACCOUNT_V2 (ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_SYSTEM, ACCOUNT_TYPE, BANK_NAME, BRANCH_NAME, BANK_CODE, BANK_ACCOUNT_NUMBER, ACCOUNT_BALANCE, CREDIT_LIMIT, OPEN_DATE, ACCOUNT_STATUS, IS_ACTIVE, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL, REMARK, DESCRIPTION, SYNC_STATUS, ORG_ID, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES (2, 'ETK20240002', '建设银行贴现账户-主账户', 'BECP', 'DISCOUNT', '中国建设银行', '上海浦东分行', 'CCB', '6217000012345678901', 1200000.00, 2000.00, TO_DATE('2024-02-20', 'YYYY-MM-DD'), 'NORMAL', 1, '李四', '13800138000', 'lisi@example.com', '主要贴现账户', '用于票据贴现业务的主要账户', 'SUCCESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin')",
                "INSERT INTO TC_ETICKET_ACCOUNT_V2 (ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_SYSTEM, ACCOUNT_TYPE, BANK_NAME, BRANCH_NAME, BANK_CODE, BANK_ACCOUNT_NUMBER, ACCOUNT_BALANCE, CREDIT_LIMIT, OPEN_DATE, ACCOUNT_STATUS, IS_ACTIVE, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL, REMARK, DESCRIPTION, SYNC_STATUS, ORG_ID, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES (3, 'ETK20240003', '农业银行质押账户-备用', 'BANK_ETICKET', 'PLEDGE', '中国农业银行', '广州天河支行', 'ABC', '6228480123456789012', 800000.00, 1500.00, TO_DATE('2024-03-10', 'YYYY-MM-DD'), 'FROZEN', 0, '王五', '13800138000', 'wangwu@example.com', '质押账户', '用于票据质押的账户,暂时冻结', 'PENDING', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin')",
                "INSERT INTO TC_ETICKET_ACCOUNT_V2 (ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_SYSTEM, ACCOUNT_TYPE, BANK_NAME, BRANCH_NAME, BANK_CODE, BANK_ACCOUNT_NUMBER, ACCOUNT_BALANCE, CREDIT_LIMIT, OPEN_DATE, ACCOUNT_STATUS, IS_ACTIVE, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL, REMARK, DESCRIPTION, SYNC_STATUS, ORG_ID, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES (4, 'ETK20240004', '中国银行转贴现账户', 'ECDS', 'REDISCOUNT', '中国银行', '深圳福田分行', 'BOC', '621661000012345678', 2000000.00, 3000.00, TO_DATE('2024-04-05', 'YYYY-MM-DD'), 'NORMAL', 1, '赵六', '13800138000', 'zhaoliu@example.com', '转贴现专用', '用于转贴现业务的专用账户', 'SUCCESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin')",
                "INSERT INTO TC_ETICKET_ACCOUNT_V2 (ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_SYSTEM, ACCOUNT_TYPE, BANK_NAME, BRANCH_NAME, BANK_CODE, BANK_ACCOUNT_NUMBER, ACCOUNT_BALANCE, CREDIT_LIMIT, OPEN_DATE, ACCOUNT_STATUS, IS_ACTIVE, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL, REMARK, DESCRIPTION, SYNC_STATUS, ORG_ID, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES (5, 'ETK20240005', '交通银行托管账户', 'BECP', 'CUSTODY', '交通银行', '杭州西湖支行', 'BOCOM', '622252000012345678', 3500000.00, 5000.00, TO_DATE('2024-05-12', 'YYYY-MM-DD'), 'NORMAL', 1, '孙七', '13800138000', 'sunqi@example.com', '资金托管账户', '用于资金托管的专用账户', 'FAILED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin')"
        };

        for (String data : testData) {
            try {
                jdbcTemplate.execute(data);
            } catch (Exception e) {
                log.warn("插入测试数据失败: {}", data, e);
            }
        }
    }

    /**
     * 创建 TC_SEAL_TYPE 表
     */
    private void createSealTypeTable(Connection connection) {
        try {
            String createTableSQL = "CREATE TABLE TC_SEAL_TYPE (" +
                    "    TYPE_ID DECIMAL(20,0) NOT NULL," +
                    "    TYPE_NAME VARCHAR(100) NOT NULL," +
                    "    TYPE_CODE VARCHAR(50) NOT NULL," +
                    "    IS_ACTIVE INTEGER DEFAULT 1," +
                    "    REMARK VARCHAR(500)," +
                    "    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "    CONSTRAINT PK_SEAL_TYPE PRIMARY KEY (TYPE_ID)" +
                    ")";

            new JdbcTemplate(dataSource).execute(createTableSQL);

            // 添加表注释
            String tableComment = "COMMENT ON TABLE TC_SEAL_TYPE IS '印鉴类型表'";
            new JdbcTemplate(dataSource).execute(tableComment);

            // 添加列注释
            addSealTypeColumnComments();

            // 创建索引
            createSealTypeIndexes();

            // 插入测试数据
            insertSealTypeTestData();

            log.info("TC_SEAL_TYPE 表及其索引、测试数据创建完成");

        } catch (Exception e) {
            log.error("创建 TC_SEAL_TYPE 表失败", e);
            throw new RuntimeException("创建 TC_SEAL_TYPE 表失败", e);
        }
    }

    /**
     * 添加 TC_SEAL_TYPE 列注释
     */
    private void addSealTypeColumnComments() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] columnComments = {
                "COMMENT ON COLUMN TC_SEAL_TYPE.TYPE_ID IS '类型ID(主键)'",
                "COMMENT ON COLUMN TC_SEAL_TYPE.TYPE_NAME IS '类型名称(公章/财务章/合同章/法人章/发票章)'",
                "COMMENT ON COLUMN TC_SEAL_TYPE.TYPE_CODE IS '类型编码(OFFICIAL/FINANCE/CONTRACT/LEGAL/INVOICE)'",
                "COMMENT ON COLUMN TC_SEAL_TYPE.IS_ACTIVE IS '是否启用(1启用/0禁用)'",
                "COMMENT ON COLUMN TC_SEAL_TYPE.REMARK IS '备注信息'",
                "COMMENT ON COLUMN TC_SEAL_TYPE.CREATE_TIME IS '创建时间'"
        };

        for (String comment : columnComments) {
            try {
                jdbcTemplate.execute(comment);
            } catch (Exception e) {
                log.warn("添加列注释失败: {}", comment, e);
            }
        }
    }

    /**
     * 创建 TC_SEAL_TYPE 索引
     */
    private void createSealTypeIndexes() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] indexes = {
                "CREATE INDEX IDX_SEAL_TYPE_CODE ON TC_SEAL_TYPE(TYPE_CODE)",
                "CREATE INDEX IDX_SEAL_TYPE_ACTIVE ON TC_SEAL_TYPE(IS_ACTIVE)"
        };

        for (String index : indexes) {
            try {
                jdbcTemplate.execute(index);
            } catch (Exception e) {
                log.warn("创建索引失败: {}", index, e);
            }
        }
    }

    /**
     * 插入 TC_SEAL_TYPE 测试数据
     */
    private void insertSealTypeTestData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] testData = {
                "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_NAME, TYPE_CODE, IS_ACTIVE, REMARK, CREATE_TIME) VALUES (1, '公章', 'OFFICIAL', 1, '公司公章', CURRENT_TIMESTAMP)",
                "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_NAME, TYPE_CODE, IS_ACTIVE, REMARK, CREATE_TIME) VALUES (2, '财务章', 'FINANCE', 1, '财务专用章', CURRENT_TIMESTAMP)",
                "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_NAME, TYPE_CODE, IS_ACTIVE, REMARK, CREATE_TIME) VALUES (3, '合同章', 'CONTRACT', 1, '合同专用章', CURRENT_TIMESTAMP)",
                "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_NAME, TYPE_CODE, IS_ACTIVE, REMARK, CREATE_TIME) VALUES (4, '法人章', 'LEGAL', 1, '法定代表人章', CURRENT_TIMESTAMP)",
                "INSERT INTO TC_SEAL_TYPE (TYPE_ID, TYPE_NAME, TYPE_CODE, IS_ACTIVE, REMARK, CREATE_TIME) VALUES (5, '发票章', 'INVOICE', 1, '发票专用章', CURRENT_TIMESTAMP)"
        };

        for (String data : testData) {
            try {
                jdbcTemplate.execute(data);
            } catch (Exception e) {
                log.warn("插入测试数据失败: {}", data, e);
            }
        }
    }

    /**
     * 创建 TC_SEAL_ARCHIVE_V2 表
     */
    private void createSealArchiveV2Table(Connection connection) {
        try {
            String createTableSQL = "CREATE TABLE TC_SEAL_ARCHIVE_V2 (" +
                    "    ID DECIMAL(20,0) NOT NULL," +
                    "    SEAL_CODE VARCHAR(50) NOT NULL," +
                    "    SEAL_NAME VARCHAR(200) NOT NULL," +
                    "    SEAL_TYPE_ID DECIMAL(20,0)," +
                    "    SEAL_TYPE_NAME VARCHAR(100)," +
                    "    OWNER_NAME VARCHAR(100)," +
                    "    OWNER_POSITION VARCHAR(100)," +
                    "    OWNER_ID_CARD VARCHAR(50)," +
                    "    EFFECTIVE_DATE DATE," +
                    "    EXPIRE_DATE DATE," +
                    "    SEAL_IMAGE_URL VARCHAR(500)," +
                    "    IS_ACTIVE INTEGER DEFAULT 1," +
                    "    VERSION_NO INTEGER DEFAULT 1," +
                    "    DESCRIPTION VARCHAR(500)," +
                    "    CREATE_BY VARCHAR(50)," +
                    "    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "    UPDATE_BY VARCHAR(50)," +
                    "    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "    REMARK VARCHAR(500)," +
                    "    CONSTRAINT PK_SEAL_ARCHIVE_V2 PRIMARY KEY (ID)" +
                    ")";

            new JdbcTemplate(dataSource).execute(createTableSQL);

            // 添加表注释
            String tableComment = "COMMENT ON TABLE TC_SEAL_ARCHIVE_V2 IS '印鉴档案表 V2版本'";
            new JdbcTemplate(dataSource).execute(tableComment);

            // 添加列注释
            addSealArchiveV2ColumnComments();

            // 创建索引
            createSealArchiveV2Indexes();

            // 插入测试数据
            insertSealArchiveV2TestData();

            log.info("TC_SEAL_ARCHIVE_V2 表及其索引、测试数据创建完成");

        } catch (Exception e) {
            log.error("创建 TC_SEAL_ARCHIVE_V2 表失败", e);
            throw new RuntimeException("创建 TC_SEAL_ARCHIVE_V2 表失败", e);
        }
    }

    /**
     * 添加 TC_SEAL_ARCHIVE_V2 列注释
     */
    private void addSealArchiveV2ColumnComments() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] columnComments = {
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.ID IS '印鉴ID(主键)'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.SEAL_CODE IS '印鉴编号'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.SEAL_NAME IS '印鉴名称'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.SEAL_TYPE_ID IS '印鉴类型ID(关联TC_SEAL_TYPE表)'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.SEAL_TYPE_NAME IS '印鉴类型名称(冗余字段,方便查询)'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.OWNER_NAME IS '持有人姓名'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.OWNER_POSITION IS '持有人职位'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.OWNER_ID_CARD IS '持有人身份证号'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.EFFECTIVE_DATE IS '生效日期'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.EXPIRE_DATE IS '失效日期'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.SEAL_IMAGE_URL IS '印鉴图片URL'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.IS_ACTIVE IS '是否启用(1启用/0禁用)'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.VERSION_NO IS '版本号'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.DESCRIPTION IS '描述信息'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.CREATE_BY IS '创建人'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.CREATE_TIME IS '创建时间'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.UPDATE_BY IS '更新人'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.UPDATE_TIME IS '更新时间'",
                "COMMENT ON COLUMN TC_SEAL_ARCHIVE_V2.REMARK IS '备注信息'"
        };

        for (String comment : columnComments) {
            try {
                jdbcTemplate.execute(comment);
            } catch (Exception e) {
                log.warn("添加列注释失败: {}", comment, e);
            }
        }
    }

    /**
     * 创建 TC_SEAL_ARCHIVE_V2 索引
     */
    private void createSealArchiveV2Indexes() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] indexes = {
                "CREATE UNIQUE INDEX IDX_SEAL_CODE_V2 ON TC_SEAL_ARCHIVE_V2(SEAL_CODE)",
                "CREATE INDEX IDX_SEAL_TYPE_V2 ON TC_SEAL_ARCHIVE_V2(SEAL_TYPE_ID)",
                "CREATE INDEX IDX_OWNER_V2 ON TC_SEAL_ARCHIVE_V2(OWNER_NAME)",
                "CREATE INDEX IDX_STATUS_V2 ON TC_SEAL_ARCHIVE_V2(IS_ACTIVE)",
                "CREATE INDEX IDX_CREATE_TIME_V2 ON TC_SEAL_ARCHIVE_V2(CREATE_TIME)"
        };

        for (String index : indexes) {
            try {
                jdbcTemplate.execute(index);
            } catch (Exception e) {
                log.warn("创建索引失败: {}", index, e);
            }
        }
    }

    /**
     * 插入 TC_SEAL_ARCHIVE_V2 测试数据
     */
    private void insertSealArchiveV2TestData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String[] testData = {
                "INSERT INTO TC_SEAL_ARCHIVE_V2 (ID, SEAL_CODE, SEAL_NAME, SEAL_TYPE_ID, SEAL_TYPE_NAME, OWNER_NAME, OWNER_POSITION, OWNER_ID_CARD, EFFECTIVE_DATE, EXPIRE_DATE, SEAL_IMAGE_URL, IS_ACTIVE, VERSION_NO, DESCRIPTION, CREATE_TIME, CREATE_BY) VALUES (1, 'SEAL20240001', '公司公章-总部', 1, '公章', '张三', '总经理', '110101199001011234', TO_DATE('2024-01-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), '/seals/company_seal_hq.png', 1, 1, '总部使用的公司公章', CURRENT_TIMESTAMP, 'admin')",
                "INSERT INTO TC_SEAL_ARCHIVE_V2 (ID, SEAL_CODE, SEAL_NAME, SEAL_TYPE_ID, SEAL_TYPE_NAME, OWNER_NAME, OWNER_POSITION, OWNER_ID_CARD, EFFECTIVE_DATE, EXPIRE_DATE, SEAL_IMAGE_URL, IS_ACTIVE, VERSION_NO, DESCRIPTION, CREATE_TIME, CREATE_BY) VALUES (2, 'SEAL20240002', '财务专用章-财务部', 2, '财务章', '李四', '财务总监', '110101199002022345', TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), '/seals/finance_seal.png', 1, 1, '财务部专用章', CURRENT_TIMESTAMP, 'admin')",
                "INSERT INTO TC_SEAL_ARCHIVE_V2 (ID, SEAL_CODE, SEAL_NAME, SEAL_TYPE_ID, SEAL_TYPE_NAME, OWNER_NAME, OWNER_POSITION, OWNER_ID_CARD, EFFECTIVE_DATE, EXPIRE_DATE, SEAL_IMAGE_URL, IS_ACTIVE, VERSION_NO, DESCRIPTION, CREATE_TIME, CREATE_BY) VALUES (3, 'SEAL20240003', '合同专用章-法务部', 3, '合同章', '王五', '法务经理', '110101199003033456', TO_DATE('2024-03-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), '/seals/contract_seal.png', 1, 1, '法务部合同专用章', CURRENT_TIMESTAMP, 'admin')",
                "INSERT INTO TC_SEAL_ARCHIVE_V2 (ID, SEAL_CODE, SEAL_NAME, SEAL_TYPE_ID, SEAL_TYPE_NAME, OWNER_NAME, OWNER_POSITION, OWNER_ID_CARD, EFFECTIVE_DATE, EXPIRE_DATE, SEAL_IMAGE_URL, IS_ACTIVE, VERSION_NO, DESCRIPTION, CREATE_TIME, CREATE_BY) VALUES (4, 'SEAL20240004', '法人章-备用', 4, '法人章', '赵六', '法定代表人', '110101199004044567', TO_DATE('2024-04-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), '/seals/legal_seal.png', 0, 1, '法人章(暂时停用)', CURRENT_TIMESTAMP, 'admin')",
                "INSERT INTO TC_SEAL_ARCHIVE_V2 (ID, SEAL_CODE, SEAL_NAME, SEAL_TYPE_ID, SEAL_TYPE_NAME, OWNER_NAME, OWNER_POSITION, OWNER_ID_CARD, EFFECTIVE_DATE, EXPIRE_DATE, SEAL_IMAGE_URL, IS_ACTIVE, VERSION_NO, DESCRIPTION, CREATE_TIME, CREATE_BY) VALUES (5, 'SEAL20240005', '发票专用章-税务部', 5, '发票章', '孙七', '税务主管', '110101199005055678', TO_DATE('2024-05-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), '/seals/invoice_seal.png', 1, 1, '税务部发票专用章', CURRENT_TIMESTAMP, 'admin')"
        };

        for (String data : testData) {
            try {
                jdbcTemplate.execute(data);
            } catch (Exception e) {
                log.warn("插入测试数据失败: {}", data, e);
            }
        }
    }
}
