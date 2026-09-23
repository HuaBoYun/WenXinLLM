package com.huabo.bigmodel.service;

import com.huabo.bigmodel.config.LlmProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * LlmServiceFactory 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LlmServiceFactory 单元测试")
public class LlmServiceFactoryTest {

    @Mock
    private LlmService openaiService;

    @Mock
    private LlmService claudeService;

    @Mock
    private LlmService zhipuService;

    private LlmProperties llmProperties;
    private LlmServiceFactory factory;

    @BeforeEach
    void setUp() {
        llmProperties = new LlmProperties();
        llmProperties.setProvider("zhipu"); // 默认提供商
        factory = new LlmServiceFactory(llmProperties, openaiService, claudeService, zhipuService);
    }

    // ================================================================
    // 默认服务
    // ================================================================
    @Nested
    @DisplayName("getDefaultService")
    class DefaultService {

        @Test
        @DisplayName("TC-FACTORY-001: 默认提供商为 zhipu 时应返回 zhipuService")
        void testDefaultZhipu() {
            assertSame(zhipuService, factory.getDefaultService());
        }

        @Test
        @DisplayName("TC-FACTORY-002: 修改默认提供商为 openai 后应返回 openaiService")
        void testDefaultOpenai() {
            llmProperties.setProvider("openai");
            assertSame(openaiService, factory.getDefaultService());
        }
    }

    // ================================================================
    // 按提供商名称获取
    // ================================================================
    @Nested
    @DisplayName("getService")
    class GetService {

        @Test
        @DisplayName("TC-FACTORY-003: provider=openai 应返回 openaiService")
        void testGetOpenai() {
            assertSame(openaiService, factory.getService("openai"));
        }

        @Test
        @DisplayName("TC-FACTORY-004: provider=claude 应返回 claudeService")
        void testGetClaude() {
            assertSame(claudeService, factory.getService("claude"));
        }

        @Test
        @DisplayName("TC-FACTORY-005: provider=zhipu 应返回 zhipuService")
        void testGetZhipu() {
            assertSame(zhipuService, factory.getService("zhipu"));
        }

        @Test
        @DisplayName("TC-FACTORY-006: 未知 provider 应回退到默认服务")
        void testUnknownFallback() {
            LlmService result = factory.getService("unknown_provider");
            assertSame(zhipuService, result); // 默认是 zhipu
        }
    }

    // ================================================================
    // 按模型名称智能选择
    // ================================================================
    @Nested
    @DisplayName("getServiceByModel - 智能路由")
    class GetServiceByModel {

        @Test
        @DisplayName("TC-MODEL-001: gpt-3.5-turbo 应路由到 openai")
        void testGpt35() {
            assertSame(openaiService, factory.getServiceByModel("gpt-3.5-turbo"));
        }

        @Test
        @DisplayName("TC-MODEL-002: gpt-4 应路由到 openai")
        void testGpt4() {
            assertSame(openaiService, factory.getServiceByModel("gpt-4"));
        }

        @Test
        @DisplayName("TC-MODEL-003: o1-preview 应路由到 openai")
        void testO1() {
            assertSame(openaiService, factory.getServiceByModel("o1-preview"));
        }

        @Test
        @DisplayName("TC-MODEL-004: o3-mini 应路由到 openai")
        void testO3() {
            assertSame(openaiService, factory.getServiceByModel("o3-mini"));
        }

        @Test
        @DisplayName("TC-MODEL-005: claude-3-sonnet 应路由到 claude")
        void testClaude3() {
            assertSame(claudeService, factory.getServiceByModel("claude-3-sonnet-20240229"));
        }

        @Test
        @DisplayName("TC-MODEL-006: claude-3-opus 应路由到 claude")
        void testClaudeOpus() {
            assertSame(claudeService, factory.getServiceByModel("claude-3-opus-20240229"));
        }

        @Test
        @DisplayName("TC-MODEL-007: glm-4 应路由到 zhipu")
        void testGlm4() {
            assertSame(zhipuService, factory.getServiceByModel("glm-4"));
        }

        @Test
        @DisplayName("TC-MODEL-008: glm-4.7 应路由到 zhipu")
        void testGlm47() {
            assertSame(zhipuService, factory.getServiceByModel("glm-4.7"));
        }

        @Test
        @DisplayName("TC-MODEL-009: null 模型名应返回默认服务")
        void testNullModel() {
            assertSame(zhipuService, factory.getServiceByModel(null));
        }

        @Test
        @DisplayName("TC-MODEL-010: 未知模型名应返回默认服务")
        void testUnknownModel() {
            assertSame(zhipuService, factory.getServiceByModel("llama-3-70b"));
        }

        @Test
        @DisplayName("TC-MODEL-011: 大写模型名应正确路由（大小写不敏感）")
        void testUpperCaseModel() {
            assertSame(openaiService, factory.getServiceByModel("GPT-4-TURBO"));
        }
    }

    // ================================================================
    // 可用性检查
    // ================================================================
    @Nested
    @DisplayName("isProviderAvailable")
    class ProviderAvailability {

        @Test
        @DisplayName("TC-AVAIL-001: 已注册且可用的提供商应返回 true")
        void testAvailable() {
            when(zhipuService.isAvailable()).thenReturn(true);
            assertTrue(factory.isProviderAvailable("zhipu"));
        }

        @Test
        @DisplayName("TC-AVAIL-002: 已注册但不可用的提供商应返回 false")
        void testNotAvailable() {
            when(openaiService.isAvailable()).thenReturn(false);
            assertFalse(factory.isProviderAvailable("openai"));
        }

        @Test
        @DisplayName("TC-AVAIL-003: 未注册的提供商应返回 false")
        void testUnregistered() {
            assertFalse(factory.isProviderAvailable("gemini"));
        }
    }
}

