package com.huabo.bigmodel.config;

import com.huabo.bigmodel.dto.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GlobalExceptionHandler 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler 异常处理测试")
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    // ================================================================
    // 参数校验异常
    // ================================================================
    @Nested
    @DisplayName("参数校验异常处理")
    class ValidationExceptions {

        @Test
        @DisplayName("TC-EX-001: BindException 应返回 400 和字段错误信息")
        void testBindException() {
            BindException ex = new BindException(new Object(), "target");
            ex.addError(new FieldError("target", "sql", "SQL不能为空"));

            ResponseEntity<ErrorResponse> resp = handler.handleBindException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
            assertNotNull(resp.getBody());
            assertNotNull(resp.getBody().getError());
            assertTrue(resp.getBody().getError().getMessage().contains("SQL不能为空"));
            assertEquals("invalid_request_error", resp.getBody().getError().getType());
        }

        @Test
        @DisplayName("TC-EX-002: ConstraintViolationException 应返回 400")
        void testConstraintViolation() {
            Set<ConstraintViolation<?>> violations = new HashSet<>();
            ConstraintViolation<?> violation = mock(ConstraintViolation.class);
            when(violation.getMessage()).thenReturn("不能为空");
            violations.add(violation);

            ConstraintViolationException ex = new ConstraintViolationException(violations);

            ResponseEntity<ErrorResponse> resp = handler.handleConstraintViolationException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
            assertNotNull(resp.getBody());
            assertNotNull(resp.getBody().getError());
            assertTrue(resp.getBody().getError().getMessage().contains("不能为空"));
        }

        @Test
        @DisplayName("TC-EX-003: IllegalArgumentException 应返回 400")
        void testIllegalArgument() {
            IllegalArgumentException ex = new IllegalArgumentException("参数不合法");

            ResponseEntity<ErrorResponse> resp = handler.handleIllegalArgumentException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
            assertTrue(resp.getBody().getError().getMessage().contains("参数不合法"));
        }
    }

    // ================================================================
    // 运行时异常
    // ================================================================
    @Nested
    @DisplayName("运行时异常处理")
    class RuntimeExceptions {

        @Test
        @DisplayName("TC-EX-004: RuntimeException 应返回 500")
        void testRuntimeException() {
            RuntimeException ex = new RuntimeException("数据库连接失败");

            ResponseEntity<ErrorResponse> resp = handler.handleRuntimeException(ex);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
            assertNotNull(resp.getBody());
            assertTrue(resp.getBody().getError().getMessage().contains("数据库连接失败"));
            assertEquals("server_error", resp.getBody().getError().getType());
        }

        @Test
        @DisplayName("TC-EX-005: 通用 Exception 应返回 500 和固定消息")
        void testGenericException() {
            Exception ex = new Exception("未知错误");

            ResponseEntity<ErrorResponse> resp = handler.handleException(ex);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
            assertNotNull(resp.getBody());
            assertEquals("Internal server error", resp.getBody().getError().getMessage());
        }
    }

    // ================================================================
    // ErrorResponse DTO
    // ================================================================
    @Nested
    @DisplayName("ErrorResponse DTO")
    class ErrorResponseTest {

        @Test
        @DisplayName("TC-EX-006: invalidRequest 应设置正确的 type 和 code")
        void testInvalidRequest() {
            ErrorResponse resp = ErrorResponse.invalidRequest("参数错误");
            assertEquals("参数错误", resp.getError().getMessage());
            assertEquals("invalid_request_error", resp.getError().getType());
            assertEquals("invalid_request", resp.getError().getCode());
        }

        @Test
        @DisplayName("TC-EX-007: serverError 应设置正确的 type 和 code")
        void testServerError() {
            ErrorResponse resp = ErrorResponse.serverError("服务异常");
            assertEquals("服务异常", resp.getError().getMessage());
            assertEquals("server_error", resp.getError().getType());
            assertEquals("internal_error", resp.getError().getCode());
        }

        @Test
        @DisplayName("TC-EX-008: authenticationError 应设置正确的 type 和 code")
        void testAuthError() {
            ErrorResponse resp = ErrorResponse.authenticationError("无效的API Key");
            assertEquals("无效的API Key", resp.getError().getMessage());
            assertEquals("authentication_error", resp.getError().getType());
            assertEquals("invalid_api_key", resp.getError().getCode());
        }
    }
}

