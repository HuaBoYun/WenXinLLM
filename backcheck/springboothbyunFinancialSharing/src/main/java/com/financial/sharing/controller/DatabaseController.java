package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 数据库管理控制器
 * 用于执行数据库表结构修正
 */
@Slf4j
@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 修正会计凭证表结构
     */
    @PostMapping("/fixVoucherTable")
    public String fixVoucherTable(@RequestParam(required = false) String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("开始修正会计凭证表结构...");

            // 检查表是否存在
            try {
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM T_ACCOUNTING_VOUCHER", Integer.class);
                log.info("表 T_ACCOUNTING_VOUCHER 存在");
            } catch (Exception e) {
                log.info("表 T_ACCOUNTING_VOUCHER 不存在，需要创建");
                createVoucherTable();
                JsonBean json = new JsonBean();
                json.setCode(1);
                json.setMsg("表创建成功");
                return JsonMapper.nonNullMapper().toJson(json);
            }

            // 检查列是否存在并添加缺失的列
            addMissingColumns();

            log.info("会计凭证表结构修正完成");
            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("表结构修正成功");
            return JsonMapper.nonNullMapper().toJson(json);

        } catch (Exception e) {
            log.error("修正表结构失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("修正表结构失败: " + e.getMessage());
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    private void createVoucherTable() {
      String createTableSql = "CREATE TABLE T_ACCOUNTING_VOUCHER (\n" +
                "    VOUCHER_ID NUMBER(20) NOT NULL PRIMARY KEY,\n" +
                "    VOUCHER_NO VARCHAR2(50) NOT NULL,\n" +
                "    VOUCHER_TYPE_ID NUMBER(20),\n" +
                "    VOUCHER_DATE DATE,\n" +
                "    ACCOUNTING_PERIOD VARCHAR2(20),\n" +
                "    VOUCHER_DESC VARCHAR2(500),\n" +
                "    TOTAL_DEBIT NUMBER(18,2),\n" +
                "    TOTAL_CREDIT NUMBER(18,2),\n" +
                "    ENTRY_COUNT NUMBER(10),\n" +
                "    VOUCHER_STATUS NUMBER(2) DEFAULT 0,\n" +
                "    SOURCE_TRANSACTION_ID NUMBER(20),\n" +
                "    SOURCE_SYSTEM VARCHAR2(50),\n" +
                "    PREPARER NUMBER(20),\n" +
                "    REVIEWER NUMBER(20),\n" +
                "    REVIEW_TIME TIMESTAMP,\n" +
                "    POSTER NUMBER(20),\n" +
                "    POST_TIME TIMESTAMP,\n" +
                "    ATTACHMENT_COUNT NUMBER(10) DEFAULT 0,\n" +
                "    REMARK VARCHAR2(1000),\n" +
                "    BOOK_ID NUMBER(20) NOT NULL,\n" +
                "    TENANT_ID NUMBER(20) NOT NULL,\n" +
                "    IS_DELETED NUMBER(1) DEFAULT 0,\n" +
                "    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "    CREATOR NUMBER(20),\n" +
                "    UPDATER NUMBER(20)\n" +
                ")";

        jdbcTemplate.execute(createTableSql);

        // 创建索引
        jdbcTemplate.execute("CREATE INDEX IDX_VOUCHER_NO ON T_ACCOUNTING_VOUCHER(VOUCHER_NO)");
        jdbcTemplate.execute("CREATE INDEX IDX_BOOK_ID ON T_ACCOUNTING_VOUCHER(BOOK_ID)");
        jdbcTemplate.execute("CREATE INDEX IDX_TENANT_ID ON T_ACCOUNTING_VOUCHER(TENANT_ID)");

        log.info("表 T_ACCOUNTING_VOUCHER 创建成功");
    }

    private void addMissingColumns() {
        String[] columnsToCheck = {
            "ENTRY_COUNT NUMBER(10)",
            "VOUCHER_STATUS NUMBER(2) DEFAULT 0",
            "TOTAL_DEBIT NUMBER(18,2)",
            "TOTAL_CREDIT NUMBER(18,2)",
            "VOUCHER_TYPE_ID NUMBER(20)",
            "SOURCE_TRANSACTION_ID NUMBER(20)",
            "SOURCE_SYSTEM VARCHAR2(50)",
            "PREPARER NUMBER(20)",
            "REVIEWER NUMBER(20)",
            "REVIEW_TIME TIMESTAMP",
            "POSTER NUMBER(20)",
            "POST_TIME TIMESTAMP",
            "ATTACHMENT_COUNT NUMBER(10) DEFAULT 0",
            "REMARK VARCHAR2(1000)",
            "IS_DELETED NUMBER(1) DEFAULT 0",
            "CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            "UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            "CREATOR NUMBER(20)",
            "UPDATER NUMBER(20)"
        };

        for (String columnDef : columnsToCheck) {
            String columnName = columnDef.split(" ")[0];
            try {
                jdbcTemplate.queryForObject("SELECT " + columnName + " FROM T_ACCOUNTING_VOUCHER WHERE ROWNUM = 1", Object.class);
                log.info("列 {} 已存在", columnName);
            } catch (Exception e) {
                log.info("列 {} 不存在，正在添加...", columnName);
                jdbcTemplate.execute("ALTER TABLE T_ACCOUNTING_VOUCHER ADD " + columnDef);
                log.info("列 {} 添加成功", columnName);
            }
        }
    }
}