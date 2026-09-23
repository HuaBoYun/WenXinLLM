package com.huabo.fxgl.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接测试工具类
 * 用于诊断评估模型统计接口问题
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Component
public class DatabaseTestUtil {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试数据库连接
     */
    public Map<String, Object> testDatabaseConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            result.put("connected", true);
            result.put("databaseProductName", metaData.getDatabaseProductName());
            result.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            result.put("driverName", metaData.getDriverName());
            result.put("driverVersion", metaData.getDriverVersion());
            result.put("url", metaData.getURL());
            result.put("userName", metaData.getUserName());
            
            log.info("数据库连接测试成功: {}", result);
            
        } catch (SQLException e) {
            result.put("connected", false);
            result.put("error", e.getMessage());
            log.error("数据库连接测试失败", e);
        }
        
        return result;
    }

    /**
     * 检查评估模型表是否存在
     */
    public Map<String, Object> checkEvaluationModelTable() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查表是否存在
            String checkTableSql = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'TBL_EVALUATION_MODEL'";
            Integer tableExists = jdbcTemplate.queryForObject(checkTableSql, Integer.class);
            result.put("tableExists", tableExists != null && tableExists > 0);
            
            if (tableExists != null && tableExists > 0) {
                // 检查表中数据数量
                String countSql = "SELECT COUNT(*) FROM TBL_EVALUATION_MODEL";
                Integer recordCount = jdbcTemplate.queryForObject(countSql, Integer.class);
                result.put("recordCount", recordCount);
                
                // 检查状态字段数据
                String statusSql = "SELECT STATUS, COUNT(*) as count FROM TBL_EVALUATION_MODEL WHERE STATUS IS NOT NULL GROUP BY STATUS";
                try {
                    Map<String, Object> statusStats = new HashMap<>();
                    jdbcTemplate.query(statusSql, rs -> {
                        statusStats.put(rs.getString("STATUS"), rs.getLong("count"));
                    });
                    result.put("statusStatistics", statusStats);
                } catch (Exception e) {
                    result.put("statusStatisticsError", e.getMessage());
                }
                
                // 检查业务场景字段数据
                String scenarioSql = "SELECT BUSINESS_SCENARIO, COUNT(*) as count FROM TBL_EVALUATION_MODEL WHERE BUSINESS_SCENARIO IS NOT NULL GROUP BY BUSINESS_SCENARIO";
                try {
                    Map<String, Object> scenarioStats = new HashMap<>();
                    jdbcTemplate.query(scenarioSql, rs -> {
                        scenarioStats.put(rs.getString("BUSINESS_SCENARIO"), rs.getLong("count"));
                    });
                    result.put("scenarioStatistics", scenarioStats);
                } catch (Exception e) {
                    result.put("scenarioStatisticsError", e.getMessage());
                }
            }
            
            log.info("评估模型表检查结果: {}", result);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            log.error("检查评估模型表失败", e);
        }
        
        return result;
    }

    /**
     * 执行SQL测试
     */
    public Map<String, Object> executeSqlTest(String sql) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("执行SQL测试: {}", sql);
            
            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                // 查询SQL
                jdbcTemplate.query(sql, rs -> {
                    // 这里只是测试SQL是否能正常执行
                    // 实际结果在日志中查看
                });
                result.put("success", true);
                result.put("message", "SQL执行成功");
            } else {
                result.put("success", false);
                result.put("message", "只支持SELECT查询");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            log.error("SQL执行失败: {}", sql, e);
        }
        
        return result;
    }

    /**
     * 获取表结构信息
     */
    public Map<String, Object> getTableStructure(String tableName) {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // 获取表的列信息
            try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                Map<String, Map<String, Object>> columnInfo = new HashMap<>();
                
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    Map<String, Object> columnDetails = new HashMap<>();
                    columnDetails.put("dataType", columns.getString("TYPE_NAME"));
                    columnDetails.put("columnSize", columns.getInt("COLUMN_SIZE"));
                    columnDetails.put("nullable", columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    columnDetails.put("defaultValue", columns.getString("COLUMN_DEF"));
                    
                    columnInfo.put(columnName, columnDetails);
                }
                
                result.put("columns", columnInfo);
                result.put("columnCount", columnInfo.size());
            }
            
            log.info("表结构信息获取成功: {}", tableName);
            
        } catch (SQLException e) {
            result.put("error", e.getMessage());
            log.error("获取表结构信息失败: {}", tableName, e);
        }
        
        return result;
    }
}
