package com.huabo.bigmodel.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LlmProperties 配置属性测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@DisplayName("LlmProperties 配置属性测试")
public class LlmPropertiesTest {

    @Nested
    @DisplayName("默认值验证")
    class Defaults {

        @Test
        @DisplayName("TC-CFG-001: 默认 provider 应为 zhipu")
        void testDefaultProvider() {
            LlmProperties props = new LlmProperties();
            assertEquals("zhipu", props.getProvider());
        }

        @Test
        @DisplayName("TC-CFG-002: 默认 systemPrompt 应为空字符串")
        void testDefaultSystemPrompt() {
            LlmProperties props = new LlmProperties();
            assertEquals("", props.getSystemPrompt());
        }

        @Test
        @DisplayName("TC-CFG-003: OpenAI 默认配置应正确")
        void testOpenAIDefaults() {
            LlmProperties.OpenAIConfig config = new LlmProperties.OpenAIConfig();
            assertEquals("", config.getApiKey());
            assertEquals("https://api.openai.com/v1", config.getBaseUrl());
            assertEquals("gpt-3.5-turbo", config.getModel());
            assertEquals(60, config.getTimeout());
        }

        @Test
        @DisplayName("TC-CFG-004: Claude 默认配置应正确")
        void testClaudeDefaults() {
            LlmProperties.ClaudeConfig config = new LlmProperties.ClaudeConfig();
            assertEquals("", config.getApiKey());
            assertEquals("https://api.anthropic.com/v1", config.getBaseUrl());
            assertEquals("claude-3-sonnet-20240229", config.getModel());
            assertEquals(60, config.getTimeout());
            assertEquals(4096, config.getMaxTokens());
        }

        @Test
        @DisplayName("TC-CFG-005: 智谱 AI 默认配置应正确")
        void testZhipuDefaults() {
            LlmProperties.ZhipuConfig config = new LlmProperties.ZhipuConfig();
            assertEquals("", config.getApiKey());
            assertEquals("https://open.bigmodel.cn/api/anthropic", config.getBaseUrl());
            assertEquals("glm-4.7", config.getModel());
            assertEquals(180, config.getTimeout());
            assertEquals(4096, config.getMaxTokens());
        }

        @Test
        @DisplayName("TC-CFG-006: 搜索配置默认启用")
        void testSearchDefaults() {
            LlmProperties.SearchConfig config = new LlmProperties.SearchConfig();
            assertTrue(config.getEnabled());
            assertEquals("", config.getTavilyApiKey());
            assertEquals("", config.getSerperApiKey());
        }

        @Test
        @DisplayName("TC-CFG-007: 深度思考默认 budgetTokens=10000")
        void testThinkingDefaults() {
            LlmProperties.ThinkingConfig config = new LlmProperties.ThinkingConfig();
            assertEquals(10000, config.getBudgetTokens());
        }
    }

    @Nested
    @DisplayName("属性设置")
    class SetProperties {

        @Test
        @DisplayName("TC-CFG-008: 应能正确设置和获取所有属性")
        void testSetAndGet() {
            LlmProperties props = new LlmProperties();
            props.setProvider("openai");
            props.setSystemPrompt("你是一个AI助手");

            assertEquals("openai", props.getProvider());
            assertEquals("你是一个AI助手", props.getSystemPrompt());
        }

        @Test
        @DisplayName("TC-CFG-009: 子配置对象应独立初始化")
        void testSubConfigIndependence() {
            LlmProperties props = new LlmProperties();
            assertNotNull(props.getOpenai());
            assertNotNull(props.getClaude());
            assertNotNull(props.getZhipu());
            assertNotNull(props.getSearch());
            assertNotNull(props.getThinking());
        }
    }
}

