package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.dto.SqlExecuteRequest;
import com.huabo.bigmodel.dto.SqlExecuteResponse;
import com.huabo.bigmodel.service.SqlExecuteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * SqlExecuteController 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SqlExecuteController 单元测试")
public class SqlExecuteControllerTest {

    @Mock
    private SqlExecuteService sqlExecuteService;

    @InjectMocks
    private SqlExecuteController controller;

    // ================================================================
    // 通用 SQL 执行 (POST /v1/sql/execute)
    // ================================================================
    @Nested
    @DisplayName("executeSql - 通用执行")
    class ExecuteSql {

        @Test
        @DisplayName("TC-EXEC-001: 查询成功应返回 200 和数据")
        void testExecuteQuerySuccess() {
            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> row = new HashMap<>();
            row.put("ID", 1);
            data.add(row);

            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("QUERY").data(data).executionTime(30L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("SELECT * FROM TBL_TEST").dbType("dm").autoDetect(true).build();

            ResponseEntity<SqlExecuteResponse> resp = controller.executeSql(req);

            assertEquals(200, resp.getStatusCodeValue());
            assertTrue(resp.getBody().getSuccess());
            assertEquals("QUERY", resp.getBody().getSqlType());
            assertEquals(1, resp.getBody().getData().size());
        }

        @Test
        @DisplayName("TC-EXEC-002: 执行失败应返回错误信息")
        void testExecuteFailure() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(false).message("语法错误").build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("SELEC * FROM TBL_TEST").dbType("dm").autoDetect(true).build();

            ResponseEntity<SqlExecuteResponse> resp = controller.executeSql(req);

            assertFalse(resp.getBody().getSuccess());
            assertTrue(resp.getBody().getMessage().contains("语法错误"));
        }
    }

    // ================================================================
    // DDL 执行 (POST /v1/sql/ddl?sql=...&dbType=dm)
    // ================================================================
    @Nested
    @DisplayName("executeDdl - DDL 执行")
    class ExecuteDdl {

        @Test
        @DisplayName("TC-DDL-001: DDL 成功应返回 200")
        void testDdlSuccess() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("DDL").affectedRows(1).executionTime(100L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            ResponseEntity<SqlExecuteResponse> resp = controller.executeDdl(
                    "CREATE TABLE TBL_NEW (ID INT)", "dm");

            assertTrue(resp.getBody().getSuccess());
            assertEquals("DDL", resp.getBody().getSqlType());
        }

        @Test
        @DisplayName("TC-DDL-002: DDL 请求应设置 sqlType=DDL, autoDetect=false")
        void testDdlRequestParams() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("DDL").executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            controller.executeDdl("CREATE TABLE TBL_X (ID INT)", "dm");

