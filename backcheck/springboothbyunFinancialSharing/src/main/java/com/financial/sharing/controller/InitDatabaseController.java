package com.financial.sharing.controller;

import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据库初始化控制器
 * @author system
 * @since 2025-10-21
 */
@Slf4j
@RestController
@RequestMapping("/init")
@Api(tags = "数据库初始化")
public class InitDatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/database")
    @ApiOperation("初始化数据库表")
    public MyJsonBean<String> initDatabase() {
        try {
            // 创建影响因素表
            String createInfluenceFactorTable = "CREATE TABLE IF NOT EXISTS T_INFLUENCE_FACTOR (" +
                    "FACTOR_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '影响因素ID'," +
                    "FACTOR_CODE VARCHAR(50) NOT NULL COMMENT '影响因素编码'," +
                    "FACTOR_NAME VARCHAR(200) NOT NULL COMMENT '影响因素名称'," +
                    "FACTOR_TYPE INT NOT NULL COMMENT '影响因素类型'," +
                    "DATA_TYPE INT COMMENT '数据类型'," +
                    "DATA_LENGTH INT COMMENT '数据长度'," +
                    "DECIMAL_PLACES INT COMMENT '小数位数'," +
                    "IS_REQUIRED INT DEFAULT 0 COMMENT '是否必填'," +
                    "DEFAULT_VALUE VARCHAR(500) COMMENT '默认值'," +
                    "VALUE_RANGE VARCHAR(500) COMMENT '取值范围'," +
                    "VALIDATION_RULE VARCHAR(500) COMMENT '验证规则'," +
                    "DESCRIPTION VARCHAR(500) COMMENT '描述'," +
                    "IS_ENABLED INT DEFAULT 1 COMMENT '是否启用'," +
                    "SORT_ORDER INT DEFAULT 0 COMMENT '排序号'," +
                    "BOOK_ID BIGINT NOT NULL COMMENT '账簿ID'," +
                    "TENANT_ID BIGINT NOT NULL COMMENT '租户ID'," +
                    "VERSION INT DEFAULT 1 COMMENT '版本号'," +
                    "IS_DELETED INT DEFAULT 0 COMMENT '删除标识'," +
                    "CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                    "UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                    "CREATOR BIGINT COMMENT '创建人'," +
                    "UPDATER BIGINT COMMENT '更新人'," +
                    "UNIQUE KEY UK_FACTOR_CODE (FACTOR_CODE, BOOK_ID, TENANT_ID)," +
                    "KEY IDX_BOOK_ID (BOOK_ID)," +
                    "KEY IDX_TENANT_ID (TENANT_ID)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
            jdbcTemplate.execute(createInfluenceFactorTable);
            log.info("✓ T_INFLUENCE_FACTOR 表创建成功");

            // 创建会计科目表
            String createAccountSubjectTable = "CREATE TABLE IF NOT EXISTS T_ACCOUNT_SUBJECT (" +
                    "SUBJECT_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "SUBJECT_CODE VARCHAR(50) NOT NULL," +
                    "SUBJECT_NAME VARCHAR(200) NOT NULL," +
                    "SUBJECT_LEVEL INT," +
                    "PARENT_SUBJECT_ID BIGINT," +
                    "SUBJECT_TYPE INT," +
                    "BALANCE_DIRECTION INT," +
                    "IS_LEAF INT DEFAULT 1," +
                    "IS_ENABLED INT DEFAULT 1," +
                    "IS_CASH INT DEFAULT 0," +
                    "IS_BANK INT DEFAULT 0," +
                    "AUXILIARY_TYPES VARCHAR(500)," +
                    "BOOK_ID BIGINT NOT NULL," +
                    "TENANT_ID BIGINT NOT NULL," +
                    "VERSION INT DEFAULT 1," +
                    "IS_DELETED INT DEFAULT 0," +
                    "CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "CREATOR BIGINT," +
                    "UPDATER BIGINT," +
                    "UNIQUE KEY UK_SUBJECT_CODE (SUBJECT_CODE, BOOK_ID, TENANT_ID)," +
                    "KEY IDX_BOOK_ID (BOOK_ID)," +
                    "KEY IDX_TENANT_ID (TENANT_ID)," +
                    "KEY IDX_PARENT_ID (PARENT_SUBJECT_ID)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            jdbcTemplate.execute(createAccountSubjectTable);
            log.info("✓ T_ACCOUNT_SUBJECT 表创建成功");

            // 创建币种汇率表
            String createCurrencyRateTable = "CREATE TABLE IF NOT EXISTS T_CURRENCY_RATE (" +
                    "RATE_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "CURRENCY_CODE VARCHAR(10) NOT NULL," +
                    "CURRENCY_NAME VARCHAR(50)," +
                    "BASE_CURRENCY VARCHAR(10)," +
                    "EXCHANGE_RATE DECIMAL(18,6) NOT NULL," +
                    "RATE_DATE DATE," +
                    "IS_BASE_CURRENCY INT DEFAULT 0," +
                    "IS_ENABLED INT DEFAULT 1," +
                    "BOOK_ID BIGINT NOT NULL," +
                    "TENANT_ID BIGINT NOT NULL," +
                    "VERSION INT DEFAULT 1," +
                    "IS_DELETED INT DEFAULT 0," +
                    "CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "CREATOR BIGINT," +
                    "UPDATER BIGINT," +
                    "UNIQUE KEY UK_CURRENCY_DATE (CURRENCY_CODE, RATE_DATE, BOOK_ID, TENANT_ID)," +
                    "KEY IDX_BOOK_ID (BOOK_ID)," +
                    "KEY IDX_TENANT_ID (TENANT_ID)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            jdbcTemplate.execute(createCurrencyRateTable);
            log.info("✓ T_CURRENCY_RATE 表创建成功");

            // 创建辅助核算项表
            String createAuxiliaryItemTable = "CREATE TABLE IF NOT EXISTS T_AUXILIARY_ITEM (" +
                    "ITEM_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "ITEM_CODE VARCHAR(50) NOT NULL," +
                    "ITEM_NAME VARCHAR(200) NOT NULL," +
                    "ITEM_TYPE VARCHAR(50)," +
                    "ITEM_VALUE VARCHAR(500)," +
                    "IS_ENABLED INT DEFAULT 1," +
                    "SORT_ORDER INT DEFAULT 0," +
                    "DESCRIPTION VARCHAR(500)," +
                    "BOOK_ID BIGINT NOT NULL," +
                    "TENANT_ID BIGINT NOT NULL," +
                    "VERSION INT DEFAULT 1," +
                    "IS_DELETED INT DEFAULT 0," +
                    "CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "CREATOR BIGINT," +
                    "UPDATER BIGINT," +
                    "UNIQUE KEY UK_ITEM_CODE (ITEM_CODE, BOOK_ID, TENANT_ID)," +
                    "KEY IDX_BOOK_ID (BOOK_ID)," +
                    "KEY IDX_TENANT_ID (TENANT_ID)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            jdbcTemplate.execute(createAuxiliaryItemTable);
            log.info("✓ T_AUXILIARY_ITEM 表创建成功");

            // 插入测试数据
            insertTestData();

            return MyJsonBean.successData("数据库初始化成功");
        } catch (Exception e) {
            log.error("数据库初始化失败", e);
            return MyJsonBean.errorData("数据库初始化失败: " + e.getMessage());
        }
    }

    private void insertTestData() {
        try {
            // 影响因素测试数据
            jdbcTemplate.update("INSERT IGNORE INTO T_INFLUENCE_FACTOR (FACTOR_CODE, FACTOR_NAME, FACTOR_TYPE, DATA_TYPE, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    "IF001", "测试影响因素1", 1, 1, 1, 1, 1, 1);
            jdbcTemplate.update("INSERT IGNORE INTO T_INFLUENCE_FACTOR (FACTOR_CODE, FACTOR_NAME, FACTOR_TYPE, DATA_TYPE, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    "IF002", "测试影响因素2", 2, 2, 1, 1, 1, 1);
            log.info("✓ 影响因素测试数据插入成功");

            // 会计科目测试数据
            jdbcTemplate.update("INSERT IGNORE INTO T_ACCOUNT_SUBJECT (SUBJECT_CODE, SUBJECT_NAME, SUBJECT_TYPE, BALANCE_DIRECTION, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    "1001", "库存现金", 1, 1, 1, 1, 1, 1);
            jdbcTemplate.update("INSERT IGNORE INTO T_ACCOUNT_SUBJECT (SUBJECT_CODE, SUBJECT_NAME, SUBJECT_TYPE, BALANCE_DIRECTION, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    "1002", "银行存款", 1, 1, 1, 1, 1, 1);
            log.info("✓ 会计科目测试数据插入成功");

            // 币种汇率测试数据
            jdbcTemplate.update("INSERT IGNORE INTO T_CURRENCY_RATE (CURRENCY_CODE, CURRENCY_NAME, BASE_CURRENCY, EXCHANGE_RATE, RATE_DATE, IS_BASE_CURRENCY, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, CURDATE(), ?, ?, ?, ?, ?)",
                    "CNY", "人民币", "CNY", 1.0, 1, 1, 1, 1, 1);
            jdbcTemplate.update("INSERT IGNORE INTO T_CURRENCY_RATE (CURRENCY_CODE, CURRENCY_NAME, BASE_CURRENCY, EXCHANGE_RATE, RATE_DATE, IS_BASE_CURRENCY, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, CURDATE(), ?, ?, ?, ?, ?)",
                    "USD", "美元", "CNY", 7.0, 0, 1, 1, 1, 1);
            log.info("✓ 币种汇率测试数据插入成功");

            // 辅助核算项测试数据
            jdbcTemplate.update("INSERT IGNORE INTO T_AUXILIARY_ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    "AUX001", "测试辅助项1", "部门", 1, 1, 1, 1);
            jdbcTemplate.update("INSERT IGNORE INTO T_AUXILIARY_ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, IS_ENABLED, BOOK_ID, TENANT_ID, CREATOR) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    "AUX002", "测试辅助项2", "项目", 1, 1, 1, 1);
            log.info("✓ 辅助核算项测试数据插入成功");
        } catch (Exception e) {
            log.warn("测试数据插入失败（可能已存在）: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    @ApiOperation("检查初始化状态")
    public MyJsonBean<String> checkHealth() {
        return MyJsonBean.successData("初始化服务正常");
    }
}

