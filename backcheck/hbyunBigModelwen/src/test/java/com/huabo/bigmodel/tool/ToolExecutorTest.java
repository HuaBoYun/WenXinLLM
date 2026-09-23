package com.huabo.bigmodel.tool;

import com.alibaba.fastjson.JSONObject;
import com.huabo.bigmodel.dto.SqlExecuteRequest;
import com.huabo.bigmodel.dto.SqlExecuteResponse;
import com.huabo.bigmodel.service.SqlExecuteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ToolExecutor 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ToolExecutor 单元测试")
public class ToolExecutorTest {

    @Mock
    private SqlExecuteService sqlExecuteService;

    private ToolExecutor toolExecutor;

    @BeforeEach
    void setUp() {
        toolExecutor = new ToolExecutor(sqlExecuteService);
    }

    // ================================================================
    // 工具路由
    // ================================================================
    @Nested
    @DisplayName("工具路由分发")
    class ToolRouting {

        @Test
        @DisplayName("TC-TOOL-001: 未知工具名应返回错误提示")
        void testUnknownTool() {
            String result = toolExecutor.executeTool("unknown_tool", new JSONObject());
            assertTrue(result.contains("未知的工具"));
        }

        @Test
        @DisplayName("TC-TOOL-002: web_search 应返回暂未实现提示")
        void testWebSearchPlaceholder() {
            JSONObject input = new JSONObject();
            input.put("query", "华博云");
            String result = toolExecutor.executeTool("web_search", input);
            assertTrue(result.contains("暂未实现"));
        }
    }

    // ================================================================
    // query_sql 工具
    // ================================================================
    @Nested
    @DisplayName("query_sql 工具")
    class QuerySqlTool {

        @Test
        @DisplayName("TC-QUERY-001: 查询成功应返回格式化结果")
        void testQuerySuccess() {
            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> row = new HashMap<>();
            row.put("ID", 1);
            row.put("NAME", "测试");
            data.add(row);

            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("QUERY").executionTime(50L).data(data).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            JSONObject input = new JSONObject();
            input.put("sql", "SELECT * FROM TBL_TEST");

            String result = toolExecutor.executeTool("query_sql", input);
            assertTrue(result.contains("SQL执行成功"));
            assertTrue(result.contains("QUERY"));
            assertTrue(result.contains("50ms"));
        }

        @Test
        @DisplayName("TC-QUERY-002: 查询失败应返回错误信息")
        void testQueryFailure() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(false).message("表不存在").build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            JSONObject input = new JSONObject();
            input.put("sql", "SELECT * FROM NOT_EXIST");

            String result = toolExecutor.executeTool("query_sql", input);
            assertTrue(result.contains("SQL执行失败"));
            assertTrue(result.contains("表不存在"));
        }

        @Test
        @DisplayName("TC-QUERY-003: 未指定 dbType 应默认使用 dm")
        void testDefaultDbType() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("QUERY").executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            JSONObject input = new JSONObject();
            input.put("sql", "SELECT 1");
            // 不设置 dbType

            toolExecutor.executeTool("query_sql", input);

