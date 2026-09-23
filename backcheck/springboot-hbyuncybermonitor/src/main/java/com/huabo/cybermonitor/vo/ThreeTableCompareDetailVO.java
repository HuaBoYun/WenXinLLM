package com.huabo.cybermonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 三表比对详情VO（含差异分析明细）
 * @author system
 * @date 2025-01-01
 */
@Data
@Schema(name = "ThreeTableCompareDetailVO", description = "三表比对详情")
public class ThreeTableCompareDetailVO {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "企业名称")
    private String companyName;

    @Schema(description = "差异状态")
    private String diffStatus;

    @Schema(description = "处置建议")
    private String suggestion;

    @Schema(description = "差异分析明细列表")
    private List<Map<String, Object>> diffDetails;
}
