package com.huabo.bigmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OpenAI 流式响应块DTO
 * 用于SSE流式输出
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionChunk {

    /**
     * 响应ID
     */
    private String id;

    /**
     * 对象类型，固定为 "chat.completion.chunk"
     */
    @Builder.Default
    private String object = "chat.completion.chunk";

    /**
     * 创建时间戳
     */
    private Long created;

    /**
     * 使用的模型
     */
    private String model;

    /**
     * 选择列表
     */
    private java.util.List<Choice> choices;

    /**
     * 系统指纹
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * 流式选择对象
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        /**
         * 索引
         */
        private Integer index;

        /**
         * 增量内容
         */
        private Delta delta;

        /**
         * 结束原因
         */
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    /**
     * 增量内容
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Delta {
        /**
         * 角色（仅首个chunk包含）
         */
        private String role;

        /**
         * 内容片段
         */
        private String content;

        /**
         * 函数调用
         */
        @JsonProperty("function_call")
        private ChatCompletionRequest.FunctionCall functionCall;

        /**
         * 工具调用
         */
        @JsonProperty("tool_calls")
        private java.util.List<ChatCompletionRequest.ToolCall> toolCalls;
    }
}

