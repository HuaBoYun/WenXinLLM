package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI对话响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI对话响应")
public class AiChatResponse {

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "AI回复内容")
    private String content;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "是否使用了联网搜索")
    private Boolean usedWebSearch;

    @Schema(description = "是否使用了深度思考")
    private Boolean usedThinking;

    @Schema(description = "思考过程内容（如果开启了深度思考）")
    private String thinkingContent;

    @Schema(description = "错误信息")
    private String error;

    public static AiChatResponse success(String content) {
        return AiChatResponse.builder()
                .success(true)
                .content(content)
                .build();
    }

    public static AiChatResponse error(String error) {
        return AiChatResponse.builder()
                .success(false)
                .error(error)
                .build();
    }
}

