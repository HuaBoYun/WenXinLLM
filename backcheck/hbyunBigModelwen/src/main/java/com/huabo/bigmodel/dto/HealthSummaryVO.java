package com.huabo.bigmodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务健康汇总
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "服务健康汇总信息")
public class HealthSummaryVO {

    @Schema(description = "在线服务数")
    private Integer onlineCount;

    @Schema(description = "离线服务数")
    private Integer offlineCount;

    @Schema(description = "服务总数")
    private Integer totalCount;

    @Schema(description = "总实例数")
    private Integer totalInstances;

    @Schema(description = "健康率(%)")
    private Double healthRate;
}