            ArgumentCaptor<SqlExecuteRequest> captor = ArgumentCaptor.forClass(SqlExecuteRequest.class);
            verify(sqlExecuteService).executeSql(captor.capture());
            assertEquals("dm", captor.getValue().getDbType());
        }

        @Test
        @DisplayName("TC-QUERY-004: 指定 dbType=mysql 应传递到请求")
        void testMysqlDbType() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("QUERY").executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            JSONObject input = new JSONObject();
            input.put("sql", "SELECT 1");
            input.put("dbType", "mysql");

            toolExecutor.executeTool("query_sql", input);

            ArgumentCaptor<SqlExecuteRequest> captor = ArgumentCaptor.forClass(SqlExecuteRequest.class);
            verify(sqlExecuteService).executeSql(captor.capture());
            assertEquals("mysql", captor.getValue().getDbType());
            assertEquals("QUERY", captor.getValue().getSqlType());
            assertFalse(captor.getValue().getAutoDetect());
        }
    }

    // ================================================================
    // execute_ddl 工具
    // ================================================================
    @Nested
    @DisplayName("execute_ddl 工具")
    class ExecuteDdlTool {

        @Test
        @DisplayName("TC-DDL-001: DDL 成功应返回影响行数")
        void testDdlSuccess() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("DDL").executionTime(120L).affectedRows(1).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            JSONObject input = new JSONObject();
            input.put("sql", "CREATE TABLE TBL_NEW (ID INT)");

            String result = toolExecutor.executeTool("execute_ddl", input);
            assertTrue(result.contains("SQL执行成功"));
            assertTrue(result.contains("影响行数: 1"));
        }

        @Test
        @DisplayName("TC-DDL-002: DDL 请求应设置 sqlType=DDL, autoDetect=false")
        void testDdlRequestParams() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("DDL").executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            JSONObject input = new JSONObject();
            input.put("sql", "CREATE TABLE TBL_X (ID INT)");

            toolExecutor.executeTool("execute_ddl", input);

            ArgumentCaptor<SqlExecuteRequest> captor = ArgumentCaptor.forClass(SqlExecuteRequest.class);
            verify(sqlExecuteService).executeSql(captor.capture());
            assertEquals("DDL", captor.getValue().getSqlType());
            assertFalse(captor.getValue().getAutoDetect());
        }
    }

    // ================================================================
    // execute_dml 工具
    // ================================================================
    @Nested
    @DisplayName("execute_dml 工具")
    class ExecuteDmlTool {

        @Test
        @DisplayName("TC-DML-001: DML 成功应返回影响行数")
        void testDmlSuccess() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("DML").executionTime(30L).affectedRows(5).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            JSONObject input = new JSONObject();
            input.put("sql", "INSERT INTO TBL_TEST VALUES (1, 'test')");

            String result = toolExecutor.executeTool("execute_dml", input);
            assertTrue(result.contains("SQL执行成功"));
            assertTrue(result.contains("影响行数: 5"));
        }
    }

    // ================================================================
    // execute_batch_sql 工具
    // ================================================================
    @Nested
    @DisplayName("execute_batch_sql 批量执行")
    class ExecuteBatchSql {

        @Test
        @DisplayName("TC-BATCH-001: 格式错误的 sqlList 应返回错误")
        void testInvalidJsonFormat() {
            JSONObject input = new JSONObject();
            input.put("sqlList", "not a json array");

            String result = toolExecutor.executeTool("execute_batch_sql", input);
            assertTrue(result.contains("SQL列表格式错误"));
        }

        @Test
        @DisplayName("TC-BATCH-002: 正常批量执行应逐条调用并汇总结果")
        void testBatchExecution() {
            SqlExecuteResponse successResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(20L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(successResp);

            String sqlList = "[{\"sql\":\"CREATE TABLE A (ID INT)\",\"type\":\"DDL\",\"description\":\"建表A\"},"
                    + "{\"sql\":\"INSERT INTO A VALUES(1)\",\"type\":\"DML\",\"description\":\"插入数据\"}]";

            JSONObject input = new JSONObject();
            input.put("sqlList", sqlList);

            String result = toolExecutor.executeTool("execute_batch_sql", input);
            assertTrue(result.contains("执行完成"));
            assertTrue(result.contains("成功: 2"));
            assertTrue(result.contains("失败: 0"));
            verify(sqlExecuteService, times(2)).executeSql(any());
        }

        @Test
        @DisplayName("TC-BATCH-003: 进度回调应被正确触发")
        void testProgressCallback() {
            SqlExecuteResponse successResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(successResp);

            String sqlList = "[{\"sql\":\"SELECT 1\",\"description\":\"测试\"}]";
            JSONObject input = new JSONObject();
            input.put("sqlList", sqlList);

            List<String> progressMessages = new ArrayList<>();
            toolExecutor.executeTool("execute_batch_sql", input, progressMessages::add);

            assertFalse(progressMessages.isEmpty());
            assertTrue(progressMessages.stream().anyMatch(m -> m.contains("[1/1]")));
        }

        @Test
        @DisplayName("TC-BATCH-004: 执行失败时应尝试自动修复")
        void testAutoFixOnFailure() {
            // 第一次失败，第二次（修复后）成功
            SqlExecuteResponse failResp = SqlExecuteResponse.builder()
                    .success(false).message("SYSTIMESTAMP 不支持").build();
            SqlExecuteResponse successResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(15L).build();
            when(sqlExecuteService.executeSql(any()))
                    .thenReturn(failResp)
                    .thenReturn(successResp);

            String sqlList = "[{\"sql\":\"INSERT INTO TBL_LOG VALUES(SYSTIMESTAMP)\",\"type\":\"DML\"}]";
            JSONObject input = new JSONObject();
            input.put("sqlList", sqlList);

            String result = toolExecutor.executeTool("execute_batch_sql", input);
            assertTrue(result.contains("修复后成功") || result.contains("成功: 1"));
        }
    }

    // ================================================================
    // 异常处理
    // ================================================================
    @Nested
    @DisplayName("异常处理")
    class ExceptionHandling {

        @Test
        @DisplayName("TC-EX-001: 工具执行抛异常应返回错误信息而非崩溃")
        void testExceptionCaught() {
            when(sqlExecuteService.executeSql(any())).thenThrow(new RuntimeException("数据库连接失败"));

            JSONObject input = new JSONObject();
            input.put("sql", "SELECT 1");

            String result = toolExecutor.executeTool("query_sql", input);
            assertTrue(result.contains("工具执行失败"));
            assertTrue(result.contains("数据库连接失败"));
        }
    }
}

