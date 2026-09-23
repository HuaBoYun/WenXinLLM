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
public class OracleJdbcExecuteTemplateImpl extends JdbcExecuteTemplate {
    private static final Logger log = LoggerFactory.getLogger(OracleJdbcExecuteTemplateImpl.class);



    @Override
    public String getDriver() {
        return "oracle.jdbc.driver.OracleDriver";
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

        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ");
        sqlBuilder.append("\"");
        sqlBuilder.append(tableName);
        sqlBuilder.append("\" ");
        sqlBuilder.append("(");

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
    public String updateCommentSql(String tableName, JdbcColumnVO jdbcColumnVO) {
        StringBuilder commentBuilder = new StringBuilder();
        commentBuilder.append("COMMENT ON COLUMN ");
        commentBuilder.append(tableName);
        commentBuilder.append(".");
        commentBuilder.append(jdbcColumnVO.getName());
        commentBuilder.append(" IS '");
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
    public String dropIndexSql(String tableName, String indexName) {
        StringBuilder indexOptBuilder = new StringBuilder();
        indexOptBuilder.append("DROP INDEX ");
        indexOptBuilder.append(indexName);
//        indexOptBuilder.append(";");
        return indexOptBuilder.toString();
    }

    private String getDbType(String cellType) {
        switch (cellType) {
            case "STRING":
                return "VARCHAR2(255)";
            case "TEXT":
                return "CLOB";
            case "INTEGER":
                return "NUMBER(*,0)";
            case "DATE":
                return "VARCHAR2(255)";
            case "TIME":
                return "TIMESTAMP (6)";
            case "DECIMAL":
                return "NUMBER(32,8)";
            default:
                return "VARCHAR2(255)";
        }
    }

    @Override
    public  String batchInsertSql(String tableName, List<String> columns, List<List<String>> dataList) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT ALL ");
        for (List<String> value : dataList) {
            sb.append("INTO ").append(tableName).append(" (").append(String.join(", ", columns)).append(") VALUES (");
            for (int i = 0; i < columns.size(); i++) {
                sb.append(value.get(i));
                if (i < columns.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(") ");
        }
        sb.append("SELECT * FROM dual");

        return sb.toString();
    }

}
