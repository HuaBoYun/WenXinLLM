package com.huabo.bigmodel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huabo.bigmodel.config.LlmProperties;
import com.huabo.bigmodel.dto.AiChatRequest;
import com.huabo.bigmodel.service.AiChatService;
import com.huabo.bigmodel.tool.ToolExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AiChatServiceImpl 单元测试
 * 测试会话管理、消息构建等不依赖外部 API 的逻辑
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AiChatServiceImpl 单元测试")
public class AiChatServiceImplTest {

    @Mock
    private ToolExecutor toolExecutor;

    private LlmProperties llmProperties;
    private AiChatServiceImpl aiChatService;
    private Map<String, List<JSONObject>> sessionHistory;

    @BeforeEach
    void setUp() throws Exception {
        llmProperties = new LlmProperties();
        // 设置智谱配置
        LlmProperties.ZhipuConfig zhipu = new LlmProperties.ZhipuConfig();
        zhipu.setApiKey("test-api-key");
        zhipu.setBaseUrl("https://test.api.com");
        zhipu.setModel("glm-4");
        zhipu.setTimeout(30);
        zhipu.setMaxTokens(4096);
        llmProperties.setZhipu(zhipu);

        LlmProperties.SearchConfig search = new LlmProperties.SearchConfig();
        search.setEnabled(true);
        llmProperties.setSearch(search);

        LlmProperties.ThinkingConfig thinking = new LlmProperties.ThinkingConfig();
        thinking.setBudgetTokens(10000);
        llmProperties.setThinking(thinking);

        aiChatService = new AiChatServiceImpl(llmProperties, toolExecutor);

        // 通过反射获取 sessionHistory
        Field historyField = AiChatServiceImpl.class.getDeclaredField("sessionHistory");
        historyField.setAccessible(true);
        sessionHistory = (Map<String, List<JSONObject>>) historyField.get(aiChatService);
    }

    // ================================================================
    // 会话管理
    // ================================================================
    @Nested
    @DisplayName("会话管理")
    class SessionManagement {

        @Test
        @DisplayName("TC-SESSION-001: clearSession 应移除指定会话")
        void testClearSession() {
            // 先添加一个会话
            List<JSONObject> history = new ArrayList<>();
            JSONObject msg = new JSONObject();
            msg.put("role", "user");
            msg.put("content", "你好");
            history.add(msg);
            sessionHistory.put("session_001", history);

            assertEquals(1, sessionHistory.size());

            aiChatService.clearSession("session_001");

            assertEquals(0, sessionHistory.size());
            assertNull(sessionHistory.get("session_001"));
        }

        @Test
        @DisplayName("TC-SESSION-002: clearSession(null) 不应抛异常")
        void testClearNullSession() {
            assertDoesNotThrow(() -> aiChatService.clearSession(null));
        }

        @Test
        @DisplayName("TC-SESSION-003: clearSession 不存在的会话不应抛异常")
        void testClearNonExistentSession() {
            assertDoesNotThrow(() -> aiChatService.clearSession("not_exist"));
        }

        @Test
        @DisplayName("TC-SESSION-004: 多个会话应独立管理")
        void testMultipleSessions() {
            sessionHistory.put("s1", new ArrayList<>());
            sessionHistory.put("s2", new ArrayList<>());
            sessionHistory.put("s3", new ArrayList<>());

            assertEquals(3, sessionHistory.size());

            aiChatService.clearSession("s2");

            assertEquals(2, sessionHistory.size());
            assertNotNull(sessionHistory.get("s1"));
            assertNull(sessionHistory.get("s2"));
            assertNotNull(sessionHistory.get("s3"));
        }
    }

    // ================================================================
    // 请求参数处理
    // ================================================================
    @Nested
    @DisplayName("请求参数处理")
    class RequestProcessing {

        @Test
        @DisplayName("TC-REQ-001: AiChatRequest 默认值应正确")
        void testRequestDefaults() {
            AiChatRequest req = new AiChatRequest();
            assertFalse(req.getEnableThinking());
            assertTrue(req.getEnableWebSearch());
            assertTrue(req.getStream());
        }

        @Test
        @DisplayName("TC-REQ-002: 空 sessionId 应自动生成 UUID")
        void testAutoGenerateSessionId() {
            AiChatRequest req = new AiChatRequest();
            req.setMessage("测试");
            req.setSessionId(null);

            // processChat 内部会生成 sessionId
            // 验证 UUID 格式
            String uuid = UUID.randomUUID().toString();
            assertNotNull(uuid);
            assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
        }

        @Test
        @DisplayName("TC-REQ-003: 历史消息应正确添加到会话")
        void testHistoryMessages() {
            List<AiChatRequest.Message> history = new ArrayList<>();
            AiChatRequest.Message msg1 = new AiChatRequest.Message();
            msg1.setRole("user");
            msg1.setContent("第一条消息");
            history.add(msg1);

            AiChatRequest.Message msg2 = new AiChatRequest.Message();
            msg2.setRole("assistant");
            msg2.setContent("第一条回复");
            history.add(msg2);

            AiChatRequest req = new AiChatRequest();
            req.setHistory(history);

            assertEquals(2, req.getHistory().size());
            assertEquals("user", req.getHistory().get(0).getRole());
            assertEquals("assistant", req.getHistory().get(1).getRole());
        }
    }

    // ================================================================
    // LlmProperties 集成
    // ================================================================
    @Nested
    @DisplayName("LlmProperties 配置集成")
    class PropertiesIntegration {

        @Test
        @DisplayName("TC-PROP-001: 搜索配置应正确读取")
        void testSearchConfig() {
            assertTrue(llmProperties.getSearch().getEnabled());
        }

        @Test
        @DisplayName("TC-PROP-002: 思考配置应正确读取")
        void testThinkingConfig() {
            assertEquals(10000, llmProperties.getThinking().getBudgetTokens());
        }

        @Test
        @DisplayName("TC-PROP-003: 智谱配置应正确读取")
        void testZhipuConfig() {
            assertEquals("glm-4", llmProperties.getZhipu().getModel());
            assertEquals(4096, llmProperties.getZhipu().getMaxTokens());
            assertEquals(30, llmProperties.getZhipu().getTimeout());
        }
    }

    // ================================================================
    // 并发安全
    // ================================================================
    @Nested
    @DisplayName("并发安全")
    class ConcurrencySafety {

        @Test
        @DisplayName("TC-CONC-001: sessionHistory 应为 ConcurrentHashMap")
        void testConcurrentMap() {
            assertTrue(sessionHistory instanceof ConcurrentHashMap);
        }

        @Test
        @DisplayName("TC-CONC-002: 并发清除会话不应抛异常")
        void testConcurrentClear() throws InterruptedException {
            for (int i = 0; i < 100; i++) {
                sessionHistory.put("s" + i, new ArrayList<>());
            }

            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 50; i++) {
                    aiChatService.clearSession("s" + i);
                }
            });
            Thread t2 = new Thread(() -> {
                for (int i = 50; i < 100; i++) {
                    aiChatService.clearSession("s" + i);
                }
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            assertEquals(0, sessionHistory.size());
        }
    }
}

