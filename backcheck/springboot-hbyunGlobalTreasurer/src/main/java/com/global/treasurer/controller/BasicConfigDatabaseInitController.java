package com.global.treasurer.controller;

import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 基础配置数据库初始化Controller
 * 用于创建业务系统注册、数据映射配置、电票账户配置三张表
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@RestController
@RequestMapping("/financial/basicConfig/database-init")
@Api(tags = "基础配置数据库初始化")
public class BasicConfigDatabaseInitController {
    private static final Logger log = LoggerFactory.getLogger(BasicConfigDatabaseInitController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/execute")
    @ApiOperation("执行数据库初始化脚本")
    public String executeInitScript(HttpServletResponse response) {
        try {
            log.info("开始执行基础配置数据库初始化脚本...");

            // 读取SQL脚本文件
            ClassPathResource resource = new ClassPathResource("sql/创建基础配置表_DM数据库.sql");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // 跳过注释行
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sqlBuilder.append(line).append("\n");
            }
            reader.close();

            String fullSql = sqlBuilder.toString();

            // 分割SQL语句（简单分割，以分号分隔）
            String[] sqlStatements = fullSql.split(";");

            int successCount = 0;
            int errorCount = 0;

            for (String sql : sqlStatements) {
                String trimmedSql = sql.trim();
                if (trimmedSql.isEmpty()) {
                    continue;
                }

                try {
                    // 跳过查询语句（SELECT）
                    if (trimmedSql.toUpperCase().startsWith("SELECT")) {
                        continue;
                    }
                    jdbcTemplate.execute(trimmedSql);
                    successCount++;
                    log.debug("执行SQL成功: {}", trimmedSql.substring(0, Math.min(50, trimmedSql.length())));
                } catch (Exception e) {
                    errorCount++;
                    log.warn("执行SQL失败（可能是表已存在）: {}", e.getMessage());
                }
            }

            log.info("数据库初始化脚本执行完成 - 成功: {}, 失败: {}", successCount, errorCount);

            return new JsonBean(1, "数据库初始化完成 - 成功执行 " + successCount + " 条SQL语句", null).toString();

        } catch (Exception e) {
            log.error("执行数据库初始化脚本失败", e);
            return new JsonBean(0, "数据库初始化失败: " + e.getMessage(), null).toString();
        }
    }

    @PostMapping("/verify")
    @ApiOperation("验证数据库表是否存在")
    public String verifyTables(HttpServletResponse response) {
        try {
            log.info("验证基础配置数据库表...");

            // 检查三张表是否存在
            String checkSql = "SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME IN (?, ?, ?)";
            java.util.List<String> tables = jdbcTemplate.queryForList(
                checkSql, String.class,
                "TBL_BUSINESS_SYSTEM_REGISTRY",
                "TC_DATA_MAPPING",
                "TC_ETICKET_ACCOUNT"
            );

            // 查询每张表的记录数
            java.util.Map<String, Object> result = new java.util.HashMap<>();

            for (String tableName : new String[]{"TBL_BUSINESS_SYSTEM_REGISTRY", "TC_DATA_MAPPING", "TC_ETICKET_ACCOUNT"}) {
                if (tables.contains(tableName)) {
                    String countSql = "SELECT COUNT(*) FROM " + tableName;
                    Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
                    result.put(tableName, count);
                } else {
                    result.put(tableName, "表不存在");
                }
            }

            result.put("allTablesExist", tables.size() == 3);

            return new JsonBean(1, "验证完成", result).toString();

        } catch (Exception e) {
            log.error("验证数据库表失败", e);
            return new JsonBean(0, "验证失败: " + e.getMessage(), null).toString();
        }
    }
}
