package com.huabo.bigmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * OpenAI Chat Completion 请求DTO
 * 符合OpenAI API规范: https://platform.openai.com/docs/api-reference/chat/create
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {

    /**
     * 模型ID，如 gpt-3.5-turbo, gpt-4, claude-3-sonnet 等
     */
    @NotNull(message = "model不能为空")
    private String model;

    /**
     * 消息列表
     */
    @NotEmpty(message = "messages不能为空")
    private List<Message> messages;

    /**
     * 是否流式输出
     */
    @Builder.Default
    private Boolean stream = false;

    /**
     * 采样温度，0-2之间，越高越随机
     */
    private Double temperature;

    /**
     * 核采样参数
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * 生成的completion数量
     */
    private Integer n;

    /**
     * 停止词列表
     */
    private List<String> stop;

    /**
     * 最大token数
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 存在惩罚，-2.0到2.0之间
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    /**
     * 频率惩罚，-2.0到2.0之间
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * 用户标识
     */
    private String user;

    /**
     * 函数调用相关
     */
    private List<Function> functions;

    /**
     * 函数调用控制
     */
    @JsonProperty("function_call")
    private Object functionCall;

    /**
     * 工具列表
     */
    private List<Tool> tools;

    /**
     * 工具选择
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * 响应格式
     */
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    /**
     * 消息对象
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        /**
         * 角色: system, user, assistant, function, tool
         */
        private String role;

        /**
         * 消息内容
         */
        private Object content;

        /**
         * 名称（用于function角色）
         */
        private String name;

        /**
         * 函数调用
         */
        @JsonProperty("function_call")
        private FunctionCall functionCall;

        /**
         * 工具调用列表
         */
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

        /**
         * 工具调用ID
         */
        @JsonProperty("tool_call_id")
        private String toolCallId;
    }

    /**
     * 函数定义
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Function {
        private String name;
        private String description;
        private Map<String, Object> parameters;
    }

    /**
     * 函数调用
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionCall {
        private String name;
        private String arguments;
    }

    /**
     * 工具定义
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tool {
        private String type;
        private Function function;
    }

    /**
     * 工具调用
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolCall {
        private String id;
        private String type;
        private FunctionCall function;
    }

    /**
     * 响应格式
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseFormat {
        private String type;
    }
}

