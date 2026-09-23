package com.huabo.bigmodel.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huabo.bigmodel.dto.SqlExecuteRequest;
import com.huabo.bigmodel.dto.SqlExecuteResponse;
import com.huabo.bigmodel.service.SqlExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 工具执行器
 * 负责执行AI调用的各种工具
 */
@Slf4j
@Component
public class ToolExecutor {

    private final SqlExecuteService sqlExecuteService;

    public ToolExecutor(SqlExecuteService sqlExecuteService) {
        this.sqlExecuteService = sqlExecuteService;
    }

    /**
     * 执行工具调用
     */
    public String executeTool(String toolName, JSONObject toolInput) {
        return executeTool(toolName, toolInput, null);
    }

    /**
     * 执行工具调用(支持进度回调)
     */
    public String executeTool(String toolName, JSONObject toolInput, ProgressCallback callback) {
        log.info("执行工具: {}, 参数: {}", toolName, toolInput);

        try {
            switch (toolName) {
                // 暂时注释掉 execute_sql，强制使用 execute_batch_sql
                // case "execute_sql":
                //     return executeSql(toolInput);
                case "query_sql":
                    return querySql(toolInput);
                case "execute_ddl":
                    return executeDdl(toolInput);
                case "execute_dml":
                    return executeDml(toolInput);
                case "execute_batch_sql":
                    return executeBatchSql(toolInput, callback);
                case "web_search":
                    return executeWebSearch(toolInput);
                default:
                    return "错误: 未知的工具 " + toolName;
            }
        } catch (Exception e) {
            log.error("工具执行失败: {}", toolName, e);
            return "工具执行失败: " + e.getMessage();
        }
    }

    /**
     * 进度回调接口
     */
    public interface ProgressCallback {
        void onProgress(String message);
    }

    /**
     * 执行通用SQL
     */
    private String executeSql(JSONObject input) {
        String sql = input.getString("sql");
        String dbType = input.getString("dbType");
        if (dbType == null || dbType.isEmpty()) {
            dbType = "dm";
        }

        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql(sql)
                .dbType(dbType)
                .autoDetect(true)
                .build();

        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return formatSqlResponse(response);
    }

    /**
     * 执行SQL查询
     */
    private String querySql(JSONObject input) {
        String sql = input.getString("sql");
        String dbType = input.getString("dbType");
        if (dbType == null || dbType.isEmpty()) {
            dbType = "dm";
        }

        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql(sql)
                .dbType(dbType)
                .sqlType("QUERY")
                .autoDetect(false)
                .build();

        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return formatSqlResponse(response);
    }

    /**
     * 执行DDL语句
     */
    private String executeDdl(JSONObject input) {
        String sql = input.getString("sql");
        String dbType = input.getString("dbType");
        if (dbType == null || dbType.isEmpty()) {
            dbType = "dm";
        }

        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql(sql)
                .dbType(dbType)
                .sqlType("DDL")
                .autoDetect(false)
                .build();

        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return formatSqlResponse(response);
    }

    /**
     * 执行DML语句
     */
    private String executeDml(JSONObject input) {
        String sql = input.getString("sql");
        String dbType = input.getString("dbType");
        if (dbType == null || dbType.isEmpty()) {
            dbType = "dm";
        }

        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql(sql)
                .dbType(dbType)
                .sqlType("DML")
                .autoDetect(false)
                .build();

        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return formatSqlResponse(response);
    }

    /**
     * 格式化SQL执行结果
     */
    private String formatSqlResponse(SqlExecuteResponse response) {
        if (!response.getSuccess()) {
            return "SQL执行失败: " + response.getMessage();
        }

        StringBuilder result = new StringBuilder();
        result.append("SQL执行成功\n");
        result.append("类型: ").append(response.getSqlType()).append("\n");
        result.append("执行时间: ").append(response.getExecutionTime()).append("ms\n");

        if (response.getAffectedRows() != null) {
            result.append("影响行数: ").append(response.getAffectedRows()).append("\n");
        }

        if (response.getData() != null && !response.getData().isEmpty()) {
            result.append("查询结果: \n");
            result.append(JSON.toJSONString(response.getData(), true));
        }

        return result.toString();
    }

    /**
     * 执行网络搜索(占位符)
     */
    private String executeWebSearch(JSONObject input) {
        String query = input.getString("query");
        log.info("执行网络搜索: {}", query);
        return "网络搜索功能暂未实现";
    }

