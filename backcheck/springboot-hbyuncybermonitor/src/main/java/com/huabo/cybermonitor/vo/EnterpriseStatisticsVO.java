package com.huabo.cybermonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业统计VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@Schema(name="企业统计数据")
public class EnterpriseStatisticsVO {

    @Schema(name = "企业总数")
    private Long totalCount;

    @Schema(name = "国有独资企业数量")
    private Long stateOwnedCount;

    @Schema(name = "国有控股企业数量")
    private Long stateHoldingCount;

    @Schema(name = "国有参股企业数量")
    private Long stateParticipatingCount;

    @Schema(name = "已上市企业数量")
    private Long listedCount;

    @Schema(name = "正常状态企业数量")
    private Long normalCount;

    @Schema(name = "注销企业数量")
    private Long cancelledCount;

    @Schema(name = "合并企业数量")
    private Long mergedCount;

    @Schema(name = "暂停企业数量")
    private Long suspendedCount;

    @Schema(name = "中央监管企业数量")
    private Long centralSupervisionCount;

    @Schema(name = "地方监管企业数量")
    private Long localSupervisionCount;
}
