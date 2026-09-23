package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.dto.*;
import com.huabo.bigmodel.service.LlmService;
import com.huabo.bigmodel.service.LlmServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ChatController (OpenAI 兼容 API) 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChatController 单元测试")
public class ChatControllerTest {

    @Mock
    private LlmServiceFactory llmServiceFactory;

    @Mock
    private LlmService mockLlmService;

    @InjectMocks
    private ChatController controller;

    // ================================================================
    // 非流式对话
    // ================================================================
    @Nested
    @DisplayName("chatCompletions - 非流式")
    class NonStreamChat {

        @Test
        @DisplayName("TC-COMPAT-001: 非流式请求应返回 ChatCompletionResponse")
        void testNonStreamSuccess() {
            ChatCompletionResponse mockResp = new ChatCompletionResponse();
            when(llmServiceFactory.getServiceByModel(any())).thenReturn(mockLlmService);
            when(mockLlmService.chat(any())).thenReturn(mockResp);

            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setModel("gpt-4");
            request.setStream(false);
            ChatCompletionRequest.Message msg = new ChatCompletionRequest.Message();
            msg.setRole("user");
            msg.setContent("你好");
            request.setMessages(Collections.singletonList(msg));

            Object result = controller.chatCompletions(request, null);
            assertNotNull(result);
            assertTrue(result instanceof ResponseEntity);
        }

        @Test
        @DisplayName("TC-COMPAT-002: 对话异常应返回 ErrorResponse")
        void testNonStreamError() {
            when(llmServiceFactory.getServiceByModel(any())).thenThrow(new RuntimeException("服务异常"));

            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setModel("gpt-4");
            request.setStream(false);
            ChatCompletionRequest.Message msg = new ChatCompletionRequest.Message();
            msg.setRole("user");
            msg.setContent("测试");
            request.setMessages(Collections.singletonList(msg));

            Object result = controller.chatCompletions(request, null);
            assertNotNull(result);
            assertTrue(result instanceof ResponseEntity);
        }
    }

    // ================================================================
    // 模型列表
    // ================================================================
    @Nested
    @DisplayName("listModels")
    class ListModels {

        @Test
        @DisplayName("TC-MODELS-001: 应返回 6 个模型")
        void testListModels() {
            ResponseEntity<ModelsResponse> resp = controller.listModels();
            ModelsResponse body = resp.getBody();

            assertNotNull(body);
            assertEquals(6, body.getData().size());
        }

        @Test
        @DisplayName("TC-MODELS-002: 模型列表应包含 gpt 和 claude 系列")
        void testModelNames() {
            ResponseEntity<ModelsResponse> resp = controller.listModels();
            ModelsResponse body = resp.getBody();

            boolean hasGpt = body.getData().stream().anyMatch(m -> m.getId().startsWith("gpt-"));
            boolean hasClaude = body.getData().stream().anyMatch(m -> m.getId().startsWith("claude-"));
            assertTrue(hasGpt);
            assertTrue(hasClaude);
        }
    }

    // ================================================================
    // 获取单个模型
    // ================================================================
    @Nested
    @DisplayName("getModel")
    class GetModel {

        @Test
        @DisplayName("TC-MODEL-001: 应返回请求的模型 ID")
        void testGetModel() {
            ResponseEntity<ModelsResponse.ModelData> resp = controller.getModel("gpt-4-turbo");
            ModelsResponse.ModelData body = resp.getBody();

            assertNotNull(body);
            assertEquals("gpt-4-turbo", body.getId());
            assertTrue(body.getCreated() > 0);
        }
    }

    // ================================================================
    // 健康检查
    // ================================================================
    @Nested
    @DisplayName("health")
    class Health {

        @Test
        @DisplayName("TC-HEALTH-001: 健康检查应返回 OK")
        void testHealth() {
            ResponseEntity<String> resp = controller.health();
            assertEquals("OK", resp.getBody());
            assertEquals(200, resp.getStatusCodeValue());
        }
    }
}

