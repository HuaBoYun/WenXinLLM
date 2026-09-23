package com.huabo.contract.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 数据库兼容性配置类
 * 支持达梦数据库和MySQL数据库的兼容性处理
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Configuration
public class DatabaseCompatibilityConfig {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.datasource.driverClassName:}")
    private String driverClassName;

    /**
     * 数据库类型枚举
     */
    public enum DatabaseType {
        MYSQL("mysql", "MySQL数据库"),
        DAMENG("dm", "达梦数据库"),
        UNKNOWN("unknown", "未知数据库");

        private final String code;
        private final String description;

        DatabaseType(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    private DatabaseType currentDatabaseType;

    @PostConstruct
    public void init() {
        this.currentDatabaseType = detectDatabaseType();
        log.info("检测到数据库类型：{}", currentDatabaseType.getDescription());
    }

    /**
     * 检测当前数据库类型
     *
     * @return 数据库类型
     */
    private DatabaseType detectDatabaseType() {
        if (StringUtils.hasText(datasourceUrl)) {
            String url = datasourceUrl.toLowerCase();
            if (url.contains("jdbc:mysql")) {
                return DatabaseType.MYSQL;
            } else if (url.contains("jdbc:dm")) {
                return DatabaseType.DAMENG;
            }
        }

        if (StringUtils.hasText(driverClassName)) {
            String driver = driverClassName.toLowerCase();
            if (driver.contains("mysql")) {
                return DatabaseType.MYSQL;
            } else if (driver.contains("dm.jdbc.driver")) {
                return DatabaseType.DAMENG;
            }
        }

        return DatabaseType.UNKNOWN;
    }

    /**
     * 获取当前数据库类型
     *
     * @return 数据库类型
     */
    public DatabaseType getCurrentDatabaseType() {
        return currentDatabaseType;
    }

    /**
     * 判断是否为MySQL数据库
     *
     * @return 是否为MySQL
     */
    public boolean isMySQL() {
        return DatabaseType.MYSQL.equals(currentDatabaseType);
    }

    /**
     * 判断是否为达梦数据库
     *
     * @return 是否为达梦数据库
     */
    public boolean isDaMeng() {
        return DatabaseType.DAMENG.equals(currentDatabaseType);
    }

    /**
     * 获取数据库兼容性处理器
     *
     * @return 数据库兼容性处理器
     */
    @Bean
    public DatabaseCompatibilityHandler databaseCompatibilityHandler() {
        return new DatabaseCompatibilityHandler(currentDatabaseType);
    }

    /**
     * 数据库兼容性处理器
     */
    public static class DatabaseCompatibilityHandler {

        private final DatabaseType databaseType;

        public DatabaseCompatibilityHandler(DatabaseType databaseType) {
            this.databaseType = databaseType;
        }

        /**
         * 获取当前时间函数
         *
         * @return 当前时间函数
         */
        public String getCurrentTimeFunction() {
            switch (databaseType) {
                case MYSQL:
                    return "NOW()";
                case DAMENG:
                    return "SYSDATE";
                default:
                    return "NOW()";
            }
        }

        /**
         * 获取当前日期函数
         *
         * @return 当前日期函数
         */
        public String getCurrentDateFunction() {
            switch (databaseType) {
                case MYSQL:
                    return "CURDATE()";
                case DAMENG:
                    return "TRUNC(SYSDATE)";
                default:
                    return "CURDATE()";
            }
        }

        /**
         * 获取字符串连接函数
         *
         * @param columns 要连接的列
         * @return 字符串连接函数
         */
        public String getConcatFunction(String... columns) {
            switch (databaseType) {
                case MYSQL:
                    return "CONCAT(" + String.join(", ", columns) + ")";
                case DAMENG:
                    return String.join(" || ", columns);
                default:
                    return "CONCAT(" + String.join(", ", columns) + ")";
            }
        }

        /**
         * 获取分页查询语句
         *
         * @param sql 原始SQL
         * @param offset 偏移量
         * @param limit 限制数量
         * @return 分页SQL
         */
        public String getPaginationSql(String sql, int offset, int limit) {
            switch (databaseType) {
                case MYSQL:
                    return sql + " LIMIT " + offset + ", " + limit;
                case DAMENG:
                    return "SELECT * FROM (SELECT ROWNUM rn, t.* FROM (" + sql + ") t WHERE ROWNUM <= " + (offset + limit) + ") WHERE rn > " + offset;
                default:
                    return sql + " LIMIT " + offset + ", " + limit;
            }
        }

        /**
         * 获取自增主键插入后的ID查询语句
         *
         * @return ID查询语句
         */
        public String getLastInsertIdSql() {
            switch (databaseType) {
                case MYSQL:
                    return "SELECT LAST_INSERT_ID()";
                case DAMENG:
                    return "SELECT LAST_INSERT_ROWID()";
                default:
                    return "SELECT LAST_INSERT_ID()";
            }
        }

        /**
         * 获取模糊查询语句
         *
         * @param column 列名
         * @param value 查询值
         * @return 模糊查询语句
         */
        public String getLikeCondition(String column, String value) {
            switch (databaseType) {
                case MYSQL:
                    return column + " LIKE CONCAT('%', ?, '%')";
                case DAMENG:
                    return column + " LIKE '%' || ? || '%'";
                default:
                    return column + " LIKE CONCAT('%', ?, '%')";
            }
        }

        /**
         * 获取日期格式化函数
         *
         * @param dateColumn 日期列
         * @param format 格式
         * @return 格式化函数
         */
        public String getDateFormatFunction(String dateColumn, String format) {
            switch (databaseType) {
                case MYSQL:
                    return "DATE_FORMAT(" + dateColumn + ", '" + convertToMySQLFormat(format) + "')";
                case DAMENG:
                    return "TO_CHAR(" + dateColumn + ", '" + convertToDaMengFormat(format) + "')";
                default:
                    return "DATE_FORMAT(" + dateColumn + ", '" + convertToMySQLFormat(format) + "')";
            }
        }

        /**
         * 获取日期差值计算函数
         *
         * @param date1 日期1
         * @param date2 日期2
         * @return 日期差值函数
         */
        public String getDateDiffFunction(String date1, String date2) {
            switch (databaseType) {
                case MYSQL:
                    return "DATEDIFF(" + date1 + ", " + date2 + ")";
                case DAMENG:
                    return "(" + date1 + " - " + date2 + ")";
                default:
                    return "DATEDIFF(" + date1 + ", " + date2 + ")";
            }
        }

        /**
         * 获取IF条件函数
         *
         * @param condition 条件
         * @param trueValue 真值
         * @param falseValue 假值
         * @return IF函数
         */
        public String getIfFunction(String condition, String trueValue, String falseValue) {
            switch (databaseType) {
                case MYSQL:
                    return "IF(" + condition + ", " + trueValue + ", " + falseValue + ")";
                case DAMENG:
                    return "CASE WHEN " + condition + " THEN " + trueValue + " ELSE " + falseValue + " END";
                default:
                    return "IF(" + condition + ", " + trueValue + ", " + falseValue + ")";
            }
        }

        /**
         * 转换为MySQL日期格式
         */
        private String convertToMySQLFormat(String format) {
            return format.replace("yyyy", "%Y")
                    .replace("MM", "%m")
                    .replace("dd", "%d")
                    .replace("HH", "%H")
                    .replace("mm", "%i")
                    .replace("ss", "%s");
        }

        /**
         * 转换为达梦日期格式
         */
        private String convertToDaMengFormat(String format) {
            return format.replace("yyyy", "YYYY")
                    .replace("MM", "MM")
                    .replace("dd", "DD")
                    .replace("HH", "HH24")
                    .replace("mm", "MI")
                    .replace("ss", "SS");
        }

        /**
         * 获取数据库类型
         *
         * @return 数据库类型
         */
        public DatabaseType getDatabaseType() {
            return databaseType;
        }

        /**
         * 判断是否为MySQL数据库
         *
         * @return 是否为MySQL
         */
        public boolean isMySQL() {
            return DatabaseType.MYSQL.equals(databaseType);
        }

        /**
         * 判断是否为达梦数据库
         *
         * @return 是否为达梦数据库
         */
        public boolean isDaMeng() {
            return DatabaseType.DAMENG.equals(databaseType);
        }
    }
}
