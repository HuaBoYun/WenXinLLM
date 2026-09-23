package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 业务蓝图分析请求 DTO
 */
@Data
@Schema(description = "业务蓝图分析请求")
public class BlueprintAnalyzeRequest {

    @Schema(description = "文档标题", example = "采购流程梳理")
    private String title;

    @Schema(description = "文档正文内容（纯文本）", required = true)
    private String content;
}
