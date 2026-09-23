package com.financial.sharing.dataCollection.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.dataCollection.mapper.DataSourceMapper;
import com.financial.sharing.dataCollection.service.DataSourceTestService;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * 数据源连接测试Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class DataSourceTestServiceImpl implements DataSourceTestService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Override
    public MyJsonBean testDatabaseConnectionEnhanced(TblDataSource dataSource) {
        Map<String, Object> result = new HashMap<>();
        Connection conn = null;
        
        try {
            String jdbcUrl = buildJdbcUrl(dataSource);
            result.put("jdbcUrl", jdbcUrl);
            result.put("username", dataSource.getUsername());
            result.put("connectionPoolSize", dataSource.getConnectionPoolSize());
            result.put("timeout", dataSource.getTimeout());

            long startTime = System.currentTimeMillis();
            
            // 建立连接
            conn = DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());
            
            long connectTime = System.currentTimeMillis() - startTime;
            result.put("connectTime", connectTime + "ms");
            result.put("connected", true);

            // 获取数据库元数据
            DatabaseMetaData metaData = conn.getMetaData();
            result.put("databaseProduct", metaData.getDatabaseProductName());
            result.put("databaseVersion", metaData.getDatabaseProductVersion());
            result.put("driverName", metaData.getDriverName());
            result.put("driverVersion", metaData.getDriverVersion());
            result.put("jdbcVersion", metaData.getJDBCMajorVersion() + "." + metaData.getJDBCMinorVersion());

            // 测试查询
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            if (rs.next()) {
                result.put("queryTest", "成功");
            }
            rs.close();
            stmt.close();

            // 获取表数量
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            int tableCount = 0;
            while (tables.next()) {
                tableCount++;
            }
            tables.close();
            result.put("tableCount", tableCount);

            result.put("status", "SUCCESS");
            result.put("message", "数据库连接测试成功");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("数据库连接测试失败", e);
            result.put("connected", false);
            result.put("status", "FAILED");
            result.put("message", "连接失败：" + e.getMessage());
            result.put("errorType", e.getClass().getSimpleName());
            return MyJsonBean.errorData(result);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接失败", e);
                }
            }
        }
    }

    @Override
    public MyJsonBean testApiConnectionEnhanced(TblDataSource dataSource) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String apiUrl = dataSource.getApiUrl();
            result.put("apiUrl", apiUrl);
            result.put("apiMethod", dataSource.getApiMethod());
            result.put("authType", dataSource.getAuthType());

            long startTime = System.currentTimeMillis();

            // 建立HTTP连接
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(dataSource.getApiMethod() != null ? dataSource.getApiMethod() : "GET");
            connection.setConnectTimeout(dataSource.getTimeout() != null ? dataSource.getTimeout() * 1000 : 30000);
            connection.setReadTimeout(dataSource.getTimeout() != null ? dataSource.getTimeout() * 1000 : 30000);

            // 设置请求头
            if (StringUtils.isNotBlank(dataSource.getApiHeaders())) {
                try {
                    Map<String, String> headers = JSON.parseObject(dataSource.getApiHeaders(), Map.class);
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                } catch (Exception e) {
                    log.warn("解析API请求头失败", e);
                }
            }

            // 设置认证
            if ("TOKEN".equals(dataSource.getAuthType()) && StringUtils.isNotBlank(dataSource.getAuthToken())) {
                connection.setRequestProperty("Authorization", "Bearer " + dataSource.getAuthToken());
            } else if ("BASIC".equals(dataSource.getAuthType())) {
                String auth = dataSource.getUsername() + ":" + dataSource.getPassword();
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            }

            // 发送请求
            int responseCode = connection.getResponseCode();
            long responseTime = System.currentTimeMillis() - startTime;

            result.put("responseCode", responseCode);
            result.put("responseTime", responseTime + "ms");
            result.put("responseMessage", connection.getResponseMessage());
            result.put("contentType", connection.getContentType());
            result.put("contentLength", connection.getContentLength());

            if (responseCode >= 200 && responseCode < 300) {
                result.put("connected", true);
                result.put("status", "SUCCESS");
                result.put("message", "API连接测试成功");
                return MyJsonBean.successData(result);
            } else {
                result.put("connected", false);
                result.put("status", "FAILED");
                result.put("message", "API返回错误状态码：" + responseCode);
                return MyJsonBean.errorData(result);
            }
        } catch (Exception e) {
            log.error("API连接测试失败", e);
            result.put("connected", false);
            result.put("status", "FAILED");
            result.put("message", "连接失败：" + e.getMessage());
            result.put("errorType", e.getClass().getSimpleName());
            return MyJsonBean.errorData(result);
        }
    }

    @Override
    public MyJsonBean testFileConnectionEnhanced(TblDataSource dataSource) {
        Map<String, Object> result = new HashMap<>();

        try {
            String filePath = dataSource.getFilePath();
            result.put("filePath", filePath);
            result.put("fileType", dataSource.getFileType());

            // 检查文件是否存在
            File file = new File(filePath);
            result.put("exists", file.exists());

            if (file.exists()) {
                result.put("isFile", file.isFile());
                result.put("isDirectory", file.isDirectory());
                result.put("canRead", file.canRead());
                result.put("canWrite", file.canWrite());
                result.put("size", file.length() + " bytes");
                result.put("lastModified", new java.util.Date(file.lastModified()));

                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    result.put("fileCount", files != null ? files.length : 0);
                }

                result.put("connected", true);
                result.put("status", "SUCCESS");
                result.put("message", "文件路径访问成功");
                return MyJsonBean.successData(result);
            } else {
                result.put("connected", false);
                result.put("status", "FAILED");
                result.put("message", "文件或目录不存在");
                return MyJsonBean.errorData(result);
            }
        } catch (Exception e) {
            log.error("文件连接测试失败", e);
            result.put("connected", false);
            result.put("status", "FAILED");
            result.put("message", "访问失败：" + e.getMessage());
            result.put("errorType", e.getClass().getSimpleName());
            return MyJsonBean.errorData(result);
        }
    }

    @Override
    public MyJsonBean testFinancialSharingConnectionEnhanced(TblDataSource dataSource) {
        Map<String, Object> result = new HashMap<>();

        try {
            String apiUrl = dataSource.getApiUrl();
            result.put("apiUrl", apiUrl);
            result.put("authToken", dataSource.getAuthToken() != null ? "已配置" : "未配置");

            // 测试财务共享API连接
            if (StringUtils.isBlank(apiUrl)) {
                result.put("connected", false);
                result.put("status", "FAILED");
                result.put("message", "API地址未配置");
                return MyJsonBean.errorData(result);
            }

            long startTime = System.currentTimeMillis();

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            // 设置认证令牌
            if (StringUtils.isNotBlank(dataSource.getAuthToken())) {
                connection.setRequestProperty("Authorization", "Bearer " + dataSource.getAuthToken());
            }

            int responseCode = connection.getResponseCode();
            long responseTime = System.currentTimeMillis() - startTime;

            result.put("responseCode", responseCode);
            result.put("responseTime", responseTime + "ms");
            result.put("responseMessage", connection.getResponseMessage());

            if (responseCode >= 200 && responseCode < 300) {
                result.put("connected", true);
                result.put("status", "SUCCESS");
                result.put("message", "财务共享连接测试成功");
                return MyJsonBean.successData(result);
            } else {
                result.put("connected", false);
                result.put("status", "FAILED");
                result.put("message", "财务共享返回错误状态码：" + responseCode);
                return MyJsonBean.errorData(result);
            }
        } catch (Exception e) {
            log.error("财务共享连接测试失败", e);
            result.put("connected", false);
            result.put("status", "FAILED");
            result.put("message", "连接失败：" + e.getMessage());
            result.put("errorType", e.getClass().getSimpleName());
            return MyJsonBean.errorData(result);
        }
    }

    @Override
    public MyJsonBean batchTestConnection(String[] sourceIds, String orgId) {
        List<Map<String, Object>> results = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;

        for (String sourceId : sourceIds) {
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("sourceId", sourceId);

            try {
                // 查询数据源配置
                LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblDataSource::getSourceId, sourceId);
                wrapper.eq(TblDataSource::getOrgId, orgId);

                TblDataSource dataSource = dataSourceMapper.selectOne(wrapper);
                if (dataSource == null) {
                    testResult.put("status", "FAILED");
                    testResult.put("message", "数据源不存在");
                    failedCount++;
                    results.add(testResult);
                    continue;
                }

                testResult.put("sourceCode", dataSource.getSourceCode());
                testResult.put("sourceName", dataSource.getSourceName());
                testResult.put("sourceType", dataSource.getSourceType());

                // 根据类型测试连接
                MyJsonBean result = null;
                if ("DATABASE".equals(dataSource.getSourceType())) {
                    result = testDatabaseConnectionEnhanced(dataSource);
                } else if ("API".equals(dataSource.getSourceType())) {
                    result = testApiConnectionEnhanced(dataSource);
                } else if ("FILE".equals(dataSource.getSourceType())) {
                    result = testFileConnectionEnhanced(dataSource);
                } else if ("FINANCIAL_SHARING".equals(dataSource.getSourceType())) {
                    result = testFinancialSharingConnectionEnhanced(dataSource);
                }

                if (result != null && result.getCode() == 200) {
                    testResult.put("status", "SUCCESS");
                    testResult.put("message", "连接成功");
                    successCount++;
                } else {
                    testResult.put("status", "FAILED");
                    testResult.put("message", result != null ? result.getMsg() : "连接失败");
                    failedCount++;
                }
            } catch (Exception e) {
                log.error("批量测试连接失败", e);
                testResult.put("status", "FAILED");
                testResult.put("message", "测试失败：" + e.getMessage());
                failedCount++;
            }

            results.add(testResult);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("total", sourceIds.length);
        summary.put("successCount", successCount);
        summary.put("failedCount", failedCount);
        summary.put("results", results);

        return MyJsonBean.successData(summary);
    }

    @Override
    public MyJsonBean getDatabaseTables(String sourceId, String orgId) {
        Connection conn = null;

        try {
            // 查询数据源配置
            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getSourceId, sourceId);
            wrapper.eq(TblDataSource::getOrgId, orgId);

            TblDataSource dataSource = dataSourceMapper.selectOne(wrapper);
            if (dataSource == null) {
                return MyJsonBean.errorData("数据源配置不存在");
            }

            if (!"DATABASE".equals(dataSource.getSourceType())) {
                return MyJsonBean.errorData("只支持数据库类型的数据源");
            }

            String jdbcUrl = buildJdbcUrl(dataSource);
            conn = DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            List<Map<String, Object>> tableList = new ArrayList<>();
            while (tables.next()) {
                Map<String, Object> table = new HashMap<>();
                table.put("tableName", tables.getString("TABLE_NAME"));
                table.put("tableType", tables.getString("TABLE_TYPE"));
                table.put("remarks", tables.getString("REMARKS"));
                tableList.add(table);
            }
            tables.close();

            Map<String, Object> result = new HashMap<>();
            result.put("tableCount", tableList.size());
            result.put("tables", tableList);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取数据库表列表失败", e);
            return MyJsonBean.errorData("获取数据库表列表失败：" + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接失败", e);
                }
            }
        }
    }

    @Override
    public MyJsonBean getTableStructure(String sourceId, String tableName, String orgId) {
        Connection conn = null;

        try {
            // 查询数据源配置
            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getSourceId, sourceId);
            wrapper.eq(TblDataSource::getOrgId, orgId);

            TblDataSource dataSource = dataSourceMapper.selectOne(wrapper);
            if (dataSource == null) {
                return MyJsonBean.errorData("数据源配置不存在");
            }

            if (!"DATABASE".equals(dataSource.getSourceType())) {
                return MyJsonBean.errorData("只支持数据库类型的数据源");
            }

            String jdbcUrl = buildJdbcUrl(dataSource);
            conn = DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, "%");

            List<Map<String, Object>> columnList = new ArrayList<>();
            while (columns.next()) {
                Map<String, Object> column = new HashMap<>();
                column.put("columnName", columns.getString("COLUMN_NAME"));
                column.put("dataType", columns.getString("TYPE_NAME"));
                column.put("columnSize", columns.getInt("COLUMN_SIZE"));
                column.put("nullable", columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                column.put("remarks", columns.getString("REMARKS"));
                column.put("defaultValue", columns.getString("COLUMN_DEF"));
                columnList.add(column);
            }
            columns.close();

            // 获取主键信息
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            List<String> pkList = new ArrayList<>();
            while (primaryKeys.next()) {
                pkList.add(primaryKeys.getString("COLUMN_NAME"));
            }
            primaryKeys.close();

            Map<String, Object> result = new HashMap<>();
            result.put("tableName", tableName);
            result.put("columnCount", columnList.size());
            result.put("columns", columnList);
            result.put("primaryKeys", pkList);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取表结构失败", e);
            return MyJsonBean.errorData("获取表结构失败：" + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接失败", e);
                }
            }
        }
    }

    @Override
    public MyJsonBean executeTestQuery(String sourceId, String sql, String orgId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 查询数据源配置
            LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblDataSource::getSourceId, sourceId);
            wrapper.eq(TblDataSource::getOrgId, orgId);

            TblDataSource dataSource = dataSourceMapper.selectOne(wrapper);
            if (dataSource == null) {
                return MyJsonBean.errorData("数据源配置不存在");
            }

            if (!"DATABASE".equals(dataSource.getSourceType())) {
                return MyJsonBean.errorData("只支持数据库类型的数据源");
            }

            // 安全检查：只允许SELECT查询
            String upperSql = sql.trim().toUpperCase();
            if (!upperSql.startsWith("SELECT")) {
                return MyJsonBean.errorData("只允许执行SELECT查询");
            }

            String jdbcUrl = buildJdbcUrl(dataSource);
            conn = DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());

            long startTime = System.currentTimeMillis();
            stmt = conn.createStatement();
            stmt.setMaxRows(100); // 限制最多返回100行
            rs = stmt.executeQuery(sql);
            long executeTime = System.currentTimeMillis() - startTime;

            // 获取列信息
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(metaData.getColumnName(i));
            }

            // 获取数据
            List<Map<String, Object>> rows = new ArrayList<>();
            int rowCount = 0;
            while (rs.next() && rowCount < 100) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                rows.add(row);
                rowCount++;
            }

            Map<String, Object> result = new HashMap<>();
            result.put("sql", sql);
            result.put("executeTime", executeTime + "ms");
            result.put("columnCount", columnCount);
            result.put("columns", columns);
            result.put("rowCount", rowCount);
            result.put("rows", rows);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("执行测试查询失败", e);
            return MyJsonBean.errorData("执行测试查询失败：" + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    log.error("关闭ResultSet失败", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    log.error("关闭Statement失败", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接失败", e);
                }
            }
        }
    }

    /**
     * 构建JDBC连接URL
     */
    private String buildJdbcUrl(TblDataSource dataSource) {
        String connectionType = dataSource.getConnectionType();
        String host = dataSource.getHost();
        Integer port = dataSource.getPort();
        String databaseName = dataSource.getDatabaseName();

        if ("JDBC".equals(connectionType)) {
            // 根据不同数据库类型构建URL
            if (databaseName != null && databaseName.toLowerCase().contains("dm")) {
                // 达梦数据库
                return String.format("jdbc:dm://%s:%d/%s", host, port, databaseName);
            } else if (databaseName != null && databaseName.toLowerCase().contains("mysql")) {
                // MySQL数据库
                return String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8", host, port, databaseName);
            } else if (databaseName != null && databaseName.toLowerCase().contains("oracle")) {
                // Oracle数据库
                return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, databaseName);
            } else {
                // 默认
                return String.format("jdbc://%s:%d/%s", host, port, databaseName);
            }
        }
        return "";
    }
}
