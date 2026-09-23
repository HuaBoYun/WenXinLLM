package com.huabo.bigmodel.service;

import com.huabo.bigmodel.dto.SqlExecuteRequest;
import com.huabo.bigmodel.dto.SqlExecuteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**
 * SQL执行服务
 * 支持DDL(建表)、DML(增删改)、QUERY(查询)
 */
@Slf4j
@Service
public class SqlExecuteService {

    private final JdbcTemplate dmJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    // SQL类型检测正则
    private static final Pattern DDL_PATTERN = Pattern.compile(
            "^\\s*(CREATE|ALTER|DROP|TRUNCATE|RENAME)\\s+", Pattern.CASE_INSENSITIVE);
    private static final Pattern DML_PATTERN = Pattern.compile(
            "^\\s*(INSERT|UPDATE|DELETE)\\s+", Pattern.CASE_INSENSITIVE);
    private static final Pattern QUERY_PATTERN = Pattern.compile(
            "^\\s*(SELECT|SHOW|DESC|DESCRIBE|EXPLAIN)\\s+", Pattern.CASE_INSENSITIVE);

    // 危险操作检测正则
    private static final Pattern DANGEROUS_PATTERN = Pattern.compile(
            "^\\s*(DROP|TRUNCATE|DELETE)\\s+", Pattern.CASE_INSENSITIVE);

    public SqlExecuteService(
            @Qualifier("dmJdbcTemplate") JdbcTemplate dmJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate) {
        this.dmJdbcTemplate = dmJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    /**
     * 执行SQL语句
     */
    @Transactional(rollbackFor = Exception.class)
    public SqlExecuteResponse executeSql(SqlExecuteRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            // 安全检查：禁止危险操作
            if (isDangerousOperation(request.getSql())) {
                return SqlExecuteResponse.builder()
                        .success(false)
                        .message("安全限制: 不允许执行DROP、TRUNCATE、DELETE等危险操作")
                        .executionTime(System.currentTimeMillis() - startTime)
                        .build();
            }

            // 选择数据源
            JdbcTemplate jdbcTemplate = selectDataSource(request.getDbType());

            // 检测SQL类型
            String sqlType = detectSqlType(request);
            log.info("执行SQL - 类型: {}, 数据库: {}, SQL: {}", sqlType, request.getDbType(), request.getSql());
            
            SqlExecuteResponse response;
            switch (sqlType) {
                case "DDL":
                    response = executeDdl(jdbcTemplate, request.getSql());
                    break;
                case "DML":
                    response = executeDml(jdbcTemplate, request.getSql());
                    break;
                case "QUERY":
                    response = executeQuery(jdbcTemplate, request.getSql());
                    break;
                default:
                    return SqlExecuteResponse.error("不支持的SQL类型: " + sqlType);
            }
            
            response.setSqlType(sqlType);
            response.setExecutionTime(System.currentTimeMillis() - startTime);
            return response;
            
        } catch (Exception e) {
            log.error("SQL执行失败", e);
            return SqlExecuteResponse.builder()
                    .success(false)
                    .message("SQL执行失败: " + e.getMessage())
                    .executionTime(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    /**
     * 执行DDL语句(建表、修改表结构等)
     * 支持批量执行多个DDL语句
     */
    private SqlExecuteResponse executeDdl(JdbcTemplate jdbcTemplate, String sql) {
        // 分割多个DDL语句
        List<String> ddlStatements = splitDdlStatements(sql);

        int successCount = 0;
        int skipCount = 0;
        StringBuilder messages = new StringBuilder();

        for (String statement : ddlStatements) {
            if (statement.trim().isEmpty()) {
                continue;
            }

            try {
                jdbcTemplate.execute(statement);
                successCount++;
                log.info("DDL语句执行成功: {}", statement.substring(0, Math.min(50, statement.length())));
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                // 如果是表已存在的错误,跳过并记录
                if (errorMsg != null && (errorMsg.contains("已存在") || errorMsg.contains("already exists"))) {
                    skipCount++;
                    log.warn("表已存在,跳过: {}", statement.substring(0, Math.min(50, statement.length())));
                    messages.append("跳过(已存在): ").append(extractTableName(statement)).append("\n");
                } else {
                    // 其他错误则抛出
                    throw e;
                }
            }
        }

        String message = String.format("DDL语句执行完成 - 成功: %d, 跳过: %d", successCount, skipCount);
        if (messages.length() > 0) {
            message += "\n" + messages.toString();
        }

        return SqlExecuteResponse.builder()
                .success(true)
                .message(message)
                .affectedRows(successCount)
                .build();
    }

    /**
     * 分割多个DDL语句
     * 支持通过注释分隔的多个CREATE TABLE语句
     */
    private List<String> splitDdlStatements(String sql) {
        List<String> statements = new ArrayList<>();

        // 移除SQL中的注释行(以--开头的行)
        String[] lines = sql.split("\n");
        StringBuilder currentStatement = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();

            // 跳过空行和纯注释行
            if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                // 如果当前语句不为空,且遇到注释行,可能是新语句的开始
                if (currentStatement.length() > 0 && trimmedLine.startsWith("--")) {
                    String stmt = currentStatement.toString().trim();
                    if (!stmt.isEmpty() && stmt.endsWith(";")) {
                        statements.add(stmt);
                        currentStatement = new StringBuilder();
                    }
                }
                continue;
            }

            currentStatement.append(line).append("\n");

            // 如果遇到分号,表示一个语句结束
            if (trimmedLine.endsWith(";")) {
                String stmt = currentStatement.toString().trim();
                if (!stmt.isEmpty()) {
                    statements.add(stmt);
                    currentStatement = new StringBuilder();
                }
            }
        }

        // 处理最后一个语句(可能没有分号)
        String lastStmt = currentStatement.toString().trim();
        if (!lastStmt.isEmpty()) {
            statements.add(lastStmt);
        }

        // 如果没有分割出语句,返回原始SQL
        if (statements.isEmpty()) {
            statements.add(sql);
        }

        log.info("分割DDL语句: 原始1个 -> 分割后{}个", statements.size());
        return statements;
    }

    /**
     * 从DDL语句中提取表名
     */
    private String extractTableName(String ddl) {
        try {
            String upperDdl = ddl.toUpperCase();
            int tableIndex = upperDdl.indexOf("TABLE");
            if (tableIndex > 0) {
                String afterTable = ddl.substring(tableIndex + 5).trim();
                int spaceIndex = afterTable.indexOf(" ");
                int parenIndex = afterTable.indexOf("(");
                int endIndex = Math.min(
                    spaceIndex > 0 ? spaceIndex : afterTable.length(),
                    parenIndex > 0 ? parenIndex : afterTable.length()
                );
                return afterTable.substring(0, endIndex).trim();
            }
        } catch (Exception e) {
            log.warn("提取表名失败", e);
        }
        return "未知表";
    }

    /**
     * 执行DML语句(插入、更新、删除)
     */
    private SqlExecuteResponse executeDml(JdbcTemplate jdbcTemplate, String sql) {
        int affectedRows = jdbcTemplate.update(sql);
        return SqlExecuteResponse.builder()
                .success(true)
                .message("DML语句执行成功")
                .affectedRows(affectedRows)
                .build();
    }

    /**
     * 执行查询语句
     */
    private SqlExecuteResponse executeQuery(JdbcTemplate jdbcTemplate, String sql) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        
        // 获取列信息
        List<SqlExecuteResponse.ColumnInfo> columns = new ArrayList<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columns.add(SqlExecuteResponse.ColumnInfo.builder()
                    .name(metaData.getColumnName(i))
                    .type(metaData.getColumnTypeName(i))
                    .label(metaData.getColumnLabel(i))
                    .build());
        }
        
