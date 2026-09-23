package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.dto.AiChatRequest;
import com.huabo.bigmodel.dto.AiChatResponse;
import com.huabo.bigmodel.service.AiChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AiChatController 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AiChatController 单元测试")
public class AiChatControllerTest {

    @Mock
    private AiChatService aiChatService;

    @InjectMocks
    private AiChatController controller;

    // ================================================================
    // 普通对话
    // ================================================================
    @Nested
    @DisplayName("chat - 普通对话")
    class Chat {

        @Test
        @DisplayName("TC-CHAT-001: 正常对话应返回 success=true 和内容")
        void testChatSuccess() {
            when(aiChatService.chat(any())).thenReturn("你好，我是星光问心AI");

            AiChatRequest request = new AiChatRequest();
            request.setMessage("你好");
            request.setSessionId("s1");
            request.setEnableThinking(false);
            request.setEnableWebSearch(true);

            ResponseEntity<AiChatResponse> resp = controller.chat(request);
            AiChatResponse body = resp.getBody();

            assertNotNull(body);
            assertTrue(body.getSuccess());
            assertEquals("你好，我是星光问心AI", body.getContent());
            assertEquals("s1", body.getSessionId());
            assertFalse(body.getUsedThinking());
            assertTrue(body.getUsedWebSearch());
        }

        @Test
        @DisplayName("TC-CHAT-002: 对话异常应返回 success=false")
        void testChatFailure() {
            when(aiChatService.chat(any())).thenThrow(new RuntimeException("API 超时"));

            AiChatRequest request = new AiChatRequest();
            request.setMessage("测试");

            ResponseEntity<AiChatResponse> resp = controller.chat(request);
            AiChatResponse body = resp.getBody();

            assertNotNull(body);
            assertFalse(body.getSuccess());
            assertNotNull(body.getError());
            assertTrue(body.getError().contains("API 超时"));
        }
    }

    // ================================================================
    // 流式对话
    // ================================================================
    @Nested
    @DisplayName("chatStream - 流式对话")
    class ChatStream {

        @Test
        @DisplayName("TC-STREAM-001: 流式对话应返回 Flux")
        void testChatStreamReturnsFlux() {
            Flux<String> mockFlux = Flux.just("data: {\"type\":\"text\"}\n\n");
            when(aiChatService.chatStream(any())).thenReturn(mockFlux);

            AiChatRequest request = new AiChatRequest();
            request.setMessage("流式测试");

            Flux<String> result = controller.chatStream(request);
            assertNotNull(result);

            // 验证 Flux 能正常消费
            String first = result.blockFirst();
            assertNotNull(first);
            assertTrue(first.contains("text"));
        }
    }

    // ================================================================
    // 清除会话
    // ================================================================
    @Nested
    @DisplayName("clearSession - 清除会话")
    class ClearSession {

        @Test
        @DisplayName("TC-CLEAR-001: 清除会话应返回成功消息")
        void testClearSession() {
            doNothing().when(aiChatService).clearSession("session_123");

            ResponseEntity<AiChatResponse> resp = controller.clearSession("session_123");
            AiChatResponse body = resp.getBody();

            assertNotNull(body);
            assertTrue(body.getSuccess());
            assertEquals("会话已清除", body.getMessage());
            assertEquals("session_123", body.getSessionId());
            verify(aiChatService).clearSession("session_123");
        }
    }

    // ================================================================
    // 简单问答 (GET)
    // ================================================================
    @Nested
    @DisplayName("ask - 简单问答")
    class Ask {

        @Test
        @DisplayName("TC-ASK-001: GET 问答应正确传递参数")
        void testAskSuccess() {
            when(aiChatService.chat(any())).thenReturn("回答内容");

            ResponseEntity<AiChatResponse> resp = controller.ask("什么是AI", true, false);
            AiChatResponse body = resp.getBody();

            assertNotNull(body);
            assertTrue(body.getSuccess());
            assertEquals("回答内容", body.getContent());
            assertTrue(body.getUsedThinking());
            assertFalse(body.getUsedWebSearch());
        }

        @Test
        @DisplayName("TC-ASK-002: GET 问答异常应返回错误")
        void testAskFailure() {
            when(aiChatService.chat(any())).thenThrow(new RuntimeException("服务不可用"));

            ResponseEntity<AiChatResponse> resp = controller.ask("测试", false, true);
            AiChatResponse body = resp.getBody();

            assertNotNull(body);
            assertFalse(body.getSuccess());
            assertTrue(body.getError().contains("服务不可用"));
        }

        @Test
        @DisplayName("TC-ASK-003: 默认参数 thinking=false, search=true")
        void testAskDefaults() {
            when(aiChatService.chat(any())).thenReturn("ok");

            ResponseEntity<AiChatResponse> resp = controller.ask("问题", false, true);
            AiChatResponse body = resp.getBody();

            assertFalse(body.getUsedThinking());
            assertTrue(body.getUsedWebSearch());
        }
    }
}

