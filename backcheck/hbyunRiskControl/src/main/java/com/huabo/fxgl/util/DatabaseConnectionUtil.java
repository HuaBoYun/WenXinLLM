package com.huabo.fxgl.util;

import com.huabo.fxgl.entity.TblDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接工具类
 * @author 华博云
 * @since 2025-01-21
 */
@Slf4j
public class DatabaseConnectionUtil {

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(TblDataSource dataSource) throws SQLException {
        String url = buildConnectionUrl(dataSource);
        String username = dataSource.getUsername();
        String password = dataSource.getPassword();
        
        log.info("连接数据库: {}", url);
        
        try {
            // 根据数据库类型加载驱动
            loadDriver(dataSource.getSourceType());
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("数据库驱动加载失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建连接URL
     */
    private static String buildConnectionUrl(TblDataSource dataSource) {
        String hostIp = dataSource.getHostIp();
        Integer port = dataSource.getPort();
        String databaseName = dataSource.getDatabaseName();
        String sourceType = dataSource.getSourceType();

        switch (sourceType.toUpperCase()) {
            case "DM":
                return String.format("jdbc:dm://%s:%d/%s", hostIp, port, databaseName);
            case "ORACLE":
                return String.format("jdbc:oracle:thin:@%s:%d:%s", hostIp, port, databaseName);
            case "MYSQL":
                return String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8&useSSL=false", 
                        hostIp, port, databaseName);
            default:
                throw new IllegalArgumentException("不支持的数据库类型: " + sourceType);
        }
    }

    /**
     * 加载数据库驱动
     */
    private static void loadDriver(String sourceType) throws ClassNotFoundException {
        switch (sourceType.toUpperCase()) {
            case "DM":
                Class.forName("dm.jdbc.driver.DmDriver");
                break;
            case "ORACLE":
                Class.forName("oracle.jdbc.driver.OracleDriver");
                break;
            case "MYSQL":
                Class.forName("com.mysql.cj.jdbc.Driver");
                break;
            default:
                throw new ClassNotFoundException("不支持的数据库类型: " + sourceType);
        }
    }

    /**
     * 测试数据库连接
     */
    public static boolean testConnection(TblDataSource dataSource) {
        try (Connection connection = getConnection(dataSource)) {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            log.error("数据库连接测试失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取数据库中的所有表信息
     */
    public static List<Map<String, Object>> getAllTables(TblDataSource dataSource) throws SQLException {
        List<Map<String, Object>> tables = new ArrayList<>();
        
        try (Connection connection = getConnection(dataSource)) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // 获取表信息
            try (ResultSet rs = metaData.getTables(null, getSchemaName(dataSource), "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    Map<String, Object> table = new HashMap<>();
                    table.put("tableName", rs.getString("TABLE_NAME"));
                    table.put("tableComment", rs.getString("REMARKS"));
                    table.put("tableType", rs.getString("TABLE_TYPE"));
                    tables.add(table);
                }
            }
        }
        
        return tables;
    }

    /**
     * 获取表的列信息
     */
    public static List<Map<String, Object>> getTableColumns(TblDataSource dataSource, String tableName) throws SQLException {
        List<Map<String, Object>> columns = new ArrayList<>();
        
        try (Connection connection = getConnection(dataSource)) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // 获取主键信息
            Map<String, Boolean> primaryKeys = new HashMap<>();
            try (ResultSet pkRs = metaData.getPrimaryKeys(null, getSchemaName(dataSource), tableName)) {
                while (pkRs.next()) {
                    primaryKeys.put(pkRs.getString("COLUMN_NAME"), true);
                }
            }
            
            // 获取列信息
            try (ResultSet rs = metaData.getColumns(null, getSchemaName(dataSource), tableName, "%")) {
                int order = 1;
                while (rs.next()) {
                    Map<String, Object> column = new HashMap<>();
                    String columnName = rs.getString("COLUMN_NAME");
                    
                    column.put("columnName", columnName);
                    column.put("columnType", rs.getString("TYPE_NAME"));
                    column.put("columnLength", rs.getInt("COLUMN_SIZE"));
                    column.put("columnPrecision", rs.getInt("DECIMAL_DIGITS"));
                    column.put("columnScale", rs.getInt("DECIMAL_DIGITS"));
                    column.put("isNullable", rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable ? "Y" : "N");
                    column.put("isPrimaryKey", primaryKeys.containsKey(columnName) ? "Y" : "N");
                    column.put("columnComment", rs.getString("REMARKS"));
                    column.put("columnDefault", rs.getString("COLUMN_DEF"));
                    column.put("columnOrder", order++);
                    
                    columns.add(column);
                }
            }
        }
        
        return columns;
    }

    /**
     * 获取Schema名称
     */
    private static String getSchemaName(TblDataSource dataSource) {
        String sourceType = dataSource.getSourceType().toUpperCase();
        switch (sourceType) {
            case "DM":
            case "ORACLE":
                return dataSource.getUsername().toUpperCase();
            case "MYSQL":
                return dataSource.getDatabaseName();
            default:
                return null;
        }
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("关闭数据库连接失败: {}", e.getMessage());
            }
        }
    }
}
