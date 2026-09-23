package com.huabo.cybermonitor.vo;

import java.time.LocalDate;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业管理驾驶舱查询VO
 * 
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="EnterpriseDashboardQueryVo", description="企业管理驾驶舱查询VO")
public class EnterpriseDashboardQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "统计周期开始日期")
    private LocalDate statisticsPeriodStart;

    @Schema(name = "统计周期结束日期")
    private LocalDate statisticsPeriodEnd;

    @Schema(name = "经营状态")
    private String operatingStatus;

    @Schema(name = "发展趋势")
    private String developmentTrend;

    @Schema(name = "数据状态")
    private String dataStatus;

    @Schema(name = "风险等级")
    private String riskLevel;

    @Schema(name = "更新时间开始")
    private String updateTimeStart;

    @Schema(name = "更新时间结束")
    private String updateTimeEnd;

    @Schema(name = "删除标志")
    private String delFlag;

    @Schema(name = "排序字段")
    private String orderBy;

    @Schema(name = "排序方向")
    private String orderDirection;

}
