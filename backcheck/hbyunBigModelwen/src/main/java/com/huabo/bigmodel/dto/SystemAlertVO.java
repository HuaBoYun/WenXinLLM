package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统告警信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统告警信息")
public class SystemAlertVO {

    @Schema(description = "告警级别: danger / warning", example = "danger")
    private String level;

    @Schema(description = "告警类型: CPU / MEMORY / DISK / JVM", example = "CPU")
    private String type;

    @Schema(description = "告警标题", example = "CPU使用率过高")
    private String title;

    @Schema(description = "告警详情", example = "当前CPU使用率为92.5%，超过危险阈值85%")
    private String message;

    @Schema(description = "当前值", example = "92.5")
    private Double value;

    @Schema(description = "阈值", example = "85.0")
    private Double threshold;

    @Schema(description = "单位", example = "%")
    private String unit;

    @Schema(description = "告警时间", example = "2026-06-30 14:30:00")
    private String time;
}
