package com.huabo.bigmodel.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DTO 类单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@DisplayName("DTO 类测试")
public class DtoTest {

    // ================================================================
    // AiChatRequest
    // ================================================================
    @Nested
    @DisplayName("AiChatRequest")
    class AiChatRequestTest {

        @Test
        @DisplayName("TC-DTO-001: 默认值应正确初始化")
        void testDefaults() {
            AiChatRequest req = new AiChatRequest();
            assertFalse(req.getEnableThinking());
            assertTrue(req.getEnableWebSearch());
            assertTrue(req.getStream());
            assertNull(req.getMessage());
            assertNull(req.getSessionId());
            assertNull(req.getHistory());
            assertNull(req.getSystemPrompt());
        }

        @Test
        @DisplayName("TC-DTO-002: Message 内部类应正确设置属性")
        void testMessage() {
            AiChatRequest.Message msg = new AiChatRequest.Message();
            msg.setRole("user");
            msg.setContent("你好");
            assertEquals("user", msg.getRole());
            assertEquals("你好", msg.getContent());
        }
    }

    // ================================================================
    // AiChatResponse
    // ================================================================
    @Nested
    @DisplayName("AiChatResponse")
    class AiChatResponseTest {

        @Test
        @DisplayName("TC-DTO-003: success 静态方法应返回 success=true")
        void testSuccess() {
            AiChatResponse resp = AiChatResponse.success("回答内容");
            assertTrue(resp.getSuccess());
            assertEquals("回答内容", resp.getContent());
        }

        @Test
        @DisplayName("TC-DTO-004: error 静态方法应返回 success=false")
        void testError() {
            AiChatResponse resp = AiChatResponse.error("出错了");
            assertFalse(resp.getSuccess());
            assertEquals("出错了", resp.getError());
        }

        @Test
        @DisplayName("TC-DTO-005: Builder 应正确构建所有字段")
        void testBuilder() {
            AiChatResponse resp = AiChatResponse.builder()
                    .success(true)
                    .content("内容")
                    .sessionId("s1")
                    .usedThinking(true)
                    .usedWebSearch(false)
                    .thinkingContent("思考过程")
                    .message("消息")
                    .build();

            assertTrue(resp.getSuccess());
            assertEquals("内容", resp.getContent());
            assertEquals("s1", resp.getSessionId());
            assertTrue(resp.getUsedThinking());
            assertFalse(resp.getUsedWebSearch());
            assertEquals("思考过程", resp.getThinkingContent());
        }
    }

    // ================================================================
    // SqlExecuteRequest
    // ================================================================
    @Nested
    @DisplayName("SqlExecuteRequest")
    class SqlExecuteRequestTest {

        @Test
        @DisplayName("TC-DTO-006: 默认值 dbType=dm, autoDetect=true")
        void testDefaults() {
            SqlExecuteRequest req = SqlExecuteRequest.builder().sql("SELECT 1").build();
            assertEquals("dm", req.getDbType());
            assertTrue(req.getAutoDetect());
        }

        @Test
        @DisplayName("TC-DTO-007: Builder 应正确设置所有字段")
        void testBuilder() {
            SqlExecuteRequest req = SqlExecuteRequest.builder()
                    .sql("CREATE TABLE T (ID INT)")
                    .dbType("mysql")
                    .sqlType("DDL")
                    .autoDetect(false)
                    .build();

            assertEquals("CREATE TABLE T (ID INT)", req.getSql());
            assertEquals("mysql", req.getDbType());
            assertEquals("DDL", req.getSqlType());
            assertFalse(req.getAutoDetect());
        }
    }

    // ================================================================
    // SqlExecuteResponse
    // ================================================================
    @Nested
    @DisplayName("SqlExecuteResponse")
    class SqlExecuteResponseTest {

        @Test
        @DisplayName("TC-DTO-008: success 静态方法应返回 success=true")
        void testSuccess() {
            SqlExecuteResponse resp = SqlExecuteResponse.success("执行成功");
            assertTrue(resp.getSuccess());
            assertEquals("执行成功", resp.getMessage());
        }

        @Test
        @DisplayName("TC-DTO-009: error 静态方法应返回 success=false")
        void testError() {
            SqlExecuteResponse resp = SqlExecuteResponse.error("执行失败");
            assertFalse(resp.getSuccess());
            assertEquals("执行失败", resp.getMessage());
        }

        @Test
        @DisplayName("TC-DTO-010: ColumnInfo 应正确设置属性")
        void testColumnInfo() {
            SqlExecuteResponse.ColumnInfo col = SqlExecuteResponse.ColumnInfo.builder()
                    .name("USER_ID")
                    .type("VARCHAR")
                    .label("用户ID")
                    .build();

            assertEquals("USER_ID", col.getName());
            assertEquals("VARCHAR", col.getType());
            assertEquals("用户ID", col.getLabel());
        }

        @Test
        @DisplayName("TC-DTO-011: Builder 应正确构建查询结果")
        void testBuilderWithData() {
            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> row = new HashMap<>();
            row.put("ID", 1);
            data.add(row);

            List<SqlExecuteResponse.ColumnInfo> columns = Arrays.asList(
                    SqlExecuteResponse.ColumnInfo.builder().name("ID").type("INT").build()
            );

            SqlExecuteResponse resp = SqlExecuteResponse.builder()
                    .success(true)
                    .message("查询成功")
                    .sqlType("QUERY")
                    .data(data)
                    .columns(columns)
                    .executionTime(50L)
                    .affectedRows(null)
                    .build();

            assertTrue(resp.getSuccess());
            assertEquals(1, resp.getData().size());
            assertEquals(1, resp.getColumns().size());
            assertEquals(50L, resp.getExecutionTime());
        }
    }
}

