package com.huabo.fxgl.util;

import com.huabo.fxgl.entity.TblDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

/**
 * SQL执行工具类
 * @author 华博云
 * @since 2025-01-21
 */
@Slf4j
public class SqlExecutorUtil {

    /**
     * 执行SQL查询并返回结果
     * 
     * @param dataSource 数据源配置
     * @param sql SQL语句
     * @return 执行结果
     */
    public static Map<String, Object> executeSql(TblDataSource dataSource, String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            // 获取数据库连接
            connection = DatabaseConnectionUtil.getConnection(dataSource);
            
            // 安全检查：防止危险操作
            validateSqlSafety(sql);
            
            long startTime = System.currentTimeMillis();
            
            // 判断SQL类型
            String upperSql = sql.trim().toUpperCase();
            if (upperSql.startsWith("SELECT") || upperSql.startsWith("WITH") || 
                upperSql.startsWith("SHOW") || upperSql.startsWith("DESC") || 
                upperSql.startsWith("EXPLAIN")) {
                
                // 查询语句
                statement = connection.prepareStatement(sql);
                statement.setQueryTimeout(30); // 30秒超时
                resultSet = statement.executeQuery();
                
                // 处理查询结果
                return processQueryResult(resultSet, startTime);
                
            } else {
                // 非查询语句（INSERT、UPDATE、DELETE等）
                statement = connection.prepareStatement(sql);
                statement.setQueryTimeout(30);
                int affectedRows = statement.executeUpdate();
                
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                
                // 返回执行结果
                Map<String, Object> result = new HashMap<>();
                result.put("data", Collections.singletonList(createExecutionResultRow(affectedRows)));
                result.put("columns", createExecutionResultColumns());
                result.put("rowCount", 1);
                result.put("executionTime", executionTime);
                result.put("success", true);
                result.put("message", "SQL执行成功，影响 " + affectedRows + " 行");
                
                return result;
            }
            
        } catch (SQLException e) {
            log.error("SQL执行失败: {}", e.getMessage(), e);
            throw e;
        } finally {
            // 关闭资源
            closeResources(resultSet, statement, connection);
        }
    }
    
    /**
     * 处理查询结果
     */
    private static Map<String, Object> processQueryResult(ResultSet resultSet, long startTime) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        // 构建列信息
        List<Map<String, Object>> columns = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            Map<String, Object> column = new HashMap<>();
            column.put("prop", metaData.getColumnName(i));
            column.put("label", metaData.getColumnLabel(i));
            column.put("type", getColumnType(metaData.getColumnType(i)));
            columns.add(column);
        }
        
        // 构建数据行
        List<Map<String, Object>> data = new ArrayList<>();
        int rowCount = 0;
        while (resultSet.next() && rowCount < 1000) { // 限制最多返回1000行
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                row.put(columnName, value);
            }
            data.add(row);
            rowCount++;
        }
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("columns", columns);
        result.put("rowCount", rowCount);
        result.put("executionTime", executionTime);
        result.put("success", true);
        result.put("message", "查询成功，返回 " + rowCount + " 条记录");
        
        return result;
    }
    
    /**
     * 创建执行结果行
     */
    private static Map<String, Object> createExecutionResultRow(int affectedRows) {
        Map<String, Object> row = new HashMap<>();
        row.put("RESULT", "执行成功");
        row.put("AFFECTED_ROWS", affectedRows);
        row.put("MESSAGE", "SQL语句执行完成");
        row.put("TIMESTAMP", new java.util.Date().toString());
        return row;
    }
    
    /**
     * 创建执行结果列定义
     */
    private static List<Map<String, Object>> createExecutionResultColumns() {
        List<Map<String, Object>> columns = new ArrayList<>();
        
        String[] columnNames = {"RESULT", "AFFECTED_ROWS", "MESSAGE", "TIMESTAMP"};
        String[] columnLabels = {"执行结果", "影响行数", "消息", "执行时间"};
        
        for (int i = 0; i < columnNames.length; i++) {
            Map<String, Object> column = new HashMap<>();
            column.put("prop", columnNames[i]);
            column.put("label", columnLabels[i]);
            column.put("type", i == 1 ? "number" : "string");
            columns.add(column);
        }
        
        return columns;
    }
    
    /**
     * 获取列类型
     */
    private static String getColumnType(int sqlType) {
        switch (sqlType) {
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.REAL:
                return "number";
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return "date";
            case Types.BOOLEAN:
            case Types.BIT:
                return "boolean";
            default:
                return "string";
        }
    }
    
    /**
     * SQL安全检查
     */
    private static void validateSqlSafety(String sql) throws SQLException {
        String upperSql = sql.trim().toUpperCase();
        
        // 检查危险操作
        String[] dangerousKeywords = {
            "DROP", "TRUNCATE", "DELETE", "UPDATE", "INSERT", 
            "CREATE", "ALTER", "GRANT", "REVOKE", "EXEC", "EXECUTE"
        };
        
        for (String keyword : dangerousKeywords) {
            if (upperSql.contains(keyword)) {
                // 允许SELECT语句中的子查询包含这些关键字
                if (!upperSql.startsWith("SELECT") && !upperSql.startsWith("WITH")) {
                    throw new SQLException("不允许执行包含 " + keyword + " 的SQL语句，仅支持查询操作");
                }
            }
        }
        
        // 检查SQL注入风险
        if (upperSql.contains("--") || upperSql.contains("/*") || upperSql.contains("*/")) {
            log.warn("SQL包含注释符号，请检查是否存在安全风险: {}", sql);
        }
    }
    
    /**
     * 关闭数据库资源
     */
    private static void closeResources(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            log.error("关闭ResultSet失败", e);
        }
        
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            log.error("关闭Statement失败", e);
        }
        
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("关闭Connection失败", e);
        }
    }
}
