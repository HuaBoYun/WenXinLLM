package com.global.treasurer.controller;

import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接测试Controller
 * 用于验证远程达梦数据库连接状态
 */
@RestController
@RequestMapping("/test")
@Api(tags = "数据库连接测试")
public class DatabaseConnectionTestController {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试数据库连接
     * @return 连接状态信息
     */
    @GetMapping("/dbConnection")
    @ApiOperation("测试数据库连接状态")
    public String testDatabaseConnection() {
        Map<String, Object> result = new HashMap<>();

        Connection connection = null;
        long startTime = System.currentTimeMillis();

        try {
            // 1. 测试基本连接(设置超时)
            DataSource dataSource = jdbcTemplate.getDataSource();
            if (dataSource instanceof com.alibaba.druid.pool.DruidDataSource) {
                com.alibaba.druid.pool.DruidDataSource druidDataSource =
                    (com.alibaba.druid.pool.DruidDataSource) dataSource;
                druidDataSource.setMaxWait(5000); // 设置5秒超时
            }

            connection = jdbcTemplate.getDataSource().getConnection();

            long connectTime = System.currentTimeMillis() - startTime;
            result.put("connectTime", connectTime + "ms");

            // 2. 获取数据库元数据
            DatabaseMetaData metaData = connection.getMetaData();

            result.put("connected", true);
            result.put("databaseProductName", metaData.getDatabaseProductName());
            result.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            result.put("driverName", metaData.getDriverName());
            result.put("driverVersion", metaData.getDriverVersion());
            result.put("url", metaData.getURL());
            result.put("userName", metaData.getUserName());

            // 3. 执行简单查询验证数据库可用性
            Statement stmt = connection.createStatement();
            stmt.setQueryTimeout(5); // 设置5秒查询超时
            java.sql.ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL");
            if (rs.next()) {
                result.put("queryTest", true);
                result.put("queryResult", rs.getInt(1));
            }
            stmt.close();

            // 4. 查询当前时间
            stmt = connection.createStatement();
            stmt.setQueryTimeout(5);
            rs = stmt.executeQuery("SELECT SYSDATE FROM DUAL");
            if (rs.next()) {
                result.put("currentTime", rs.getString(1));
            }
            stmt.close();

            connection.close();

            return new JsonBean(1, "数据库连接成功", result).toJson();

        } catch (java.sql.SQLTimeoutException e) {
            long failTime = System.currentTimeMillis() - startTime;
            result.put("connected", false);
            result.put("connectTime", failTime + "ms");
            result.put("error", "连接超时(>5秒)");
            result.put("errorType", "SQLTimeoutException");
            result.put("suggestion", "数据库服务器响应超时,请检查:1.数据库服务状态 2.网络连接 3.防火墙设置");

            return new JsonBean(0, "数据库连接超时", result).toJson();

        } catch (Exception e) {
            long failTime = System.currentTimeMillis() - startTime;
            result.put("connected", false);
            result.put("connectTime", failTime + "ms");
            result.put("error", e.getMessage());
            result.put("errorType", e.getClass().getSimpleName());

            // 提供诊断建议
            String suggestion = getDiagnosticSuggestion(e);
            result.put("suggestion", suggestion);

            return new JsonBean(0, "数据库连接失败", result).toJson();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    // 忽略关闭错误
                }
            }
        }
    }

    /**
     * 根据错误类型提供诊断建议
     */
    private String getDiagnosticSuggestion(Exception e) {
        String errorMsg = e.getMessage();
        if (errorMsg == null) {
            errorMsg = "";
        }

        if (errorMsg.contains("网络通信异常") || e.getCause() instanceof java.io.EOFException) {
            return "EOFException表示服务器主动关闭连接。可能原因:1.数据库用户无远程连接权限 2.IP白名单限制 3.数据库服务异常 4.最大连接数限制。建议联系数据库管理员检查用户权限和数据库配置。";
        } else if (errorMsg.contains("连接被拒绝")) {
            return "连接被拒绝。请检查:1.数据库服务是否启动 2.端口5236是否开放 3.防火墙设置";
        } else if (errorMsg.contains("密码")) {
            return "密码错误。请检查数据库用户名和密码配置";
        } else if (errorMsg.contains("用户")) {
            return "用户不存在或被锁定。请联系数据库管理员检查用户状态";
        } else {
            return "数据库连接失败。错误信息:" + errorMsg;
        }
    }

    /**
     * 测试数据库连接池状态
     * @return 连接池状态信息
     */
    @GetMapping("/dbPoolStatus")
    @ApiOperation("测试数据库连接池状态")
    public String testDatabasePoolStatus() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取数据源信息
            if (jdbcTemplate.getDataSource() instanceof com.alibaba.druid.pool.DruidDataSource) {
                com.alibaba.druid.pool.DruidDataSource druidDataSource =
                    (com.alibaba.druid.pool.DruidDataSource) jdbcTemplate.getDataSource();

                result.put("poolClass", "DruidDataSource");
                result.put("initialSize", druidDataSource.getInitialSize());
                result.put("minIdle", druidDataSource.getMinIdle());
                result.put("maxActive", druidDataSource.getMaxActive());
                result.put("maxWait", druidDataSource.getMaxWait());
                result.put("activeCount", druidDataSource.getActiveCount());
                result.put("poolingCount", druidDataSource.getPoolingCount());
                result.put("connectCount", druidDataSource.getConnectCount());
                result.put("closeCount", druidDataSource.getCloseCount());

                return new JsonBean(1, "连接池状态获取成功", result).toJson();
            } else {
                result.put("poolClass", jdbcTemplate.getDataSource().getClass().getSimpleName());
                return new JsonBean(1, "连接池状态获取成功(非Druid)", result).toJson();
            }

        } catch (Exception e) {
            result.put("error", e.getMessage());
            return new JsonBean(0, "连接池状态获取失败: " + e.getMessage(), result).toJson();
        }
    }
}
