package com.huabo.contract.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.contract.config.DatabaseCompatibilityConfig;
import com.huabo.contract.utils.DatabaseCompatibilityUtils;
import com.huabo.contract.utils.DatabaseSchemaGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库兼容性测试控制器
 * 用于测试和验证数据库兼容性功能
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/test/database")
@Tag(name="数据库兼容性测试",description="数据库兼容性测试")
public class DatabaseCompatibilityTestController {

    @Autowired
    private DatabaseCompatibilityUtils compatibilityUtils;

    @Autowired
    private DatabaseSchemaGenerator schemaGenerator;

    @Autowired
    private DatabaseCompatibilityConfig databaseConfig;

    /**
     * 获取数据库信息
     *
     * @return 数据库信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取数据库信息", description = "获取当前数据库类型和兼容性信息")
    public String getDatabaseInfo() {
        try {
            log.info("获取数据库信息");

            Map<String, Object> info = new HashMap<>();
            info.put("databaseType", compatibilityUtils.getCurrentDatabaseType());
            info.put("databaseName", compatibilityUtils.getDatabaseProductName());
            info.put("isMySQL", compatibilityUtils.isMySQL());
            info.put("isDaMeng", compatibilityUtils.isDaMeng());
            info.put("compatibilityEnabled", true);

            return JsonBean.success("获取成功", info);
        } catch (Exception e) {
            log.error("获取数据库信息失败", e);
            return JsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 测试SQL转换功能
     *
     * @param sql 原始SQL
     * @return 转换结果
     */
    @PostMapping("/convertSql")
    @Operation(summary = "测试SQL转换", description = "测试SQL语句的数据库兼容性转换")
    public String convertSql(@RequestParam String sql) {
        try {
            log.info("测试SQL转换，原始SQL：{}", sql);

            String convertedSql = compatibilityUtils.convertSql(sql);
            
            Map<String, Object> result = new HashMap<>();
            result.put("originalSql", sql);
            result.put("convertedSql", convertedSql);
            result.put("isConverted", !sql.equals(convertedSql));
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());

            return JsonBean.success("转换成功", result);
        } catch (Exception e) {
            log.error("SQL转换失败", e);
            return JsonBean.error("转换失败：" + e.getMessage());
        }
    }

    /**
     * 测试兼容函数生成
     *
     * @param functionType 函数类型
     * @return 函数生成结果
     */
    @GetMapping("/testFunction/{functionType}")
    @Operation(summary = "测试兼容函数生成", description = "测试数据库兼容函数的生成")
    public String testFunction(@PathVariable String functionType) {
        try {
            log.info("测试兼容函数生成，函数类型：{}", functionType);

            Map<String, Object> result = new HashMap<>();
            result.put("functionType", functionType);
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());

            String function;
            switch (functionType.toUpperCase()) {
                case "CURRENT_TIME":
                    function = compatibilityUtils.getCompatibleSql("CURRENT_TIME");
                    break;
                case "CURRENT_DATE":
                    function = compatibilityUtils.getCompatibleSql("CURRENT_DATE");
                    break;
                case "CONCAT":
                    function = compatibilityUtils.getCompatibleSql("CONCAT", "column1", "column2", "column3");
                    break;
                case "LIKE":
                    function = compatibilityUtils.getCompatibleSql("LIKE", "name", "keyword");
                    break;
                case "DATE_FORMAT":
                    function = compatibilityUtils.getCompatibleSql("DATE_FORMAT", "create_time", "yyyy-MM-dd");
                    break;
                case "DATE_DIFF":
                    function = compatibilityUtils.getCompatibleSql("DATE_DIFF", "end_date", "start_date");
                    break;
                case "IF":
                    function = compatibilityUtils.getCompatibleSql("IF", "status = 1", "'启用'", "'禁用'");
                    break;
                default:
                    function = "不支持的函数类型";
            }

            result.put("generatedFunction", function);

            return JsonBean.success("生成成功", result);
        } catch (Exception e) {
            log.error("兼容函数生成失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 生成建表脚本
     *
     * @return 建表脚本
     */
    @GetMapping("/generateSchema")
    @Operation(summary = "生成建表脚本", description = "生成当前数据库类型的建表脚本")
    public String generateSchema() {
        try {
            log.info("生成建表脚本");

            String initScript = schemaGenerator.generateFullInitScript();
            
            Map<String, Object> result = new HashMap<>();
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());
            result.put("initScript", initScript);

            return JsonBean.success("生成成功", result);
        } catch (Exception e) {
            log.error("生成建表脚本失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 生成表结构SQL
     *
     * @param tableName 表名
     * @return 表结构SQL
     */
    @GetMapping("/generateTable/{tableName}")
    @Operation(summary = "生成指定表的建表脚本", description = "生成指定表的建表脚本")
    public String generateTable(@PathVariable String tableName) {
        try {
            log.info("生成表结构SQL，表名：{}", tableName);

            String sql;
            switch (tableName.toLowerCase()) {
                case "bid_template":
                    sql = schemaGenerator.generateBidTemplateTableSql();
                    break;
                case "deposit_management":
                    sql = schemaGenerator.generateDepositManagementTableSql();
                    break;
                default:
                    return JsonBean.error("不支持的表名：" + tableName);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("tableName", tableName);
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());
            result.put("createTableSql", sql);

            return JsonBean.success("生成成功", result);
        } catch (Exception e) {
            log.error("生成表结构SQL失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 生成索引脚本
     *
     * @return 索引脚本
     */
    @GetMapping("/generateIndexes")
    @Operation(summary = "生成索引脚本", description = "生成当前数据库类型的索引脚本")
    public String generateIndexes() {
        try {
            log.info("生成索引脚本");

            List<String> indexSqlList = schemaGenerator.generateIndexSql();
            
            Map<String, Object> result = new HashMap<>();
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());
            result.put("indexCount", indexSqlList.size());
            result.put("indexSqlList", indexSqlList);

            return JsonBean.success("生成成功", result);
        } catch (Exception e) {
            log.error("生成索引脚本失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 验证数据库兼容性
     *
     * @return 验证结果
     */
    @GetMapping("/validate")
    @Operation(summary = "验证数据库兼容性", description = "验证当前数据库的兼容性配置")
    public String validateCompatibility() {
        try {
            log.info("验证数据库兼容性");

            boolean isValid = schemaGenerator.validateDatabaseCompatibility();
            
            Map<String, Object> result = new HashMap<>();
            result.put("isValid", isValid);
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());
            result.put("databaseName", compatibilityUtils.getDatabaseProductName());
            result.put("message", isValid ? "数据库兼容性验证通过" : "数据库兼容性验证失败");

            return JsonBean.success("验证完成", result);
        } catch (Exception e) {
            log.error("验证数据库兼容性失败", e);
            return JsonBean.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 获取数据类型映射
     *
     * @return 数据类型映射
     */
    @GetMapping("/dataTypeMapping")
    @Operation(summary = "获取数据类型映射", description = "获取当前数据库的数据类型映射")
    public String getDataTypeMapping() {
        try {
            log.info("获取数据类型映射");

            Map<String, String> dataTypeMapping = compatibilityUtils.getDataTypeMapping();
            Map<String, String> functionMapping = compatibilityUtils.getFunctionMapping();
            
            Map<String, Object> result = new HashMap<>();
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());
            result.put("dataTypeMapping", dataTypeMapping);
            result.put("functionMapping", functionMapping);

            return JsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取数据类型映射失败", e);
            return JsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 测试分页SQL生成
     *
     * @param sql 原始SQL
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 分页SQL
     */
    @PostMapping("/testPagination")
    @Operation(summary = "测试分页SQL生成", description = "测试分页SQL的生成")
    public String testPagination(@RequestParam String sql, 
                                @RequestParam(defaultValue = "0") int offset,
                                @RequestParam(defaultValue = "10") int limit) {
        try {
            log.info("测试分页SQL生成，原始SQL：{}，偏移量：{}，限制：{}", sql, offset, limit);

            String paginationSql = compatibilityUtils.getPaginationSql(sql, offset, limit);
            
            Map<String, Object> result = new HashMap<>();
            result.put("originalSql", sql);
            result.put("offset", offset);
            result.put("limit", limit);
            result.put("paginationSql", paginationSql);
            result.put("databaseType", compatibilityUtils.getCurrentDatabaseType());

            return JsonBean.success("生成成功", result);
        } catch (Exception e) {
            log.error("测试分页SQL生成失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 打印初始化脚本到日志
     *
     * @return 操作结果
     */
    @PostMapping("/printInitScript")
    @Operation(summary = "打印初始化脚本", description = "将初始化脚本打印到日志中")
    public String printInitScript() {
        try {
            log.info("打印初始化脚本到日志");

            schemaGenerator.printInitScript();
            compatibilityUtils.logDatabaseInfo();

            return JsonBean.success("脚本已打印到日志");
        } catch (Exception e) {
            log.error("打印初始化脚本失败", e);
            return JsonBean.error("打印失败：" + e.getMessage());
        }
    }
}
