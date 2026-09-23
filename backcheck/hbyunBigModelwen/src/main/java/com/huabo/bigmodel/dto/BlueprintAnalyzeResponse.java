package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 业务蓝图分析响应 DTO
 */
@Data
@Schema(description = "业务蓝图分析响应")
public class BlueprintAnalyzeResponse {

    @Schema(description = "是否成功")
    private boolean success;

    @Schema(description = "错误信息（失败时）")
    private String message;

    @Schema(description = "解析出的需求条目列表")
    private List<RequirementItem> requirements;

    @Data
    @Schema(description = "需求条目")
    public static class RequirementItem {

        @Schema(description = "问题状态：待处理 / 已完成")
        private String status = "待处理";

        @Schema(description = "提交人")
        private String submitter = "";

        @Schema(description = "提交时间 yyyy-MM-dd")
        private String submitTime = "";

        @Schema(description = "问题领域：需求调整 / 新增需求 / BUG修复 / 功能优化")
        private String domain = "新增需求";

        @Schema(description = "问题模块")
        private String module = "";

        @Schema(description = "问题描述")
        private String description = "";

        @Schema(description = "紧急程度：高 / 中 / 低")
        private String urgency = "中";

        @Schema(description = "预计解决时间 yyyy-MM-dd")
        private String resolveDate = "";

        @Schema(description = "处理人")
        private String handler = "";

        @Schema(description = "解决方案")
        private String solution = "";

        @Schema(description = "是否解决：是 / 否")
        private String resolved = "否";
    }

    public static BlueprintAnalyzeResponse success(List<RequirementItem> items) {
        BlueprintAnalyzeResponse resp = new BlueprintAnalyzeResponse();
        resp.setSuccess(true);
        resp.setRequirements(items);
        return resp;
    }

    public static BlueprintAnalyzeResponse error(String msg) {
        BlueprintAnalyzeResponse resp = new BlueprintAnalyzeResponse();
        resp.setSuccess(false);
        resp.setMessage(msg);
        return resp;
    }
}
