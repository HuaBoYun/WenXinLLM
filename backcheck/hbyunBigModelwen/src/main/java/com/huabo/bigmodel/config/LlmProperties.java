package com.huabo.bigmodel.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 大模型配置属性
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "llm")
public class LlmProperties {

    /**
     * 默认提供商: openai, claude, zhipu
     */
    private String provider = "zhipu";

    /**
     * OpenAI配置
     */
    private OpenAIConfig openai = new OpenAIConfig();

    /**
     * Claude配置
     */
    private ClaudeConfig claude = new ClaudeConfig();

    /**
     * 智谱AI配置 (Anthropic兼容接口)
     */
    private ZhipuConfig zhipu = new ZhipuConfig();

    /**
     * MiniMaxi配置 (Anthropic兼容接口)
     */
    private MinimaxiConfig minimaxi = new MinimaxiConfig();

    /**
     * 搜索API配置
     */
    private SearchConfig search = new SearchConfig();

    /**
     * 深度思考配置
     */
    private ThinkingConfig thinking = new ThinkingConfig();

    /**
     * 系统提示词（AI 建模 / 默认场景）
     */
    private String systemPrompt = "";

    /**
     * AI 咨询专属系统提示词（财经/业务咨询场景，不涉及SQL建表等开发操作）
     * 为空时使用内置的默认咨询提示词
     */
    private String consultSystemPrompt = "";

    /**
     * AI 咨询页（请求 scene=consult）单轮输出 token 上限。
     * 仅对该页面生效；AI 办公/AI 建模依赖超长输出，不受影响
     */
    private Integer consultMaxTokens = 10000;

    /**
     * AI 咨询页（请求 scene=consult）单会话累计 token 上限（输入+输出，含思考）。
     * 达到上限后拒绝该会话继续对话，提示开启新对话；0=不限制
     */
    private Integer consultSessionMaxTokens = 100000;

    /**
     * AI 数据治理专属系统提示词（数据梳理/资产目录/数据标准/血缘等治理场景，不涉及SQL建表等开发操作）
     * 为空时使用内置的默认数据治理提示词
     */
    private String governanceSystemPrompt = "";

    /**
     * AI 数据治理页（请求 scene=governance）单轮输出 token 上限。
     * 治理场景常输出资产目录、血缘报告等长文档，默认比咨询页放宽；仅对该页面生效
     */
    private Integer governanceMaxTokens = 32768;

    /**
     * AI 数据治理页（请求 scene=governance）单会话累计 token 上限（输入+输出，含思考）。
     * 达到上限后拒绝该会话继续对话，提示开启新对话；0=不限制
     */
    private Integer governanceSessionMaxTokens = 200000;

    @Data
    public static class OpenAIConfig {
        private String apiKey = "";
        private String baseUrl = "https://api.openai.com/v1";
        private String model = "gpt-3.5-turbo";
        private Integer timeout = 60;
    }

    @Data
    public static class ClaudeConfig {
        private String apiKey = "";
        private String baseUrl = "https://api.anthropic.com/v1";
        private String model = "claude-3-sonnet-20240229";
        private Integer timeout = 60;
        private Integer maxTokens = 4096;
    }

    @Data
    public static class ZhipuConfig {
        private String apiKey = "";
        private String baseUrl = "https://open.bigmodel.cn/api/anthropic";
        private String model = "glm-4.7";
        private Integer timeout = 180;
        private Integer maxTokens = 4096;
    }

    @Data
    public static class SearchConfig {
        private String tavilyApiKey = "";
        private String serperApiKey = "";
        private Boolean enabled = true;
    }

    @Data
    public static class ThinkingConfig {
        private Integer budgetTokens = 10000;
    }

    @Data
    public static class MinimaxiConfig {
        private String apiKey = "";
        private String baseUrl = "https://api.minimaxi.com/anthropic";
        private String model = "MiniMax-M2.7";
        private Integer timeout = 1800;
        private Integer maxTokens = 196608;
    }

    /**
     * 根据provider获取对应的配置
     */
    public Object getProviderConfig() {
        switch (provider.toLowerCase()) {
            case "openai":
                return openai;
            case "claude":
                return claude;
            case "zhipu":
                return zhipu;
            case "minimaxi":
                return minimaxi;
            default:
                log.warn("未知的provider: {}, 使用默认配置zhipu", provider);
                return zhipu;
        }
    }

    /**
     * 获取当前provider的API Key
     */
    public String getCurrentApiKey() {
        switch (provider.toLowerCase()) {
            case "openai":
                return openai.getApiKey();
            case "claude":
                return claude.getApiKey();
            case "zhipu":
                return zhipu.getApiKey();
            case "minimaxi":
                return minimaxi.getApiKey();
            default:
                return zhipu.getApiKey();
        }
    }

    /**
     * 获取当前provider的Base URL
     */
    public String getCurrentBaseUrl() {
        switch (provider.toLowerCase()) {
            case "openai":
                return openai.getBaseUrl();
            case "claude":
                return claude.getBaseUrl();
            case "zhipu":
                return zhipu.getBaseUrl();
            case "minimaxi":
                return minimaxi.getBaseUrl();
            default:
                return zhipu.getBaseUrl();
        }
    }

    /**
     * 获取当前provider的模型名称
     */
    public String getCurrentModel() {
        switch (provider.toLowerCase()) {
            case "openai":
                return openai.getModel();
            case "claude":
                return claude.getModel();
            case "zhipu":
                return zhipu.getModel();
            case "minimaxi":
                return minimaxi.getModel();
            default:
                return zhipu.getModel();
        }
    }

    /**
     * 获取当前provider的超时时间
     */
    public Integer getCurrentTimeout() {
        switch (provider.toLowerCase()) {
            case "openai":
                return openai.getTimeout();
            case "claude":
                return claude.getTimeout();
            case "zhipu":
                return zhipu.getTimeout();
            case "minimaxi":
                return minimaxi.getTimeout();
            default:
                return zhipu.getTimeout();
        }
    }

    /**
     * 获取当前provider的Max Tokens
     */
    public Integer getCurrentMaxTokens() {
        switch (provider.toLowerCase()) {
            case "claude":
                return claude.getMaxTokens();
            case "zhipu":
                return zhipu.getMaxTokens();
            case "minimaxi":
                return minimaxi.getMaxTokens();
            default:
                return zhipu.getMaxTokens();
        }
    }
}