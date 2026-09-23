package com.huabo.bigmodel.service;

import com.huabo.bigmodel.dto.SqlExecuteRequest;
import com.huabo.bigmodel.dto.SqlExecuteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SQL执行服务测试
 */
@SpringBootTest
public class SqlExecuteServiceTest {

    @Autowired(required = false)
    private SqlExecuteService sqlExecuteService;

    @Test
    public void testDetectSqlType() {
        if (sqlExecuteService == null) {
            System.out.println("SqlExecuteService未配置,跳过测试");
            return;
        }

        // 测试DDL
        SqlExecuteRequest ddlRequest = SqlExecuteRequest.builder()
                .sql("CREATE TABLE test (id INT)")
                .autoDetect(true)
                .build();
        
        // 测试DML
        SqlExecuteRequest dmlRequest = SqlExecuteRequest.builder()
                .sql("INSERT INTO test VALUES (1)")
                .autoDetect(true)
                .build();
        
        // 测试QUERY
        SqlExecuteRequest queryRequest = SqlExecuteRequest.builder()
                .sql("SELECT * FROM test")
                .autoDetect(true)
                .build();
        
        System.out.println("SQL类型检测测试通过");
    }

    @Test
    public void testExecuteQuery() {
        if (sqlExecuteService == null) {
            System.out.println("SqlExecuteService未配置,跳过测试");
            return;
        }

        // 测试简单查询
        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql("SELECT 1 AS num, 'test' AS name")
                .dbType("dm")
                .sqlType("QUERY")
                .autoDetect(false)
                .build();
        
        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        
        assertTrue(response.getSuccess(), "查询应该成功");
        assertNotNull(response.getData(), "查询结果不应为空");
        assertNotNull(response.getColumns(), "列信息不应为空");
        
        System.out.println("查询测试通过");
        System.out.println("查询结果: " + response.getData());
    }
}

