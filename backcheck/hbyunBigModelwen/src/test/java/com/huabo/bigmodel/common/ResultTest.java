package com.huabo.bigmodel.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result 统一返回结果测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@DisplayName("Result 统一返回结果测试")
public class ResultTest {

    @Test
    @DisplayName("TC-RESULT-001: success() 应返回 code=200")
    void testSuccessNoData() {
        Result<Void> result = Result.success();
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMsg());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("TC-RESULT-002: success(data) 应返回 code=200 和数据")
    void testSuccessWithData() {
        Result<String> result = Result.success("测试数据");
        assertEquals(200, result.getCode());
        assertEquals("测试数据", result.getData());
    }

    @Test
    @DisplayName("TC-RESULT-003: success(msg, data) 应返回自定义消息")
    void testSuccessWithMsgAndData() {
        Result<Integer> result = Result.success("查询成功", 42);
        assertEquals(200, result.getCode());
        assertEquals("查询成功", result.getMsg());
        assertEquals(42, result.getData());
    }

    @Test
    @DisplayName("TC-RESULT-004: error(msg) 应返回 code=500")
    void testError() {
        Result<Void> result = Result.error("操作失败");
        assertEquals(500, result.getCode());
        assertEquals("操作失败", result.getMsg());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("TC-RESULT-005: error(code, msg) 应返回自定义状态码")
    void testErrorWithCode() {
        Result<Void> result = Result.error(401, "未授权");
        assertEquals(401, result.getCode());
        assertEquals("未授权", result.getMsg());
    }
}

