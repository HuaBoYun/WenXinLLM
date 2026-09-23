package com.huabo.contract.utils;

import com.huabo.contract.config.DatabaseCompatibilityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库兼容性工具类
 * 提供数据库兼容性相关的工具方法
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Component
@Slf4j
public class DatabaseCompatibilityUtils {

    @Autowired
    private DatabaseCompatibilityConfig.DatabaseCompatibilityHandler compatibilityHandler;

    /**
     * 获取兼容的SQL片段
     *
     * @param sqlType SQL类型
     * @param params 参数
     * @return SQL片段
     */
    public String getCompatibleSql(String sqlType, Object... params) {
        switch (sqlType.toUpperCase()) {
            case "CURRENT_TIME":
                return compatibilityHandler.getCurrentTimeFunction();
            case "CURRENT_DATE":
                return compatibilityHandler.getCurrentDateFunction();
            case "CONCAT":
                String[] columns = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    columns[i] = String.valueOf(params[i]);
                }
                return compatibilityHandler.getConcatFunction(columns);
            case "LIKE":
                return compatibilityHandler.getLikeCondition(String.valueOf(params[0]), String.valueOf(params[1]));
            case "DATE_FORMAT":
                return compatibilityHandler.getDateFormatFunction(String.valueOf(params[0]), String.valueOf(params[1]));
            case "DATE_DIFF":
                return compatibilityHandler.getDateDiffFunction(String.valueOf(params[0]), String.valueOf(params[1]));
            case "IF":
                return compatibilityHandler.getIfFunction(String.valueOf(params[0]), String.valueOf(params[1]), String.valueOf(params[2]));
            case "LAST_INSERT_ID":
                return compatibilityHandler.getLastInsertIdSql();
            default:
                log.warn("未知的SQL类型：{}", sqlType);
                return "";
        }
    }

    /**
     * 获取分页SQL
     *
     * @param sql 原始SQL
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 分页SQL
     */
    public String getPaginationSql(String sql, int offset, int limit) {
        return compatibilityHandler.getPaginationSql(sql, offset, limit);
    }

    /**
     * 获取数据库特定的数据类型映射
     *
     * @return 数据类型映射
     */
    public Map<String, String> getDataTypeMapping() {
        Map<String, String> mapping = new HashMap<>();
        
        if (compatibilityHandler.isMySQL()) {
            // MySQL数据类型映射
            mapping.put("BIGINT_AUTO", "BIGINT AUTO_INCREMENT");
            mapping.put("DATETIME", "DATETIME");
            mapping.put("TEXT", "TEXT");
            mapping.put("DECIMAL", "DECIMAL");
            mapping.put("VARCHAR", "VARCHAR");
            mapping.put("TINYINT", "TINYINT");
            mapping.put("CURRENT_TIMESTAMP", "CURRENT_TIMESTAMP");
        } else if (compatibilityHandler.isDaMeng()) {
            // 达梦数据类型映射
            mapping.put("BIGINT_AUTO", "BIGINT IDENTITY(1,1)");
            mapping.put("DATETIME", "DATETIME");
            mapping.put("TEXT", "CLOB");
            mapping.put("DECIMAL", "DECIMAL");
            mapping.put("VARCHAR", "VARCHAR");
            mapping.put("TINYINT", "SMALLINT");
            mapping.put("CURRENT_TIMESTAMP", "SYSDATE");
        }
        
        return mapping;
    }

    /**
     * 获取数据库特定的函数映射
     *
     * @return 函数映射
     */
    public Map<String, String> getFunctionMapping() {
        Map<String, String> mapping = new HashMap<>();
        
        if (compatibilityHandler.isMySQL()) {
            // MySQL函数映射
            mapping.put("SYSDATE", "NOW()");
            mapping.put("TRUNC", "DATE");
            mapping.put("TO_CHAR", "DATE_FORMAT");
            mapping.put("NVL", "IFNULL");
            mapping.put("DECODE", "CASE WHEN");
        } else if (compatibilityHandler.isDaMeng()) {
            // 达梦函数映射
            mapping.put("NOW", "SYSDATE");
            mapping.put("CURDATE", "TRUNC(SYSDATE)");
            mapping.put("DATE_FORMAT", "TO_CHAR");
            mapping.put("IFNULL", "NVL");
            mapping.put("IF", "CASE WHEN");
        }
        
        return mapping;
    }

    /**
     * 转换SQL语句以适配当前数据库
     *
     * @param sql 原始SQL
     * @return 转换后的SQL
     */
    public String convertSql(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }

        String convertedSql = sql;
        Map<String, String> functionMapping = getFunctionMapping();
        
        // 替换函数
        for (Map.Entry<String, String> entry : functionMapping.entrySet()) {
            convertedSql = convertedSql.replaceAll("(?i)\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        // 处理特殊语法
        if (compatibilityHandler.isDaMeng()) {
            // 达梦数据库特殊处理
            convertedSql = convertedSql.replaceAll("(?i)\\bLIMIT\\s+(\\d+)\\s*,\\s*(\\d+)", 
                "AND ROWNUM BETWEEN $1 + 1 AND $1 + $2");
            convertedSql = convertedSql.replaceAll("(?i)\\bLIMIT\\s+(\\d+)", 
                "AND ROWNUM <= $1");
        } else if (compatibilityHandler.isMySQL()) {
            // MySQL数据库特殊处理
            convertedSql = convertedSql.replaceAll("(?i)\\bROWNUM\\s*<=\\s*(\\d+)", 
                "LIMIT $1");
            convertedSql = convertedSql.replaceAll("(?i)\\bROWNUM\\s*BETWEEN\\s*(\\d+)\\s*AND\\s*(\\d+)", 
                "LIMIT $1, " + "($2 - $1 + 1)");
        }

        return convertedSql;
    }

    /**
     * 获取建表语句的数据库兼容版本
     *
     * @param tableName 表名
     * @param columns 列定义
     * @return 建表语句
     */
    public String getCreateTableSql(String tableName, Map<String, String> columns) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(tableName).append(" (\n");
        
        Map<String, String> dataTypeMapping = getDataTypeMapping();
        
        int index = 0;
        for (Map.Entry<String, String> column : columns.entrySet()) {
            if (index > 0) {
                sql.append(",\n");
            }
            
            String columnName = column.getKey();
            String columnType = column.getValue();
            
            // 转换数据类型
            for (Map.Entry<String, String> typeMapping : dataTypeMapping.entrySet()) {
                columnType = columnType.replace(typeMapping.getKey(), typeMapping.getValue());
            }
            
            sql.append("  ").append(columnName).append(" ").append(columnType);
            index++;
        }
        
        sql.append("\n)");
        
        // 添加数据库特定的表选项
        if (compatibilityHandler.isMySQL()) {
            sql.append(" ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
        }
        
        return sql.toString();
    }

    /**
     * 获取索引创建语句
     *
     * @param tableName 表名
     * @param indexName 索引名
     * @param columns 索引列
     * @param unique 是否唯一索引
     * @return 索引创建语句
     */
    public String getCreateIndexSql(String tableName, String indexName, String[] columns, boolean unique) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE ");
        if (unique) {
            sql.append("UNIQUE ");
        }
        sql.append("INDEX ").append(indexName);
        sql.append(" ON ").append(tableName);
        sql.append(" (").append(String.join(", ", columns)).append(")");
        
        return sql.toString();
    }

    /**
     * 获取序列创建语句（主要用于达梦数据库）
     *
     * @param sequenceName 序列名
     * @param startValue 起始值
     * @param increment 增量
     * @return 序列创建语句
     */
    public String getCreateSequenceSql(String sequenceName, long startValue, long increment) {
        if (compatibilityHandler.isDaMeng()) {
            return "CREATE SEQUENCE " + sequenceName + 
                   " START WITH " + startValue + 
                   " INCREMENT BY " + increment + 
                   " NOCACHE";
        } else {
            // MySQL不支持序列，返回空字符串
            return "";
        }
    }

    /**
     * 获取下一个序列值的SQL
     *
     * @param sequenceName 序列名
     * @return 获取序列值的SQL
     */
    public String getNextSequenceValueSql(String sequenceName) {
        if (compatibilityHandler.isDaMeng()) {
            return sequenceName + ".NEXTVAL";
        } else {
            // MySQL使用AUTO_INCREMENT，返回空字符串
            return "";
        }
    }

    /**
     * 检查当前数据库类型
     *
     * @return 数据库类型
     */
    public DatabaseCompatibilityConfig.DatabaseType getCurrentDatabaseType() {
        return compatibilityHandler.getDatabaseType();
    }

    /**
     * 判断是否为MySQL数据库
     *
     * @return 是否为MySQL
     */
    public boolean isMySQL() {
        return compatibilityHandler.isMySQL();
    }

    /**
     * 判断是否为达梦数据库
     *
     * @return 是否为达梦数据库
     */
    public boolean isDaMeng() {
        return compatibilityHandler.isDaMeng();
    }

    /**
     * 获取数据库产品名称
     *
     * @return 数据库产品名称
     */
    public String getDatabaseProductName() {
        return compatibilityHandler.getDatabaseType().getDescription();
    }

    /**
     * 记录数据库兼容性信息
     */
    public void logDatabaseInfo() {
        log.info("当前数据库类型：{}", getDatabaseProductName());
        log.info("数据库兼容性处理器已启用");
    }
}
