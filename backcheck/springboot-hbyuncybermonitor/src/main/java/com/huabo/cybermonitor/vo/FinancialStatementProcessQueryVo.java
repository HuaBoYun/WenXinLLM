package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 财务报表编制流程查询VO
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="FinancialStatementProcessQueryVo", description="财务报表编制流程查询VO")
public class FinancialStatementProcessQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "报表类型")
    private String statementType;

    @Schema(name = "报表名称")
    private String statementName;

    @Schema(name = "报告期间")
    private String reportingPeriod;

    @Schema(name = "报告年度")
    private Integer reportingYear;

    @Schema(name = "报告月份")
    private Integer reportingMonth;

    @Schema(name = "编制状态")
    private String compilationStatus;

    @Schema(name = "编制进度范围-最小值")
    private BigDecimal compilationProgressMin;

    @Schema(name = "编制进度范围-最大值")
    private BigDecimal compilationProgressMax;

    @Schema(name = "计划开始时间-开始")
    private LocalDateTime plannedStartTimeBegin;

    @Schema(name = "计划开始时间-结束")
    private LocalDateTime plannedStartTimeEnd;

    @Schema(name = "计划结束时间-开始")
    private LocalDateTime plannedEndTimeBegin;

    @Schema(name = "计划结束时间-结束")
    private LocalDateTime plannedEndTimeEnd;

    @Schema(name = "编制负责人")
    private String compilationManager;

    @Schema(name = "编制部门")
    private String compilationDepartment;

    @Schema(name = "数据采集状态")
    private String dataCollectionStatus;

    @Schema(name = "数据采集进度范围-最小值")
    private BigDecimal dataCollectionProgressMin;

    @Schema(name = "数据采集进度范围-最大值")
    private BigDecimal dataCollectionProgressMax;

    @Schema(name = "数据质量评分范围-最小值")
    private BigDecimal dataQualityScoreMin;

    @Schema(name = "数据质量评分范围-最大值")
    private BigDecimal dataQualityScoreMax;

    @Schema(name = "审核状态")
    private String auditStatus;

    @Schema(name = "审核进度范围-最小值")
    private BigDecimal auditProgressMin;

    @Schema(name = "审核进度范围-最大值")
    private BigDecimal auditProgressMax;

    @Schema(name = "初审人")
    private String firstAuditor;

    @Schema(name = "初审时间-开始")
    private LocalDateTime firstAuditTimeBegin;

    @Schema(name = "初审时间-结束")
    private LocalDateTime firstAuditTimeEnd;

    @Schema(name = "复审人")
    private String secondAuditor;

    @Schema(name = "复审时间-开始")
    private LocalDateTime secondAuditTimeBegin;

    @Schema(name = "复审时间-结束")
    private LocalDateTime secondAuditTimeEnd;

    @Schema(name = "终审人")
    private String finalAuditor;

    @Schema(name = "终审时间-开始")
    private LocalDateTime finalAuditTimeBegin;

    @Schema(name = "终审时间-结束")
    private LocalDateTime finalAuditTimeEnd;

    @Schema(name = "签字确认人")
    private String signatory;

    @Schema(name = "签字确认时间-开始")
    private LocalDateTime signatureTimeBegin;

    @Schema(name = "签字确认时间-结束")
    private LocalDateTime signatureTimeEnd;

    @Schema(name = "质量控制状态")
    private String qualityControlStatus;

    @Schema(name = "质量评估结果")
    private String qualityAssessmentResult;

    @Schema(name = "准确性评分范围-最小值")
    private BigDecimal accuracyScoreMin;

    @Schema(name = "准确性评分范围-最大值")
    private BigDecimal accuracyScoreMax;

    @Schema(name = "完整性评分范围-最小值")
    private BigDecimal completenessScoreMin;

    @Schema(name = "完整性评分范围-最大值")
    private BigDecimal completenessScoreMax;

    @Schema(name = "及时性评分范围-最小值")
    private BigDecimal timelinessScoreMin;

    @Schema(name = "及时性评分范围-最大值")
    private BigDecimal timelinessScoreMax;

    @Schema(name = "合规性评分范围-最小值")
    private BigDecimal complianceScoreMin;

    @Schema(name = "合规性评分范围-最大值")
    private BigDecimal complianceScoreMax;

    @Schema(name = "综合质量评分范围-最小值")
    private BigDecimal overallQualityScoreMin;

    @Schema(name = "综合质量评分范围-最大值")
    private BigDecimal overallQualityScoreMax;

    @Schema(name = "问题数量范围-最小值")
    private Integer issueCountMin;

    @Schema(name = "问题数量范围-最大值")
    private Integer issueCountMax;

    @Schema(name = "严重问题数量范围-最小值")
    private Integer criticalIssueCountMin;

    @Schema(name = "严重问题数量范围-最大值")
    private Integer criticalIssueCountMax;

    @Schema(name = "问题处理状态")
    private String issueResolutionStatus;

    @Schema(name = "整改完成时间-开始")
    private LocalDateTime correctionCompletionTimeBegin;

    @Schema(name = "整改完成时间-结束")
    private LocalDateTime correctionCompletionTimeEnd;

    @Schema(name = "模板版本")
    private String templateVersion;

    @Schema(name = "格式标准")
    private String formatStandards;

    @Schema(name = "版本控制")
    private String versionControl;

    @Schema(name = "合规检查结果")
    private String complianceCheckResult;

    @Schema(name = "更新时间-开始")
    private LocalDateTime updateTimeBegin;

    @Schema(name = "更新时间-结束")
    private LocalDateTime updateTimeEnd;

    @Schema(name = "创建时间-开始")
    private LocalDateTime createTimeBegin;

    @Schema(name = "创建时间-结束")
    private LocalDateTime createTimeEnd;

    @Schema(name = "创建人")
    private String createBy;

    @Schema(name = "更新人")
    private String updateBy;
}
