package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 经营计划管理查询VO
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="OperationPlanQueryVo", description="经营计划管理查询VO")
public class OperationPlanQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "计划类型")
    private String planType;

    @Schema(name = "计划名称")
    private String planName;

    @Schema(name = "计划年度")
    private Integer planYear;

    @Schema(name = "计划期间")
    private String planPeriod;

    @Schema(name = "计划开始日期-开始")
    private LocalDate planStartDateBegin;

    @Schema(name = "计划开始日期-结束")
    private LocalDate planStartDateEnd;

    @Schema(name = "计划结束日期-开始")
    private LocalDate planEndDateBegin;

    @Schema(name = "计划结束日期-结束")
    private LocalDate planEndDateEnd;

    @Schema(name = "计划状态")
    private String planStatus;

    @Schema(name = "计划版本")
    private String planVersion;

    @Schema(name = "收入计划范围-最小值")
    private BigDecimal revenuePlanMin;

    @Schema(name = "收入计划范围-最大值")
    private BigDecimal revenuePlanMax;

    @Schema(name = "成本计划范围-最小值")
    private BigDecimal costPlanMin;

    @Schema(name = "成本计划范围-最大值")
    private BigDecimal costPlanMax;

    @Schema(name = "利润计划范围-最小值")
    private BigDecimal profitPlanMin;

    @Schema(name = "利润计划范围-最大值")
    private BigDecimal profitPlanMax;

    @Schema(name = "投资计划范围-最小值")
    private BigDecimal investmentPlanMin;

    @Schema(name = "投资计划范围-最大值")
    private BigDecimal investmentPlanMax;

    @Schema(name = "资金计划范围-最小值")
    private BigDecimal fundingPlanMin;

    @Schema(name = "资金计划范围-最大值")
    private BigDecimal fundingPlanMax;

    @Schema(name = "计划制定负责人")
    private String planManager;

    @Schema(name = "计划制定部门")
    private String planDepartment;

    @Schema(name = "制定开始时间-开始")
    private LocalDateTime formulationStartTimeBegin;

    @Schema(name = "制定开始时间-结束")
    private LocalDateTime formulationStartTimeEnd;

    @Schema(name = "制定结束时间-开始")
    private LocalDateTime formulationEndTimeBegin;

    @Schema(name = "制定结束时间-结束")
    private LocalDateTime formulationEndTimeEnd;

    @Schema(name = "制定方法")
    private String formulationMethod;

    @Schema(name = "审批状态")
    private String approvalStatus;

    @Schema(name = "审批人")
    private String approver;

    @Schema(name = "审批时间-开始")
    private LocalDateTime approvalTimeBegin;

    @Schema(name = "审批时间-结束")
    private LocalDateTime approvalTimeEnd;

    @Schema(name = "发布状态")
    private String publicationStatus;

    @Schema(name = "发布时间-开始")
    private LocalDateTime publicationTimeBegin;

    @Schema(name = "发布时间-结束")
    private LocalDateTime publicationTimeEnd;

    @Schema(name = "执行启动时间-开始")
    private LocalDateTime executionStartTimeBegin;

    @Schema(name = "执行启动时间-结束")
    private LocalDateTime executionStartTimeEnd;

    @Schema(name = "执行状态")
    private String executionStatus;

    @Schema(name = "执行进度范围-最小值")
    private BigDecimal executionProgressMin;

    @Schema(name = "执行进度范围-最大值")
    private BigDecimal executionProgressMax;

    @Schema(name = "完成情况")
    private String completionStatus;

    @Schema(name = "执行质量")
    private String executionQuality;

    @Schema(name = "执行效率")
    private String executionEfficiency;

    @Schema(name = "执行效果")
    private String executionEffectiveness;

    @Schema(name = "执行创新")
    private String executionInnovation;

    @Schema(name = "调整时间-开始")
    private LocalDateTime adjustmentTimeBegin;

    @Schema(name = "调整时间-结束")
    private LocalDateTime adjustmentTimeEnd;

    @Schema(name = "调整合理性")
    private String adjustmentRationality;

    @Schema(name = "调整及时性")
    private String adjustmentTimeliness;

    @Schema(name = "调整有效性")
    private String adjustmentEffectiveness;

    @Schema(name = "调整创新性")
    private String adjustmentInnovation;

    @Schema(name = "监控频率")
    private String monitoringFrequency;

    @Schema(name = "最后监控时间-开始")
    private LocalDateTime lastMonitoringTimeBegin;

    @Schema(name = "最后监控时间-结束")
    private LocalDateTime lastMonitoringTimeEnd;

    @Schema(name = "监控负责人")
    private String monitoringManager;

    @Schema(name = "更新时间-开始")
    private LocalDateTime updateTimeBegin;

    @Schema(name = "更新时间-结束")
    private LocalDateTime updateTimeEnd;

    @Schema(name = "创建人")
    private String createBy;

    @Schema(name = "更新人")
    private String updateBy;
}
