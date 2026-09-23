package com.global.treasurer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 数据库初始化配置
 * 在应用启动时自动执行SQL脚本
 *
 * @author 华博云开发团队
 * @since 2024-12-25
 */
//@Component  // 已禁用，避免数据库依赖导致启动失败
public class DatabaseInitConfig implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitConfig.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 检查表是否存在
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_SEAL_TYPE'",
                Integer.class
            );

            if (count != null && count == 0) {
                log.info("检测到TBL_SEAL_TYPE表不存在,开始创建表...");

                // 创建表
                String createTableSql = "CREATE TABLE TBL_SEAL_TYPE (\n" +
                        "    ID VARCHAR(32) NOT NULL,\n" +
                        "    CODE VARCHAR(50) NOT NULL,\n" +
                        "    NAME VARCHAR(100) NOT NULL,\n" +
                        "    SEAL_LEVEL INT,\n" +
                        "    SCOPE VARCHAR(500),\n" +
                        "    DESCRIPTION VARCHAR(500),\n" +
                        "    STATUS INT DEFAULT 1,\n" +
                        "    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "    CREATE_USER VARCHAR(50),\n" +
                        "    UPDATE_USER VARCHAR(50),\n" +
                        "    CONSTRAINT PK_TBL_SEAL_TYPE PRIMARY KEY (ID)\n" +
                        ")";
                jdbcTemplate.execute(createTableSql);
                log.info("TBL_SEAL_TYPE表创建成功");

                // 添加注释
                jdbcTemplate.execute("COMMENT ON TABLE TBL_SEAL_TYPE IS '印鉴类型表'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.ID IS '主键ID'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.CODE IS '类型编码'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.NAME IS '类型名称'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.SEAL_LEVEL IS '印鉴级别(1-一级,2-二级,3-三级)'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.SCOPE IS '适用范围'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.DESCRIPTION IS '描述'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.STATUS IS '状态(1-启用,0-禁用)'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.CREATE_TIME IS '创建时间'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.UPDATE_TIME IS '更新时间'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.CREATE_USER IS '创建人'");
                jdbcTemplate.execute("COMMENT ON COLUMN TBL_SEAL_TYPE.UPDATE_USER IS '更新人'");

                // 创建索引
                jdbcTemplate.execute("CREATE INDEX IDX_SEAL_TYPE_CODE ON TBL_SEAL_TYPE(CODE)");
                jdbcTemplate.execute("CREATE INDEX IDX_SEAL_TYPE_STATUS ON TBL_SEAL_TYPE(STATUS)");
                jdbcTemplate.execute("CREATE INDEX IDX_SEAL_TYPE_CREATE_TIME ON TBL_SEAL_TYPE(CREATE_TIME)");
                log.info("TBL_SEAL_TYPE索引创建成功");

                // 插入测试数据
                String[] insertSqls = {
                    "INSERT INTO TBL_SEAL_TYPE (ID, CODE, NAME, SEAL_LEVEL, SCOPE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES ('1', 'OFFICIAL_SEAL', '公章', 1, '全公司通用', '公司正式公章', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system')",
                    "INSERT INTO TBL_SEAL_TYPE (ID, CODE, NAME, SEAL_LEVEL, SCOPE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES ('2', 'FINANCE_SEAL', '财务章', 2, '财务部门专用', '财务专用章', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system')",
                    "INSERT INTO TBL_SEAL_TYPE (ID, CODE, NAME, SEAL_LEVEL, SCOPE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES ('3', 'CONTRACT_SEAL', '合同章', 1, '合同签署专用', '合同专用章', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system')",
                    "INSERT INTO TBL_SEAL_TYPE (ID, CODE, NAME, SEAL_LEVEL, SCOPE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES ('4', 'LEGAL_SEAL', '法人章', 1, '法定代表人专用', '法定代表人名章', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system')",
                    "INSERT INTO TBL_SEAL_TYPE (ID, CODE, NAME, SEAL_LEVEL, SCOPE, DESCRIPTION, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER) VALUES ('5', 'DEPARTMENT_SEAL', '部门章', 3, '各部门内部使用', '部门内部章', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system')"
                };

                for (String sql : insertSqls) {
                    jdbcTemplate.execute(sql);
                }
                log.info("TBL_SEAL_TYPE测试数据插入成功,共5条");

            } else {
                log.info("TBL_SEAL_TYPE表已存在,跳过创建");
            }

        } catch (Exception e) {
            log.error("数据库初始化失败", e);
            // 不抛出异常,避免应用启动失败
        }
    }
}
