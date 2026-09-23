package com.huabo.financialdata.jdbc;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author weizhihui
 * @Date 2024/4/25 9:34
 * @Description 功能简要描述
 */
@Component
public class MysqlJdbcExecuteTemplateImpl extends JdbcExecuteTemplate {
    private static final Logger log = LoggerFactory.getLogger(MysqlJdbcExecuteTemplateImpl.class);

    @Override
    public String getDriver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String createTableSql(String tableName, List<JdbcColumnVO> columns) {
        if (StringUtils.isBlank(tableName)) {
            log.error("oracle createTable 表名为空");
            return null;
        }
        if (CollectionUtils.isEmpty(columns)) {
            log.error("oracle createTable 表字段为空");
            return null;
        }

        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE \"");
        sqlBuilder.append(tableName);
        sqlBuilder.append("\" (");

        List<String> columnsList = new ArrayList<>(); // 收集表字段

        for (JdbcColumnVO column : columns) {
            StringBuilder columnBuilder = new StringBuilder();
            // 字段名称
            columnBuilder.append("\"");
            columnBuilder.append(column.getName());
            columnBuilder.append("\"");
            columnBuilder.append(" ");

            //字段类型
            String dbType = getDbType(column.getFieldType());
            columnBuilder.append(dbType);
            columnsList.add(columnBuilder.toString());
        }

        //拼接columns
        sqlBuilder.append(String.join(",", columnsList));

        //创建结构体结束
        sqlBuilder.append(")");

        log.info("oracle 创建数据库表结构，sql：{}", sqlBuilder);
        return sqlBuilder.toString();
    }

    @Override
    public String updateCommentSql(String tableName,JdbcColumnVO jdbcColumnVO) {
        StringBuilder commentBuilder = new StringBuilder();
        commentBuilder.append("ALTER TABLE ");
        commentBuilder.append(tableName);
        commentBuilder.append(" MODIFY COLUMN ");

        commentBuilder.append(jdbcColumnVO.getName());
        String dbType = getDbType(jdbcColumnVO.getFieldType());

        commentBuilder.append(" ");
        commentBuilder.append(dbType);

        commentBuilder.append(" COMMENT");

        commentBuilder.append("'");
        commentBuilder.append(jdbcColumnVO.getComment() == null ? "" : jdbcColumnVO.getComment());
        commentBuilder.append("'");

        return commentBuilder.toString();
    }

    @Override
    public String createIndexSql(String tableName,String indexName,String columnName) {
        StringBuilder indexOptBuilder = new StringBuilder();
        indexOptBuilder.append("CREATE INDEX ");
        indexOptBuilder.append(indexName);
        indexOptBuilder.append(" ON ");
        indexOptBuilder.append(tableName);
        indexOptBuilder.append("('");
        indexOptBuilder.append(columnName);
        indexOptBuilder.append("')");
        return indexOptBuilder.toString();
    }

    @Override
    public String dropIndexSql(String tableName,String indexName) {
        StringBuilder indexOptBuilder = new StringBuilder();
        indexOptBuilder.append("DROP INDEX ");
        indexOptBuilder.append(indexName);
        indexOptBuilder.append(" ON ");
        indexOptBuilder.append(tableName);
//        indexOptBuilder.append(";");
        return indexOptBuilder.toString();
    }


    @Override
    public String batchInsertSql(String tableName, List<String> columns, List<List<String>> dataList) {
        StringBuilder sb = new StringBuilder("INSERT INTO " + tableName + " (");

        // 添加列名
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i));
            if (i < columns.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(") VALUES ");

        // 添加多个值集
        for (int i = 0; i < dataList.size(); i++) {
            sb.append("(");
            for (int j = 0; j < dataList.get(i).size(); j++) {
                sb.append("'").append(dataList.get(i).get(j)).append("'");
                if (j < dataList.get(i).size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            if (i < dataList.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    private String getDbType(String cellType) {
        switch (cellType) {
            case "STRING":
                return "VARCHAR2(255)";
            case "TEXT":
                return "text";
            case "INTEGER":
                return "int(11)";
            case "DATE":
                return "date";
            case "TIME":
                return "datetime";
            case "DECIMAL":
                return "decimal(32,8)";
            default:
                return "VARCHAR2(255)";
        }


    }
}