        // 获取数据
        List<Map<String, Object>> data = new ArrayList<>();
        while (rowSet.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rowSet.getObject(i));
            }
            data.add(row);
        }
        
        return SqlExecuteResponse.builder()
                .success(true)
                .message("查询成功")
                .data(data)
                .columns(columns)
                .build();
    }

    /**
     * 选择数据源
     */
    private JdbcTemplate selectDataSource(String dbType) {
        if ("mysql".equalsIgnoreCase(dbType)) {
            return mysqlJdbcTemplate;
        }
        return dmJdbcTemplate; // 默认使用达梦数据库
    }

    /**
     * 检测SQL类型
     */
    private String detectSqlType(SqlExecuteRequest request) {
        // 如果指定了SQL类型且不自动检测,直接返回
        if (!request.getAutoDetect() && request.getSqlType() != null) {
            return request.getSqlType();
        }
        
        String sql = request.getSql().trim();
        
        if (DDL_PATTERN.matcher(sql).find()) {
            return "DDL";
        } else if (DML_PATTERN.matcher(sql).find()) {
            return "DML";
        } else if (QUERY_PATTERN.matcher(sql).find()) {
            return "QUERY";
        }

        return "UNKNOWN";
    }

    /**
     * 检测是否为危险操作
     */
    private boolean isDangerousOperation(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return false;
        }

        String trimmedSql = sql.trim();
        return DANGEROUS_PATTERN.matcher(trimmedSql).find();
    }
}

