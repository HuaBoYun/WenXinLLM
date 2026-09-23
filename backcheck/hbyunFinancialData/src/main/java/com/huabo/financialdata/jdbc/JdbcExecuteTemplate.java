package com.huabo.financialdata.jdbc;

import com.huabo.financialdata.config.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author weizhihui
 * @Date 2024/4/25 9:34
 * @Description 功能简要描述
 */
@Slf4j
public abstract class JdbcExecuteTemplate {

    static final String INDEX_OPT_CREATE = "CREATE";
    static final String INDEX_OPT_DROP = "DROP";

    public void executeUpdateSql(String url, String user, String password, String driver, String sql) {
        // 连接对象
        Connection conn = null;
        // 用于执行SQL语句的Statement对象
        Statement stmt = null;

        try {
            // 注册JDBC驱动
            Class.forName(driver);
            // 打开连接
            conn = DriverManager.getConnection(url, user, password);
            // 创建Statement对象
            stmt = conn.createStatement();

            // 执行SQL语句
            stmt.executeUpdate(sql);

            log.error("Table created successfully");
        } catch (ClassNotFoundException e) {
            log.error("JDBC driver not found");
            throw new BizException(e.getMessage());
        } catch (SQLException e) {
            log.error("SQLException:{} " ,e.getMessage());
            throw new BizException(e.getMessage());
        } catch (Exception e) {
            log.error("Exception:{} " ,e.getMessage());
            throw new BizException(e.getMessage());
        }finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new BizException(ex.getMessage());
            }
        }
    }


    public abstract String getDriver();

    public abstract String createTableSql(String tableName, List<JdbcColumnVO> columns) ;

    public abstract String updateCommentSql(String tableName, JdbcColumnVO jdbcColumnVO);

    public abstract String createIndexSql(String tableName,String indexName,String columnName);

    public abstract String dropIndexSql(String tableName, String indexName);

    public abstract String batchInsertSql(String tableName, List<String> columns, List<List<String>> dataList);
}