    /**
     * 批量执行SQL语句(逐条执行,支持错误重试和进度回调)
     */
    private String executeBatchSql(JSONObject input, ProgressCallback callback) {
        String sqlListStr = input.getString("sqlList");
        String dbType = input.getString("dbType");
        if (dbType == null || dbType.isEmpty()) {
            dbType = "dm";
        }

        // 解析SQL列表
        com.alibaba.fastjson.JSONArray sqlArray;
        try {
            sqlArray = JSON.parseArray(sqlListStr);
        } catch (Exception e) {
            return "SQL列表格式错误: " + e.getMessage();
        }

        int total = sqlArray.size();
        int success = 0;
        int failed = 0;
        int retried = 0;
        StringBuilder result = new StringBuilder();
        result.append("批量SQL执行开始\n");
        result.append(String.format("总数: %d\n\n", total));

        // 逐条执行
        for (int i = 0; i < sqlArray.size(); i++) {
            JSONObject sqlItem = sqlArray.getJSONObject(i);
            String sql = sqlItem.getString("sql");
            String sqlType = sqlItem.getString("type"); // DDL, DML, QUERY
            String description = sqlItem.getString("description");

            int currentIndex = i + 1;
            String progressMsg = String.format("[%d/%d] %s", currentIndex, total,
                description != null ? description : "执行SQL");

            // 发送进度
            if (callback != null) {
                callback.onProgress(progressMsg);
            }

            log.info("批量执行 {}", progressMsg);
            result.append(progressMsg).append("\n");

            // 构建请求
            SqlExecuteRequest request = SqlExecuteRequest.builder()
                    .sql(sql)
                    .dbType(dbType)
                    .sqlType(sqlType)
                    .autoDetect(sqlType == null || sqlType.isEmpty())
                    .build();

            // 执行SQL
            SqlExecuteResponse response = sqlExecuteService.executeSql(request);

            if (response.getSuccess()) {
                success++;
                String successMsg = String.format("✓ 成功 (耗时: %dms)", response.getExecutionTime());
                result.append(successMsg).append("\n");
                if (callback != null) {
                    callback.onProgress(successMsg);
                }
            } else {
                // 执行失败,尝试自动修复
                String errorMsg = response.getMessage();
                result.append(String.format("✗ 失败: %s\n", errorMsg)).append("\n");

                // 检查是否可以自动修复
                String fixedSql = tryFixSql(sql, errorMsg, dbType);
                if (fixedSql != null && !fixedSql.equals(sql)) {
                    retried++;
                    String retryMsg = "尝试修复后重试...";
                    result.append(retryMsg).append("\n");
                    if (callback != null) {
                        callback.onProgress(retryMsg);
                    }

                    // 重试
                    request.setSql(fixedSql);
                    SqlExecuteResponse retryResponse = sqlExecuteService.executeSql(request);

                    if (retryResponse.getSuccess()) {
                        success++;
                        String retrySuccessMsg = String.format("✓ 修复后成功 (耗时: %dms)",
                            retryResponse.getExecutionTime());
                        result.append(retrySuccessMsg).append("\n");
                        if (callback != null) {
                            callback.onProgress(retrySuccessMsg);
                        }
                    } else {
                        failed++;
                        String retryFailMsg = String.format("✗ 修复后仍失败: %s",
                            retryResponse.getMessage());
                        result.append(retryFailMsg).append("\n");
                        if (callback != null) {
                            callback.onProgress(retryFailMsg);
                        }
                    }
                } else {
                    failed++;
                    if (callback != null) {
                        callback.onProgress("✗ 无法自动修复");
                    }
                }
            }

            result.append("\n");

            // 短暂延迟,避免数据库压力过大
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 汇总结果
        String summary = String.format("\n执行完成\n成功: %d, 失败: %d, 重试: %d, 总计: %d",
            success, failed, retried, total);
        result.append(summary);

        if (callback != null) {
            callback.onProgress(summary);
        }

        return result.toString();
    }

    /**
     * 尝试修复SQL错误
     */
    private String tryFixSql(String sql, String errorMsg, String dbType) {
        if (errorMsg == null) {
            return null;
        }

        String fixedSql = sql;

        // 修复1: SYSTIMESTAMP -> SYSDATE
        if (errorMsg.contains("SYSTIMESTAMP") || sql.contains("SYSTIMESTAMP")) {
            fixedSql = fixedSql.replace("SYSTIMESTAMP", "SYSDATE");
            log.info("自动修复: SYSTIMESTAMP -> SYSDATE");
        }

        // 修复2: CURRENT_TIMESTAMP -> SYSDATE
        if (errorMsg.contains("CURRENT_TIMESTAMP") || sql.contains("CURRENT_TIMESTAMP")) {
            fixedSql = fixedSql.replace("CURRENT_TIMESTAMP", "SYSDATE");
            log.info("自动修复: CURRENT_TIMESTAMP -> SYSDATE");
        }

        // 修复3: TEXT -> CLOB
        if (errorMsg.contains("TEXT") || (sql.contains("TEXT") && "dm".equalsIgnoreCase(dbType))) {
            fixedSql = fixedSql.replaceAll("\\bTEXT\\b", "CLOB");
            log.info("自动修复: TEXT -> CLOB");
        }

        // 修复4: 表已存在 - 添加注释说明跳过
        if (errorMsg.contains("已存在") || errorMsg.contains("already exists")) {
            log.info("表已存在,跳过");
            return null; // 返回null表示跳过
        }

        return fixedSql.equals(sql) ? null : fixedSql;
    }
}

