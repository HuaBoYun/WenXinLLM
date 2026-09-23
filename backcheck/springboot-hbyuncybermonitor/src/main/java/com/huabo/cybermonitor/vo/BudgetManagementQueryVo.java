package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预算管理查询VO
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="BudgetManagementQueryVo", description="预算管理查询VO")
public class BudgetManagementQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "预算类型")
    private String budgetType;

    @Schema(name = "预算名称")
    private String budgetName;

    @Schema(name = "预算年度")
    private Integer budgetYear;

    @Schema(name = "预算期间")
    private String budgetPeriod;

    @Schema(name = "预算开始日期-开始")
    private LocalDate budgetStartDateBegin;

    @Schema(name = "预算开始日期-结束")
    private LocalDate budgetStartDateEnd;

    @Schema(name = "预算结束日期-开始")
    private LocalDate budgetEndDateBegin;

    @Schema(name = "预算结束日期-结束")
    private LocalDate budgetEndDateEnd;

    @Schema(name = "预算状态")
    private String budgetStatus;

    @Schema(name = "预算版本")
    private String budgetVersion;

    @Schema(name = "预算总额范围-最小值")
    private BigDecimal totalBudgetAmountMin;

    @Schema(name = "预算总额范围-最大值")
    private BigDecimal totalBudgetAmountMax;

    @Schema(name = "收入预算范围-最小值")
    private BigDecimal revenueBudgetMin;

    @Schema(name = "收入预算范围-最大值")
    private BigDecimal revenueBudgetMax;

    @Schema(name = "支出预算范围-最小值")
    private BigDecimal expenseBudgetMin;

    @Schema(name = "支出预算范围-最大值")
    private BigDecimal expenseBudgetMax;

    @Schema(name = "投资预算范围-最小值")
    private BigDecimal investmentBudgetMin;

    @Schema(name = "投资预算范围-最大值")
    private BigDecimal investmentBudgetMax;

    @Schema(name = "已执行金额范围-最小值")
    private BigDecimal executedAmountMin;

    @Schema(name = "已执行金额范围-最大值")
    private BigDecimal executedAmountMax;

    @Schema(name = "执行进度范围-最小值")
    private BigDecimal executionProgressMin;

    @Schema(name = "执行进度范围-最大值")
    private BigDecimal executionProgressMax;

    @Schema(name = "执行率范围-最小值")
    private BigDecimal executionRateMin;

    @Schema(name = "执行率范围-最大值")
    private BigDecimal executionRateMax;

    @Schema(name = "预算差异范围-最小值")
    private BigDecimal budgetVarianceMin;

    @Schema(name = "预算差异范围-最大值")
    private BigDecimal budgetVarianceMax;

    @Schema(name = "差异率范围-最小值")
    private BigDecimal varianceRateMin;

    @Schema(name = "差异率范围-最大值")
    private BigDecimal varianceRateMax;

    @Schema(name = "预算编制负责人")
    private String budgetManager;

    @Schema(name = "预算编制部门")
    private String budgetDepartment;

    @Schema(name = "编制开始时间-开始")
    private LocalDateTime compilationStartTimeBegin;

    @Schema(name = "编制开始时间-结束")
    private LocalDateTime compilationStartTimeEnd;

    @Schema(name = "编制结束时间-开始")
    private LocalDateTime compilationEndTimeBegin;

    @Schema(name = "编制结束时间-结束")
    private LocalDateTime compilationEndTimeEnd;

    @Schema(name = "编制方法")
    private String compilationMethod;

    @Schema(name = "审批状态")
    private String approvalStatus;

    @Schema(name = "初审人")
    private String firstApprover;

    @Schema(name = "初审时间-开始")
    private LocalDateTime firstApprovalTimeBegin;

    @Schema(name = "初审时间-结束")
    private LocalDateTime firstApprovalTimeEnd;

    @Schema(name = "终审人")
    private String finalApprover;

    @Schema(name = "终审时间-开始")
    private LocalDateTime finalApprovalTimeBegin;

    @Schema(name = "终审时间-结束")
    private LocalDateTime finalApprovalTimeEnd;

    @Schema(name = "执行开始时间-开始")
    private LocalDateTime executionStartTimeBegin;

    @Schema(name = "执行开始时间-结束")
    private LocalDateTime executionStartTimeEnd;

    @Schema(name = "监控频率")
    private String monitoringFrequency;

    @Schema(name = "最后监控时间-开始")
    private LocalDateTime lastMonitoringTimeBegin;

    @Schema(name = "最后监控时间-结束")
    private LocalDateTime lastMonitoringTimeEnd;

    @Schema(name = "监控负责人")
    private String monitoringManager;

    @Schema(name = "执行质量评分范围-最小值")
    private BigDecimal executionQualityScoreMin;

    @Schema(name = "执行质量评分范围-最大值")
    private BigDecimal executionQualityScoreMax;

    @Schema(name = "执行效率评分范围-最小值")
    private BigDecimal executionEfficiencyScoreMin;

    @Schema(name = "执行效率评分范围-最大值")
    private BigDecimal executionEfficiencyScoreMax;

    @Schema(name = "执行合规性评分范围-最小值")
    private BigDecimal executionComplianceScoreMin;

    @Schema(name = "执行合规性评分范围-最大值")
    private BigDecimal executionComplianceScoreMax;

    @Schema(name = "调整次数范围-最小值")
    private Integer adjustmentCountMin;

    @Schema(name = "调整次数范围-最大值")
    private Integer adjustmentCountMax;

    @Schema(name = "最后调整时间-开始")
    private LocalDateTime lastAdjustmentTimeBegin;

    @Schema(name = "最后调整时间-结束")
    private LocalDateTime lastAdjustmentTimeEnd;

    @Schema(name = "调整审批人")
    private String adjustmentApprover;

    @Schema(name = "调整审批时间-开始")
    private LocalDateTime adjustmentApprovalTimeBegin;

    @Schema(name = "调整审批时间-结束")
    private LocalDateTime adjustmentApprovalTimeEnd;

    @Schema(name = "风险等级")
    private String riskLevel;

    @Schema(name = "绩效评价结果")
    private String performanceEvaluationResult;

    @Schema(name = "更新时间-开始")
    private LocalDateTime updateTimeBegin;

    @Schema(name = "更新时间-结束")
    private LocalDateTime updateTimeEnd;

    @Schema(name = "创建人")
    private String createBy;

    @Schema(name = "更新人")
    private String updateBy;
}