            ArgumentCaptor<SqlExecuteRequest> captor = ArgumentCaptor.forClass(SqlExecuteRequest.class);
            verify(sqlExecuteService).executeSql(captor.capture());
            assertEquals("DDL", captor.getValue().getSqlType());
            assertFalse(captor.getValue().getAutoDetect());
            assertEquals("dm", captor.getValue().getDbType());
        }
    }

    // ================================================================
    // DML 执行 (POST /v1/sql/dml?sql=...&dbType=dm)
    // ================================================================
    @Nested
    @DisplayName("executeDml - DML 执行")
    class ExecuteDml {

        @Test
        @DisplayName("TC-DML-001: DML 成功应返回影响行数")
        void testDmlSuccess() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("DML").affectedRows(3).executionTime(50L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            ResponseEntity<SqlExecuteResponse> resp = controller.executeDml(
                    "INSERT INTO TBL_TEST VALUES (1, 'test')", "dm");

            assertTrue(resp.getBody().getSuccess());
            assertEquals(3, resp.getBody().getAffectedRows());
        }

        @Test
        @DisplayName("TC-DML-002: DML 请求应设置 sqlType=DML")
        void testDmlRequestParams() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            controller.executeDml("UPDATE TBL_TEST SET NAME='x'", "mysql");

            ArgumentCaptor<SqlExecuteRequest> captor = ArgumentCaptor.forClass(SqlExecuteRequest.class);
            verify(sqlExecuteService).executeSql(captor.capture());
            assertEquals("DML", captor.getValue().getSqlType());
            assertEquals("mysql", captor.getValue().getDbType());
        }
    }

    // ================================================================
    // 查询执行 (POST /v1/sql/query?sql=...&dbType=dm)
    // ================================================================
    @Nested
    @DisplayName("executeQuery - 查询执行")
    class ExecuteQuery {

        @Test
        @DisplayName("TC-QUERY-001: 查询应返回列信息和数据")
        void testQueryWithColumns() {
            List<SqlExecuteResponse.ColumnInfo> columns = Arrays.asList(
                    SqlExecuteResponse.ColumnInfo.builder().name("ID").type("INT").build(),
                    SqlExecuteResponse.ColumnInfo.builder().name("NAME").type("VARCHAR").build()
            );
            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> row = new HashMap<>();
            row.put("ID", 1);
            row.put("NAME", "测试");
            data.add(row);

            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).sqlType("QUERY").columns(columns).data(data).executionTime(20L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            ResponseEntity<SqlExecuteResponse> resp = controller.executeQuery(
                    "SELECT ID, NAME FROM TBL_TEST", "dm");

            assertTrue(resp.getBody().getSuccess());
            assertEquals(2, resp.getBody().getColumns().size());
            assertEquals(1, resp.getBody().getData().size());
        }

        @Test
        @DisplayName("TC-QUERY-002: 查询请求应设置 sqlType=QUERY")
        void testQueryRequestParams() {
            SqlExecuteResponse mockResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(mockResp);

            controller.executeQuery("SELECT 1 FROM DUAL", "dm");

            ArgumentCaptor<SqlExecuteRequest> captor = ArgumentCaptor.forClass(SqlExecuteRequest.class);
            verify(sqlExecuteService).executeSql(captor.capture());
            assertEquals("QUERY", captor.getValue().getSqlType());
        }
    }

    // ================================================================
    // 批量执行 (POST /v1/sql/batch?sqls=...&dbType=dm)
    // ================================================================
    @Nested
    @DisplayName("executeBatch - 批量执行")
    class ExecuteBatch {

        @Test
        @DisplayName("TC-BATCH-001: 分号分隔的多条 SQL 应逐条执行")
        void testBatchExecution() {
            SqlExecuteResponse successResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(successResp);

            ResponseEntity<SqlExecuteResponse> resp = controller.executeBatch(
                    "CREATE TABLE A (ID INT);INSERT INTO A VALUES(1)", "dm");

            assertTrue(resp.getBody().getSuccess());
            assertTrue(resp.getBody().getMessage().contains("成功2条"));
            assertEquals(2, resp.getBody().getAffectedRows());
            verify(sqlExecuteService, times(2)).executeSql(any());
        }

        @Test
        @DisplayName("TC-BATCH-002: 部分失败应返回 success=false")
        void testBatchPartialFailure() {
            SqlExecuteResponse successResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(10L).build();
            SqlExecuteResponse failResp = SqlExecuteResponse.builder()
                    .success(false).message("表已存在").build();
            when(sqlExecuteService.executeSql(any()))
                    .thenReturn(successResp)
                    .thenReturn(failResp);

            ResponseEntity<SqlExecuteResponse> resp = controller.executeBatch(
                    "CREATE TABLE A (ID INT);CREATE TABLE A (ID INT)", "dm");

            assertFalse(resp.getBody().getSuccess());
            assertTrue(resp.getBody().getMessage().contains("失败1条"));
        }

        @Test
        @DisplayName("TC-BATCH-003: 空 SQL 段应被跳过")
        void testBatchSkipsEmpty() {
            SqlExecuteResponse successResp = SqlExecuteResponse.builder()
                    .success(true).executionTime(10L).build();
            when(sqlExecuteService.executeSql(any())).thenReturn(successResp);

            ResponseEntity<SqlExecuteResponse> resp = controller.executeBatch(
                    "SELECT 1;  ;  ;SELECT 2", "dm");

            // 只有 2 条有效 SQL
            verify(sqlExecuteService, times(2)).executeSql(any());
        }
    }
}

