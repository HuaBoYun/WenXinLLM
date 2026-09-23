package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * AI对话请求DTO
 */
@Data
@Schema(description = "AI对话请求")
public class AiChatRequest {

    @Schema(description = "用户消息", required = true, example = "你好，请介绍一下你自己")
    private String message;

    @Schema(description = "会话ID，用于多轮对话", example = "session-123")
    private String sessionId;

    @Schema(description = "是否开启深度思考", example = "false")
    private Boolean enableThinking = false;

    @Schema(description = "是否开启联网搜索", example = "true")
    private Boolean enableWebSearch = true;

    @Schema(description = "是否流式输出", example = "true")
    private Boolean stream = true;

    @Schema(description = "对话历史（可选，用于传入历史上下文）")
    private List<Message> history;

    @Schema(description = "自定义系统提示词（可选，覆盖默认提示词）")
    private String systemPrompt;

    @Schema(description = "是否附加SQL执行工具（默认true）。纯文本分析类调用方（如蓝图需求拆分）传false，"
            + "避免模型在分析文档时调用SQL工具拖慢响应或污染输出", example = "true")
    private Boolean enableTools = true;

    @Schema(description = "调用方页面标识（consult=AI 咨询页，governance=AI 数据治理页；带标识才应用对应页面的 token 限制，其他调用方不受影响）", example = "consult")
    private String scene;

    @Schema(description = "是否启用数据梳理向导模式（true 时按数据梳理向导的固定步骤顺序引导用户逐步治理，"
            + "仅 AI 数据治理页传 true；其他调用方不传或传 false，行为与原来完全一致）", example = "false")
    private Boolean wizardGuide = false;

    @Schema(description = "向导步骤进度 JSON（wizardGuide=true 时生效）：[{key,name,complete}]，"
            + "后端原样注入系统提示词供模型判断当前应聚焦的步骤", example = "[{\"key\":\"resource\",\"name\":\"数据资源管理\",\"complete\":true}]")
    private String wizardProgress;

    @Data
    @Schema(description = "对话消息")
    public static class Message {
        @Schema(description = "角色: user/assistant", example = "user")
        private String role;

        @Schema(description = "消息内容", example = "你好")
        private String content;
    }
}

